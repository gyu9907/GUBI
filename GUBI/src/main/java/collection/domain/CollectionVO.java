package collection.domain;

import java.util.List;

import category.domain.CategoryVO;
import product.domain.ProductVO;

public class CollectionVO {
	private int collectionno;      /* 컬렉션 일련번호 */
	private String name;           /* 컬렉션 이름 */
	private String thumbnail_img;  /* 컬렉션 미리보기 이미지 */
	private String fullscreen_img; /* 컬렉션 대표 이미지 */
	private String detail_html;    /* 컬렉션 상세설명 html */
	private String designer;       /* 컬렉션 디자이너 */
	private String registerday;    /* 등록일자 */
	private int is_delete;         /* 삭제여부 */
	
	// select용 field
	List<ProductVO> productList;   /* 컬렉션에 포함된 상품 목록 */
	int productListCnt;            /* 컬렉션에 포함된 상품 개수 */
	
	CategoryVO ctgrvo; /*카테고리목록*/
	


	public int getCollectionno() {
		return collectionno;
	}

	public void setCollectionno(int collectionno) {
		this.collectionno = collectionno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail_img() {
		return thumbnail_img;
	}

	public void setThumbnail_img(String thumbnail_img) {
		this.thumbnail_img = thumbnail_img;
	}

	public String getFullscreen_img() {
		return fullscreen_img;
	}

	public void setFullscreen_img(String fullscreen_img) {
		this.fullscreen_img = fullscreen_img;
	}

	public String getDetail_html() {
		return detail_html;
	}

	public void setDetail_html(String detail_html) {
		this.detail_html = detail_html;
	}

	public String getDesigner() {
		return designer;
	}

	public void setDesigner(String designer) {
		this.designer = designer;
	}

	public String getRegisterday() {
		return registerday;
	}

	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public List<ProductVO> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductVO> productList) {
		this.productList = productList;
	}

	public int getProductListCnt() {
		return productListCnt;
	}

	public void setProductListCnt(int productListCnt) {
		this.productListCnt = productListCnt;
	}
	public CategoryVO getCtgrvo() {
		return ctgrvo;
	}

	public void setCtgrvo(CategoryVO ctgrvo) {
		this.ctgrvo = ctgrvo;
	}
	
}
