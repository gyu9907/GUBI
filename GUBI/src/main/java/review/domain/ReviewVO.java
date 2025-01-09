package review.domain;

import member.domain.MemberVO;
import product.domain.ProductVO;

public class ReviewVO {

	private int reviewno; /* 리뷰 일련번호 */
	private String fk_userid; /* 회원 아이디 */
	private int fk_optionno; /* 옵션 일련번호 */
	private String  title; /* 제목 */
	private String  content; /* 내용 */
	private int  score; /* 별점 */
	private String registerday; /* 등록일자 */
	private String img;
	
	// join
	MemberVO mvo;
	ProductVO pvo;
	
	/////////////////////////////
	public MemberVO getMvo() {
		return mvo;
	}
	public ProductVO getPvo() {
		return pvo;
	}
	
	public void setPvo(ProductVO pvo) {
		this.pvo = pvo;
	}
	public void setMvo(MemberVO mvo) {
		this.mvo = mvo;
	}
	////////////////////////////////////
	
	public int getReviewno() {
		return reviewno;
	}
	public void setReviewno(int reviewno) {
		this.reviewno = reviewno;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public int getFk_optionno() {
		return fk_optionno;
	}
	public void setFk_optionno(int fk_optionno) {
		this.fk_optionno = fk_optionno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
}
