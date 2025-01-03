package member.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import member.domain.MemberVO;

public interface MemberDAO {

	// 회원목록조회
	//List<MemberVO> memberSelectAll() throws SQLException;

	// 회원수조회
	int memberAllCnt() throws SQLException;

	// 회원 조건검색
	List<MemberVO> optionMemberList(Map<String, String> paraMap) throws SQLException;

	// 조건 검색한 회원수
	int optionMembercnt(Map<String, String> paraMap) throws SQLException;

	// 페이징처리 위한 총페이지수 
	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	// 회원 탈퇴시키기 
	int deleteMember(String deleteuserid) throws SQLException;

	// 회원 복구시키기
	int recoverMember(String recoveruserid)  throws SQLException;

}
