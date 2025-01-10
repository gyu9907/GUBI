package ask.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ask.domain.AskVO;

public interface AskDAO {

	// 문의하기 테이블 insert
	int insertAsk(Map<String, String> paraMap) throws SQLException;

	// 분류에 따른 리스트 뽑아주기 메소드
	List<AskVO> sortAsk(Map<String, String> paraMap) throws SQLException;

	// 문의 삭제하기 메소드
	int deletetAsk(Map<String, String> paraMap) throws SQLException;

	// 회원 삭제 메소드
	int AskDelete(String[] asknoArr) throws SQLException;

	// 문의 내역 조회
	List<AskVO> getAskList(String userid) throws SQLException;
	
	
	
	
	
	

}
