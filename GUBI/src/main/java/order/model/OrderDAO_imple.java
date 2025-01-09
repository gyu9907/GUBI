package order.model;

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

import org.json.JSONArray;
import org.json.JSONObject;

import member.domain.MemberVO;
import order.domain.OrderDetailVO;
import order.domain.OrderVO;
import product.domain.OptionVO;
import product.domain.ProductVO;
import util.check.Check;
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
						+ " select rownum as rno, orderno, orderday, total_cnt, a.status, fk_userid, total_price, name, tel "
						+ " from tbl_order a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " where userid != 'admin' ";

			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String orderStatus = paraMap.get("orderStatus");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				sql += " and " + searchType + " like '%'|| ? ||'%' ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql +=  " and orderday BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') ";
			}
			if(! orderStatus.isBlank() ) {
				sql += " and a.status = ? ";
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
				ovo.setFk_userid(rs.getString("fk_userid"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setStatus(rs.getString("status"));
				ovo.setOrderno(rs.getInt("orderno"));
				
				mvo.setName(rs.getString("name"));
				mvo.setTel(aes.decrypt(rs.getString("tel")));
				ovo.setMvo(mvo);
				
				selectOrderList.add(ovo);
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
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
			
			String sql =   " select count(*) as cnt"
						 + " from "
						 + " ( "
						 + " select rownum as rno, orderno, orderday, total_cnt, a.status, fk_userid, total_price, name, tel "
						 + " from tbl_order a join tbl_member b "
						 + " on a.fk_userid = b.userid "
						 + " where userid != 'admin' ";
			 
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String orderStatus = paraMap.get("orderStatus");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				sql += " and " + searchType + " like '%'|| ? ||'%' ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql +=  " and orderday BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
					  + " AND TO_DATE(?, 'YYYY-MM-DD')" ;
			}
			if(! orderStatus.isBlank()) {
				sql += " and a.status = ? ";
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
			

			String sql =   " select ceil(count(*)/?) "
						 + " from "
						 + " ( "
						 + " select rownum as rno, orderno, orderday, total_cnt, a.status, fk_userid, total_price, name, tel "
						 + " from tbl_order a join tbl_member b "
						 + " on a.fk_userid = b.userid "
						 + " where userid != 'admin' ";
			
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String orderStatus = paraMap.get("orderStatus");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				sql += " and " + searchType + " like '%'|| ? ||'%' ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql +=  " and orderday BETWEEN TO_DATE(?, 'YYYY-MM-DD') "
					  + " AND TO_DATE(?, 'YYYY-MM-DD') " ;	
			}
			if(! orderStatus.isBlank()) {
				sql += " and a.status = ? ";
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
						+ " select orderno, productno, a.fk_userid, d.name as username, cnt, e.price as price, total_price, total_cnt, orderday, status, delivery_price, "
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
				pvo.setProductno(rs.getInt("productno"));
				ovo.setPvo(pvo);
				
				odvo.setCnt(rs.getInt("cnt"));
				ovo.setOdvo(odvo);
				
				mvo.setName(rs.getString("username"));
				ovo.setMvo(mvo);
				
				detailOrderList.add(ovo);
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
						+ " select rownum as rno, orderno, b.name, fk_userid, tel, fk_deliveryno, total_price, total_cnt, orderday, delivery_end_day, a.status "
						+ " from tbl_order a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " ) "
						+ " select  orderno, name, fk_userid, tel,fk_deliveryno, total_price, total_cnt, orderday, delivery_end_day, status "
						+ " from a "
						+ " where status = ? "
						+ " ) " ;
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
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
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, orderno, b.name, fk_userid, tel, fk_deliveryno, total_price, total_cnt, orderday, delivery_end_day, a.status "
						+ " from tbl_order a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " ) "
						+ " select rno, orderno, name, fk_userid, tel, total_price, total_cnt, orderday, delivery_end_day, status "
						+ " from a "
						+ " where status = ? "
						+ " and rno between ? and ? ";
			
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
				
				ovo.setOrderno(rs.getInt("orderno"));
				ovo.setFk_userid(rs.getString("fk_userid"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setOrderday(rs.getString("orderday"));
				ovo.setDelivery_end_day(rs.getString("delivery_end_day"));
				ovo.setStatus(rs.getString("status"));
				
				mvo.setName(rs.getString("name"));
				mvo.setTel(aes.decrypt(rs.getString("tel")));
				ovo.setMvo(mvo);
				
				statusList.add(ovo);
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return statusList;
	}

	// 주문상태별 회원수 
	@Override
	public int statusOrderCnt(String status) throws SQLException {
		
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select count(*) "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ " select rownum as rno, orderno, b.name, fk_userid, tel, fk_deliveryno, total_price, total_cnt, orderday, delivery_end_day, a.status "
						+ " from tbl_order a join tbl_member b "
						+ " on a.fk_userid = b.userid "
						+ " ) "
						+ " select  orderno, name, fk_userid, tel,fk_deliveryno, total_price, total_cnt, orderday, delivery_end_day, status "
						+ " from a "
						+ " where status = ? "
						+ " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, status);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}
		
		
		return cnt;
	}

	// status 수정하기
	@Override
	public int updateStatus(String status, String orderno) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " update tbl_order set status = ? where orderno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, status);
			pstmt.setString(2, orderno);
			
			n = pstmt.executeUpdate();
	
		} finally {
			close();
		}

		return n;
	}


	// 주문 일련번호 채번
	@Override
	public int getOrderno() throws SQLException {
		int orderno = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT seq_orderno.nextval "
					   + " FROM dual ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				orderno = rs.getInt(1);
			}
		} finally {
			close();
		}
		
		return orderno;
	}

	// 주문
	@Override
	public int insertOrder(OrderVO ovo) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false); // 수동 커밋
			
			String sql;
			
			/* 재고 차감 */
			for(OrderDetailVO odvo : ovo.getOrderDetailList()) {
				
				sql = " UPDATE tbl_product SET cnt = cnt - ? "
					+ " WHERE productno = (select fk_productno from tbl_option where optionno = ?) ";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, odvo.getCnt());
				pstmt.setInt(2, odvo.getFk_optionno());
				
				n = pstmt.executeUpdate();
				
				if(n != 1) {
					System.out.println("재고 차감 중 오류 발생");
					throw new SQLException();
				}
			}
			/* 재고 차감 완료 */
			
			/* 포인트 차감 */
			sql = " UPDATE tbl_member SET point = point - ? "
				+ " WHERE userid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ovo.getUse_point());
			pstmt.setString(2, ovo.getFk_userid());
			
			n = pstmt.executeUpdate();
			
			if(n != 1) {
				System.out.println("포인트 차감 중 오류 발생");
				throw new SQLException();
			}
			/* 포인트 차감 완료 */

			/* 주문 등록 */
			sql = " INSERT INTO tbl_order(orderno, fk_userid, fk_deliveryno, total_price, use_point, reward_point, delivery_price, total_cnt, status) "
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ovo.getOrderno());
			pstmt.setString(2, ovo.getFk_userid());
			pstmt.setInt(3, ovo.getFk_deliveryno());
			pstmt.setInt(4, ovo.getTotal_price());
			pstmt.setInt(5, ovo.getUse_point());
			pstmt.setInt(6, ovo.getReward_point());
			pstmt.setInt(7, ovo.getDelivery_price());
			pstmt.setInt(8, ovo.getTotal_cnt());
			pstmt.setInt(9, 2); // 2: 주문완료
			
			n = pstmt.executeUpdate();

			if(n != 1) {
				System.out.println("주문 등록 중 오류 발생");
				throw new SQLException();
			}
			/* 주문 등록 완료 */

			/* 주문상세 등록 */
			for(OrderDetailVO odvo : ovo.getOrderDetailList()) {
				sql = " INSERT INTO tbl_order_detail(order_detailno, fk_optionno, fk_orderno, cnt, price) "
					+ " VALUES(seq_order_detailno.nextval, ?, ?, ?, ?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, odvo.getFk_optionno());
				pstmt.setInt(2, ovo.getOrderno());
				pstmt.setInt(3, odvo.getCnt());
				pstmt.setInt(4, odvo.getPrice());
				
				n = pstmt.executeUpdate();
				
				if(n != 1) {
					System.out.println("주문상세 등록 중 오류 발생");
					throw new SQLException();
				}
			}
			/* 주문상세 등록 완료 */
			
			/* 장바구니 삭제 */
			for(OrderDetailVO odvo : ovo.getOrderDetailList()) {
				
				if(odvo.getCartno() == -1) { // 장바구니를 거치지 않고 상품을 구매한 경우
					break;
				}
				
				sql = " DELETE FROM tbl_cart "
					+ " WHERE cartno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, odvo.getCartno());
				
				n = pstmt.executeUpdate();
				
				if(n != 1) {
					System.out.println("장바구니 삭제 중 오류 발생");
					throw new SQLException();
				}
			}
			/* 장바구니 삭제 완료 */
			
			conn.commit();
			
		} catch(SQLException e) {
			conn.rollback();
		} finally {
			conn.setAutoCommit(true); // 자동커밋으로 전환
			close();
		}
		
		return n;
	}

	// 주문 일련번호로 주문 조회
	@Override
	public OrderVO getOrder(String orderno) throws SQLException {
		OrderVO ovo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select orderno, total_price, total_cnt, to_char(orderday, 'yyyy-mm-dd') AS orderday "
					   + "      , to_char(delivery_end_day, 'yyyy-mm-dd') AS delivery_end_day "
					   + "      , status, use_point, o.delivery_price, reward_point "
					   + "      , '[' || LISTAGG( "
					   + "             '{\"order_detailno\":' || order_detailno || "
					   + "             ',\"p_no\":\"' || p.productno || '\"' || "
					   + "             ',\"p_name\":\"' || cast(p.name as varchar2(100)) || '\"' || "
					   + "             ',\"op_name\":\"' || cast(op.name as varchar2(100)) || '\"' || "
					   + "             ',\"op_img\":\"' || cast(op.img as varchar2(200)) || '\"' || "
					   + "             ',\"cnt\":\"' || od.cnt || '\"' || "
					   + "             ',\"price\":\"' || od.price || '\"}', "
					   + "             ',' "
					   + "        ) WITHIN GROUP (ORDER BY order_detailno) || ']' AS order_detail_json "
					   + " from tbl_order o "
					   + " join tbl_order_detail od "
					   + " on o.orderno = od.fk_orderno "
					   + " join tbl_option op "
					   + " on od.fk_optionno = op.optionno "
					   + " join tbl_product p "
					   + " on op.fk_productno = p.productno "
					   + " where orderno = ? "
					   + " group by orderno, total_price, total_cnt, to_char(orderday, 'yyyy-mm-dd') "
					   + "      , to_char(delivery_end_day, 'yyyy-mm-dd') "
					   + "      , status, use_point, o.delivery_price, reward_point ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ovo = new OrderVO();
				
				ovo.setOrderno(rs.getInt("orderno"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setOrderday(rs.getString("orderday"));
				ovo.setDelivery_end_day(rs.getString("delivery_end_day"));
				ovo.setStatus(rs.getString("status"));
				ovo.setUse_point(rs.getInt("use_point"));
				ovo.setDelivery_price(rs.getInt("delivery_price"));
				ovo.setReward_point(rs.getInt("reward_point"));
				
				// 주문상세를 JSON 배열 객체로 가져온다
				JSONArray jsonArr = new JSONArray(rs.getString("order_detail_json"));
				
				List<OrderDetailVO> orderDetailList = new ArrayList<>();
				
				for(int i=0; i<jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					
					OrderDetailVO odvo = new OrderDetailVO();
					odvo.setOrder_detailno(jsonObj.getInt("order_detailno"));
					odvo.setP_no(jsonObj.getInt("p_no"));
					odvo.setP_name(jsonObj.getString("p_name"));
					odvo.setOp_name(jsonObj.getString("op_name"));
					odvo.setOp_img(jsonObj.getString("op_img"));
					odvo.setCnt(jsonObj.getInt("cnt"));
					odvo.setPrice(jsonObj.getInt("price"));
					
					orderDetailList.add(odvo);
				}

				ovo.setOrderDetailList(orderDetailList);
			}
			
		} finally {
			close();
		}
				
		return ovo;
	}

	// 회원 아이디로 주문 목록 조회
	@Override
	public List<OrderVO> getOrderList(Map<String, String> paraMap) throws SQLException {
		List<OrderVO> orderList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select * "
					   + " from "
					   + " ( "
					   + " select rownum as rno, orderno, total_price, total_cnt, orderday "
					   + "      , delivery_end_day "
					   + "      , status, use_point, delivery_price, reward_point "
					   + "      , order_detail_json "
					   + " from "
					   + " ( "
					   + " select orderno, total_price, total_cnt, to_char(orderday, 'yyyy-mm-dd') AS orderday "
					   + "      , to_char(delivery_end_day, 'yyyy-mm-dd') AS delivery_end_day "
					   + "      , o.status, use_point, o.delivery_price, reward_point "
					   + "      , '[' || LISTAGG( "
					   + "             '{\"order_detailno\":' || order_detailno || "
					   + "             ',\"p_no\":\"' || p.productno || '\"' || "
					   + "             ',\"p_name\":\"' || cast(p.name as varchar2(100)) || '\"' || "
					   + "             ',\"op_name\":\"' || cast(op.name as varchar2(100)) || '\"' || "
					   + "             ',\"op_img\":\"' || cast(op.img as varchar2(200)) || '\"' || "
					   + "             ',\"reviewno\":\"' || r.reviewno || '\"' || "
					   + "             ',\"cnt\":\"' || od.cnt || '\"' || "
					   + "             ',\"price\":\"' || od.price || '\"}', "
					   + "             ',' "
					   + "        ) WITHIN GROUP (ORDER BY order_detailno) || ']' AS order_detail_json "
					   + " from tbl_order o "
					   + " join tbl_member m "
					   + " on m.userid = o.fk_userid "
					   + " join tbl_order_detail od "
					   + " on o.orderno = od.fk_orderno "
					   + " join tbl_option op "
					   + " on od.fk_optionno = op.optionno "
					   + " join tbl_product p "
					   + " on op.fk_productno = p.productno"
					   + " left join tbl_review r "
					   + " on r.fk_optionno = od.fk_optionno "
					   + " where userid = ? ";
			
			String status = paraMap.get("status");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");

			if(!Check.isNullOrBlank(status)) {
				if("refund".equals(status)) {
					sql += " AND o.status NOT IN(1,2,4,5,6) ";
				}
				else {
					sql += " AND o.status IN(1,2,4,5,6) ";
				}
			}
			if (!Check.isNullOrBlank(startDate)) {
				sql += " AND orderday >= to_date(?, 'yyyy-mm-dd') ";
			}

			if (!Check.isNullOrBlank(endDate)) {
				sql += " AND orderday <= to_date(?, 'yyyy-mm-dd') + 1 ";
			}

			sql += " group by orderno, total_price, total_cnt, to_char(orderday, 'yyyy-mm-dd') "
				 + "      , to_char(delivery_end_day, 'yyyy-mm-dd') "
				 + "      , o.status, use_point, o.delivery_price, reward_point "
				 + " order by orderday desc "
				 + " )) "
				 + " where rno between ? and ? ";

			/*
			 * === 페이징처리의 공식 === where RNO between (조회하고자하는페이지번호 * 한페이지당보여줄행의개수) -
			 * (한페이지당보여줄행의개수 - 1) and (조회하고자하는페이지번호 * 한페이지당보여줄행의개수);
			 */
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));

			pstmt = conn.prepareStatement(sql);

			int index = 1;
			pstmt.setString(index++, paraMap.get("userid"));

//			if(!Check.isNullOrBlank(status)) {
//				pstmt.setString(index++, status);
//			}
			if (!Check.isNullOrBlank(startDate)) {
				pstmt.setString(index++, startDate);
			}

			if (!Check.isNullOrBlank(endDate)) {
				pstmt.setString(index++, endDate);
			}

			pstmt.setInt(index++, (currentShowPageNo * sizePerPage) - (sizePerPage - 1)); // 공식
			pstmt.setInt(index++, (currentShowPageNo * sizePerPage)); // 공식
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {

				OrderVO ovo = new OrderVO();
				
				ovo.setOrderno(rs.getInt("orderno"));
				ovo.setTotal_price(rs.getInt("total_price"));
				ovo.setTotal_cnt(rs.getInt("total_cnt"));
				ovo.setOrderday(rs.getString("orderday"));
				ovo.setDelivery_end_day(rs.getString("delivery_end_day"));
				ovo.setStatus(rs.getString("status"));
				ovo.setUse_point(rs.getInt("use_point"));
				ovo.setDelivery_price(rs.getInt("delivery_price"));
				ovo.setReward_point(rs.getInt("reward_point"));
				
				// 주문상세를 JSON 배열 객체로 가져온다
				JSONArray jsonArr = new JSONArray(rs.getString("order_detail_json"));
				
				List<OrderDetailVO> orderDetailList = new ArrayList<>();
				
				for(int i=0; i<jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					
					OrderDetailVO odvo = new OrderDetailVO();
					odvo.setOrder_detailno(jsonObj.getInt("order_detailno"));
					odvo.setP_no(jsonObj.getInt("p_no"));
					odvo.setP_name(jsonObj.getString("p_name"));
					odvo.setOp_name(jsonObj.getString("op_name"));
					odvo.setOp_img(jsonObj.getString("op_img"));
					odvo.setCnt(jsonObj.getInt("cnt"));
					odvo.setPrice(jsonObj.getInt("price"));
					
					orderDetailList.add(odvo);
				}

				ovo.setOrderDetailList(orderDetailList);
				
				orderList.add(ovo);
			}
		} finally {
			close();
		}
		
		return orderList;
	}

	// 회원 아이디와 기간으로 주문 목록 조회 총 개수
	@Override
	public int getTotalOrder(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;

		try {
			conn = ds.getConnection();
			
			String sql = " select count(*) "
					   + " from "
					   + " ( "
					   + " select orderno, total_price, total_cnt, to_char(orderday, 'yyyy-mm-dd') AS orderday "
					   + "      , to_char(delivery_end_day, 'yyyy-mm-dd') AS delivery_end_day "
					   + "      , o.status, use_point, o.delivery_price, reward_point "
					   + "      , '[' || LISTAGG( "
					   + "             '{\"order_detailno\":' || order_detailno || "
					   + "             ',\"p_no\":\"' || p.productno || '\"' || "
					   + "             ',\"p_name\":\"' || cast(p.name as varchar2(100)) || '\"' || "
					   + "             ',\"op_name\":\"' || cast(op.name as varchar2(100)) || '\"' || "
					   + "             ',\"op_img\":\"' || cast(op.img as varchar2(200)) || '\"' || "
					   + "             ',\"reviewno\":\"' || r.reviewno || '\"' || "
					   + "             ',\"cnt\":\"' || od.cnt || '\"' || "
					   + "             ',\"price\":\"' || od.price || '\"}', "
					   + "             ',' "
					   + "        ) WITHIN GROUP (ORDER BY order_detailno) || ']' AS order_detail_json "
					   + " from tbl_order o "
					   + " join tbl_member m "
					   + " on m.userid = o.fk_userid "
					   + " join tbl_order_detail od "
					   + " on o.orderno = od.fk_orderno "
					   + " join tbl_option op "
					   + " on od.fk_optionno = op.optionno "
					   + " join tbl_product p "
					   + " on op.fk_productno = p.productno "
					   + " left join tbl_review r "
					   + " on r.fk_optionno = od.fk_optionno "
					   + " where userid = ? ";

			String status = paraMap.get("status");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");

			if(!Check.isNullOrBlank(status)) {
				if("refund".equals(status)) {
					sql += " AND o.status NOT IN(1,2,4,5,6) ";
				}
				else {
					sql += " AND o.status IN(1,2,4,5,6) ";
				}
			}
			if(!Check.isNullOrBlank(startDate)) {
				sql += " AND orderday >= to_date(?, 'yyyy-mm-dd') ";
			}
			
			if(!Check.isNullOrBlank(endDate)) {
				sql += " AND orderday <= to_date(?, 'yyyy-mm-dd') + 1 ";
			}
			
			sql += " group by orderno, total_price, total_cnt, to_char(orderday, 'yyyy-mm-dd') "
			     + "      , to_char(delivery_end_day, 'yyyy-mm-dd') "
			     + "      , o.status, use_point, o.delivery_price, reward_point "
			     + " order by orderday desc "
			     + " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			int i = 1;
			pstmt.setString(i++, paraMap.get("userid"));

//			if(!Check.isNullOrBlank(status)) {
//				pstmt.setString(i++, status);
//			}
			if(!Check.isNullOrBlank(startDate)) {
				pstmt.setString(i++, startDate);
			}

			if(!Check.isNullOrBlank(endDate)) {
				pstmt.setString(i++, endDate);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
			
		} finally {
			close();
		}
		
		return result;
	}

	// 주문 상태 변경
	@Override
	public int updateOrderStatus(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false); // 수동 커밋으로 전환
			
			String sql = " UPDATE tbl_order SET status = ? WHERE orderno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("status"));
			pstmt.setString(2, paraMap.get("orderno"));
			
			n = pstmt.executeUpdate();
			
			if(n != 1) {
				throw new SQLException();
			}
			
			if("6".equals(paraMap.get("status")) && n == 1) { // 구매확정으로 업데이트한 경우
				sql = " UPDATE tbl_member SET point = (select reward_point from tbl_order where orderno = ?) WHERE userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("orderno"));
				pstmt.setString(2, paraMap.get("userid"));
				
				n = pstmt.executeUpdate();
				
				if(n != 1) {
					throw new SQLException();
				}
			}
			
			conn.commit(); // 모든 과정이 완료된 경우 커밋
			
		} catch(SQLException e) {
			e.printStackTrace();
			conn.rollback(); // 오류 발생시 롤백
		} finally {
			conn.setAutoCommit(true); // 자동 커밋으로 전환
			close();
		}
		
		return n;
	}

}