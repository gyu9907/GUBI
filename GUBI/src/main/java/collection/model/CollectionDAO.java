package collection.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import collection.domain.CollectionImgVO;
import collection.domain.CollectionVO;

public interface CollectionDAO {

	/**
	 * 일반 회원이 컬렉션을 조회하는 메소드
	 * @param paraMap category: 조회할 카테고리, start: 시작 인덱스, end: 끝 인덱스
	 * @return
	 * @throws SQLException
	 */
	List<CollectionVO> getCollectionList(Map<String, String> paraMap) throws SQLException;

	/**
	 * 무한 스크롤 컬렉션 조회 시 컬렉션의 총 개수를 알아오는 메소드
	 * @param major_category "SEATING" or "LIGHTING" or "TABLE"
	 * @return
	 * @throws SQLException
	 */
	int totalCollectionCnt(String major_category) throws SQLException;

	/**
	 * 일반 회원에 의해 컬렉션 하나의 정보를 가져오는 메소드
	 * @param collectionno
	 * @return
	 */
	CollectionVO getCollectionDetail(String collectionno) throws SQLException;

	/**
	 * 다음 컬렉션 번호를 가져오는 메소드
	 * @return collectionno.nextval() 값
	 * @throws SQLException
	 */
	int getNextCollectionno() throws SQLException;

	/**
	 * 컬렉션 등록 메소드
	 * @param colvo
	 * @return
	 * @throws SQLException
	 */
	int insertCollection(CollectionVO colvo) throws SQLException;

	/**
	 * 컬렉션에 포함된 상품 목록 등록
	 * @param paraMap
	 * @return
	 * @throws SQLException
	 */
	int insertColProduct(Map<String, String> paraMap) throws SQLException;

	/**
	 * 컬렉션 상세설명 이미지 등록
	 * @param paraMap
	 * @return
	 * @throws SQLException
	 */
	int insertCollectionImg(Map<String, String> paraMap) throws SQLException;

	/**
	 * 컬렉션을 테이블에서 완전히 삭제하는 메소드, 컬렉션 등록 실패시에만 사용
	 * @param collectionno
	 * @return
	 * @throws SQLException
	 */
	int deleteCollection(int collectionno) throws SQLException;

	/**
	 * 관리자 컬렉션 검색
	 * @param paraMap 컬렉션 검색 정보
	 * @return
	 * @throws SQLException
	 */
	List<CollectionVO> getCollectionListForAdmin(Map<String, String> paraMap) throws SQLException;

	/**
	 * 관리자 컬렉션 검색 시 총 개수
	 * @param paraMap 컬렉션 검색 정보
	 * @return
	 * @throws SQLException
	 */
	int totalCollectionCntForAdmin(Map<String, String> paraMap) throws SQLException;
	
	/**
	 * 관리자가 상품으로 컬렉션 검색
	 * @param paraMap 상품 검색 정보
	 * @return
	 * @throws SQLException
	 */
	List<CollectionVO> getCollectionListByProductForAdmin(Map<String, String> paraMap) throws SQLException;

	/**
	 * 관리자가 상품으로 컬렉션 검색 시 총 개수
	 * @param paraMap 상품 검색 정보
	 * @return
	 * @throws SQLException
	 */
	int totalCntByProductForAdmin(Map<String, String> paraMap) throws SQLException;

	/**
	 * is_delete를 1로 만들어 컬렉션 삭제
	 * @param collectionno
	 * @return
	 * @throws SQLException
	 */
	int updateIsDeleteCollection(String collectionno) throws SQLException;

	/**
	 * 컬렉션의 이미지 목록을 가져오는 메소드
	 * @param collectionno
	 * @return
	 * @throws SQLException
	 */
	List<CollectionImgVO> getCollectionImgs(String collectionno) throws SQLException;
	
	/**
	 * 컬렉션의 이미지를 삭제하는 메소드
	 * @param collection_imgno 컬렉션 이미지 일련번호
	 * @return
	 * @throws SQLException
	 */
	int deleteCollectionImg(String collection_imgno) throws SQLException;

	/**
	 * 컬렉션을 수정하는 메소드
	 * @param colvo
	 * @return
	 * @throws SQLException
	 */
	int updateCollection(CollectionVO colvo) throws SQLException;

	/**
	 * 컬렉션에 포함된 상품을 모두 삭제하는 메소드
	 * @param collectionno
	 * @return
	 * @throws SQLException
	 */
	int deleteColProduct(String collectionno) throws SQLException;

}
