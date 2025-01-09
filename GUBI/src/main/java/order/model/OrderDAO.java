package order.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import order.domain.OrderVO;

public interface OrderDAO {

	// 주문리스트
	List<OrderVO> selectOrderList(Map<String, String> paraMap) throws SQLException;

	// 주문수
	int orderCnt(Map<String, String> paraMap) throws SQLException;

	// 총페이지
	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	// 주문현황 count
	List<OrderVO> statusCnt() throws SQLException;

	// 주문상세보기
	List<OrderVO> detailOrderList(String orderno) throws SQLException;

	
	
	// status 페이징 처리위함
	int StatusTotalPage(Map<String, String> paraMap) throws SQLException;

	// 주문상태목록
	List<OrderVO> statusList(Map<String, String> paraMap) throws SQLException;

	// 주문상태별 회원수
	int statusOrderCnt(String status) throws SQLException;

	// status 수정하기
	int updateStatus(String status, String orderno) throws SQLException;
	
	// 밑에서부턴 이진호멤버가 추가함 //
	
	// 마이페이지에서 주문 상태 가져오는 메소드 
	Map<String, Integer> getOrderStatusByUserId(String userid) throws SQLException;

	// 주문 일련번호 채번
	int getOrderno() throws SQLException;

	// 주문
	int insertOrder(OrderVO ovo) throws SQLException;

	// 주문 일련번호로 주문 조회
	OrderVO getOrder(String orderno) throws SQLException;

	// 회원 아이디와 기간으로 주문 목록 조회
	List<OrderVO> getOrderList(Map<String, String> paraMap) throws SQLException;

	// 회원 아이디와 기간으로 주문 목록 조회 총 개수
	int getTotalOrder(Map<String, String> paraMap) throws SQLException;

	// 주문 상태 변경
	int updateOrderStatus(Map<String, String> paraMap) throws SQLException;

}
