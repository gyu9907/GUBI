package product.domain;

public class OptionVO {
	
	 private int optionno; 		/* 옵션 일련번호 */
	 private int  fk_productno; /* 상품 일련번호 */
	 private String  name; 		/* 옵션명 */
	 private String  color; 	/* 색상 (#123123 형태) */
	 private String img; 		/* 옵션 이미지 */
	 
	 // getter setter 
	public int getOptionno() {
		return optionno;
	}
	public void setOptionno(int optionno) {
		this.optionno = optionno;
	}
	public int getFk_productno() {
		return fk_productno;
	}
	public void setFk_productno(int fk_productno) {
		this.fk_productno = fk_productno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
