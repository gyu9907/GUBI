package product.domain;

public class ProductImgVO {
	
	 private int product_imgno; 	/* 상품 이미지 일련번호 */
	 private int fk_productno; 		/* 상품 일련번호 */
	 private String img; 			/* 이미지 */
	 
	 // getter setter 
	 public int getProduct_imgno() {
		return product_imgno;
	 }
	 public void setProduct_imgno(int product_imgno) {
	 	this.product_imgno = product_imgno;
	 }
	 public int getFk_productno() {
		return fk_productno;
	 }
	 public void setFk_productno(int fk_productno) {
		this.fk_productno = fk_productno;
	 }
	 public String getImg() {
		return img;
	 }
	 public void setImg(String img) {
		this.img = img;
	 }
}
