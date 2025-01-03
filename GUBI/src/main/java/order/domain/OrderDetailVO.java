package order.domain;

public class OrderDetailVO {

	private int cart_productno; /* 주문상세 일련번호 */
	private int fk_orderno; /* 주문 일련번호 */
	private int fk_optionno; /* 옵션 일련번호 */
	private int cnt; /* 수량 */
	private int price; /* 단가 */
	public int getCart_productno() {
		return cart_productno;
	}
	public void setCart_productno(int cart_productno) {
		this.cart_productno = cart_productno;
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
	
	
	
	
}
