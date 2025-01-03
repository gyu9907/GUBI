package order.model;

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

import member.domain.MemberVO;
import order.domain.OrderDetailVO;
import order.domain.OrderVO;
import product.domain.OptionVO;
import product.domain.ProductVO;
import util.security.AES256;
import util.security.SecretMyKey;

public class OrderDAO_imple implements OrderDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private AES256 aes;

	// 생성자
	public OrderDAO_imple() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/semioracle");

			aes = new AES256(SecretMyKey.KEY);

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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

	
	// 조건검색 한 주문리스트
	@Override
	public List<OrderVO> selectOrderList(Map<String, String> paraMap) throws SQLException {
		
		List<OrderVO> selectOrderList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select * "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, b.orderno, a.userid, a.name, a.tel, total_price, total_cnt, orderday, b.status, delivery_end_day "
						+ " from tbl_member a join tbl_order b "
						+ " on a.userid = b.fk_userid "
						+ " ), "
						+ " b as "
						+ " (  "
						+ " select a.name as productname, a.productno, b.name as optionname, b.optionno "
						+ " from tbl_product a join tbl_option b "
						+ " on a.productno = b.fk_productno "
						+ " ), "
						+ " c as "
						+ " (  "
						+ " select cart_productno, fk_optionno, fk_orderno, price "
						+ " from tbl_order_detail "
						+ " ) "
						+ " select distinct rno, a.orderno, a.orderday, a.total_cnt, a.status, a.userid, a.name, a.tel, a.total_price"
						+ " from a join c "
						+ " on a.orderno = c.fk_orderno "
						+ " join b "
						+ " on b.optionno = c.fk_optionno "
						+ " where userid != 'admin' ";
			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String orderStatus = paraMap.get("orderStatus");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				sql += " and a." + searchType + " like '%'|| ? ||'%' ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql +=  " and a.orderday BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') ";
			}
			if(! orderStatus.isBlank() ) {
				sql += " and status = ? ";
			}

			sql += " ) where rno between ? and ? ";
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			// System.out.println("currentShowPageNo"+currentShowPageNo+ "sizePerPage"+sizePerPage);
			pstmt = conn.prepareStatement(sql);

			int index = 1;
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				pstmt.setString(index++, searchWord);
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			if(!( orderStatus.isBlank() ) ) { 
				pstmt.setInt(index++, Integer.parseInt(orderStatus));
			}

			pstmt.setInt(index++, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
			pstmt.setInt(index++, (currentShowPageNo*sizePerPage));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {		
				OrderVO ovo = new OrderVO();
				MemberVO mvo = new MemberVO();
				
				ovo.setOrderday(rs.getString("orderday"));
				ovo.setFk_userid(rs.getString("userid"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setStatus(rs.getString("status"));
				ovo.setOrderno(rs.getInt("orderno"));
				
				mvo.setName(rs.getString("name"));
				mvo.setTel(rs.getString("tel"));
				ovo.setMvo(mvo);
				
				selectOrderList.add(ovo);
			}
			
		} finally {
			close();
		}

		return selectOrderList;
	}

	// 주문개수
	@Override
	public int orderCnt(Map<String, String> paraMap) throws SQLException {
		
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select count(*) as cnt "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, b.orderno, a.userid, a.name, a.tel, total_price, total_cnt, orderday, b.status, delivery_end_day "
						+ " from tbl_member a join tbl_order b "
						+ " on a.userid = b.fk_userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select a.name as productname, a.productno, b.name as optionname, b.optionno "
						+ " from tbl_product a join tbl_option b "
						+ " on a.productno = b.fk_productno "
						+ " ), "
						+ " c as "
						+ " ( "
						+ " select cart_productno, fk_optionno, fk_orderno, price "
						+ " from tbl_order_detail "
						+ " ) "
						+ " select distinct a.orderno, a.orderday, a.total_cnt, a.status, a.userid, a.name, a.tel, a.total_price "
						+ " from a join c "
						+ " on a.orderno = c.fk_orderno "
						+ " join b "
						+ " on b.optionno = c.fk_optionno "
						+ " where userid != 'admin' ";
			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String orderStatus = paraMap.get("orderStatus");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				sql += " and a." + searchType + " like '%'|| ? ||'%' ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql +=  " and a.orderday BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
					  + " AND TO_DATE(?, 'YYYY-MM-DD')" ;
			}
			if(! orderStatus.isBlank()) {
				sql += " and status = ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			
			if(! searchType.isBlank() && ! searchWord.isBlank()) {
				pstmt.setString(index++, searchWord);
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			if(!orderStatus.isBlank() ) { 
				pstmt.setInt(index++, Integer.parseInt(orderStatus));
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt("cnt");
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
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " 	select rownum as rno, b.orderno, a.userid, a.name, a.tel, total_price, total_cnt, orderday, b.status, delivery_end_day "
						+ " 	from tbl_member a join tbl_order b "
						+ " 	on a.userid = b.fk_userid "
						+ " ), "
						+ " b as "
						+ " (  "
						+ " 	select a.name as productname, a.productno, b.name as optionname, b.optionno "
						+ " 	from tbl_product a join tbl_option b "
						+ " 	on a.productno = b.fk_productno "
						+ " ), "
						+ " c as "
						+ " ( "
						+ " 	select cart_productno, fk_optionno, fk_orderno, price "
						+ " 	from tbl_order_detail "
						+ " ) "
						+ " select distinct a.orderno, a.orderday, a.total_cnt, a.status, a.userid, a.name, a.tel, a.total_price "
						+ " from a join c "
						+ " on a.orderno = c.fk_orderno "
						+ " join b "
						+ " on b.optionno = c.fk_optionno "
						+ " where a.userid != 'admin' ";
			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String orderStatus = paraMap.get("orderStatus");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				sql += " and a." + searchType + " like '%'|| ? ||'%' ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql +=  " and a.orderday BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
					  + " AND TO_DATE(?, 'YYYY-MM-DD') " ;	
			}
			if(! orderStatus.isBlank()) {
				sql += " and status = ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
			int index = 2;
			
			if(! searchType.isBlank() && !searchWord.isBlank()) {
				pstmt.setString(index++, searchWord);
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			if(! (orderStatus.isBlank() && orderStatus == "") ) { 
				pstmt.setInt(index++, Integer.parseInt(orderStatus));
			}
			
			rs = pstmt.executeQuery();

			rs.next();
			
			totalPage = rs.getInt(1);
			
		} finally {
			close();
		}
		
		return totalPage;
	}

	// 주문현황 수량 
	@Override
	public List<OrderVO> statusCnt() throws SQLException {
		
		List<OrderVO> statusCnt = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " SELECT lvl AS status, nvl(cnt, 0) AS cnt "
						+ " FROM "
						+ " ( "
						+ "    SELECT LEVEL as lvl "
						+ "    FROM dual CONNECT BY LEVEL <= 8 "
						+ " ) "
						+ " LEFT JOIN "
						+ " ( "
						+ "    SELECT status, count(status) as cnt "
						+ "    FROM tbl_order "
						+ "    GROUP BY status "
						+ " ) "
						+ " ON lvl = status "
						+ " ORDER BY status ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OrderVO ovo = new OrderVO();
				
				ovo.setStatus(rs.getString("status"));
				ovo.setStatusCnt(rs.getInt("cnt"));
				
				statusCnt.add(ovo);
			}
			
		} finally {
			close();
		}
	
		return statusCnt;
	}

	// 상세주문리스트
	@Override
	public List<OrderVO> detailOrderList(String orderno) throws SQLException {
		
		List<OrderVO> detailOrderList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " (  "
						+ "    select orderno, fk_userid, total_price, total_cnt, orderday, status "
						+ "    from tbl_order "
						+ " ), "
						+ " b as "
						+ " ( "
						+ "    select fk_orderno, fk_optionno, cnt, price "
						+ "    from tbl_order_detail "
						+ " ), "
						+ " c as "
						+ " ( "
						+ "    select optionno, fk_productno, name, color, img "
						+ "    from tbl_option "
						+ " ), "
						+ " d as "
						+ " ( "
						+ "    select userid, name "
						+ "    from tbl_member "
						+ " ), "
						+ " e as "
						+ " ( "
						+ "    select productno, name, price, thumbnail_img, delivery_price "
						+ "    from tbl_product "
						+ " ) "
						+ " select orderno, a.fk_userid, d.name as username, cnt, e.price as price, total_price, total_cnt, orderday, status, delivery_price, "
						+ "       fk_optionno, c.name as optionname, color, c.img as optionimg, "
						+ "       e.name as productname, thumbnail_img  "
						+ " from a join b "
						+ " on a.orderno = b.fk_orderno "
						+ " join c "
						+ " on b.fk_optionno = c.optionno "
						+ " join d "
						+ " on a.fk_userid = d.userid "
						+ " join e "
						+ " on c.fk_productno = e.productno "
						+ " where orderno = ? "
						+ " order by orderno ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, orderno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OrderVO ovo = new OrderVO();
				OptionVO opvo = new OptionVO();
				ProductVO pvo = new ProductVO();
				MemberVO mvo = new MemberVO();
				OrderDetailVO odvo = new OrderDetailVO();
				
				ovo.setOrderno(rs.getInt("orderno"));
				ovo.setFk_userid(rs.getString("fk_userid"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setStatus(rs.getString("status"));
				ovo.setOrderday(rs.getString("orderday"));
				
				opvo.setColor(rs.getString("color"));
				opvo.setImg(rs.getString("optionimg"));
				opvo.setName(rs.getString("optionname"));
				opvo.setOptionno(rs.getInt("fk_optionno"));
				ovo.setOpvo(opvo);
				
				pvo.setName(rs.getString("productname"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				ovo.setPvo(pvo);
				
				odvo.setCnt(rs.getInt("cnt"));
				ovo.setOdvo(odvo);
				
				mvo.setName(rs.getString("username"));
				ovo.setMvo(mvo);
				
				detailOrderList.add(ovo);
				System.out.println(detailOrderList+"detailOrderList");
			}
			
		} finally {
			close();
		}
	
		return detailOrderList;
	}
	
	// status 페이징 처리위함
	@Override
	public int StatusTotalPage(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();	
			
			String sql =  " select ceil(count(*)/?) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, b.orderno, a.userid, a.name, a.tel, total_price, total_cnt, orderday, b.status, delivery_end_day "
						+ " from tbl_member a join tbl_order b "
						+ " on a.userid = b.fk_userid "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select a.name as productname, a.productno, b.name as optionname, b.optionno "
						+ " from tbl_product a join tbl_option b "
						+ " on a.productno = b.fk_productno "
						+ " ), "
						+ " c as "
						+ " ( "
						+ " select cart_productno, fk_optionno, fk_orderno, price "
						+ " from tbl_order_detail "
						+ " ) "
						+ " select distinct rno, a.orderno, a.orderday, a.total_cnt, a.status, a.userid, a.name, a.tel, a.total_price "
						+ " from a join c "
						+ " on a.orderno = c.fk_orderno "
						+ " join b "
						+ " on b.optionno = c.fk_optionno  "
						+ " where userid != 'admin' "
						+ " and status = ? "
						+ " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("currentShowPageNo"));
			pstmt.setString(2, paraMap.get("status"));
				
			rs = pstmt.executeQuery();

			if(rs.next()) {
				n = rs.getInt(1);
			}

		} finally {
			close();
			
		}
		
		return n;
	}

	// 상태별 목록보기 
	@Override
	public List<OrderVO> statusList(Map<String, String> paraMap) throws SQLException {
		
		List<OrderVO> statusList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select * "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, b.orderno, a.userid, a.name, a.tel, total_price, total_cnt, orderday, b.status, delivery_end_day "
						+ " from tbl_member a join tbl_order b "
						+ " on a.userid = b.fk_userid "
						+ " ), "
						+ " b as "
						+ " (  "
						+ " select a.name as productname, a.productno, b.name as optionname, b.optionno "
						+ " from tbl_product a join tbl_option b "
						+ " on a.productno = b.fk_productno "
						+ " ), "
						+ " c as "
						+ " ( "
						+ " select cart_productno, fk_optionno, fk_orderno, price "
						+ " from tbl_order_detail "
						+ " ) "
						+ " select distinct rno, a.orderno, a.orderday, a.total_cnt, a.status, a.userid, a.name, a.tel, a.total_price "
						+ " from a join c "
						+ " on a.orderno = c.fk_orderno "
						+ " join b "
						+ " on b.optionno = c.fk_optionno "
						+ " where userid != 'admin' and status = ? "
						+ " ) where rno between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			pstmt.setString(1, paraMap.get("status"));
			pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
			pstmt.setInt(3, (currentShowPageNo*sizePerPage));

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				OrderVO ovo = new OrderVO();
				MemberVO mvo = new MemberVO();
				
				ovo.setOrderday(rs.getString("orderday"));
				ovo.setFk_userid(rs.getString("userid"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setStatus(rs.getString("status"));
				ovo.setOrderno(rs.getInt("orderno"));
				
				mvo.setName(rs.getString("name"));
				mvo.setTel(rs.getString("tel"));
				ovo.setMvo(mvo);
				
				statusList.add(ovo);
			}
		} finally {
			close();
		}

		return statusList;
	}

}