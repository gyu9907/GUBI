package review.model;

import java.sql.SQLException;
import java.util.Map;

public interface ReviewDAO {

	// 리뷰를 작성하여 insert 하는 메소드 
	int addReview(Map<String, String> paraMap) throws SQLException;

	// 상품을 구매한 유저가 작성한 리뷰 수정하는 메소드
	int reviewEdit(Map<String, String> paraMap) throws SQLException;

	// 상품을 구매한 유저가 작성한 리뷰 삭제하는 메소드
	int reviewDelete(String reviewno) throws SQLException;

}
