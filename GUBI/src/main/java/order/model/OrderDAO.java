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

}
