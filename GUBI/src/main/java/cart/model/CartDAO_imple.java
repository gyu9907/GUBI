package cart.model;

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

import cart.domain.CartVO;
import product.domain.OptionVO;
import product.domain.ProductVO;

public class CartDAO_imple implements CartDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public CartDAO_imple() {

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
	
	// 장바구니 목록 조회
	@Override
	public List<CartVO> getCartList(String[] cartno_arr) throws SQLException {
		List<CartVO> cartList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cartno, fk_optionno, fk_userid, c.cnt "
					   + "      , p.name AS p_name, (p.price * c.cnt) as price, p.point_pct, p.delivery_price "
					   + "      , o.name AS o_name, o.img AS o_img "
					   + " from tbl_cart c "
					   + " join tbl_option o "
					   + " on c.fk_optionno = o.optionno "
					   + " join tbl_product p "
					   + " on o.fk_productno = p.productno "
					   + " where cartno IN ( ";
			
			for(int i=0; i< cartno_arr.length; i++) {
				String comma = (i!=0) ? "," : "";
				sql += comma + " ?";
			}
			
			sql += " ) and c.cnt <= p.cnt ";
			
			pstmt = conn.prepareStatement(sql);
			
			int i = 1;
			
			for(String cartno : cartno_arr) {
				pstmt.setString(i++, cartno);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CartVO cartvo = new CartVO();
				
				cartvo.setCartno(rs.getInt("cartno"));
				cartvo.setFk_optionno(rs.getInt("fk_optionno"));
				cartvo.setFk_userid(rs.getString("fk_userid"));
				cartvo.setCnt(rs.getInt("cnt"));
				
				ProductVO pvo = new ProductVO();
				pvo.setName(rs.getString("p_name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setPoint_pct(rs.getInt("point_pct"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				cartvo.setProductVO(pvo);
				
				OptionVO ovo = new OptionVO();
				ovo.setName(rs.getString("o_name"));
				ovo.setImg(rs.getString("o_img"));
				cartvo.setOptionVO(ovo);
				
				cartList.add(cartvo);
			}
			
		} finally {
			close();
		}
		
		return cartList;
	}
	
	// 장바구니 목록 조회(회원 아이디)
	@Override
	public List<CartVO> getCartList(String userid) throws SQLException {
		List<CartVO> cartList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cartno, fk_optionno, fk_userid, c.cnt AS c_cnt "
					   + "      , p.name AS p_name, (p.price * c.cnt) as price, p.point_pct, p.delivery_price, p.cnt AS p_cnt "
					   + "      , o.name AS o_name, o.img AS o_img "
					   + " from tbl_cart c "
					   + " join tbl_option o "
					   + " on c.fk_optionno = o.optionno "
					   + " join tbl_product p "
					   + " on o.fk_productno = p.productno "
					   + " where c.fk_userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CartVO cartvo = new CartVO();
				
				cartvo.setCartno(rs.getInt("cartno"));
				cartvo.setFk_optionno(rs.getInt("fk_optionno"));
				cartvo.setFk_userid(rs.getString("fk_userid"));
				cartvo.setCnt(rs.getInt("c_cnt"));
				
				ProductVO pvo = new ProductVO();
				pvo.setName(rs.getString("p_name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setPoint_pct(rs.getInt("point_pct"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				pvo.setCnt(rs.getInt("p_cnt"));
				cartvo.setProductVO(pvo);
				
				OptionVO ovo = new OptionVO();
				ovo.setName(rs.getString("o_name"));
				ovo.setImg(rs.getString("o_img"));
				cartvo.setOptionVO(ovo);
				
				cartList.add(cartvo);
			}
			
		} finally {
			close();
		}
		
		return cartList;
	}

	// 총 금액, 포인트, 배송비를 가져오는 메소드(장바구니 일련번호)
	@Override
	public Map<String, String> getTotal(String[] cartno_arr) throws SQLException {
		Map<String, String> resultMap = new HashMap<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT SUM(P.price * C.cnt) + MIN(P.delivery_price) AS total_price"
					   + "      , SUM(P.price * P.point_pct/100 * C.cnt) AS total_point"
					   + "      , MIN(P.delivery_price) AS total_delivery "
					   + " FROM tbl_cart C "
					   + " JOIN tbl_option O "
					   + " ON C.fk_optionno = O.optionno "
					   + " JOIN tbl_product P "
					   + " ON O.fk_productno = P.productno "
					   + " WHERE cartno IN ( ";
			
			for(int i=0; i< cartno_arr.length; i++) {
				String comma = (i!=0) ? "," : "";
				sql += comma + " ?";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			int i = 1;
			
			for(String cartno : cartno_arr) {
				pstmt.setString(i++, cartno);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				resultMap.put("total_price", rs.getString("total_price"));
				resultMap.put("total_point", rs.getString("total_point"));
				resultMap.put("total_delivery", rs.getString("total_delivery"));
			}
			
		} finally {
			close();
		}
		
		return resultMap;
	}

	// 총 금액, 포인트, 배송비를 가져오는 메소드(회원 아이디)
	@Override
	public Map<String, String> getTotal(String userid) throws SQLException {
		Map<String, String> resultMap = new HashMap<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT SUM(P.price * C.cnt) + MIN(P.delivery_price) AS total_price"
					   + "      , SUM(P.price * P.point_pct/100 * C.cnt) AS total_point"
					   + "      , MIN(P.delivery_price) AS total_delivery "
					   + " FROM tbl_cart C "
					   + " JOIN tbl_option O "
					   + " ON C.fk_optionno = O.optionno "
					   + " JOIN tbl_product P "
					   + " ON O.fk_productno = P.productno "
					   + " WHERE c.fk_userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				resultMap.put("total_price", rs.getString("total_price"));
				resultMap.put("total_point", rs.getString("total_point"));
				resultMap.put("total_delivery", rs.getString("total_delivery"));
			}
			
		} finally {
			close();
		}
		
		return resultMap;
	}

	// 장바구니 개수 수정
	@Override
	public int updateCart(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " UPDATE tbl_cart SET cnt = ( "
					   + "  select case when P.cnt <= 0 then 1 when P.cnt >= to_number(?) then to_number(?) else P.cnt end "
					   + "  from tbl_product P "
					   + "  join tbl_option O "
					   + "  on P.productno = O.fk_productno "
					   + "  join tbl_cart C "
					   + "  on C.fk_optionno = O.optionno "
					   + "  where cartno = to_number(?)) "
					   + " WHERE cartno = to_number(?) AND fk_userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("cnt"));
			pstmt.setString(2, paraMap.get("cnt"));
			pstmt.setString(3, paraMap.get("cartno"));
			pstmt.setString(4, paraMap.get("cartno"));
			pstmt.setString(5, paraMap.get("fk_userid"));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;
	}

	// 장바구니 삭제
	@Override
	public int deleteCart(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " DELETE FROM tbl_cart WHERE cartno = ? AND fk_userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("cartno"));
			pstmt.setString(2, paraMap.get("fk_userid"));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;
	}

}
