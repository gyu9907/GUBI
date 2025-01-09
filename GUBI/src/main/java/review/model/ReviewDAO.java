package review.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import review.domain.ReviewVO;

public interface ReviewDAO {
	// 내 리뷰 개수  
	int selectReviewCount(Map<String, String> paraMap) throws SQLException;

	// 내 리뷰 조회  
	List<ReviewVO> selectReviewList(Map<String, String> paraMap) throws SQLException;

	// 리뷰를 작성하여 insert 하는 메소드 
	int addReview(Map<String, String> paraMap) throws SQLException;

	// 상품을 구매한 유저가 작성한 리뷰 수정하는 메소드
	int reviewEdit(Map<String, String> paraMap) throws SQLException;

	// 상품을 구매한 유저가 작성한 리뷰 삭제하는 메소드
	int reviewDelete(String reviewno) throws SQLException;

}
