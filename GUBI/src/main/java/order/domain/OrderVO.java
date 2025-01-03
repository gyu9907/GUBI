package order.domain;

import member.domain.MemberVO;
import product.domain.OptionVO;
import product.domain.ProductVO;

public class OrderVO {

	private int orderno; /* 주문 일련번호 */
	private String fk_userid; /* 회원 아이디 */
	private int fk_deliveryno; /* 배송지 일련번호 */
	private int total_price; /* 총 결제금액 */
	private int total_cnt; /* 총 수량 */
	private int payment; /* 결제수단 */
	private String orderday;  /* 주문일자 */
	private String delivery_end_day; /* 배송완료일자 */
	private String status; /* 주문현황  */ /* 1결제대기/2주문완료/3주문취소/4배송중/5배송완료/6구매확정/7환불접수/8환불완료 */

	
	
	// select 
	private int statusCnt;
	
	public int getStatusCnt() {
		return statusCnt;
	}
	public void setStatusCnt(int statusCnt) {
		this.statusCnt = statusCnt;
	}
	MemberVO mvo;
	OptionVO opvo;
	ProductVO pvo;
	OrderDetailVO odvo;
	
	public OptionVO getOpvo() {
		return opvo;
	}
	public void setOpvo(OptionVO opvo) {
		this.opvo = opvo;
	}
	public ProductVO getPvo() {
		return pvo;
	}
	public void setPvo(ProductVO pvo) {
		this.pvo = pvo;
	}
	public OrderDetailVO getOdvo() {
		return odvo;
	}
	public void setOdvo(OrderDetailVO odvo) {
		this.odvo = odvo;
	}
	public MemberVO getMvo() {
		return mvo;
	}
	public void setMvo(MemberVO mvo) {
		this.mvo = mvo;
	}
	public String getName() {
		return mvo.getName();
	}
	public String getTel() {
		return mvo.getTel();
	}
	// getter setter
	public int getOrderno() {
		return orderno;
	}
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public int getFk_deliveryno() {
		return fk_deliveryno;
	}
	public void setFk_deliveryno(int fk_deliveryno) {
		this.fk_deliveryno = fk_deliveryno;
	}
	public int getTotal_price() {
		return total_price;
	}
	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}
	public int getTotal_cnt() {
		return total_cnt;
	}
	public void setTotal_cnt(int total_cnt) {
		this.total_cnt = total_cnt;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public String getOrderday() {
		return orderday;
	}
	public void setOrderday(String orderday) {
		this.orderday = orderday;
	}
	public String getDelivery_end_day() {
		return delivery_end_day;
	}
	public void setDelivery_end_day(String delivery_end_day) {
		this.delivery_end_day = delivery_end_day;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
