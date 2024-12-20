package product.domain;

public class ProductVO {
	
	private int productno; 		/* 상품 일련번호 */
	private int fk_categoryno;  /* 카테고리 일련번호 */
	private String name; 		/* 상품명 */
	private String description; /* 상세설명 */
	private int price; 			/* 가격 */
	private String thumbnail_img; 	/* 상품 이미지 */
	private String registerday; 	/* 등록일자 */
	private int cnt; 				/* 재고 */
	private int delivery_price; 	/* 배송비 */
	private int is_delete; 			/* 삭제여부 */
	private int point_pct; 			/* 포인트적립비율 */
	
	// getter setter 
	public int getProductno() {
		return productno;
	}
	public void setProductno(int productno) {
		this.productno = productno;
	}
	public int getFk_categoryno() {
		return fk_categoryno;
	}
	public void setFk_categoryno(int fk_categoryno) {
		this.fk_categoryno = fk_categoryno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getThumbnail_img() {
		return thumbnail_img;
	}
	public void setThumbnail_img(String thumbnail_img) {
		this.thumbnail_img = thumbnail_img;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public int getDelivery_price() {
		return delivery_price;
	}
	public void setDelivery_price(int delivery_price) {
		this.delivery_price = delivery_price;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public int getPoint_pct() {
		return point_pct;
	}
	public void setPoint_pct(int point_pct) {
		this.point_pct = point_pct;
	}
	
	
}