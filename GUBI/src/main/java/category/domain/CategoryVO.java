package category.domain;

public class CategoryVO {

	private int categoryno; 		/* 카테고리 일련번호 */
	private String major_category; 	/* 대분류 */
	private String small_category; 	/* 소분류 */
	private int is_delete; 			/* 삭제여부 */
	private String category_img; 			/* 삭제여부 */
	
	// getter settter 
	public int getCategoryno() {
		return categoryno;
	}
	public void setCategoryno(int categoryno) {
		this.categoryno = categoryno;
	}
	public String getMajor_category() {
		return major_category;
	}
	public void setMajor_category(String major_category) {
		this.major_category = major_category;
	}
	public String getSmall_category() {
		return small_category;
	}
	public void setSmall_category(String small_category) {
		this.small_category = small_category;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public String getCategory_img() {
		return category_img;
	}
	public void setCategory_img(String category_img) {
		this.category_img = category_img;
	}
	
	
	
	
}
