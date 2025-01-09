package product.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import category.domain.CategoryVO;
import collection.domain.CollectionVO;
import product.domain.OptionVO;
import product.domain.ProductImgVO;
import product.domain.ProductVO;
import review.domain.ReviewVO;

public interface ProductDAO {

	// 검색한 상품 목록을 가져오는 메소드(페이징)
	List<ProductVO> selectProducts(Map<String, String> paraMap) throws SQLException;

	// 검색한 상품 목록을 가져오는 메소드(무한 스크롤)
	List<ProductVO> getProductListWithScroll(Map<String, String> paraMap) throws SQLException;

	// 검색한 상품 목록의 전체 페이지 수를 알아오는 메소드
	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	// 상품 옵션의 대표 색상을 추출하기 위한 메소드
	List<OptionVO> selectAllOptions() throws SQLException;
	
	// 검색이 있는 또는 검색이 없는 상품의 총개수 알아오기 
	public int getTotalProductCnt(Map<String, String> paraMap) throws SQLException;
	
	// 한 컬렉션에 포함된 상품 검색
	List<ProductVO> selectColProductList(String collectionno) throws SQLException;

	// 주문에 필요한 정보를 가져오기 위한 메소드
	Map<String, String> getProductForOrder(String optionno) throws SQLException;

	// 상품의 전체개수를 알아오기
	int totalProductCount(Map<String, String> paraMap) throws SQLException;
	
	// 모든 상품 또는 카테고리별 상품목록을 조회하는 메소드
	List<ProductVO> selectCnoProduct(Map<String, String> paraMap) throws SQLException;
	
	// 제품번호로 해당 제품의 상세정보를 조회해오는 메소드
	ProductVO selectDetailByProductno(String productno) throws SQLException;

	// 제품번호로 해당 제품의 옵션별 이미지와 옵션을 조회하는 메소드
	List<OptionVO> selectOptionList(String productno) throws SQLException;

	// 콜렉션 리스트 조회하는 메소드
	List<CollectionVO> selectCollectionList() throws SQLException;
	
	// 콜렉션별 상품 리스트 조회하는 메소드
	List<ProductVO> selectProductByCollection(Map<String, String> paraMap) throws SQLException;
	
	// 메인페이지에서 사용할 최신순 3개 신상품 조회
	List<ProductVO> selectTop3Product() throws SQLException;
	
	// 메인페이지에서 사용할 인기상품목록 조회
	List<ProductVO> selectBestProd() throws SQLException;

	// 제품상세페이지에서 장바구니로 insert 하는 메소드
	int addCart(Map<String, String> paraMap) throws SQLException;

	// 제품상세페이지에서 리뷰조회하는 메소드
	List<ReviewVO> selectReview(int productno) throws SQLException;
	
	/////////////////////////////////////////////////////////////////
	
	// 카테고리 대분류 가져오기 
	List<CategoryVO> selectMajorCategory() throws SQLException;

	// 카테고리 소분류 가져오기 
	List<CategoryVO> selectSmallCategory(String major_category) throws SQLException;

	// 상품리스트 
	List<ProductVO> selectProduct(Map<String, String> paraMap) throws SQLException;

	// 총페이지수 가져오기
	int totalPage(Map<String, String> paraMap) throws SQLException;

	// 상품개수 알아오기 
	int productCnt(Map<String, String> paraMap) throws SQLException;

	// 상품 삭제하기 
	int deleteProduct(String deleteproductno) throws SQLException;

	// 상품 재등록하기
	int recoverProduct(String recoverproductno) throws SQLException;

	// 카테고리 번호 찾기 
	int searchCategoryNo(Map<String, String> paraMap) throws SQLException;

	// 상품번호 채번하기
	int getProductNum() throws SQLException;
	
	// 상품등록하기
	int addProduct(Map<String, String> paraMap) throws SQLException;

	// 옵션 등록하기 
	int addOption(OptionVO ovo, int num) throws SQLException;

	// 이미지 등록하기
	int addImage(ProductImgVO pvo, int num) throws SQLException;

	// 상품상세보기
	List<ProductVO> detailProductList(String productno) throws SQLException;

	// 상품 옵션리스트
	List<OptionVO> optionList(String productno) throws SQLException;

	// 리뷰개수
	int reviewcnt(String productno) throws SQLException;


}
