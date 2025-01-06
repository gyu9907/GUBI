package admin.model;

import java.io.UnsupportedEncodingException;
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
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class AdminDAO_imple implements AdminDAO {
	
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes; // 양방향 암호화 객체 만들어야함

	// 생성자
	public AdminDAO_imple() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/semioracle");
			
			aes = new AES256(SecretMyKey.KEY);          
			// SecretMyKey.KEY 은 우리가 만든 암호화/복호화 키이다.
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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

	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class..


