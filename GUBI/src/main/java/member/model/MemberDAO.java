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
	int recoverMember(String recoveruserid) throws SQLException;

	// ======**** 준영이 만든 메소드들
	// 회원가입을 해주는 메소드(tbl_member 테이블에 insert)
	int registerMember(MemberVO member) throws SQLException;

	// ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true 를 리턴해주고, userid 가 존재하지 않으면 false 를 리턴한다)
	public boolean idDuplicateCheck(String userid) throws SQLException;

	// Email 중복검사 (tbl_member 테이블에서 email 가 존재하면 true 를 리턴해주고, email 가 존재하지 않으면 false 를 리턴한다)
	public boolean emailDuplicateCheck(String email) throws SQLException;

	// 로그인 메소드
	MemberVO login(Map<String, String> paraMap) throws SQLException;

	// 휴면 회원 복구 해주는 메소드
	int memberReactivation(Map<String, String> paraMap) throws SQLException;

	// 회원 비밀번호 변경하는 메소드
	int passwdUpdate(Map<String, String> paraMap) throws SQLException;

	// 아이디를 찾아주는 메소드
	String idFind(Map<String, String> paraMap) throws SQLException;

	// 기존 비밀번호랑 새 비밀번호가 다른지 확인
	boolean passwdExist(Map<String, String> paraMap) throws SQLException;

	// 회원정보 수정 메소드
	int memberEdit(MemberVO member) throws SQLException;

	// 회원이 존재하는지 검색하는 메소드
	boolean memberIsExist(MemberVO member) throws SQLException;
	
	
	
	
	
	
}
