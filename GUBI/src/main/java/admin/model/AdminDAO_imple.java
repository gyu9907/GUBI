package admin.model;

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

import admin.domain.AdminVO;
import admin.domain.StatisticsVO;
import member.domain.MemberVO;
import order.domain.OrderVO;
import product.domain.ProductVO;
import review.domain.ReviewVO;
import util.security.Sha256;

public class AdminDAO_imple implements AdminDAO {
	
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public AdminDAO_imple() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/semioracle");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}// end of public ProductDAO_imple() { }...

	// 자원반납 메소드
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

	}// end of close...
	
	

	
	// 관리자 로그인 메소드
	@Override
	public AdminVO adminlogin(Map<String, String> paraMap) throws SQLException {
		
		AdminVO admin = null;

		try {
			conn = ds.getConnection();

			String sql = " SELECT ADMINID, NAME, EMAIL "
					   + " FROM TBL_ADMIN "
					   + " WHERE ADMINID = ? AND PASSWD = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("adminid"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("adminpasswd")));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				admin = new AdminVO();
				
				admin.setAdminid(rs.getString("ADMINID"));
				admin.setName(rs.getString("name"));
				admin.setEmail(rs.getString("email"));
			}
			
		} finally {
			close();
		}
		
		return admin;
		
	}//end of public AdminVO adminlogin(Map<String, String> paraMap) throws SQLException {}...

	// 카테고리별 주문 통계
	@Override
	public List<StatisticsVO> getOrderStatByCategory(Map<String, String> paraMap) throws SQLException {
		List<StatisticsVO> statisticsList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();

			String sql = " select NVL(small_category, '합계'), NVL(sum(cnt), 0) AS cnt"
					   + "      , NVL(ROUND(RATIO_TO_REPORT(sum(cnt)) OVER(PARTITION BY (GROUPING(small_category))), 3) * 100, 0) as percentage "
					   + " from "
					   + " ( "
					   + "     select p.fk_categoryno, od.cnt "
					   + "     from tbl_order o "
					   + "     join tbl_order_detail od "
					   + "     on o.orderno = od.fk_orderno "
					   + "     join tbl_option op "
					   + "     on op.optionno = od.fk_optionno "
					   + "     join tbl_product p "
					   + "     on p.productno = op.fk_productno "
					   + "     where o.status = ? "
					   + " ) V "
					   + " right join tbl_category c "
					   + " on c.categoryno = V.fk_categoryno "
					   + " where major_category = ? "
					   + " group by rollup(small_category) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("orderStatus"));
			pstmt.setString(2, paraMap.get("majorCategory"));
			
			rs = pstmt.executeQuery();

			while(rs.next()) {
				StatisticsVO svo = new StatisticsVO();
				
				svo.setName(rs.getString(1));
				svo.setValue(rs.getInt(2));
				svo.setPercentage(rs.getFloat(3));
				
				statisticsList.add(svo);
			}
		} finally {
			close();
		}
		
		return statisticsList;
	}
	
	// 날짜별 주문 통계
	@Override
	public List<StatisticsVO> getOrderStatByDate(Map<String, String> paraMap) throws SQLException {
		List<StatisticsVO> statisticsList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();

			String dateSql = "";
			
			if("day".equals(paraMap.get("date"))) {
				dateSql = "'yyyy-mm-dd'";
			}
			
			if("month".equals(paraMap.get("date"))) {
				dateSql = "'yyyy-mm'";
			}
			
			if("year".equals(paraMap.get("date"))) {
				dateSql = "'yyyy'";
			}

			String sql = " select NVL(to_char(orderday, "+dateSql+"), '합계') AS orderday, NVL(sum(cnt), 0)"
					   + "      , NVL(ROUND(RATIO_TO_REPORT(sum(cnt)) OVER(PARTITION BY (to_char(orderday, "+dateSql+"))), 3) * 100, 0) "
					   + " from tbl_order o "
					   + " join tbl_order_detail od "
					   + " on o.orderno = od.fk_orderno "
					   + " where o.status = ? "
					   + " group by rollup(to_char(orderday, "+dateSql+")) ";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("orderStatus"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StatisticsVO svo = new StatisticsVO();
				
				svo.setName(rs.getString(1));
				svo.setValue(rs.getInt(2));
				svo.setPercentage(rs.getFloat(3));
				
				statisticsList.add(svo);
			}
		} finally {
			close();
		}
		
		return statisticsList;
	}

	// 접속자수 통계
	@Override
	public List<StatisticsVO> getLoginStat(String date) throws SQLException {
		List<StatisticsVO> statisticsList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();

			String dateSql = "";
			
			if("day".equals(date)) {
				dateSql = "'yyyy-mm-dd'";
			}
			
			if("month".equals(date)) {
				dateSql = "'yyyy-mm'";
			}
			
			if("year".equals(date)) {
				dateSql = "'yyyy'";
			}

			String sql = " select NVL(to_char(loginday, "+dateSql+"), '합계') AS loginday "
					   + "      , count(distinct fk_userid || to_char(loginday, "+dateSql+")) as value "
					   + "      , ROUND(RATIO_TO_REPORT(count(distinct fk_userid || to_char(loginday, "+dateSql+"))) OVER(PARTITION BY GROUPING(to_char(loginday, "+dateSql+"))), 3) * 100 as percentage "
					   + " from tbl_login "
					   + " group by ROLLUP(to_char(loginday, "+dateSql+")) ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StatisticsVO svo = new StatisticsVO();
				
				svo.setName(rs.getString(1));
				svo.setValue(rs.getInt(2));
				svo.setPercentage(rs.getFloat(3));
				
				statisticsList.add(svo);
			}
		} finally {
			close();
		}
		
		return statisticsList;
	}

	
	// 방문자통계
	@Override
	public List<String> visitorCnt() throws SQLException {
		
		List<String> visitorCnt = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			
			String sql =  " select count(*) cnt "
						+ " from tbl_login "
						+ " where to_char(loginday, 'yyyymmdd') = to_char(sysdate, 'yyyymmdd') "
						+ " union all "
						+ " select count(*) "
						+ " from tbl_login "
						+ " where loginday >= trunc(sysdate) - 7  "
						+ " and loginday < trunc(sysdate) + 1 "
						+ " union all "
						+ " select count(*) "
						+ " from tbl_login "
						+ " where loginday >= trunc(sysdate) - 30 "
						+ " and loginday < trunc(sysdate) + 1 ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				visitorCnt.add(rs.getString(1));
			}
			
		} finally {
			close();
		}
		
		return visitorCnt;
	}

	// 회원가입통계
	@Override
	public List<String> registerCnt() throws SQLException {
		
		List<String> registerCnt = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			
			String sql =  " select count(*) cnt "
						+ " from tbl_member "
						+ " where to_char(registerday, 'yyyymmdd') = to_char(sysdate, 'yyyymmdd') "
						+ " union all "
						+ " select count(*) "
						+ " from tbl_member "
						+ " where registerday >= trunc(sysdate) - 7  "
						+ " and registerday < trunc(sysdate) + 1 "
						+ " union all "
						+ " select count(*) "
						+ " from tbl_member "
						+ " where registerday >= trunc(sysdate) - 30 "
						+ " and registerday < trunc(sysdate) + 1 ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				registerCnt.add(rs.getString(1));
			}
			
		} finally {
			close();
		}
		
		return registerCnt;
	}

	// 구매수통계 
	@Override
	public List<String> purchaseCnt() throws SQLException {
		
		List<String> purchaseCnt = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			
			String sql =  " select count(*) "
						+ " from tbl_order "
						+ " where to_char(orderday, 'yyyymmdd') = to_char(sysdate, 'yyyymmdd') "
						+ " union all "
						+ " select count(*) "
						+ " from tbl_order "
						+ " where orderday >= trunc(sysdate) - 7 "
						+ " and orderday < trunc(sysdate) + 1 "
						+ " union all "
						+ " select count(*) "
						+ " from tbl_order "
						+ " where orderday >= trunc(sysdate) - 30  "
						+ " and orderday < trunc(sysdate) + 1 ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				purchaseCnt.add(rs.getString(1));
			}
			
		} finally {
			close();
		}
		
		return purchaseCnt;
	}

	// 구매금액 통계
	@Override
	public List<String> salesCnt() throws SQLException {
		
		List<String> salesCnt = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			
			String sql =  " select nvl(sum(total_price), 0) "
						+ " from tbl_order "
						+ " where to_char(orderday, 'yyyymmdd') = to_char(sysdate, 'yyyymmdd') "
						+ " union all "
						+ " select nvl(sum(total_price), 0) "
						+ " from tbl_order "
						+ " where orderday >= trunc(sysdate) - 7 "
						+ " and orderday < trunc(sysdate) + 1 "
						+ " union all "
						+ " select nvl(sum(total_price), 0) "
						+ " from tbl_order "
						+ " where orderday >= trunc(sysdate) - 30 "
						+ " and orderday < trunc(sysdate) + 1 ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				salesCnt.add(rs.getString(1));
			}
			
		} finally {
			close();
		}
		
		return salesCnt;
	}

	// 최근 주문목록 
	@Override
	public List<OrderVO> orderlist(Map<String, String> paraMap) throws SQLException {
		
		List<OrderVO> orderlist = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, orderno, fk_userid, total_cnt, total_price, orderday "
						+ " from tbl_order "
						+ " ) "
						+ " select rno, orderno, fk_userid, total_cnt, total_price, orderday "
						+ " from a "
						+ " where rno between ? and ?";
			
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo")); // 현재페이지
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
			pstmt.setInt(2, (currentShowPageNo*sizePerPage));
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OrderVO ovo = new OrderVO();
				
				ovo.setOrderno(rs.getInt("orderno"));
				ovo.setFk_userid(rs.getString("fk_userid"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setOrderday(rs.getString("orderday"));
				
				orderlist.add(ovo);
			}
			
		} finally {
			close();
		}

		return orderlist;
	}

	// 주문목록페이징수
	@Override
	public int orderTotalPage(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select ceil(count(*)/?) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, orderno, fk_userid, total_cnt, total_price, orderday "
						+ " from tbl_order "
						+ " ) "
						+ " select orderno, fk_userid, total_cnt, total_price, orderday "
						+ " from a "
						+ " ) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			n = rs.getInt(1);
			
		} finally {
			close();
		}
		
		return n;
	}

	// 리뷰리스트
	@Override
	public List<ReviewVO> reviewList() throws SQLException {
		
		List<ReviewVO> reviewList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as ( "
						+ " select reviewno, fk_userid, fk_productno, "
						+ " case "
						+ " when length(title) > 10 then substr(title, 1, 10) || ' ...' "
						+ " else title end as title, "	
						+ " registerday "	
						+ " from tbl_review a join tbl_option b "
						+ " on a.fk_optionno = b.optionno "
						+ " ), "
						+ " b as ( "
						+ " select productno, "
						+ " case "
						+ " when length(name) > 12 then substr(name, 1, 12) || ' ...' "
						+ " else name end as name "	
						+ " from tbl_product "
						+ " ) "
						+ " select reviewno, fk_userid, name, title, registerday "
						+ " from a join b "
						+ " on a.fk_productno = b.productno "
						+ " where registerday between to_date(add_months(sysdate, -1)) and sysdate "
						+ " order by registerday desc ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewVO rvo = new ReviewVO();
				ProductVO pvo = new ProductVO();
				
				rvo.setReviewno(rs.getInt("reviewno"));
				rvo.setFk_userid(rs.getString("fk_userid"));
				pvo.setName(rs.getString("name"));
				rvo.setPvo(pvo);
				rvo.setTitle(rs.getString("title"));
				rvo.setRegisterday(rs.getString("registerday"));
				
				reviewList.add(rvo);
			}
			
		} finally {
			close();
		}
		return reviewList;
	}

	// 회원가입페이지수
	@Override
	public int registerTotalPage(Map<String, String> paraMap2) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select ceil(count(*)/?) "
						+ " from "
						+ " ("
						+ " with "
						+ " a as ( "
						+ " select rownum as rno, userid, name, registerday "
						+ " from tbl_member "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select fk_userid, count(*) as logincnt "
						+ " from tbl_login "
						+ " group by fk_userid "
						+ " ) "
						+ " select rno, userid, name, logincnt, registerday "
						+ " from a join b "
						+ " on a.userid = b.fk_userid "
						+ " ) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(paraMap2.get("sizePerpage")));
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			n = rs.getInt(1);
			
		} finally {
			close();
		}		
		return n;
	}

	// 회원가입리스트
	@Override
	public List<MemberVO> registerlist(Map<String, String> paraMap2) throws SQLException {
		
		List<MemberVO> registerlist = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as ( "
						+ " select rownum as rno, userid, name, registerday "
						+ " from tbl_member "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select fk_userid, count(*) as logincnt "
						+ " from tbl_login "
						+ " group by fk_userid "
						+ " ) "
						+ " select rno, userid, name, logincnt, registerday "
						+ " from a join b "
						+ " on a.userid = b.fk_userid"
						+ " where rno between ? and ? ";
			
			int currentShowPageno = Integer.parseInt(paraMap2.get("currentShowPageno")); // 현재페이지
			int sizePerpage = Integer.parseInt(paraMap2.get("sizePerpage"));
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, (currentShowPageno*sizePerpage)-(sizePerpage-1));
			pstmt.setInt(2, (currentShowPageno*sizePerpage));
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				
				mvo.setUserid(rs.getString("userid"));
				mvo.setName(rs.getString("name"));
				mvo.setLogincnt(rs.getInt("logincnt"));
				mvo.setRegisterday(rs.getString("registerday"));
				
				registerlist.add(mvo);
			}

		} finally {
			close();
		}
		
		return registerlist;
	}

	// 최근회원가입수
	@Override
	public int registerListCnt() throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select count(*) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as ( "
						+ " select rownum as rno, userid, name, registerday "
						+ " from tbl_member "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select fk_userid, count(*) as logincnt "
						+ " from tbl_login "
						+ " group by fk_userid "
						+ " ) "
						+ " select rno, userid, name, logincnt, registerday"
						+ " from a join b "
						+ " on a.userid = b.fk_userid ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n = rs.getInt(1);
			}
			
		} finally {
			close();
		}
		
		return n;
		
	}

	// 최근주문수
	@Override
	public int orderListCnt() throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select count(*) "
						+ " from "
						+ " ( select rownum as rno, orderno, fk_userid, total_cnt, total_price, orderday "
						+ " from tbl_order ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n = rs.getInt(1);
			}
			
		} finally {
			close();
		}
		
		return n;
	}


}//end of class..


