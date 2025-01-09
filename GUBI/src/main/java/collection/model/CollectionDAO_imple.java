package collection.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import category.domain.CategoryVO;
import collection.domain.CollectionImgVO;
import collection.domain.CollectionVO;
import util.check.Check;
import util.color.ColorUtil;

public class CollectionDAO_imple implements CollectionDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public CollectionDAO_imple() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/semioracle");

		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	// 사용한 자원을 반납하는 close() 메소드 생성하기
	private void close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()----------------
	
	// 일반 회원이 컬렉션을 조회하는 메소드
	@Override
	public List<CollectionVO> getCollectionList(Map<String, String> paraMap) throws SQLException {
		List<CollectionVO> collectionList = new ArrayList<>();
		

		try {
			conn = ds.getConnection();
			
			String sql = " SELECT rownum as rno, collectionno, thumbnail_img, fullscreen_img, NAME "
					   + " FROM "
					   + " ( "
					   + "     SELECT rownum as rno, collectionno, thumbnail_img, fullscreen_img, NAME "
					   + "     FROM "
					   + "     ( "
					   + "         SELECT collectionno, thumbnail_img, NAME, fullscreen_img, registerday "
					   + "         FROM tbl_collection "
					   + "         WHERE is_delete = 0 ";
			
			String major_category = paraMap.get("major_category");
			
			if(!Check.isNullOrBlank(major_category)) {
				sql += " AND collectionno IN "
				     + "     ( "
				     + "         SELECT fk_collectionno "
				     + "         FROM tbl_col_product CP "
				     + "         JOIN tbl_product P "
				     + "         ON CP.fk_productno = productno "
				     + "         JOIN tbl_category C "
				     + "         ON P.fk_categoryno = categoryno "
				     + "         WHERE C.major_category = ? "
				     + "         GROUP BY fk_collectionno "
				     + "     ) ";
			}
			
			sql += "         ORDER BY registerday DESC "
				 + "     ) "
				 + " ) "
				 + " WHERE rno BETWEEN ? AND ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			
			if(!Check.isNullOrBlank(major_category)) {
				pstmt.setString(index++, major_category);
			}
			pstmt.setString(index++, paraMap.get("start"));
			pstmt.setString(index++, paraMap.get("end"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CollectionVO colvo = new CollectionVO();
				
				colvo.setCollectionno(rs.getInt("collectionno"));
				colvo.setThumbnail_img(rs.getString("thumbnail_img"));
				colvo.setName(rs.getString("name"));
				colvo.setFullscreen_img(rs.getString("fullscreen_img"));
				
				collectionList.add(colvo);
			}// end of while(rs.next())--------------
			
		} finally {
			close();
		}
		
		return collectionList;
	}

	// 무한 스크롤 컬렉션 조회 시 컬렉션의 총 개수를 알아오는 메소드
	@Override
	public int totalCollectionCnt(String major_category) throws SQLException {
		int result = 0;

		try {
			conn = ds.getConnection();
			
			String sql = " SELECT count(*) "
					   + " FROM "
					   + " ( "
					   + "     SELECT collectionno, thumbnail_img, NAME, registerday "
					   + "     FROM tbl_collection "
					   + "     WHERE is_delete = 0 ";
			
			if(!Check.isNullOrBlank(major_category)) {
				sql += " AND collectionno IN "
				     + "     ( "
				     + "         SELECT fk_collectionno "
				     + "         FROM tbl_col_product CP "
				     + "         JOIN tbl_product P "
				     + "         ON CP.fk_productno = productno "
				     + "         JOIN tbl_category C "
				     + "         ON P.fk_categoryno = categoryno "
				     + "         WHERE C.major_category = ? "
				     + "         GROUP BY fk_collectionno "
				     + "     ) ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			if(!Check.isNullOrBlank(major_category)) {
				pstmt.setString(1, major_category);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} finally {
			close();
		}
		
		return result;
	}

	// 일반 회원에 의해 컬렉션 하나의 정보를 가져오는 메소드
	@Override
	public CollectionVO getCollectionDetail(String collectionno) throws SQLException {
		CollectionVO colvo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select collectionno, name, thumbnail_img, fullscreen_img, designer, detail_html "
					   + " from tbl_collection "
					   + " where is_delete = 0 and collectionno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, collectionno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				colvo = new CollectionVO();
				
				colvo.setCollectionno(rs.getInt("collectionno"));
				colvo.setName(rs.getString("name"));
				colvo.setThumbnail_img(rs.getString("thumbnail_img"));
				colvo.setFullscreen_img(rs.getString("fullscreen_img"));
				colvo.setDetail_html(rs.getString("detail_html"));
				colvo.setDesigner(rs.getString("designer"));
				
			}
		} finally {
			close();
		}
		
		return colvo;
	}

	// 다음 컬렉션 번호를 가져오는 메소드
	@Override
	public int getNextCollectionno() throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select seq_collectionno.nextval "
				       + " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} finally {
			close();
		}
		
		return result;
	}

	// 컬렉션 등록 메소드
	@Override
	public int insertCollection(CollectionVO colvo) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " INSERT INTO tbl_collection(collectionno, name, designer, thumbnail_img, fullscreen_img, detail_html) "
					   + " VALUES(?, ?, ?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, colvo.getCollectionno());
			pstmt.setString(2, colvo.getName());
			pstmt.setString(3, colvo.getDesigner());
			pstmt.setString(4, colvo.getThumbnail_img());
			pstmt.setString(5, colvo.getFullscreen_img());
			pstmt.setString(6, colvo.getDetail_html());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}

		return result;
	}

	// 컬렉션에 포함된 상품 목록 등록
	@Override
	public int insertColProduct(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " INSERT INTO tbl_col_product(col_productno, fk_collectionno, fk_productno) "
					   + " VALUES(seq_col_productno.nextval, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("fk_collectionno"));
			pstmt.setString(2, paraMap.get("fk_productno"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 컬렉션 상세설명 이미지 등록
	@Override
	public int insertCollectionImg(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " INSERT INTO tbl_collection_img(collection_imgno, fk_collectionno, img) "
					   + " VALUES(seq_collection_imgno.nextval, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("fk_collectionno"));
			pstmt.setString(2, paraMap.get("img"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 컬렉션을 테이블에서 완전히 삭제하는 메소드, 컬렉션 등록 실패시에만 사용
	@Override
	public int deleteCollection(int collectionno) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " DELETE FROM tbl_collection "
					   + " WHERE collectionno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, collectionno);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 관리자 컬렉션 검색
	@Override
	public List<CollectionVO> getCollectionListForAdmin(Map<String, String> paraMap) throws SQLException {
		List<CollectionVO> collectionList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT collectionno, name, designer, thumbnail_img, fullscreen_img, is_delete, registerday, product_cnt "
					   + " FROM "
					   + " ( "
					   + " select rownum AS rno, collectionno, name, designer, thumbnail_img, fullscreen_img, is_delete, to_char(registerday, 'yyyy-mm-dd') as registerday, product_cnt "
					   + " from tbl_collection c "
					   + " join  "
					   + " ( "
					   + "     select fk_collectionno, count(*) AS product_cnt "
					   + "     from tbl_col_product "
					   + "     group by fk_collectionno "
					   + " ) cp "
					   + " on c.collectionno = cp.fk_collectionno "
					   + " where is_delete = 0 and ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			
			if(!colname.isBlank() && !searchWord.isBlank()) {
				if("collectionno".equals(colname)) {
					sql += " "+colname+" = ? ";
				}
				else {
					sql += " lower("+colname+") like '%'|| lower(?) ||'%' ";
				}
			}
			else {
				sql += "name like '%' ";
			}
			
			if(!Check.isNullOrBlank(startDate)) {
				sql += " AND registerday >= to_date(?, 'yyyy-mm-dd') ";
			}
			
			if(!Check.isNullOrBlank(endDate)) {
				sql += "AND registerday <= to_date(?, 'yyyy-mm-dd') + 1 ";
			}
			
			sql += " ) "
			     + " WHERE RNO BETWEEN ? AND ? ";
			
			pstmt = conn.prepareStatement(sql);

			/*
			    === 페이징처리의 공식 ===
			    where RNO between (조회하고자하는페이지번호 * 한페이지당보여줄행의개수) - (한페이지당보여줄행의개수 - 1) and (조회하고자하는페이지번호 * 한페이지당보여줄행의개수);
			*/
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			int i = 1;
			
			if(!colname.isBlank() && !searchWord.isBlank()) { // 검색이 있는 경우
				pstmt.setString(i++, searchWord);
			}

			if(!Check.isNullOrBlank(startDate)) {
				pstmt.setString(i++, startDate);
			}

			if(!Check.isNullOrBlank(endDate)) {
				pstmt.setString(i++, endDate);
			}
			
			pstmt.setInt(i++, (currentShowPageNo * sizePerPage) - (sizePerPage - 1)); // 공식
			pstmt.setInt(i++, (currentShowPageNo * sizePerPage)); // 공식
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				CollectionVO colvo = new CollectionVO();
				
				colvo.setCollectionno(rs.getInt("collectionno"));
				colvo.setName(rs.getString("name"));
				colvo.setDesigner(rs.getString("designer"));
				colvo.setThumbnail_img(rs.getString("thumbnail_img"));
				colvo.setFullscreen_img(rs.getString("fullscreen_img"));
				colvo.setIs_delete(rs.getInt("is_delete"));
				colvo.setRegisterday(rs.getString("registerday"));
				colvo.setProductListCnt(rs.getInt("product_cnt"));
				
				collectionList.add(colvo);
			}
		} finally {
			close();
		}
		
		return collectionList;
	}

	// 관리자 컬렉션 검색 시 총 개수
	@Override
	public int totalCollectionCntForAdmin(Map<String, String> paraMap) throws SQLException {
		
		int totalCnt = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT count(*) "
					   + " from tbl_collection c "
					   + " join  "
					   + " ( "
					   + "     select fk_collectionno, count(*) AS product_cnt "
					   + "     from tbl_col_product "
					   + "     group by fk_collectionno "
					   + " ) cp "
					   + " on c.collectionno = cp.fk_collectionno "
					   + " where is_delete = 0 and ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			
			if(!colname.isBlank() && !searchWord.isBlank()) {
				if("collectionno".equals(colname)) {
					sql += " "+colname+" = ? ";
				}
				else {
					sql += " lower("+colname+") like '%'|| lower(?) ||'%' ";
				}
			}
			else {
				sql += "name like '%' ";
			}
			
			if(!Check.isNullOrBlank(startDate)) {
				sql += " AND registerday >= to_date(?, 'yyyy-mm-dd') ";
			}
			
			if(!Check.isNullOrBlank(endDate)) {
				sql += "AND registerday <= to_date(?, 'yyyy-mm-dd') + 1 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			int i = 1;
			
			if(!colname.isBlank() && !searchWord.isBlank()) { // 검색이 있는 경우
				pstmt.setString(i++, searchWord);
			}

			if(!Check.isNullOrBlank(startDate)) {
				pstmt.setString(i++, startDate);
			}

			if(!Check.isNullOrBlank(endDate)) {
				pstmt.setString(i++, endDate);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalCnt = rs.getInt(1);
			}
		} finally {
			close();
		}
		
		return totalCnt;
	}

	// 관리자가 상품으로 컬렉션 검색
	@Override
	public List<CollectionVO> getCollectionListByProductForAdmin(Map<String, String> paraMap) throws SQLException {
		List<CollectionVO> collectionList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT collectionno, name, designer, thumbnail_img, fullscreen_img, is_delete, registerday, product_cnt "
					   + " FROM "
					   + " ( "
					   + " select rownum AS rno, collectionno, name, designer, thumbnail_img, fullscreen_img, is_delete, to_char(registerday, 'yyyy-mm-dd') as registerday, product_cnt "
					   + " from tbl_collection c "
					   + " join  "
					   + " ( "
					   + "     select fk_collectionno, count(*) as product_cnt "
					   + "     from tbl_col_product "
					   + "     where fk_productno in (";
			
			/*** 상품 검색 쿼리 시작 ***/
			sql += " SELECT productno "
					   + " FROM tbl_product P JOIN "
					   + " ( "
					   + "     select fk_productno, count(*) as optionCnt "
					   + "     from tbl_option "
					   + "     group by fk_productno "
					   + " )O "
					   + " ON P.productno = O.fk_productno "
					   + " JOIN "
					   + " ( "
					   + "     select fk_productno, count(*) as productImgCnt "
					   + "     from tbl_product_img "
					   + "     group by fk_productno "
					   + " )I "
					   + " ON P.productno = I.fk_productno "
					   + " JOIN tbl_category C "
					   + " ON P.fk_categoryno = C.categoryno "
					   + " WHERE ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			String major_category = paraMap.get("major_category");
			String small_category = paraMap.get("small_category");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String is_delete = paraMap.get("is_delete");
			String startPrice = paraMap.get("startPrice");
			String endPrice = paraMap.get("endPrice");
			
			String colors = paraMap.get("colors");
			String[] arrColor = null;
			
			String startCnt = paraMap.get("startCnt");
			String endCnt = paraMap.get("endCnt");
			
			if(!colname.isBlank() && !searchWord.isBlank()) {
				if("productno".equals(colname)) {
					sql += " "+colname+" = ? ";
				}
				else {
					sql += " lower("+colname+") like '%'|| lower(?) ||'%' ";
				}
			}
			else {
				sql += "name like '%' ";
			}
			
			if(!Check.isNullOrBlank(major_category)) {
				sql += " AND fk_categoryno IN ( "
					 + "            select categoryno "
					 + "            from tbl_category "
					 + "            where major_category = ? ";
				if(!Check.isNullOrBlank(small_category)) {
					sql += "                AND small_category = ? ";
				}
				sql += "            ) ";
			}
			
			if(!Check.isNullOrBlank(startDate)) {
				sql += " AND registerday >= to_date(?, 'yyyy-mm-dd') ";
			}
			
			if(!Check.isNullOrBlank(endDate)) {
				sql += "AND registerday <= to_date(?, 'yyyy-mm-dd') + 1 ";
			}
			
			if(!Check.isNullOrBlank(is_delete)) {
				sql += " AND P.is_delete = ? ";
			}
			
			if(!Check.isNullOrBlank(startPrice)) {
				sql += " AND price >= ? ";
			}
			
			if(!Check.isNullOrBlank(endPrice)) {
				sql += " AND price <= ?";
			}
			
			if(!Check.isNullOrBlank(colors)) {
				arrColor = colors.split(",");
				
				sql += " AND P.productno IN (SELECT fk_productno "
						 + " FROM tbl_option "
						 + " WHERE ";
				for(int i=0; i<arrColor.length; i++) {
					String or = (i==0) ? "":" OR ";
					
					sql += or +"   SQRT( "
							 + "     POWER(TO_NUMBER(SUBSTR(color, 2, 2), 'XX') - ?, 2) + "
							 + "     POWER(TO_NUMBER(SUBSTR(color, 4, 2), 'XX') - ?, 2) + "
							 + "     POWER(TO_NUMBER(SUBSTR(color, 6, 2), 'XX') - ?, 2) "
							 + "   ) < 50 ";
				}
				sql	+= " GROUP BY fk_productno) ";
			}
			
			if(!Check.isNullOrBlank(startCnt)) {
				sql += " AND cnt >= ? ";
			}
			
			if(!Check.isNullOrBlank(endCnt)) {
				sql += " AND cnt <= ?";
			}
			/*** 상품 검색 쿼리 끝 ***/
			
			sql += " ) "
				 + "     group by fk_collectionno "
				 + " ) "
				 + " cp "
				 + " on c.collectionno = cp.fk_collectionno "
				 + " where is_delete = 0 "
				 + " ) "
				 + " WHERE RNO BETWEEN ? AND ? ";
			
			pstmt = conn.prepareStatement(sql);

			/*
			    === 페이징처리의 공식 ===
			    where RNO between (조회하고자하는페이지번호 * 한페이지당보여줄행의개수) - (한페이지당보여줄행의개수 - 1) and (조회하고자하는페이지번호 * 한페이지당보여줄행의개수);
			*/
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			int i = 1;
			
			if(!colname.isBlank() && !searchWord.isBlank()) { // 검색이 있는 경우
				pstmt.setString(i++, searchWord);
			}

			if(!Check.isNullOrBlank(major_category)) {
				pstmt.setString(i++, major_category);
				if(!Check.isNullOrBlank(small_category)) {
					pstmt.setString(i++, small_category);
				}
			}

			if(!Check.isNullOrBlank(startDate)) {
				pstmt.setString(i++, startDate);
			}

			if(!Check.isNullOrBlank(endDate)) {
				pstmt.setString(i++, endDate);
			}
			
			if(!Check.isNullOrBlank(is_delete)) {
				pstmt.setInt(i++, Integer.parseInt(is_delete));
			}
			
			if(!Check.isNullOrBlank(startPrice)) {
				pstmt.setString(i++, startPrice);
			}
			
			if(!Check.isNullOrBlank(endPrice)) {
				pstmt.setString(i++, endPrice);
			}
			
			if(!Check.isNullOrBlank(colors)) {
				for(String color : arrColor) {
					int[] colorRGB = ColorUtil.hexToRGB(color);
					pstmt.setInt(i++, colorRGB[0]);
					pstmt.setInt(i++, colorRGB[1]);
					pstmt.setInt(i++, colorRGB[2]);
				}
			}

			if(!Check.isNullOrBlank(startCnt)) {
				pstmt.setString(i++, startCnt);
			}

			if(!Check.isNullOrBlank(endCnt)) {
				pstmt.setString(i++, endCnt);
			}
			
			pstmt.setInt(i++, (currentShowPageNo * sizePerPage) - (sizePerPage - 1)); // 공식
			pstmt.setInt(i++, (currentShowPageNo * sizePerPage)); // 공식
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				CollectionVO colvo = new CollectionVO();
				
				colvo.setCollectionno(rs.getInt("collectionno"));
				colvo.setName(rs.getString("name"));
				colvo.setDesigner(rs.getString("designer"));
				colvo.setThumbnail_img(rs.getString("thumbnail_img"));
				colvo.setFullscreen_img(rs.getString("fullscreen_img"));
				colvo.setIs_delete(rs.getInt("is_delete"));
				colvo.setRegisterday(rs.getString("registerday"));
				colvo.setProductListCnt(rs.getInt("product_cnt"));
				
				collectionList.add(colvo);
			}
		} finally {
			close();
		}
		
		return collectionList;
	}

	// 관리자가 상품으로 컬렉션 검색 시 총 개수
	@Override
	public int totalCntByProductForAdmin(Map<String, String> paraMap) throws SQLException {
		int totalCnt = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select count(*) "
					   + " from tbl_collection c "
					   + " join  "
					   + " ( "
					   + "     select fk_collectionno, count(*) as product_cnt "
					   + "     from tbl_col_product "
					   + "     where fk_productno in (";
			
			/*** 상품 검색 쿼리 시작 ***/
			sql += " SELECT productno "
					   + " FROM tbl_product P JOIN "
					   + " ( "
					   + "     select fk_productno, count(*) as optionCnt "
					   + "     from tbl_option "
					   + "     group by fk_productno "
					   + " )O "
					   + " ON P.productno = O.fk_productno "
					   + " JOIN "
					   + " ( "
					   + "     select fk_productno, count(*) as productImgCnt "
					   + "     from tbl_product_img "
					   + "     group by fk_productno "
					   + " )I "
					   + " ON P.productno = I.fk_productno "
					   + " JOIN tbl_category C "
					   + " ON P.fk_categoryno = C.categoryno "
					   + " WHERE ";
			
			String colname = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			
			String major_category = paraMap.get("major_category");
			String small_category = paraMap.get("small_category");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String is_delete = paraMap.get("is_delete");
			String startPrice = paraMap.get("startPrice");
			String endPrice = paraMap.get("endPrice");
			
			String colors = paraMap.get("colors");
			String[] arrColor = null;
			
			String startCnt = paraMap.get("startCnt");
			String endCnt = paraMap.get("endCnt");
			
			if(!colname.isBlank() && !searchWord.isBlank()) {
				if("productno".equals(colname)) {
					sql += " "+colname+" = ? ";
				}
				else {
					sql += " lower("+colname+") like '%'|| lower(?) ||'%' ";
				}
			}
			else {
				sql += "name like '%' ";
			}
			
			if(!Check.isNullOrBlank(major_category)) {
				sql += " AND fk_categoryno IN ( "
					 + "            select categoryno "
					 + "            from tbl_category "
					 + "            where major_category = ? ";
				if(!Check.isNullOrBlank(small_category)) {
					sql += "                AND small_category = ? ";
				}
				sql += "            ) ";
			}
			
			if(!Check.isNullOrBlank(startDate)) {
				sql += " AND registerday >= to_date(?, 'yyyy-mm-dd') ";
			}
			
			if(!Check.isNullOrBlank(endDate)) {
				sql += "AND registerday <= to_date(?, 'yyyy-mm-dd') + 1 ";
			}
			
			if(!Check.isNullOrBlank(is_delete)) {
				sql += " AND P.is_delete = ? ";
			}
			
			if(!Check.isNullOrBlank(startPrice)) {
				sql += " AND price >= ? ";
			}
			
			if(!Check.isNullOrBlank(endPrice)) {
				sql += " AND price <= ?";
			}
			
			if(!Check.isNullOrBlank(colors)) {
				arrColor = colors.split(",");
				
				sql += " AND P.productno IN (SELECT fk_productno "
						 + " FROM tbl_option "
						 + " WHERE ";
				for(int i=0; i<arrColor.length; i++) {
					String or = (i==0) ? "":" OR ";
					
					sql += or +"   SQRT( "
							 + "     POWER(TO_NUMBER(SUBSTR(color, 2, 2), 'XX') - ?, 2) + "
							 + "     POWER(TO_NUMBER(SUBSTR(color, 4, 2), 'XX') - ?, 2) + "
							 + "     POWER(TO_NUMBER(SUBSTR(color, 6, 2), 'XX') - ?, 2) "
							 + "   ) < 50 ";
				}
				sql	+= " GROUP BY fk_productno) ";
			}
			
			if(!Check.isNullOrBlank(startCnt)) {
				sql += " AND cnt >= ? ";
			}
			
			if(!Check.isNullOrBlank(endCnt)) {
				sql += " AND cnt <= ?";
			}
			/*** 상품 검색 쿼리 끝 ***/
			
			sql += " ) "
				 + "     group by fk_collectionno "
				 + " ) "
				 + " cp "
				 + " on c.collectionno = cp.fk_collectionno ";
			
			pstmt = conn.prepareStatement(sql);
			
			int i = 1;
			
			if(!colname.isBlank() && !searchWord.isBlank()) { // 검색이 있는 경우
				pstmt.setString(i++, searchWord);
			}

			if(!Check.isNullOrBlank(major_category)) {
				pstmt.setString(i++, major_category);
				if(!Check.isNullOrBlank(small_category)) {
					pstmt.setString(i++, small_category);
				}
			}

			if(!Check.isNullOrBlank(startDate)) {
				pstmt.setString(i++, startDate);
			}

			if(!Check.isNullOrBlank(endDate)) {
				pstmt.setString(i++, endDate);
			}
			
			if(!Check.isNullOrBlank(is_delete)) {
				pstmt.setInt(i++, Integer.parseInt(is_delete));
			}
			
			if(!Check.isNullOrBlank(startPrice)) {
				pstmt.setString(i++, startPrice);
			}
			
			if(!Check.isNullOrBlank(endPrice)) {
				pstmt.setString(i++, endPrice);
			}
			
			if(!Check.isNullOrBlank(colors)) {
				for(String color : arrColor) {
					int[] colorRGB = ColorUtil.hexToRGB(color);
					pstmt.setInt(i++, colorRGB[0]);
					pstmt.setInt(i++, colorRGB[1]);
					pstmt.setInt(i++, colorRGB[2]);
				}
			}

			if(!Check.isNullOrBlank(startCnt)) {
				pstmt.setString(i++, startCnt);
			}

			if(!Check.isNullOrBlank(endCnt)) {
				pstmt.setString(i++, endCnt);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalCnt = rs.getInt(1);
			}
		} finally {
			close();
		}
		
		return totalCnt;
	}

	// is_delete를 1로 만들어 컬렉션 삭제
	@Override
	public int updateIsDeleteCollection(String collectionno) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_collection set is_delete = 1 where collectionno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, collectionno);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 컬렉션 이미지 목록 가져오기
	@Override
	public List<CollectionImgVO> getCollectionImgs(String collectionno) throws SQLException {
		List<CollectionImgVO> collectionImgList = new ArrayList<>();
		
		try {

			conn = ds.getConnection();
			
			String sql = " select collection_imgno, fk_collectionno, img "
					   + " from tbl_collection_img "
					   + " where fk_collectionno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, collectionno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CollectionImgVO colimgvo = new CollectionImgVO();
				
				colimgvo.setCollection_imgno(rs.getInt("collection_imgno"));
				colimgvo.setFk_collectionno(rs.getInt("fk_collectionno"));
				colimgvo.setImg(rs.getString("img"));
				
				collectionImgList.add(colimgvo);
			}
			
			
		} finally {
			close();
		}
		
		return collectionImgList;
	}
	
	// 컬렉션 이미지를 삭제하는 메소드
	@Override
	public int deleteCollectionImg(String collection_imgno) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " DELETE FROM tbl_collection_img "
					   + " WHERE collection_imgno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, collection_imgno);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 컬렉션을 수정하는 메소드
	@Override
	public int updateCollection(CollectionVO colvo) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " UPDATE tbl_collection set name = ?, designer = ?, detail_html = ? ";
			
			if(!Check.isNullOrBlank(colvo.getThumbnail_img())) {
				sql += ", thumbnail_img = ? ";
			}

			if(!Check.isNullOrBlank(colvo.getFullscreen_img())) {
				sql += ", fullscreen_img = ? ";
			}
			
			sql += " WHERE collectionno = ? ";

			pstmt = conn.prepareStatement(sql);

			int i = 1;
			
			pstmt.setString(i++, colvo.getName());
			pstmt.setString(i++, colvo.getDesigner());
			pstmt.setString(i++, colvo.getDetail_html());
			
			if(!Check.isNullOrBlank(colvo.getThumbnail_img())) {
				pstmt.setString(i++, colvo.getThumbnail_img());
			}

			if(!Check.isNullOrBlank(colvo.getFullscreen_img())) {
				pstmt.setString(i++, colvo.getFullscreen_img());
			}
			
			pstmt.setInt(i++, colvo.getCollectionno());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 컬렉션에 포함된 상품을 모두 삭제하는 메소드
	@Override
	public int deleteColProduct(String collectionno) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " DELETE FROM tbl_col_product "
					   + " WHERE fk_collectionno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, collectionno);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	
	
	// 메인페이지 카테고리별 컬렉션 목록을 가져오는 메소드
	@Override
	public List<CollectionVO> selectCollectionProd(String majorCategory) throws SQLException {
	
		List<CollectionVO> collectionList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select col.collectionno, c.major_category, col.thumbnail_img "
					   + " from tbl_product P JOIN tbl_category C "
					   + " ON P.fk_categoryno = C.categoryno "
					   + " JOIN tbl_col_product CP "
					   + " ON P.productno = CP.fk_productno "
					   + " JOIN tbl_collection COL "
					   + " ON COL.collectionno = CP.fk_collectionno "
					   + " WHERE c.major_category = '' "
					   + " GROUP BY col.collectionno, c.major_category, col.thumbnail_img ";
			
			pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, '');
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				CollectionVO cvo = new CollectionVO();
				cvo.setThumbnail_img(rs.getString("thumbnail_img"));
				
				CategoryVO ctgrvo = new CategoryVO();
				ctgrvo.setMajor_category(rs.getString("major_category"));
				cvo.setCtgrvo(ctgrvo);
				
				collectionList.add(cvo);
			}
		} finally {
			close();
		}
		
		return collectionList;
	}

}
