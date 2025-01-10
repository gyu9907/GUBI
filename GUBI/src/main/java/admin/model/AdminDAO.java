package admin.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import admin.domain.AdminVO;
import admin.domain.StatisticsVO;
import member.domain.MemberVO;
import order.domain.OrderVO;
import review.domain.ReviewVO;

public interface AdminDAO {

	// 관리자 로그인 메소드
	AdminVO adminlogin(Map<String, String> paraMap) throws SQLException;

	// 카테고리별 주문 통계
	List<StatisticsVO> getOrderStatByCategory(Map<String, String> paraMap) throws SQLException;
	
	// 날짜별 주문 통계
	List<StatisticsVO> getOrderStatByDate(Map<String, String> paraMap) throws SQLException;

	// 접속자수 통계
	List<StatisticsVO> getLoginStat(String date) throws SQLException;

	// 방문자통계
	List<String> visitorCnt() throws SQLException;

	// 회원가입통계
	List<String> registerCnt() throws SQLException;

	// 구매수통계
	List<String> purchaseCnt() throws SQLException;

	// 판매금액통계
	List<String> salesCnt() throws SQLException;

	// 페이징 주문목록
	List<OrderVO> orderlist(Map<String, String> paraMap) throws SQLException;

	// 주문리스트페이지수
	int orderTotalPage(Map<String, String> paraMap) throws SQLException;

	// 리뷰목록 
	List<ReviewVO> reviewList() throws SQLException;

	// 회원가입페이지수
	int registerTotalPage(Map<String, String> paraMap2) throws SQLException;

	// 회원가입리스트
	List<MemberVO> registerlist(Map<String, String> paraMap2) throws SQLException;

	// 최근회원가입수
	int registerListCnt() throws SQLException;

	// 최근주문수
	int orderListCnt() throws SQLException;
	
	

	
	
	
	
	
	
	
	
	
}
