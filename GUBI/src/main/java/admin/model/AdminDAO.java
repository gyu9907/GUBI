package admin.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import admin.domain.AdminVO;
import admin.domain.StatisticsVO;

public interface AdminDAO {

	// 관리자 로그인 메소드
	AdminVO adminlogin(Map<String, String> paraMap) throws SQLException;

	// 카테고리별 주문 통계
	List<StatisticsVO> getOrderStatByCategory(Map<String, String> paraMap) throws SQLException;
	
	// 날짜별 주문 통계
	List<StatisticsVO> getOrderStatByDate(Map<String, String> paraMap) throws SQLException;

	// 접속자수 통계
	List<StatisticsVO> getLoginStat(String date) throws SQLException;

	
	
	
	
	
	
	
	
	
}
