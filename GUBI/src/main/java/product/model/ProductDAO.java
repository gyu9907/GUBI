package product.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import category.domain.CategoryVO;
import collection.domain.CollectionVO;
import product.domain.OptionVO;
import product.domain.ProductVO;
import review.domain.ReviewVO;

public interface ProductDAO {

	// 검색한 상품 목록을 가져오는 메소드
	List<ProductVO> selectProducts(Map<String, String> paraMap) throws SQLException;

	// 검색한 상품 목록의 전체 페이지 수를 알아오는 메소드
	int getTotalPage(Map<String, String> paraMap) throws SQLException;

	// 상품 옵션의 대표 색상을 추출하기 위한 메소드
	List<OptionVO> selectAllOptions() throws SQLException;
	
	/* >>> 뷰단에서 "페이징 처리시 보여주는 순번 공식" 에서 사용하기 위해 
    검색이 있는 또는 검색이 없는 상품의 총개수 알아오기 시작 <<< */
	public int getTotalProductCnt(Map<String, String> paraMap) throws SQLException;

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

	// 제품상세페이지에서 장바구니로 insert 하는 메소드
	int addCart(Map<String, String> paraMap) throws SQLException;

	// 제품상세페이지에서 리뷰조회하는 메소드
	List<ReviewVO> selectReview(int productno) throws SQLException;
	
	

}
