package member.domain;

public class MemberVO {

	
//	VARCHAR2(20) N/A
//  회원아이디
//	비밀번호 passwd VARCHAR2(200) SHA256
//	성명 name NVARCHAR2(10) N/A
//	생년월일 birth DATE N/A
//	이메일 email VARCHAR2(200) AES256
//	전화번호 tel VARCHAR2(200) AES256 01012341234
//	우편번호 postcode VARCHAR(5) N/A
//	주소 address NVARCHAR2(100) N/A
//	상세주소 detail_address NVARCHAR2(100) N/A
//	가입일자 registerday DATE 기본값 sysdate
//	비밀번호 변경일자 pwdupdateday DATE NULL null이면 가입일자를 참조
//	가입상태 status NUMBER(1) 기본값 0, 0: 정상 1: 탈퇴
//	휴면상태 idle NUMBER(1) 기본값 0, 0: 정상 1: 휴면
//	포인트 point NUMBER(10) N/A

	
	// field
	private String userid;          // 회원아이디
	private String passwd;          // 비밀번호 (SHA-256 암호화 대상)
	private String name;            // 회원명
	private String birth;           // 생년월일
	private String email;           // 이메일 (AES-256 암호화/복호화 대상)
	private String tel;             // 전화번호 (AES-256 암호화/복호화 대상)
	private String postcode;        // 우편번호
	private String address;         // 주소
	private String detail_address;  // 상세주소
//	private String extra_address;   // 상세 주소에 합칠 것이다!
	private String registerday;     // 가입일자
	private String passwdupdateday; // 마지막으로 비밀번호 변경일자
	private int status;             // 회원탈퇴유무 0: 사용가능(가입중) / 1:사용불능(탈퇴)
	private int idle;               // 휴면유무 0 : 활동중 / 1 : 휴면중
	                                // 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정
	private int point;              // 포인트


	// select 
	private int ordercnt;
	private int logincnt;
	private int membercnt;
	private String loginday;
	private String fulladdress;

	
	/*
	 * public String getDeletemember() { if(userid != null) { return
	 * String.join(",", userid); // System.out.println(); } else { return ""; } }//
	 * end of public String getStrFood() ---------------------
	 */


	public String getFulladdress() {
		return fulladdress;
	}





	public void setFulladdress(String fulladdress) {
		this.fulladdress = fulladdress;
	}





	public int getMembercnt() {
		return membercnt;
	}





	public void setMembercnt(int membercnt) {
		this.membercnt = membercnt;
	}





	public String getLoginday() {
		return loginday;
	}





	public void setLoginday(String loginday) {
		this.loginday = loginday;
	}


	private boolean requirePwdChange = false;
	// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 true
	// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 false





	public String getUserid() {
		return userid;
	}





	public void setUserid(String userid) {
		this.userid = userid;
	}





	public String getPasswd() {
		return passwd;
	}





	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public String getBirth() {
		return birth;
	}





	public void setBirth(String birth) {
		this.birth = birth;
	}





	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public String getTel() {
		return tel;
	}





	public void setTel(String tel) {
		this.tel = tel;
	}





	public String getPostcode() {
		return postcode;
	}





	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}





	public String getAddress() {
		return address;
	}





	public void setAddress(String address) {
		this.address = address;
	}





	public String getDetail_address() {
		return detail_address;
	}





	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}





	public String getRegisterday() {
		return registerday;
	}





	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}





	public String getPasswdupdateday() {
		return passwdupdateday;
	}





	public void setPasswdupdateday(String passwdupdateday) {
		this.passwdupdateday = passwdupdateday;
	}





	public int getStatus() {
		return status;
	}





	public void setStatus(int status) {
		this.status = status;
	}





	public int getIdle() {
		return idle;
	}





	public void setIdle(int idle) {
		this.idle = idle;
	}





	public int getPoint() {
		return point;
	}





	public void setPoint(int point) {
		this.point = point;
	}





	public boolean isRequirePwdChange() {
		return requirePwdChange;
	}





	public void setRequirePwdChange(boolean requirePwdChange) {
		this.requirePwdChange = requirePwdChange;
	}
	
	public int getOrdercnt() {
		return ordercnt;
	}
	
	
	public void setOrdercnt(int ordercnt) {
		this.ordercnt = ordercnt;
	}
	
	
	public int getLogincnt() {
		return logincnt;
	}
	
	
	public void setLogincnt(int logincnt) {
		this.logincnt = logincnt;
	}
	
	

	
	
	
	
}//end of class...
