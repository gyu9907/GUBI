package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
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

import category.domain.CategoryVO;
import member.domain.MemberVO;
import order.domain.OrderVO;
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class MemberDAO_imple implements MemberDAO {

	private DataSource ds;  // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다. 
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes;
	
	// 생성자
	public MemberDAO_imple() {
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semioracle");
		    
		    aes = new AES256(SecretMyKey.KEY);
		    // SecretMyKey.KEY 은 우리가 만든 암호화/복호화 키이다.
		    
		} catch(NamingException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
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
	

	// 회원수 조회
	@Override
	public int memberAllCnt() throws SQLException {
		
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select count(*) as membercnt from tbl_member ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("membercnt");
			}
			
			
		} finally {
			close();
		}
		
		return result;
	}

	// 회원 조건검색하기
	@Override
	public List<MemberVO> optionMemberList(Map<String, String> paraMap) throws SQLException {
		
		List<MemberVO> optionMemberList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select * "
						+ " from "
						+ " ( "
						+ " 	with "
						+ " 	a as "
						+ " 	( "
						+ "    		select userid ,nvl(count(orderno), 0) as ordercnt "
						+ "    		from tbl_order a right join tbl_member b "
						+ "    		on a.fk_userid = b.userid "
						+ "    		group by userid "
						+ " 	), "
						+ " 	b as "
						+ " 	( "
						+ "     	select name, userid, tel, email,  '(' || postcode || ')' || ' ' || address|| ' ' || detail_address as fulladdress, "
						+ "     	registerday, status, idle, point "
						+ "     	from tbl_member "
						+ " 	), "
						+ " 	c as "
						+ " 	( "
						+ "    		select fk_userid, count(*) as logincnt "
						+ "    		from tbl_login "
						+ "    		group by fk_userid "
						+ " 	), "
						+ " 	d as "
						+ " 	( "
						+ "   		select rank() over(partition by fk_userid order by loginday desc) as rank, loginday , fk_userid "
						+ "    		from tbl_login "
						+ " 	) "
						+ " 	select rownum as rno, b.name, b.userid, b.tel, b.registerday, loginday, b.status, a.ordercnt, c.logincnt, b.point, email, fulladdress, idle "
						+ " 	from a join b "
						+ " 	on a.userid = b.userid "
						+ " 	join c "
						+ " 	on a.userid = c.fk_userid "
						+ " 	join d "
						+ " 	on a.userid = d.fk_userid and rank = 1 "
						+ " 	where a.userid != 'admin' "; 

			String searchSelect = paraMap.get("searchSelect"); // 검색어 아이디, 회원명, 핸드폰번호
			String searchWord = paraMap.get("searchWord"); // 검색어 input
			
			String dateSelect = paraMap.get("dateSelect"); // 기간검색 가입날짜, 최근접속
			String startDay = paraMap.get("startDate"); // 기간검색 시작날짜
			String endDate = paraMap.get("endDate"); // 기간검색 끝나는 날짜
			
			String status = paraMap.get("status"); // 회원여부 전체, 일반회원 0, 탈퇴회원 1 
			// System.out.println(status);
			
			// 검색어 타입이 비어있지 않고 검색어 input 이 비어있지 않다면
			if(! searchSelect.isBlank() && ! searchWord.isBlank() ) {
				sql += " and b." + searchSelect +" like '%' || ? || '%' ";
			}
			// 기간검색의 조건이 비어있지 않고 시작날짜 input 이 비어있지 않고 끝나는 날짜 input 이 비어있지 않다면
			if(! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())) {
				sql += " and " + dateSelect + " BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
								+ " AND TO_DATE(?, 'YYYY-MM-DD') ";
			}
			// 회원여부가 비어있지 않다면
			if(! status.isBlank()) {
				sql += " and status = ? ";
			}
			
			sql += " ) where rno between ? and ? ";
						
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo")); // 현재페이지
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			pstmt = conn.prepareStatement(sql);
			
			
			/* 조건 3 세개씩 선택함 */
			if(! searchSelect.isBlank() && ! searchWord.isBlank() &&
			   ! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank()) &&
			   ! status.isBlank()) {
				pstmt.setString(1, searchWord);
				pstmt.setString(2, startDay);
				pstmt.setString(3, endDate);
				pstmt.setInt(4, Integer.parseInt(status));
				pstmt.setInt(5, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(6, (currentShowPageNo*sizePerPage));
			}
			
			/* 조건 2 두개씩 선택함 */
			else if(! searchSelect.isBlank() && ! searchWord.isBlank() &&
			   ! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())	) {
				pstmt.setString(1, searchWord);
				pstmt.setString(2, startDay);
				pstmt.setString(3, endDate);
				pstmt.setInt(4, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(5, (currentShowPageNo*sizePerPage));
			}
			else if(! searchSelect.isBlank() && ! searchWord.isBlank() &&
			   ! status.isBlank()) {
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, Integer.parseInt(status));
				pstmt.setInt(3, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(4, (currentShowPageNo*sizePerPage));
			}
			else if(! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank()) && ! status.isBlank()) {
				pstmt.setString(1, startDay);
				pstmt.setString(2, endDate);
				pstmt.setInt(3, Integer.parseInt(status));
				pstmt.setInt(4, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(5, (currentShowPageNo*sizePerPage));
			}
			
			
			/* 조건 1 : 하나씩만 선택함 */ 
			else if(! searchSelect.isBlank() && ! searchWord.isBlank() ) {
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(3, (currentShowPageNo*sizePerPage));
			}
			else if(! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())) {
				pstmt.setString(1, startDay);
				pstmt.setString(2, endDate);
				pstmt.setInt(3, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(4, (currentShowPageNo*sizePerPage));
			}
			else if(! status.isBlank()) {
				pstmt.setInt(1, Integer.parseInt(status));
				pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(3, (currentShowPageNo*sizePerPage));
			}
			else {
				pstmt.setInt(1, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
				pstmt.setInt(2, (currentShowPageNo*sizePerPage));
			}
			
		
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				
				mvo.setName(rs.getString("name"));
				mvo.setUserid(rs.getString("userid"));
				mvo.setTel(rs.getString("tel"));
				mvo.setRegisterday(rs.getString("registerday"));
				mvo.setStatus(rs.getInt("status"));
				mvo.setPoint(rs.getInt("point"));
				mvo.setOrdercnt(rs.getInt("ordercnt"));
				mvo.setLogincnt(rs.getInt("logincnt"));
				mvo.setLoginday(rs.getString("loginday"));
				mvo.setFulladdress(rs.getString("fulladdress"));
				mvo.setEmail(rs.getString("email"));
				mvo.setIdle(rs.getInt("idle"));
				// 회원 삭제하고 aes.decrypt 로 복호화하기
				optionMemberList.add(mvo);
			}
		} finally {
			close();
		}
		return optionMemberList;
	}

	
	// 조건 검색한 회원수
	@Override
	public int optionMembercnt(Map<String, String> paraMap) throws SQLException {
		
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select count(*) as optionmembercnt "
						+ " from "
						+ " ( with "
						+ " a as "
						+ " ( "
						+ "    select userid ,nvl(count(orderno), 0) as ordercnt "
						+ "    from tbl_order a right join tbl_member b "
						+ "    on a.fk_userid = b.userid "
						+ "    group by userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ "     select name, userid, tel, registerday, status, point "
						+ "     from tbl_member "
						+ " ), "
						+ " c as "
						+ " ( "
						+ "    select fk_userid, count(*) as logincnt "
						+ "    from tbl_login "
						+ "    group by fk_userid "
						+ " ), "
						+ " d as "
						+ " ( "
						+ "    select rank() over(partition by fk_userid order by loginday desc) as rank, loginday , fk_userid "
						+ "    from tbl_login "
						+ " ) "
						+ " select b.name, b.userid, b.tel, b.registerday, loginday, b.status, a.ordercnt, c.logincnt, b.point "
						+ " from a join b "
						+ " on a.userid = b.userid "
						+ " join c "
						+ " on a.userid = c.fk_userid "
						+ " join d "
						+ " on a.userid = d.fk_userid and rank=1 "
						+ " where a.userid != 'admin' "; 

			String searchSelect = paraMap.get("searchSelect"); // 검색어 아이디, 회원명, 핸드폰번호
			String searchWord = paraMap.get("searchWord"); // 검색어 input
			
			String dateSelect = paraMap.get("dateSelect"); // 기간검색 가입날짜, 최근접속
			String startDay = paraMap.get("startDate"); // 기간검색 시작날짜
			String endDate = paraMap.get("endDate"); // 기간검색 끝나는 날짜
			
			String status = paraMap.get("status"); // 회원여부 전체, 일반회원 0, 탈퇴회원 1 
			
			// 검색어 타입이 비어있지 않고 검색어 input 이 비어있지 않다면
			if(! searchSelect.isBlank() && ! searchWord.isBlank() ) {
				sql += " and b." + searchSelect +" like '%' || ? || '%' ";
			}
			
			// 기간검색의 조건이 비어있지 않고 시작날짜 input 이 비어있지 않고 끝나는 날짜 input 이 비어있지 않다면
			if(! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())) {
				sql += " and " + dateSelect + " BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
								  + " AND TO_DATE(?, 'YYYY-MM-DD') ";
			}
			
			// 회원여부가 비어있지 않다면
			if(! status.isBlank()) {
				sql += " and status = ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;

			if (!searchSelect.isBlank() && !searchWord.isBlank()) {
				pstmt.setString(index++, searchWord);
			}

			if (!(dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())) {
				pstmt.setString(index++, startDay);
				pstmt.setString(index++, endDate);
			}
			if (!status.isBlank()) {
				pstmt.setInt(index++, Integer.parseInt(status));
			}

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt("optionmembercnt");
			}
			
		} finally {
			close();
		}
		return cnt;
	}

	// 총페이지수 알아오기 
	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {
		
		int totalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select ceil(count(*)/?) "
						+ " from "
						+ " ( with "
						+ " a as "
						+ " ( "
						+ "    select userid ,nvl(count(orderno), 0) as ordercnt "
						+ "    from tbl_order a right join tbl_member b "
						+ "    on a.fk_userid = b.userid "
						+ "    group by userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ "     select name, userid, tel, registerday, status, point "
						+ "     from tbl_member "
						+ " ), "
						+ " c as "
						+ " ( "
						+ "    select fk_userid, count(*) as logincnt "
						+ "    from tbl_login "
						+ "    group by fk_userid "
						+ " ), "
						+ " d as "
						+ " ( "
						+ "    select rank() over(partition by fk_userid order by loginday desc) as rank, loginday , fk_userid "
						+ "    from tbl_login "
						+ " ) "
						+ " select b.name, b.userid, b.tel, b.registerday, loginday, b.status, a.ordercnt, c.logincnt, b.point "
						+ " from a join b "
						+ " on a.userid = b.userid "
						+ " join c "
						+ " on a.userid = c.fk_userid "
						+ " join d "
						+ " on a.userid = d.fk_userid and rank=1 "
						+ " where a.userid != 'admin' ";
					
			String searchSelect = paraMap.get("searchSelect"); // 검색어 아이디, 회원명, 핸드폰번호
			String searchWord = paraMap.get("searchWord"); // 검색어 input
			
			String dateSelect = paraMap.get("dateSelect"); // 기간검색 가입날짜, 최근접속
			String startDay = paraMap.get("startDate"); // 기간검색 시작날짜
			String endDate = paraMap.get("endDate"); // 기간검색 끝나는 날짜
			
			String status = paraMap.get("status"); // 회원여부 전체, 일반회원 0, 탈퇴회원 1 
			
			
			// 검색어 타입이 비어있지 않고 검색어 input 이 비어있지 않다면
			if(! searchSelect.isBlank() && ! searchWord.isBlank() ) {
				sql += " and b." + searchSelect +" like '%' || ? || '%' ";
			}
			
			// 기간검색의 조건이 비어있지 않고 시작날짜 input 이 비어있지 않고 끝나는 날짜 input 이 비어있지 않다면
			if(! (dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())) {
				sql += " and " + dateSelect + " BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
								  + " AND TO_DATE(?, 'YYYY-MM-DD') ";
			}
			
			// 회원여부가 비어있지 않다면
			if(! status.isBlank()) {
				sql += " and status = ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
			int index = 2;

			if (!searchSelect.isBlank() && !searchWord.isBlank()) {
				pstmt.setString(index++, searchWord);
			}

			if (!(dateSelect.isBlank() || startDay.isBlank() || endDate.isBlank())) {
				pstmt.setString(index++, startDay);
				pstmt.setString(index++, endDate);
			}
			if (! (status.isBlank() && status == "")) {
				pstmt.setInt(index++, Integer.parseInt(status));
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalPage = rs.getInt(1);
			
		} finally {
			close();
		}
		
		return totalPage;
	}

	// 회원탈퇴시키기
	@Override
	public int deleteMember(String deleteuserid) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_member set status = 1 where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			

			pstmt.setString(1, deleteuserid);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 회원복구시키기
	@Override
	public int recoverMember(String recoveruserid) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_member set status = 0 where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			

			pstmt.setString(1, recoveruserid);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}
	
	
	
	
	
	// 회원가입을 해주는 메소드(tbl_member 테이블에 insert)
	@Override
	public int registerMember(MemberVO member) throws SQLException {

		int n = 0;
		int n2 = 0;
		int isSuccess = 0;

		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false); // 수동전환

			String sql = " INSERT INTO TBL_MEMBER(USERID, PASSWD, NAME, BIRTH, EMAIL, TEL, POSTCODE, ADDRESS, DETAIL_ADDRESS) "
					   + " VALUES(?, ?, ?, ? ,? ,? ,? ,? ,? ) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, member.getUserid());
			pstmt.setString(2, Sha256.encrypt(member.getPasswd())); // 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getBirth());
			pstmt.setString(5, aes.encrypt(member.getEmail())); // 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다.
			pstmt.setString(6, aes.encrypt(member.getTel())); // 휴대폰을 AES256 알고리즘으로 양방향 암호화 시킨다.
			pstmt.setString(7, member.getPostcode());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getDetail_address());

			n = pstmt.executeUpdate();
			
			if (n == 1) {
				sql = " INSERT INTO TBL_DELIVERY "
					+ " (DELIVERYNO, FK_USERID, IS_DEFAULT, DELIVERY_NAME, RECEIVER, RECEIVER_TEL, POSTCODE, ADDRESS, DETAIL_ADDRESS) "
					+ " VALUES "
					+ " (SEQ_DELIVERYNO.NEXTVAL, ?, 1, ?, ?, ?, ?, ?, ?); ";
				
				pstmt.setString(1, member.getUserid());
				pstmt.setString(2, member.getName());
				pstmt.setString(3, member.getName());
				pstmt.setString(4, aes.encrypt(member.getTel()));
				pstmt.setString(5, member.getPostcode());
				pstmt.setString(6, member.getAddress());
				pstmt.setString(7, member.getDetail_address());
				
				n2 = pstmt.executeUpdate();
				
			}//end of if (n == 1) {}...
			
			if (n2 == 1) {
				conn.commit(); // 커밋
				conn.setAutoCommit(true); // 자동 전환
			} else { // 그냥 실패한 경우
				conn.rollback(); // 롤백
				conn.setAutoCommit(true); // 자동 전환
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace(); // 암호화키 catch 문
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback(); // 롤백
			conn.setAutoCommit(true); // 자동 전환
		} finally {
			close();
		}

		return isSuccess;

	}// end of public int registerMember(MemberVO member) throws SQLException { }...
		
		
	// ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true 를 리턴해주고, userid 가 존재하지 않으면 false 를 리턴한다)
	@Override
	public boolean idDuplicateCheck(String userid) throws SQLException {

		boolean isExists = false;

		try {
			conn = ds.getConnection();

			String sql = " SELECT USERID " + " FROM tbl_member " + " WHERE USERID = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();

			isExists = rs.next(); // 있으면 true 나온다!

		} finally {
			close();
		}

		return isExists;

	}// end of public boolean idDuplicateCheck(String userid) throws SQLException {
		// }...
		
		
		
		
	// Email 중복검사 메소드 (tbl_member 테이블에서 email 가 존재하면 true 를 리턴해주고, email 가 존재하지 않으면 false 를 리턴한다)
	@Override
	public boolean emailDuplicateCheck(String email) throws SQLException {

		boolean isExists = false;

		try {
			conn = ds.getConnection();

			String sql = " SELECT EMAIL " + " FROM tbl_member " + " WHERE EMAIL = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aes.encrypt(email)); // 넣어줄 때 암호화해서 넣어준다!

			rs = pstmt.executeQuery();

			isExists = rs.next(); // 중복으로 있으면 true 나온다!

		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return isExists;

	}// end of public boolean emailDuplicateCheck(String email) throws SQLException { }...
		
		
		
		
		
	// 로그인 처리 메소드
	@Override
	public MemberVO login(Map<String, String> paraMap) throws SQLException {

		MemberVO member = null;

		try {
			conn = ds.getConnection();

			String sql = " SELECT USERID, PASSWD, NAME, BIRTH, EMAIL, TEL, POSTCODE, ADDRESS, DETAIL_ADDRESS, REGISTERDAY, "
					   + "       PASSWDUPDATEGAP, "
					   + "       NVL(lastlogingap, TRUNC(MONTHS_BETWEEN(SYSDATE, REGISTERDAY))) AS lastlogingap, "
					   + "       STATUS, IDLE, POINT "
					   + " FROM "
					   + " (SELECT USERID "
					   + "       ,PASSWD "
					   + "       ,NAME "
					   + "       ,BIRTH "
					   + "       ,EMAIL "
					   + "       ,TEL "
					   + "       ,POSTCODE "
					   + "       ,ADDRESS "
					   + "       ,DETAIL_ADDRESS "
					   + "       ,REGISTERDAY "
					   + "       ,TRUNC(MONTHS_BETWEEN(SYSDATE, PASSWDUPDATEDAY)) AS passwdupdategap "
					   + "       ,STATUS "
					   + "       ,IDLE "
					   + "       ,POINT "
					   + " FROM TBL_MEMBER "
					   + " WHERE STATUS = 0 AND USERID = ? AND PASSWD = ? "
					   + " ) M "
					   + " CROSS JOIN "
					   + " ( "
					   + " SELECT TRUNC(MONTHS_BETWEEN(SYSDATE, MAX(LOGINDAY))) AS lastlogingap "
					   + " FROM TBL_LOGIN "
					   + " WHERE FK_USERID = ? "
					   + " ) H ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("passwd")));
			pstmt.setString(3, paraMap.get("userid"));

			rs = pstmt.executeQuery();

			if (rs.next()) {
				member = new MemberVO();
				member.setUserid(rs.getString("userid"));
				member.setName(rs.getString("name"));
				member.setBirth(rs.getString("BIRTH"));
				member.setEmail(aes.decrypt(rs.getString("email"))); // 복호화
				member.setTel(aes.decrypt(rs.getString("tel"))); // 복호화
				member.setPostcode(rs.getString("postcode")); // 복호화
				member.setAddress(rs.getString("address"));
				member.setDetail_address(rs.getString("DETAIL_ADDRESS"));
//					member.setRegisterday(rs.getString("REGISTERDAY")); // 이거 데이트타입
				member.setStatus(rs.getInt("status"));
				member.setPoint(rs.getInt("point"));

				// 오라클에서 idle 자동 갱신 작업을 하면 상관없다.
				member.setIdle(rs.getInt("IDLE"));

				// 오라클에서 idle 자동 갱신 작업을 하면 상관없다.
//					if (rs.getInt("lastlogingap") >= 12) {
//						// 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정
//						member.setIdle(1); // 휴면 지정
				//
//						if (rs.getInt("idle") == 0) {
//							// tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기! ==
//							sql = " update tbl_member set idle = 1" 
//								+ " where userid = ? ";
				//
//							pstmt = conn.prepareStatement(sql);
//							pstmt.setString(1, paraMap.get("userid"));
				//
//							pstmt.executeUpdate(); // 그냥 돌리면 끝
//						} // end of if (rs.getInt("idle")== 0) { }...
				//
//					} // end of if (rs.getInt("lastlogingap") >= 12) { }...

				// 휴면이 아닌 회원만 tbl_login(로그인기록) 테이블에 insert 하기 시작
				if (rs.getInt("lastlogingap") < 12) {

					sql = " INSERT INTO TBL_LOGIN(HISTORYNO, FK_USERID, CLIENTIP) "
						+ " values(SEQ_HISTORYNO.NEXTVAL, ?, ?) ";

					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("userid"));
					pstmt.setString(2, paraMap.get("clientip"));

					pstmt.executeUpdate();

				} // end of if (rs.getInt("lastlogingap") < 12) { }...

				// 암호가 3개월 지났는지 판별
				if (rs.getInt("passwdupdategap") >= 3) {
					// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 true
					// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 false

					member.setRequirePwdChange(true); // 로그인시 암호를 변경해라는 alert 를 띄우도록 할때 사용한다.

				} // end of if (rs.getInt("pwdchangegap") >= 3) { }...

			} // end of if (rs.next())...

		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return member;

	}// end of public MemberVO login(Map<String, String> paraMap) throws
		// SQLException...
		
	// 휴면 회원 복구해주는 메소드
	@Override
	public int memberReactivation(Map<String, String> paraMap) throws SQLException {

		int n = 0;

		try {
			conn = ds.getConnection();

			String sql = " UPDATE TBL_MEMBER SET IDLE = 0 WHERE userid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));

			n = pstmt.executeUpdate(); // 성공하면 1

		} finally {
			close();
		}

		return n;

	}// end of public int memberReactivation(Map<String, String> paraMap) throws
		// SQLException {}...
		
		
	// 기존 비밀번호랑 새 비밀번호가 다른지 확인
	@Override
	public boolean passwdExist(Map<String, String> paraMap) throws SQLException {

		boolean isPasswdExist = false;

		try {
			conn = ds.getConnection();

			String sql = " SELECT PASSWD "
					   + " FROM TBL_MEMBER "
					   + " WHERE USERID = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));

			rs = pstmt.executeQuery();

			if (rs.next()) { // 검색성공 시
				if (rs.getString("passwd").equals(Sha256.encrypt(paraMap.get("new_passwd")))) {
					isPasswdExist = true;
				}
			} //

		} finally {
			close();
		}

		return isPasswdExist;

	}// end of public boolean passwdExist(Map<String, String> paraMap) throws SQLException {}...
		
	// 회원 비밀번호 변경하는 메소드
	@Override
	public int passwdUpdate(Map<String, String> paraMap) throws SQLException {

		int n = 0;

		try {
			conn = ds.getConnection();

			String sql = " UPDATE TBL_MEMBER SET PASSWD = ?, PASSWDUPDATEDAY = SYSDATE WHERE userid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Sha256.encrypt(paraMap.get("new_passwd")));
			pstmt.setString(2, paraMap.get("userid"));

			n = pstmt.executeUpdate(); // 성공하면 1

		} finally {
			close();
		}

		return n;

	}// end of public int passwdUpdate(Map<String, String> paraMap) throws SQLException {}...
		
	// 아이디 찾아주는 메소드
	@Override
	public String idFind(Map<String, String> paraMap) throws SQLException {

		String userid = null;

		String name = paraMap.get("name");
		String email = paraMap.get("email");

		try {
			conn = ds.getConnection();

			String sql = " SELECT USERID "
					   + " FROM TBL_MEMBER "
				       + " WHERE EMAIL = ? AND NAME = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aes.encrypt(email));
			pstmt.setString(2, name);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				userid = rs.getString("userid");
			}

		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return userid;

	}// end of public String idFind(Map<String, String> paraMap) {}...
		
		
		
	// 회원정보 수정 메소드
	@Override
	public int memberEdit(MemberVO member) throws SQLException {

		int n = 0;

		try {
			conn = ds.getConnection();

			String sql = " UPDATE TBL_MEMBER SET PASSWD =?, NAME = ?, EMAIL = ?, TEL = ?, POSTCODE = ?, ADDRESS = ?, DETAIL_ADDRESS = ? "
					+ " WHERE USERID = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Sha256.encrypt(member.getPasswd())); // 암호화
			pstmt.setString(2, member.getName());
			pstmt.setString(3, aes.encrypt(member.getEmail())); // 암호화
			pstmt.setString(4, aes.encrypt(member.getTel())); // 암호화
			pstmt.setString(5, member.getPostcode());
			pstmt.setString(6, member.getAddress());
			pstmt.setString(7, member.getDetail_address());
			pstmt.setString(8, member.getUserid());

			n = pstmt.executeUpdate(); // 성공하면 1

		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return n;

	}// end of public int memberEdit(MemberVO member) throws SQLException {}...

	
	
	
	
	// 회원이 존재하는지 검색하는 메소드
	@Override
	public boolean memberIsExist(MemberVO member) throws SQLException {
		
		boolean isExist = false;
		String userid = member.getUserid();
		String passwd = member.getPasswd();
		
		try {
			conn = ds.getConnection();

			String sql = " SELECT USERID "
					   + " FROM TBL_MEMBER "
				       + " WHERE USERID = ? AND PASSWD = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, Sha256.encrypt(passwd));
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				isExist = true;
			}
			
		} finally {
			close();
		}
		
		return isExist;
		
	}//end of public boolean memberIsExist(MemberVO member) throws SQLException {}...
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
}
