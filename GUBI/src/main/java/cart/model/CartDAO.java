package cart.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cart.domain.CartVO;

public interface CartDAO {

	/**
	 * 장바구니 목록 조회(장바구니 일련번호)
	 * @param cartno_arr 장바구니 일련번호 배열
	 * @return
	 * @throws SQLException
	 */
	List<CartVO> getCartList(String[] cartno_arr) throws SQLException;
	
	/**
	 * 장바구니 목록 조회(회원 아이디)
	 * @param userid 회원 아이디
	 * @return
	 * @throws SQLException
	 */
	List<CartVO> getCartList(String userid) throws SQLException;

	/**
	 * 총 금액, 포인트, 배송비를 가져오는 메소드(장바구니 일련번호)
	 * @param cartno_arr 장바구니 일련번호 배열
	 * @return total_price: 총 금액, total_point: 총 포인트, total_delivery: 총 배송비
	 * @throws SQLException
	 */
	Map<String, String> getTotal(String[] cartno_arr) throws SQLException;

	/**
	 * 총 금액, 포인트, 배송비를 가져오는 메소드(회원 아이디)
	 * @param userid 회원 아이디
	 * @return total_price: 총 금액, total_point: 총 포인트, total_delivery: 총 배송비
	 * @throws SQLException
	 */
	Map<String, String> getTotal(String userid) throws SQLException;

	/**
	 * 장바구니 개수 수정. 상품 개수보다 크게 바꾸려고 하면 상품 개수만큼으로 수정됨. 상품 개수가 0이면 1로 수정됨
	 * @param paraMap cartno: 장바구니 일련번호, cnt: 개수, fk_userid: 유저 아이디
	 * @return
	 * @throws SQLException
	 */
	int updateCart(Map<String, String> paraMap) throws SQLException;

	/**
	 * 장바구니 삭제
	 * @param paraMap cartno: 장바구니 일련번호, fk_userid: 유저 아이디
	 * @return
	 * @throws SQLException
	 */
	int deleteCart(Map<String, String> paraMap) throws SQLException;

	
}
