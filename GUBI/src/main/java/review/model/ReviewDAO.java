package review.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import category.domain.CategoryVO;
import product.domain.ProductVO;
import review.domain.AskVO;
import review.domain.ReviewVO;

public interface ReviewDAO {

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지수 알아오기 //
	int getTotalPage(Map<String, String> paraMap) throws SQLException ;

	// 리뷰목록
	List<ReviewVO> reviewList(Map<String, String> paraMap) throws SQLException ;

	// 조건검색한 리뷰수 
	int reviewCnt(Map<String, String> paraMap) throws SQLException ;

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
	
	// *** 문의 *** //
	// 문의리스트
	List<AskVO> askList(Map<String, String> paraMap) throws SQLException ;

	// 문의 총 페이지수 구하기
	int askTotalPage(Map<String, String> paraMap) throws SQLException;

	// 문의개수구하기
	int askCnt(String ask_category) throws SQLException;

	// 문의상세
	AskVO detailAsk(String askno) throws SQLException;

	// 문의 답변작성하기
	int answerAdd(String answer, String askno) throws SQLException;
}
