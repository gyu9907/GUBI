package admin.model;

import java.sql.SQLException;
import java.util.Map;

import admin.domain.AdminVO;

public interface AdminDAO {

	// 관리자 로그인 메소드
	AdminVO adminlogin(Map<String, String> paraMap) throws SQLException;

	
	
	
	
	
	
	
	
	
}
