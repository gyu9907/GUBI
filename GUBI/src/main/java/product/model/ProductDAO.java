package product.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import product.domain.OptionVO;
import product.domain.ProductVO;

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

}
