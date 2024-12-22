package product.model;

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
import product.domain.OptionVO;
import product.domain.ProductVO;
import util.check.Check;
import util.color.ColorUtil;

public class ProductDAO_imple implements ProductDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public ProductDAO_imple() {

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
	
	
	@Override
	public List<ProductVO> selectProducts(Map<String, String> paraMap) throws SQLException {
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT productno, fk_categoryno, name, description, "
					   + "        price, thumbnail_img, registerday, "
					   + "        cnt, delivery_price, detail_html, "
					   + "        p_is_delete, point_pct, "
					   + "        optioncnt, "
					   + "        productimgcnt, "
					   + "        major_category, "
					   + "        small_category,"
					   + "        c_is_delete "
					   + " FROM ( "
					   + "    SELECT rownum AS RNO, productno, fk_categoryno, name, description, "
					   + "        price, thumbnail_img, to_char(registerday, 'yyyy-mm-dd') AS registerday, "
					   + "        cnt, delivery_price, detail_html, "
					   + "        P.is_delete AS p_is_delete, point_pct, "
					   + "        O.optioncnt, "
					   + "        I.productimgcnt, "
					   + "        major_category, "
					   + "        small_category,"
					   + "        C.is_delete AS c_is_delete "
					   + "    FROM tbl_product P JOIN "
					   + "    ( "
					   + "        select fk_productno, count(*) as optionCnt "
					   + "        from tbl_option "
					   + "        group by fk_productno "
					   + "    )O "
					   + "    ON P.productno = O.fk_productno "
					   + "    JOIN "
					   + "    ( "
					   + "        select fk_productno, count(*) as productImgCnt "
					   + "        from tbl_product_img "
					   + "        group by fk_productno "
					   + "    )I "
					   + "    ON P.productno = I.fk_productno "
					   + "    JOIN tbl_category C "
					   + "    ON P.fk_categoryno = C.categoryno "
					   + "    WHERE ";
			
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
				sql += "lower("+colname+") like '%'|| lower(?) ||'%' ";
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
			
			sql += " ) T "
			     + " WHERE T.RNO between ? and ? ";

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
				
				ProductVO pvo = new ProductVO();
				
				pvo.setProductno(rs.getInt("productno"));
				pvo.setFk_categoryno(rs.getInt("fk_categoryno"));
				pvo.setName(rs.getString("name"));
				pvo.setDescription(rs.getString("description"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				pvo.setRegisterday(rs.getString("registerday"));
				pvo.setCnt(rs.getInt("cnt"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				pvo.setIs_delete(rs.getInt("p_is_delete"));
				pvo.setPoint_pct(rs.getInt("point_pct"));
				
				pvo.setOptionCnt(rs.getInt("optionCnt")); // 상품 옵션의 개수
				pvo.setProductImgCnt(rs.getInt("productImgCnt")); // 상품 이미지의 개수
				
				CategoryVO cvo = new CategoryVO();
				
				cvo.setCategoryno(rs.getInt("fk_categoryno"));
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				cvo.setIs_delete(rs.getInt("c_is_delete"));
				
				pvo.setCategoryVO(cvo);
				
				productList.add(pvo);

			}// end of while(rs.next())---------------------------
			
		} finally {
			close();
		}
		return productList;
	}

	// 검색한 상품 목록의 전체 페이지 수를 알아오는 메소드
	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {
		int totalPage = 0;

		try {
			conn = ds.getConnection();
			
			String sql = " SELECT ceil(count(*)/?) "
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
				sql += "lower("+colname+") like '%'|| lower(?) ||'%' ";
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

			pstmt = conn.prepareStatement(sql);
			
			int i = 1;

			pstmt.setInt(i++, Integer.parseInt(paraMap.get("sizePerPage")));
			
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
			
			rs.next();

			totalPage = rs.getInt(1);
			
		} finally {
			close();
		}
		return totalPage;
	}

	/* >>> 뷰단에서 "페이징 처리시 보여주는 순번 공식" 에서 사용하기 위해 
    검색이 있는 또는 검색이 없는 상품의 총개수 알아오기 시작 <<< */
	@Override
	public int getTotalProductCnt(Map<String, String> paraMap) throws SQLException {
		int totalPage = 0;

		try {
			conn = ds.getConnection();
			
			String sql = " SELECT count(*) "
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
				sql += "lower("+colname+") like '%'|| lower(?) ||'%' ";
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
			
			rs.next();

			totalPage = rs.getInt(1);
			
		} finally {
			close();
		}
		return totalPage;
	}

	// 상품 옵션의 대표 색상을 추출하기 위한 메소드
	@Override
	public List<OptionVO> selectAllOptions() throws SQLException {
		List<OptionVO> optionVOList = new ArrayList<>();

		try {
			conn = ds.getConnection();
			
			String sql = " SELECT optionno, fk_productno, color "
					   + " FROM tbl_option ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OptionVO ovo = new OptionVO();

				ovo.setOptionno(rs.getInt("optionno"));
				ovo.setFk_productno(rs.getInt("fk_productno"));
				ovo.setColor(rs.getString("color"));
				
				optionVOList.add(ovo);
			}
			
		} finally {
			close();
		}
		return optionVOList;
	}

}
