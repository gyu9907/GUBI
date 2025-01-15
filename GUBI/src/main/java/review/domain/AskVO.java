package review.domain;

import member.domain.MemberVO;
import product.domain.ProductVO;

public class AskVO {

	private int askno; /* 문의 일련번호 */
	private String fk_userid; /* 회원 아이디 */
	private int fk_productno; /* 상품 일련번호 */
	private String fk_adminid; /* 관리자 아이디 */
	private String question; /* 질문 */
	private String answer; /* 답변 */
	private String passwd; /* 비밀번호 */
	private int is_hide; /* 공개여부 */
	private String registerday; /* 등록일자 */
	private String answerday; /* 답변일자 */
	private int category; /* 분류 */

	private MemberVO mvo;
	private ProductVO pvo;
	 
	public MemberVO getMvo() {
		return mvo;
	}
	public void setMvo(MemberVO mvo) {
		this.mvo = mvo;
	}
	public ProductVO getPvo() {
		return pvo;
	}
	public void setPvo(ProductVO pvo) {
		this.pvo = pvo;
	}
	public int getAskno() {
		return askno;
	}
	public void setAskno(int askno) {
		this.askno = askno;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public int getFk_productno() {
		return fk_productno;
	}
	public void setFk_productno(int fk_productno) {
		this.fk_productno = fk_productno;
	}
	public String getFk_adminid() {
		return fk_adminid;
	}
	public void setFk_adminid(String fk_adminid) {
		this.fk_adminid = fk_adminid;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getIs_hide() {
		return is_hide;
	}
	public void setIs_hide(int is_hide) {
		this.is_hide = is_hide;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	public String getAnswerday() {
		return answerday;
	}
	public void setAnswerday(String answerday) {
		this.answerday = answerday;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	 
	 
	 
}
