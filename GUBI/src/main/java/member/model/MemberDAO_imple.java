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


	
}
