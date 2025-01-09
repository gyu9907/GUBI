package category.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import category.domain.CategoryVO;

public interface CategoryDAO {
	
	// 헤더카테고리목록조회
	List<CategoryVO> getHeaderCategoryList() throws SQLException;

	// 카테고리 목록을 조회해오기
	List<CategoryVO> getCategoryList(String major_category) throws SQLException;

	// 카테고리 목록 select 
	List<CategoryVO> CategorySelectAll() throws SQLException;

	// 카테고리 추가
	int addCategory(Map<String, String> paraMap) throws SQLException;

	// 카테고리삭제
	int deleteCategory(String categoryno) throws SQLException;

	// 카테고리 조회
	List<CategoryVO> selectCategory(Map<String, String> paraMap) throws SQLException;

	// 카테고리 개수
	int categoryCnt(Map<String, String> paraMap) throws SQLException;

	// 카테고리 중복검사
	boolean categoryDuplicateCheck(String smallCategory) throws SQLException;

	// 수정 가능한 카테고리 목록 ( is_delete = 0)
	List<CategoryVO> updateCategorySelect() throws SQLException;

	// 카테고리 대분류 조회하기
	List<CategoryVO> majorCategory() throws SQLException;

	// 카테고리 수정하기
	int updateCategory(Map<String, String> paraMap) throws SQLException;

	// 카테고리등록의 카테고리개수
	List<String> categorycnt(String categoryno) throws SQLException;
}
