package category.model;

import java.sql.SQLException;
import java.util.List;

import category.domain.CategoryVO;

public interface CategoryDAO {
	
	// 헤더카테고리목록조회
	List<CategoryVO> getHeaderCategoryList() throws SQLException;

	// 카테고리 목록을 조회해오기
	List<CategoryVO> getCategoryList(String major_category) throws SQLException;

	

	
}
