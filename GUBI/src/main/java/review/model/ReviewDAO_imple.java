package review.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import category.domain.CategoryVO;
import member.domain.MemberVO;
import product.domain.ProductVO;
import review.domain.AskVO;
import review.domain.ReviewVO;
import product.domain.OptionVO;
import util.check.Check;

public class ReviewDAO_imple implements ReviewDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public ReviewDAO_imple() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/semioracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	// 사용한 자원을 반납하는 close() 메소드 생성하기
	private void close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()----------------

	
	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지수 알아오기 //
	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {
		
		int totalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select ceil(count(*)/?) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, reviewno, userid, b.name, fk_optionno, title, content, score, a.registerday "
						+ " from tbl_review a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select fk_productno, optionno, b.name "
						+ " from tbl_option a join tbl_product b "
						+ " on a.fk_productno = b.productno "
						+ " ) "
						+ " select rno, reviewno, title, content, a.userid, a.name as username, a.registerday, score, b.name as productname "
						+ " from a join b "
						+ " on a.fk_optionno = b.optionno "
						+ " where reviewno is not null ";
			
			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");

			if(! searchType.isBlank() && ! searchWord.isBlank()) {
				sql += " and a." + searchType + " like '%' || ? || '%'";
			}
			
			if(! (startDate.isBlank() || endDate.isBlank() ) ) {
				sql += " and a.registerday between to_date(?, 'YYYY-MM-DD') "
					+  "     and to_date(?, 'YYYY-MM-DD') ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));

			int index = 2;
			
			if(! searchType.isBlank() && ! searchWord.isBlank()) {
				pstmt.setString(index++, searchWord);
			}
			
			if(! (startDate.isBlank() || endDate.isBlank() ) ) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}

			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalPage = rs.getInt(1);

		} finally {
			close();
		}
		
		return totalPage;
	}

	
	// 페이징 처리한 리뷰목록
	@Override
	public List<ReviewVO> reviewList(Map<String, String> paraMap) throws SQLException {
		
		List<ReviewVO> reviewList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select * "
						+ " from "
						+ " ( "
						+ " select rownum as rno, reviewno, userid, img, title, content, score, registerday, "
						+ " fk_productno, optionno, username, productname "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " 	select rownum as rno, reviewno, userid, b.name, img, fk_optionno, title, "
						+ " 	case "
						+ " 	when length(content) > 10 THEN SUBSTR(content, 1, 10) || ' ...' "
						+ " 	else content end as content, "	
						+ " 	score, a.registerday "
						+ " 	from tbl_review a join tbl_member b "
						+ " 	on a.fk_userid = b.userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " 	select fk_productno, optionno, b.name "
						+ " 	from tbl_option a join tbl_product b "
						+ " 	on a.fk_productno = b.productno "
						+ " ) "
						+ " select reviewno, img, title, content, a.userid, a.name as username, a.registerday, score,"
						+ " b.name as productname, optionno, fk_productno "
						+ " from a join b "
						+ " on a.fk_optionno = b.optionno "
						+ " where reviewno is not null ";
			
				String searchType = paraMap.get("searchType");
				String searchWord = paraMap.get("searchWord");
				String startDate = paraMap.get("startDate");
				String endDate = paraMap.get("endDate");
				
	
				if(! searchType.isBlank() && ! searchWord.isBlank()) {
					sql += " and a." + searchType + " like '%' || ? || '%'";
				}
				
				if(! (startDate.isBlank() || endDate.isBlank() ) ) {
					sql += " and a.registerday between to_date(?, 'YYYY-MM-DD') "
						+  "      and to_date(?, 'YYYY-MM-DD') ";
				}
				
				sql +=    " order by a.registerday desc ) "
						+ ") where rno between ? and ? ";
				
				int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
				int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
				pstmt = conn.prepareStatement(sql);
				
				int index = 1;
				
				if(! searchType.isBlank() && ! searchWord.isBlank()) {
					pstmt.setString(index++, searchWord);
				}
				
				if(! (startDate.isBlank() || endDate.isBlank() ) ) {
					pstmt.setString(index++, startDate);
					pstmt.setString(index++, endDate);
				}
				
				
				pstmt.setInt(index++, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(index++, (currentShowPageNo*sizePerPage));
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					ReviewVO rvo = new ReviewVO();
					
					rvo.setReviewno(rs.getInt("reviewno"));
					rvo.setTitle(rs.getString("title"));
					rvo.setContent(rs.getString("content"));
					rvo.setFk_userid(rs.getString("userid"));
					rvo.setRegisterday(rs.getString("registerday"));
					rvo.setScore(rs.getInt("score"));
					rvo.setImg(rs.getString("img"));
					
					MemberVO mvo = new MemberVO();
					mvo.setName(rs.getString("username"));
					rvo.setMvo(mvo);
					
					ProductVO pvo = new ProductVO();
					pvo.setName(rs.getString("productname"));
					rvo.setPvo(pvo);
					
					reviewList.add(rvo);
				}
			
		} finally {
			close();
		}

		return reviewList;
	}

	// 조건검색한 리뷰수 
	@Override
	public int reviewCnt(Map<String, String> paraMap) throws SQLException {

		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select count(*) "
						+ " from  "
						+ " ( "
						+ " with "
						+ " a as  "
						+ " ( "
						+ " 	select rownum as rno, reviewno, userid, b.name, fk_optionno, title, content, score, a.registerday "
						+ " 	from tbl_review a join tbl_member b "
						+ " 	on a.fk_userid = b.userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " 	select fk_productno, optionno, b.name "
					    + " 	from tbl_option a join tbl_product b "
						+ " 	on a.fk_productno = b.productno "
						+ " ) "
						+ " select reviewno, title, content, a.userid, a.name as username, a.registerday, score, b.name as productname "
						+ " from a join b "
						+ " on a.fk_optionno = b.optionno "
						+ " where reviewno is not null ";
			
			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			
			if(! searchType.isBlank() && ! searchWord.isBlank()) {
				sql += " and a." + searchType + " like '%' || ? || '%'";
			}
			
			if(! (startDate.isBlank() || endDate.isBlank() ) ) {
				sql += " and a.registerday between to_date(?, 'YYYY-MM-DD') "
					+  "      and to_date(?, 'YYYY-MM-DD') ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			
			if(! searchType.isBlank() && ! searchWord.isBlank()) {
				pstmt.setString(index++, searchWord);
			}
			
			if(! (startDate.isBlank() || endDate.isBlank() ) ) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}

		} finally {
			close();
		}
		
		return cnt;
	}

	// 문의리스트 페이징
	@Override
	public List<AskVO> askList(Map<String, String> paraMap) throws SQLException {
		
		List<AskVO> askList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select * "
						+ " from  "
						+ " ( "
						+ " select rownum rno, askno, userid, fk_productno, fk_adminid, name, productno, optionname, optionno, "
						+ " answer, is_hide, registerday, answerday, ask_category, question "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " (  "
						+ "    select askno, userid, fk_productno, fk_adminid, name, "
						+ "    case "
						+ "    when length(question) > 15 THEN SUBSTR(question, 1, 15) || ' ...' "
						+ "    else question end as question, "
						+ "    answer, is_hide, a.registerday, answerday, ask_category "
						+ "    from tbl_ask a join tbl_member b "
						+ "    on a.fk_userid = b.userid "
						+ " ), "
						+ " b  "
						+ " as "
						+ " ( "
						+ "    select productno, optionno, b.name as optionname "
						+ "    from tbl_product a join tbl_option b "
						+ "    on a.productno = b.fk_productno "
						+ " ) "
						+ " select askno, userid, fk_productno, fk_adminid, name, productno, optionname, optionno, "
						+ " answer, is_hide, a.registerday, answerday, ask_category, question "
						+ " from a join b "
						+ " on a.fk_productno = b.productno "
						+ " order by registerday desc "
						+ " ) "
						+ " ) "
						+ " where rno between ? and ? ";
							
				
			String ask_category = paraMap.get("ask_category");
			int  sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			
			if(! ask_category.isBlank()) {
				sql += " and ask_category = ? ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
			pstmt.setInt(2, (currentShowPageNo*sizePerPage));
			
			if(! ask_category.isBlank()) {
				pstmt.setString(3, ask_category);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AskVO avo = new AskVO();
				MemberVO mvo = new MemberVO();
				
				avo.setAskno(rs.getInt("askno"));
				avo.setFk_productno(rs.getInt("fk_productno"));
				avo.setFk_adminid(rs.getString("fk_adminid"));
				avo.setQuestion(rs.getString("question"));
				avo.setAnswer(rs.getString("answer"));
				avo.setIs_hide(rs.getInt("is_hide"));
				avo.setRegisterday(rs.getString("registerday"));
				avo.setAnswerday(rs.getString("answerday"));
				avo.setCategory(rs.getInt("ask_category"));
				
				mvo.setName(rs.getString("name"));
				mvo.setUserid(rs.getString("userid"));
				avo.setMvo(mvo);
				
				askList.add(avo);
			}
	
		} finally {
			close();
		}

		return askList;
	}

	
	// 문의 총페이지수 구하기
	@Override
	public int askTotalPage(Map<String, String> paraMap) throws SQLException {
		
		int totalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select ceil(count(*)/?) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum rno, askno, userid, fk_productno, fk_adminid, name, question, answer, is_hide, a.registerday, answerday, ask_category "
						+ " from tbl_ask a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " ), "
						+ " b "
						+ " as  "
						+ " ( "
						+ " select productno "
						+ " from tbl_product "
						+ " ) "
						+ " select * "
						+ " from a join b "
						+ " on a.fk_productno = b.productno "
						+ " where rno is not null ";
			
			String ask_category = paraMap.get("ask_category");
			int sizePerPage  = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(! ask_category.isBlank() ) {
				sql += " and ask_category = ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, sizePerPage);
			
			if(! ask_category.isBlank()) {
				pstmt.setString(2, ask_category);
			}
			
			rs = pstmt.executeQuery();

			rs.next();
			
			totalPage = rs.getInt(1);
			
		} finally {
			close();
		}
		
		return totalPage;
	}

	// 문의개수구하기
	@Override
	public int askCnt(String ask_category) throws SQLException {
		
		int cnt = 0;
		
		try {
			conn = ds.getConnection();
			
			
			String sql =  " select count(*) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum rno, askno, userid, fk_productno, fk_adminid, name, question, answer, is_hide, a.registerday, answerday, ask_category "
						+ " from tbl_ask a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " ), "
						+ " b "
						+ " as  "
						+ " ( "
						+ " select productno "
						+ " from tbl_product "
						+ " ) "
						+ " select * "
						+ " from a join b "
						+ " on a.fk_productno = b.productno "
						+ " where rno is not null ";
		
		
			if(! ask_category.isBlank()) {
				sql += " and ask_category = ? ";
			}
			
			sql += " order by askno ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			if(! ask_category.isBlank()) {
				pstmt.setString(1, ask_category);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
				
		} finally {
			close();
		}
		
		return cnt;
	}

	// 문의상세보기
	@Override
	public AskVO detailAsk(String askno) throws SQLException {
		
		AskVO detailAsk = new AskVO();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select askno, fk_userid, fk_productno, fk_adminid, question, answer, "
						+ "       is_hide, a.registerday, nvl(to_char(answerday),'답변없음') as answerday, ask_category, name "
						+ " from tbl_ask a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " where askno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, askno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				MemberVO mvo = new MemberVO();
				
				detailAsk.setAskno(rs.getInt("askno"));
				detailAsk.setFk_userid(rs.getString("fk_userid"));
				detailAsk.setFk_productno(rs.getInt("fk_productno"));
				detailAsk.setFk_adminid(rs.getString("fk_adminid"));
				detailAsk.setQuestion(rs.getString("question"));
				detailAsk.setAnswer(rs.getString("answer"));
				detailAsk.setIs_hide(rs.getInt("is_hide"));
				detailAsk.setRegisterday(rs.getString("registerday"));
				detailAsk.setAnswerday(rs.getString("answerday"));
				detailAsk.setCategory(rs.getInt("ask_category"));
				mvo.setName(rs.getString("name"));
				detailAsk.setMvo(mvo);
			}
	
		} finally {
			close();
		}
		
		return detailAsk;
	}

	// 문의 답변작성하기
	@Override
	public int answerAdd(String answer, String askno) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " update tbl_ask set answer = ? , answerday = sysdate "
						+ " where askno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, answer);
			pstmt.setString(2, askno);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return n;
	}

	
	
	// 내 리뷰 개수 
	@Override
	public int selectReviewCount(Map<String, String> paraMap) throws SQLException {
		
		int count = 0;

		try {
			conn = ds.getConnection();
			
			String sql = "SELECT COUNT(*) "
	                   + " FROM tbl_product P "
	                   + " JOIN tbl_option OPT ON P.productno = OPT.fk_productno "
	                   + " JOIN tbl_review R ON OPT.optionno = R.fk_optionno "
	                   + " WHERE r.fk_userid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userId"));

			rs = pstmt.executeQuery();
			
			rs.next();
	         
	        count = rs.getInt(1); 
	        
		} finally {
			close();
		}
		
		return count;
	}
	
	
	
	
	// 내 리뷰 조회  
	@Override
	public List<ReviewVO> selectReviewList(Map<String, String> paraMap) throws SQLException {
		List<ReviewVO> reviewList = new ArrayList<>();
		try {
			conn = ds.getConnection();
			
			String sql = " WITH ReviewCTE AS ( "
					+ "    SELECT row_number() over(order by reviewno desc) AS RNO , "
					+ "        r.reviewno,  "
					+ "        r.fk_optionno,  "
					+ "        r.fk_userid,  "
					+ "        r.score,  "
					+ "        OPT.name,  "
					+ "        p.productno,  "
					+ "        r.title,  "
					+ "        r.content,  "
					+ "        r.img,  "
					+ "        r.registerday "
					+ "         "
					+ "    FROM tbl_option OPT  "
					+ "    JOIN tbl_review r ON OPT.optionno = r.fk_optionno    "
					+ "    JOIN tbl_product p ON p.productno = OPT.fk_productno "
					+ "    WHERE r.fk_userid = ? "
					+ " ) "
					+ " SELECT  "
					+ "    reviewno,  "
					+ "    fk_optionno,  "
					+ "    fk_userid,  "
					+ "    score,  "
					+ "    name,  "
					+ "    productno,  "
					+ "    title,  "
					+ "    content,  "
					+ "    img,  "
					+ "    registerday "
					+ " FROM ReviewCTE "
					+ " WHERE rownum BETWEEN ? AND ? ";


			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userId"));
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ReviewVO rvo = new ReviewVO();
				
				rvo.setReviewno(rs.getInt("reviewno"));
				rvo.setFk_optionno(rs.getInt("fk_optionno"));
				rvo.setFk_userid(rs.getString("fk_userid"));
				rvo.setScore(rs.getInt("score"));
				
				OptionVO otpvo = new OptionVO();
				otpvo.setName(rs.getString("name"));
				otpvo.setFk_productno(rs.getInt("productno"));
				rvo.setOptionvo(otpvo);
				
				rvo.setTitle(rs.getString("title"));
				rvo.setContent(rs.getString("content"));
				rvo.setImg(rs.getString("img"));
				rvo.setRegisterday(rs.getString("registerday"));
				
				reviewList.add(rvo);
				
			}
			
		} finally {
			close();
		}
		
		return reviewList;
	}
	
	// 리뷰를 작성하여 insert 하는 메소드 
	@Override
	public int addReview(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " insert into tbl_review(reviewno, fk_userid, fk_optionno, title, content, score, img, registerday) "
					   + " values(seq_reviewno.nextval, ? , ? , ? , ? , ? , ? , default ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setInt(2, Integer.parseInt(paraMap.get("optionno")));
			pstmt.setString(3, paraMap.get("title"));
			pstmt.setString(4, paraMap.get("content"));
			pstmt.setString(5, paraMap.get("score"));
			pstmt.setString(6, paraMap.get("img"));
			
			//System.out.println(sql);
			//System.out.println(paraMap);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;
	}

	// 리뷰 수정하는 메소드
	@Override
	public int reviewEdit(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		System.out.println(paraMap);
		
		int reviewno = Integer.parseInt(paraMap.get("reviewno"));
		String title = paraMap.get("title");
		int score = Integer.parseInt(paraMap.get("score"));
		String content = paraMap.get("content");
		String img = paraMap.get("img");
		
		try {
			conn = ds.getConnection();
			
			String sql = " UPDATE tbl_review "
					   + " SET title = ?, score = ?, content = ?";
					
			 if (!Check.isNullOrBlank(img)) {
				 sql += ", img = ? ";
			 }
			sql += " WHERE reviewno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setInt(2, score);
			pstmt.setString(3, content);
			if (!Check.isNullOrBlank(img)) {
				pstmt.setString(4, img);
				pstmt.setInt(5, reviewno);
			} else {
				pstmt.setInt(4, reviewno);
			}
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		
		return n;
	}

	
	// 상품을 구매한 유저가 작성한 리뷰 삭제하는 메소드
	@Override
	public int reviewDelete(String reviewno) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " delete from tbl_review "
					   + " where reviewno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(reviewno));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		
		return n;
	}

	
		
}
