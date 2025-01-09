package order.domain;

public class OrderDetailVO {

	private int order_detailno; /* 주문상세 일련번호 */
	private int fk_orderno; /* 주문 일련번호 */
	private int fk_optionno; /* 옵션 일련번호 */
	private int cnt; /* 수량 */
	private int price; /* 단가 */
	
	// select 용 field
	private int p_no;  // 상품번호
	private String p_name;  // 상품명
	private String op_name; // 옵션명
	private String op_img; // 옵션이미지
	private int reviewno; // 리뷰 일련번호
	
	// insert 용 field
	private int cartno; // 장바구니 일련번호
	
	public int getOrder_detailno() {
		return order_detailno;
	}
	public void setOrder_detailno(int cart_productno) {
		this.order_detailno = cart_productno;
	}
	public int getFk_orderno() {
		return fk_orderno;
	}
	public void setFk_orderno(int fk_orderno) {
		this.fk_orderno = fk_orderno;
	}
	public int getFk_optionno() {
		return fk_optionno;
	}
	public void setFk_optionno(int fk_optionno) {
		this.fk_optionno = fk_optionno;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getP_no() {
		return p_no;
	}
	public void setP_no(int p_no) {
		this.p_no = p_no;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getOp_name() {
		return op_name;
	}
	public void setOp_name(String op_name) {
		this.op_name = op_name;
	}
	public String getOp_img() {
		return op_img;
	}
	public void setOp_img(String op_img) {
		this.op_img = op_img;
	}
	public int getCartno() {
		return cartno;
	}
	public void setCartno(int cartno) {
		this.cartno = cartno;
	}
	public int getReviewno() {
		return reviewno;
	}
	public void setReviewno(int reviewno) {
		this.reviewno = reviewno;
	}
	
	
	
	
}
