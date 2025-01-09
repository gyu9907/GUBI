package ask.domain;

public class AskVO {

	/* 문의 */
//	CREATE TABLE tbl_ask (
//	   askno NUMBER NOT NULL, /* 문의 일련번호 */
//	   fk_userid VARCHAR2(20) NOT NULL, /* 회원 아이디 */
//	   fk_productno NUMBER NOT NULL, /* 상품 일련번호 */
//	   fk_adminid VARCHAR2(20) NOT NULL, /* 관리자 아이디 */
//	   question NVARCHAR2(100) NOT NULL, /* 질문 */
//	   answer NVARCHAR2(200), /* 답변 */
//	   passwd VARCHAR2(200) NOT NULL, /* 비밀번호 */
//	   is_hide NUMBER(1) NOT NULL, /* 공개여부 */ -- 공개 0, 비공개 1
//	   registerday DATE DEFAULT SYSDATE NOT NULL, /* 등록일자 */
//	   answerday DATE, /* 답변일자 */
//	   ask_category NUMBER(1) NOT NULL, /* 분류 */ -- 0:상품문의/1:배송문의/2:기타
//
//	   CONSTRAINT PK_tbl_ask_qnano PRIMARY KEY(askno),
//	   CONSTRAINT FK_tbl_ask_fk_userid foreign key(fk_userid) REFERENCES tbl_member(userid), 
//	   CONSTRAINT FK_tbl_ask_fk_productno foreign key(fk_productno) REFERENCES tbl_product(productno), 
//	   CONSTRAINT FK_tbl_ask_fk_adminid foreign key(fk_adminid) REFERENCES tbl_admin(adminid),
//	   CONSTRAINT CK_tbl_ask_is_hide check(is_hide in(0, 1)), -- 0, 1만 들어갈 수 있음
//	   CONSTRAINT CK_tbl_ask_ask_category check(ask_category in(0, 1, 2)) -- 0:상품문의/1:배송문의/2:기타만 들어갈 수 있음
	
	
	// field
		private int askno;           // 문의 일렬번호
		private String fk_userid;    // 회원 아이디
		private int fk_productno;    // 상품 일련번호
		private String fk_adminid;   // 관리자 아이디
		private String question;     // 질문
		private String answer;       // 답변
		private String passwd;       // 공개여부 비밀번호
		private int is_hide;         // 공개여부 공개 0, 비공개 1
		private String registerday;  // 등록일자
		private String answerday;    // 답변일자
		private int ask_category;    // 분류    0:상품문의/1:배송문의/2:기타
		
		
		
		//
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
		public int getAsk_category() {
			return ask_category;
		}
		public void setAsk_category(int ask_category) {
			this.ask_category = ask_category;
		}

		
		
		// join
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
