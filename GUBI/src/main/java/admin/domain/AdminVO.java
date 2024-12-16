package admin.domain;

public class AdminVO {
	
	private String adminid; 	/* 관리자 아이디 */
	private String passwd; 		/* 비밀번호 SHA256 */
	private String name; 		/* 성명 */
	private String email; 		/* 이메일 AES256 */
	private String registerday; /* 가입일자 */
	
	// getter setter 
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
	

}
