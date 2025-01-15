package ask.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ask.domain.AskVO;
import util.security.Sha256;

public class AskDAO_imple implements AskDAO {

	private DataSource ds;  // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다. 
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// 생성자
	public AskDAO_imple() {
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semioracle");
		    
		} catch(NamingException e) {
			e.printStackTrace();
		}
	}

	// 사용한 자원을 반납하는 close() 메소드 생성하기
	private void close() {
		try {
			if(rs    != null) {rs.close();	  rs=null;}
			if(pstmt != null) {pstmt.close(); pstmt=null;}
			if(conn  != null) {conn.close();  conn=null;}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()---------------
	
	
	

	
	
	// 문의하기 테이블 insert
	@Override
	public int insertAsk(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		String userid = paraMap.get("userid");
		String productno = paraMap.get("productno");
		String question = paraMap.get("question");
		String is_hide = paraMap.get("is_hide");
		String QnA_passwd = paraMap.get("QnA_passwd");
		String QnA_type = paraMap.get("QnA_type");
		
		try {
			conn = ds.getConnection();
			
			// 
			String sql = "";
			if (is_hide == "0") {// 공개 문의인 경우 0
				sql = " INSERT INTO TBL_ASK(ASKNO, FK_USERID, FK_PRODUCTNO, QUESTION, IS_HIDE, ASK_CATEGORY) "
				    + " VALUES(SEQ_ASKNO.NEXTVAL, ?, ?, ?, ?, ?) ";
				
			} else {// 비공개 문의인 경우 1
				sql = " INSERT INTO TBL_ASK(ASKNO, FK_USERID, FK_PRODUCTNO, QUESTION, PASSWD, IS_HIDE, ASK_CATEGORY) "
					+ " VALUES(SEQ_ASKNO.NEXTVAL, ?, ?, ?, ?, ?, ?) ";
				
			}//end of if else (QnA_passwd == "") {}...
			
			pstmt = conn.prepareStatement(sql);
			
			if (is_hide == "0") {
				pstmt.setString(1, userid);
				pstmt.setInt(2, Integer.parseInt(productno));
				pstmt.setString(3, question);
				pstmt.setInt(4, Integer.parseInt(is_hide));
				pstmt.setString(5, QnA_type);
				
			} else {
				pstmt.setString(1, userid);
				pstmt.setInt(2, Integer.parseInt(productno));
				pstmt.setString(3, question);
				pstmt.setString(4, Sha256.encrypt(QnA_passwd)); // 암호화
				pstmt.setInt(5, Integer.parseInt(is_hide));
				pstmt.setString(6, QnA_type);
				
			}//end of if else (is_hide == "0") {}...
			
			n = pstmt.executeUpdate();
			
			
		} finally {
			close();
		}
		
		return n;

	}//end of public int insertAsk(Map<String, String> paraMap) throws SQLException {}...

	
	
	
	
	// 분류에 따른 리스트 뽑아주기 메소드
	@Override
	public List<AskVO> sortAsk(Map<String, String> paraMap) throws SQLException {
		
		List<AskVO> askList = new ArrayList<>();;
		
		String userid = paraMap.get("userid"); // 로그인 안했으면 null
		String QnA_type = paraMap.get("QnA_type"); // ask 카테고리 초기값 -1 올꺼다
		String productno = paraMap.get("productno");
		
		try {
			conn = ds.getConnection();

			String sql = " SELECT FK_USERID, "
					          + " FK_PRODUCTNO,"
					          + " QUESTION,"
					          + " NVL(ANSWER, '아직 답변이 없습니다.') AS ANSWER,"
					          + " IS_HIDE, "
					          + " TO_CHAR(A.REGISTERDAY, 'yyyy-mm-dd') AS REGISTERDAY, "
					          + " NVL(TO_CHAR(ANSWERDAY, 'yyyy-mm-dd'), '없음.') AS ANSWERDAY, "
					          + " ASK_CATEGORY "   
					   + " FROM TBL_ASK A " 
					   + " JOIN TBL_PRODUCT P " 
					   + " ON A.FK_PRODUCTNO = P.PRODUCTNO "
					   + " WHERE P.PRODUCTNO = ? "; // product no
			
			if (userid !=  null) { // 로그인을 해서 유저아이디가 있으면
				sql += " AND FK_USERID = ? "; // userid
			}
			if (!"-1".equals(QnA_type)) { // ALL 은 -1, 아니면 추가
				sql += " AND A.ASK_CATEGORY = ? ";
			}
			
			sql += " UNION "
				 + " SELECT FK_USERID,"
				 		+ " FK_PRODUCTNO, "
				 		+ " QUESTION, "
				 		+ " NVL(ANSWER, '아직 답변이 없습니다.') AS ANSWER, "
				 		+ " IS_HIDE, "
						+ " TO_CHAR(A.REGISTERDAY, 'yyyy-mm-dd') AS REGISTERDAY, "
				 		+ " NVL(TO_CHAR(ANSWERDAY, 'yyyy-mm-dd'), '없음.') AS ANSWERDAY, "
				 		+ " ASK_CATEGORY "
				 + " FROM TBL_ASK A "
				 + " JOIN TBL_PRODUCT P "
				 + " ON A.FK_PRODUCTNO = P.PRODUCTNO "
				 + " WHERE P.PRODUCTNO = ? ";
				 
			if (!"-1".equals(QnA_type)) {
				sql += " AND A.ASK_CATEGORY = ? ";
			}
				 
			sql += " ORDER BY REGISTERDAY DESC ";
			
			pstmt = conn.prepareStatement(sql);
			
			int i = 1;
			
			pstmt.setInt(i++, Integer.parseInt(productno)); // 무조건 들어가야 하는 값
			
			if (userid != null) {
				pstmt.setString(i++, userid);
			}
			if (!"-1".equals(QnA_type)) { // ALL 은 -1, 아니면 추가
				pstmt.setInt(i++, Integer.parseInt(QnA_type));
			}
			
			pstmt.setInt(i++, Integer.parseInt(productno)); // 무조건 들어가야 하는 값
			
			if (!"-1".equals(QnA_type)) { // ALL 은 -1, 아니면 추가
				pstmt.setInt(i++, Integer.parseInt(QnA_type));
			}
			
			rs = pstmt.executeQuery();
	         
			while (rs.next()) {
				AskVO askvo = new AskVO(); 
				// FK_USERID, FK_PRODUCTNO, QUESTION, ANSWER, IS_HIDE, A.REGISTERDAY, A.ANSWERDAY, A.ASK_CATEGORY
				askvo.setFk_userid(rs.getString("FK_USERID")); // min******* 로 처리
				askvo.setFk_productno(rs.getInt("FK_PRODUCTNO"));
				askvo.setQuestion(rs.getString("QUESTION"));
				askvo.setAnswer(rs.getString("ANSWER"));
				askvo.setIs_hide(rs.getInt("IS_HIDE"));
				askvo.setRegisterday(rs.getString("REGISTERDAY")); // 답변 등록일자
				askvo.setAnswerday(rs.getString("ANSWERDAY"));
				askvo.setAsk_category(rs.getInt("ASK_CATEGORY")); // 문의 종류

				askList.add(askvo);
				
			} // end of while-----------------

		} finally {
			close();
		}
		
		return askList;
		
	}//end of public List<AskVO> sortAsk(Map<String, String> paraMap) throws SQLException {}...

	
	
	
	
	// 문의 삭제 메소드
	@Override
	public int deletetAsk(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		String userid = paraMap.get("userid");
		String askno = paraMap.get("askno");
		
		try {
			conn = ds.getConnection();
			
			String sql = " DELETE FROM TBL_ASK WHERE FK_USERID = ? AND ASKNO = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			pstmt.setString(2, askno);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;
		
	}//end of public int deletetAsk(Map<String, String> paraMap) throws SQLException {}...

	
	
	
	
	
	// 문의를 삭제하는 메소드
	@Override
	public int AskDelete(String[] asknoArr) throws SQLException {

		int deletedCount = 0;

		try {
			conn = ds.getConnection();
			String sql = "DELETE FROM tbl_ask WHERE askno = ?";

			pstmt = conn.prepareStatement(sql);

			for (String askno : asknoArr) {
				pstmt.setInt(1, Integer.parseInt(askno));
				deletedCount += pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return deletedCount;

	}// end of public int AskDelete(String[] asknoArr) throws SQLException {}--------------------------------------
	
	
	
	
	
	
	
	// 문의 내역 조회하는 메소드
	@Override
	public List<AskVO> getAskList(String userid) throws SQLException {
		List<AskVO> askList = new ArrayList<>();
		try {
			conn = ds.getConnection();
	        String sql = " SELECT " 
	                  +  "    askno, " 
	                  +  "    ask_category, " 
	                  +  "    question, " 
	                  +  "    fk_userid, " 
	                  +  "    TO_CHAR(registerday, 'YYYY-MM-DD') AS registerday," 
	                  +  "    CASE " 
	                  +  "         WHEN answer IS NOT NULL THEN 'O' "
	                  +  "        ELSE 'X' "
	                  +  "    END AS answer_status " 
	                  +  " FROM " 
	                  +  " TBL_ASK "
	                  +  " WHERE fk_userid = ? " 
	                  +  " ORDER BY REGISTERDAY DESC ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				AskVO ask = new AskVO();
				ask.setAskno(rs.getInt("askno")); // 문의 번호
				ask.setAsk_category(rs.getInt("ask_category")); // 문의 유형
				ask.setQuestion(rs.getString("question")); // 질문
				ask.setFk_userid(rs.getString("fk_userid")); // 작성자
				ask.setRegisterday(rs.getString("registerday"));// 작성일
				ask.setAnswer(rs.getString("answer_status")); // 답변 여부

				askList.add(ask); // 리스트에 추가
			}

		} catch (SQLException e) {
			e.printStackTrace(); // SQLException 출력
			throw e; // 예외를 다시 던져 호출하는 곳
		}

		return askList;
	}// end of public List<AskVO> getAskList(String userid) throws SQLException {}-----------------------------------------------
	   
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
