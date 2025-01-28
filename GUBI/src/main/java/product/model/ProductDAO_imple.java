package product.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import category.domain.CategoryVO;
import collection.domain.CollectionVO;
import product.domain.OptionVO;
import product.domain.ProductImgVO;
import product.domain.ProductVO;
import review.domain.ReviewVO;
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
	
	// 검색한 상품 목록을 가져오는 메소드(무한 스크롤)
	@Override
	public List<ProductVO> getProductListWithScroll(Map<String, String> paraMap) throws SQLException {
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sortby = paraMap.get("sortby");
			
			String orderSql = "";
			
			if("bestItem".equalsIgnoreCase(sortby)) {
				orderSql = " (select nvl(sum(cnt), 0) AS sales_cnt "
						 + "  from tbl_option op "
						 + "  join tbl_order_detail od "
						 + "  on op.optionno = od.fk_optionno "
						 + "  where fk_productno = productno) ";
			}
			else {
				orderSql = "registerday";
			}
			
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
					   + "    SELECT row_number() over(order by "+orderSql+" desc) AS rno, "
					   + "        productno, fk_categoryno, name, description, "
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
			
			sql += " ORDER BY rno "
				 + " ) T "
			     + " WHERE T.RNO between ? and ? ";

			pstmt = conn.prepareStatement(sql);

			int start = Integer.parseInt(paraMap.get("start"));
			int end = Integer.parseInt(paraMap.get("end"));
			
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
			
			pstmt.setInt(i++, start);
			pstmt.setInt(i++, end);
			
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
	
	// 검색한 상품 목록을 가져오는 메소드(페이징)
	@Override
	public List<ProductVO> selectProducts(Map<String, String> paraMap) throws SQLException {
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sortby = paraMap.get("sortby");
			
			String orderSql = "";
			
			if("bestItem".equalsIgnoreCase(sortby)) {
				orderSql = " (select nvl(sum(cnt), 0) AS sales_cnt "
						 + "  from tbl_option op "
						 + "  join tbl_order_detail od "
						 + "  on op.optionno = od.fk_optionno "
						 + "  where fk_productno = productno) ";
			}
			else {
				orderSql = "registerday";
			}
			
			String sql = " SELECT productno, fk_categoryno, name, description, "
					   + "        price, thumbnail_img, registerday, "
					   + "        cnt, delivery_price, detail_html, "
					   + "        p_is_delete, point_pct, "
					   + "        major_category, "
					   + "        small_category,"
					   + "        c_is_delete "
					   + " FROM ( "
					   + "    SELECT row_number() over(order by "+orderSql+" desc) AS rno, "
					   + "        productno, fk_categoryno, name, description, "
					   + "        price, thumbnail_img, to_char(registerday, 'yyyy-mm-dd') AS registerday, "
					   + "        cnt, delivery_price, detail_html, "
					   + "        P.is_delete AS p_is_delete, point_pct, "
					   + "        major_category, "
					   + "        small_category,"
					   + "        C.is_delete AS c_is_delete "
					   + "    FROM tbl_product P "
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
			
			sql += " ORDER BY rno "
				 + " ) T "
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

	
	
	// 상품의 전체개수를 알아오기
	@Override
	public int totalProductCount(Map<String, String> paraMap) throws SQLException {
		
		int totalCount = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT count(*) "
					   + " FROM "
					   + " ( "
					   + "    SELECT row_number() over(order by productno desc) AS RNO "
				   	   + "         , productno, name, C.major_category, C.small_category, price, thumbnail_img "
					   + "    FROM tbl_product P "
					   + "    JOIN tbl_category C "
					   + "    ON P.fk_categoryno = C.categoryno ";

			String majorCname = paraMap.get("majorCname");
			String smallCname = paraMap.get("smallCname");
			String freeshipping = paraMap.get("freeshipping");
			
			boolean is_free = "true".equalsIgnoreCase(freeshipping);
			
			
			
			if(!Check.isNullOrBlank(majorCname)) {
				sql += " WHERE fk_categoryno IN ( "
					 + "            select categoryno "
					 + "            from tbl_category "
					 + "            where major_category = ?  ";
				
				if(!Check.isNullOrBlank(smallCname)) { 
					  sql += "              AND small_category = ? "; 
				}
				 
				sql += "            ) ";
				
				if(is_free) {	
					sql += " AND delivery_price = 0 ";
				}
			} else if(is_free) {
				sql += " WHERE delivery_price = 0 ";
			}

			sql += " ) V ";

			System.out.println(" ----------------count[totalProductCount] 조회-----------");
			System.out.println("majorCname:: " + majorCname);
			System.out.println("smallCname:: " + smallCname);
			System.out.println("freeshipping:: " + freeshipping);
			System.out.println("count[totalProductCount] 조회 sql :: " + sql);
			
			pstmt = conn.prepareStatement(sql);
			
			if(!Check.isNullOrBlank(majorCname)) {
				pstmt.setString(1, majorCname);
				if(!Check.isNullOrBlank(smallCname)) {
					pstmt.setString(2, smallCname);
				}
			}
			
			rs = pstmt.executeQuery();
	         
	        rs.next();
	         
	        totalCount = rs.getInt(1); 
	        
		} finally {
			close();
		}
		
		
		return totalCount;
	}
	
	
	
	
	
	// 모든 상품 또는 카테고리별 상품목록을 조회하는 메소드
	@Override
	public List<ProductVO> selectCnoProduct(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			   
			String majorCname = paraMap.get("majorCname");
			String smallCname = paraMap.get("smallCname");
			String freeshipping = paraMap.get("freeshipping");
			String sortby = paraMap.get("sortby");
			
			boolean is_free = "true".equalsIgnoreCase(freeshipping);
			
			String orderSql = "";
			
			if("bestItem".equalsIgnoreCase(sortby)) {
				orderSql = " (select nvl(sum(cnt), 0) AS sales_cnt "
						 + "  from tbl_option op "
						 + "  join tbl_order_detail od "
						 + "  on op.optionno = od.fk_optionno "
						 + "  where fk_productno = productno) ";
			}
			else {
				orderSql = "registerday";
			}
			
			String sql = " SELECT productno, name, price, thumbnail_img, major_category, small_category, registerday, is_delete "
					   + " FROM "
					   + " ( "
					   + "    SELECT row_number() over(order by "+orderSql+" desc) AS RNO "
				   	   + "         , productno, name, C.major_category, C.small_category, price, thumbnail_img"
				   	   + "		   , registerday,P.is_delete "
					   + "    FROM tbl_product P "
					   + "    JOIN tbl_category C "
					   + "    ON P.fk_categoryno = C.categoryno "
					   + "    WHERE P.is_delete = 0 ";
			
			if(!Check.isNullOrBlank(majorCname)) {
				sql += " AND fk_categoryno IN ( "
					 + "            select categoryno "
					 + "            from tbl_category "
					 + "            where major_category = ?  ";
				
				if(!Check.isNullOrBlank(smallCname)) { 
					  sql += "              AND small_category = ? "; 
				}
				 
				sql += "            ) ";
				
				if(is_free) {	
					sql += " AND delivery_price = 0 ";
				}
			} else if(is_free) {
				sql += " AND delivery_price = 0 ";
			}
			sql += " ORDER BY RNO "
				 + " ) V "
			     + " WHERE rno between ? and ? ";
			
			System.out.println("sql ::: " + sql);

			System.out.println(" ----------------리스트[selectCnoProduct] 조회-----------");
			System.out.println("majorCname:: " + majorCname);
			System.out.println("smallCname:: " + smallCname);
			System.out.println("freeshipping:: " + freeshipping);
			System.out.println("selectCnoProduct 조회 sql :: " + sql);
			
			pstmt = conn.prepareStatement(sql);
			
			if(!Check.isNullOrBlank(majorCname)) {
				pstmt.setString(1, majorCname);
				pstmt.setString(2, paraMap.get("start"));
				pstmt.setString(3, paraMap.get("end"));
				if(!Check.isNullOrBlank(smallCname)) {
					pstmt.setString(2, smallCname);
					pstmt.setString(3, paraMap.get("start"));
					pstmt.setString(4, paraMap.get("end"));
				}
			} else {
				pstmt.setString(1, paraMap.get("start"));
				pstmt.setString(2, paraMap.get("end"));
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ProductVO pvo = new ProductVO();
				
				pvo.setProductno(rs.getInt("productno")); 
				
				pvo.setName(rs.getString("name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				pvo.setRegisterday(rs.getString("registerday"));
				
				productList.add(pvo);
				
			} // end of while(rs.next()) {}--------------
			
		} finally {
			close();
		}
		
		return productList;
	}

	
	// 제품번호로 해당 제품의 상세정보를 조회해오기
	@Override
	public ProductVO selectDetailByProductno(String productno) throws SQLException {
		
		ProductVO pvo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT productno, "
					   + "        P.name AS product_name"
					   + "      , P.description "
					   + "      , P.price, P.delivery_price, P.point_pct, P.detail_html "
					   + "		, C.small_category"
					   + "      , cnt "
					   + " FROM tbl_product P "
					   + " JOIN tbl_option O "
					   + " ON P.productno = O.fk_productno"
					   + " JOIN tbl_category C "
					   + " ON P.fk_categoryno = C.categoryno"
					   + " WHERE productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pvo = new ProductVO();
		            
	            pvo.setProductno(rs.getInt("productno"));  // 제품번호
	            pvo.setName(rs.getString("product_name"));  // 제품명
	            pvo.setDescription(rs.getString("description")); // 제품상세설명
	            pvo.setPrice(rs.getInt("price"));   // 제품가격
	            pvo.setDelivery_price(rs.getInt("delivery_price"));   // 배송비
	            pvo.setPoint_pct(rs.getInt("point_pct"));   // 포인트적립퍼센티지
	            pvo.setDetail_html(rs.getString("detail_html"));   // 제품상세html
	            pvo.setCnt(rs.getInt("cnt")); // 재고량
	            CategoryVO cvo = new CategoryVO();
	            cvo.setSmall_category(rs.getString("small_category"));
	            pvo.setCategoryVO(cvo);
	             
			}
			
		} finally {
			close();
		}
		
		
		return pvo;
	}

	
	// 제품번호로 해당 제품의 옵션별 이미지와 옵션을 조회하는 메소드
	@Override
	public List<OptionVO> selectOptionList(String productno) throws SQLException {
		List<OptionVO> optionList = new ArrayList<>();
		
		 try {
			 conn = ds.getConnection();
			 
			 String sql = " SELECT productno, "
			 		    + " O.img AS option_thumbnail_img, "
			 		    + " O.color,"
			 		    + " O.name AS option_cname,"
			 		    + " O.optionno "
			 		    + " FROM tbl_product P JOIN tbl_option O "
			 		    + " ON P.productno = O.fk_productno "
					    + " WHERE productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productno); 
			
			rs = pstmt.executeQuery();
	         
	         while(rs.next()) {
	        	 	OptionVO optvo = new OptionVO();
	        	 	optvo.setFk_productno(rs.getInt("productno"));
	        	 	optvo.setImg(rs.getString("option_thumbnail_img"));
	        	 	optvo.setColor(rs.getString("color"));
	        	 	optvo.setName(rs.getString("option_cname"));
	        	 	optvo.setOptionno(rs.getInt("optionno"));
	            
	        	 	optionList.add(optvo);
	         }// end of while-----------------
	         
	      } finally {
	         close();
	      }
		
		return optionList;
	}

	
	// 콜렉션 리스트 조회하는 메소드
	@Override
	public List<CollectionVO> selectCollectionList() throws SQLException {
		
		List<CollectionVO> clist = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select collectionno, name "
					   + " from tbl_collection ";
			
			pstmt = conn.prepareStatement(sql);			
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				CollectionVO cvo = new CollectionVO();
				cvo.setCollectionno(rs.getInt(1)); 
				cvo.setName(rs.getString(2)); 
				
				clist.add(cvo);
			}
		
		} finally {
			close();
		}
		return clist;
	}

	// 콜렉션별 상품 리스트 조회하는 메소드
	@Override
	public List<ProductVO> selectProductByCollection(Map<String, String> paraMap) throws SQLException {
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT p.productno, p.name, p.price, p.THUMBNAIL_IMG "
					   + " FROM tbl_col_product CP "
					   + " JOIN tbl_collection C "
					   + " ON CP.fk_collectionno = C.collectionno "
					   + " JOIN tbl_product P "
					   + " ON CP.fk_productno = P.productno "
					   + " JOIN tbl_category CT "
					   + " ON CT.categoryno = P.fk_categoryno "
					   + " WHERE C.name = ? " ;
			
			
			String majorCname = paraMap.get("majorCname");
			String smallCname = paraMap.get("smallCname");
			String freeshipping = paraMap.get("freeshipping");
			String collection = paraMap.get("collection");

			if (!Check.isNullOrBlank(majorCname)) {
				sql += " AND major_category = ? ";
				if (!Check.isNullOrBlank(smallCname)) {
					sql += " AND small_category = ? ";
				}
			}
			
			if (!Check.isNullOrBlank(freeshipping)) {
				sql += " AND P.delivery_price = 0 ";
			}
			
			sql += " GROUP BY p.productno, p.name, p.price, p.THUMBNAIL_IMG ";
			
			System.out.println(sql);
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, collection);
			
			if (!Check.isNullOrBlank(majorCname)) {
				pstmt.setString(2, majorCname);
				if (!Check.isNullOrBlank(smallCname)) {
					pstmt.setString(3, smallCname);
				}
			} 

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ProductVO pvo = new ProductVO();
				pvo.setProductno(rs.getInt("productno"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				
				productList.add(pvo);
				
			} // end of while(rs.next()) {}--------------
			
		} finally {
			close();
		}
		
		return productList;
	}

	
	// 메인페이지에서 사용할 최신순 3개 신상품 조회
	@Override
	public List<ProductVO> selectTop3Product() throws SQLException {
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " SELECT   p.productno, "
						+ " 			 p.name, "
						+ "  		p.description, "
						+ " ( "
						+ "  SELECT PI.IMG "
						+ "   FROM tbl_product_img PI "
						+ "   WHERE PI.FK_PRODUCTNO = P.PRODUCTNO and ROWNUM = 1 "
						+ "  ) AS thumbnail_img "
						+ " FROM tbl_product P  "
						+ " WHERE ROWNUM <= 3 "
						+ " ORDER BY P.REGISTERDAY DESC " ;
			

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ProductVO pvo = new ProductVO();
				pvo.setProductno(rs.getInt("productno"));
				pvo.setName(rs.getString("name"));
				pvo.setDescription(rs.getString("description"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				
				productList.add(pvo);
				
			} // end of while(rs.next()) {}--------------
			
		} finally {
			close();
		}
		
		return productList;
	}
	
	
	
	
	
	// 메인페이지에서 사용할 인기상품목록 조회
	@Override
	public List<ProductVO> selectBestProd() throws SQLException {
		
		List<ProductVO> bestProdList = new ArrayList<>();
		try {
			conn = ds.getConnection();
			
			
			String sql = " select productno, P.name, P.price, P.thumbnail_img, sum(OD.cnt) AS salesCount "
					   + " from tbl_option O "
				 	   + " JOIN tbl_order_detail OD "
					   + " ON O.optionno = OD.fk_optionno "
					   + " JOIN tbl_product P "
					   + " ON O.fk_productno = P.productno "
					   + " group by productno, P.name, P.price, P.thumbnail_img "
					   + " order by salesCount desc ";
			
			pstmt = conn.prepareStatement(sql);
			rs= pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO pvo  = new ProductVO();
				pvo.setProductno(rs.getInt("productno"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				
				bestProdList.add(pvo);
			}
		} finally {
			close();
		}
		
		return bestProdList;
	}
	
	
	
	
	

	
	
	// 제품상세페이지에서 장바구니로 insert 하는 메소드
	@Override
	public int addCart(Map<String, String> paraMap) throws SQLException {

		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			/*
	           먼저 장바구니 테이블(tbl_cart)에 어떤 회원이 새로운 제품을 넣는 것인지,
	           아니면 또 다시 제품을 추가로 더 구매하는 것인지를 알아야 한다.
	           이것을 알기 위해서 어떤 회원이 어떤 제품을 장바구니 테이블(tbl_cart) 넣을때
	           그 제품이 이미 존재하는지 select 를 통해서 알아와야 한다.
	           
	         -------------------------------------------
	          cartno   fk_userid     fk_pnum   oqty  
	         -------------------------------------------
	            1       mjhan          7        12     
	            2       mjhan          6         3     
	            3       leess          7         5     
	        */
			
			String sql = " select cartno "
	                   + " from tbl_cart "
	                   + " where FK_USERID = ? and FK_OPTIONNO = ? ";
			
			pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, paraMap.get("userid"));
	        pstmt.setString(2, paraMap.get("optionno"));
	         
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
		        	// 어떤 제품을 추가로 장바구니에 넣고자 하는 경우
		        	
		        	sql = " update tbl_cart set cnt = cnt + ? "
		                + " where cartno = ? ";
		        	
		        	pstmt = conn.prepareStatement(sql);
		        	pstmt.setInt(1, Integer.parseInt(paraMap.get("cnt")));
		        	pstmt.setInt(2, rs.getInt("cartno"));
		        	
		        	n = pstmt.executeUpdate();
		        	
	        } else {
		        	// 장바구니에 존재하지 않는 새로운 제품을 넣고자 하는 경우
		        	sql = " insert into tbl_cart(cartno, fk_optionno, fk_userid, cnt) "
		            + " values(seq_cartno.nextval, ?, ?, ?) ";
		                    
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(paraMap.get("optionno")));
                pstmt.setString(2, paraMap.get("userid"));
                pstmt.setInt(3, Integer.parseInt(paraMap.get("cnt")));
                 
                n = pstmt.executeUpdate();
	        }
			
		} finally {
			close();
		}
		
		return n;
	}
	
	// 제품상세페이지에서 리뷰조회하는 메소드
	@Override
	public List<ReviewVO> selectReview(int productno) throws SQLException {
		List<ReviewVO> reviewList = new ArrayList<>();
		try {
			conn = ds.getConnection();
			
			String sql =  " SELECT reviewno, fk_optionno, fk_userid, score, OPT.name "
					    + "      , title, content, r.img, r.registerday "
						+ " FROM tbl_product P "
						+ " JOIN tbl_option OPT "
						+ " ON P.productno = OPT.fk_productno "
						+ " JOIN tbl_review R "
						+ " ON OPT.optionno = R.fk_optionno "
						+ " WHERE productno = ? " ;

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, productno);
			
//			System.out.println("Product No: " + productno);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ReviewVO rvo = new ReviewVO();
				
				rvo.setReviewno(rs.getInt("reviewno"));
				rvo.setFk_optionno(rs.getInt("fk_optionno"));
				rvo.setFk_userid(rs.getString("fk_userid"));
				rvo.setScore(rs.getInt("score"));
				
				OptionVO otpvo = new OptionVO();
				otpvo.setName(rs.getString("name"));
				rvo.setOptionvo(otpvo);
				
				rvo.setTitle(rs.getString("title"));
				rvo.setContent(rs.getString("content"));
				rvo.setImg(rs.getString("img"));
				rvo.setRegisterday(rs.getString("registerday"));
				
				//System.out.println(" rs.getInt(\"reviewno\"): " + rs.getInt("reviewno"));
				
				reviewList.add(rvo);
				
			} // end of while(rs.next()) {}--------------
			
			//System.out.println("reviewList size: " + reviewList.size());
			
		} finally {
			close();
		}
		
		return reviewList;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	// 카테고리 대분류 가져오기 
	@Override
	public List<CategoryVO> selectMajorCategory() throws SQLException {
		
		List<CategoryVO> selectMajorCategory = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select distinct major_category "
						+ " from tbl_category ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CategoryVO pvo = new CategoryVO();
				
				pvo.setMajor_category(rs.getString("major_category"));
				
				selectMajorCategory.add(pvo);
			}

		} finally {
			close();
		}

		return selectMajorCategory;
	}

	// 카테고리 소분류 
	@Override
	public List<CategoryVO> selectSmallCategory(String major_category) throws SQLException {
		
		List<CategoryVO> selectSmallCategory = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select categoryno, major_category, small_category "
						+ " from tbl_category "
						+ " where major_category = ? and is_delete = 0 ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, major_category);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CategoryVO cvo = new CategoryVO();
				
				cvo.setSmall_category(rs.getString("small_category"));
				cvo.setCategoryno(rs.getInt("categoryno"));
				
				selectSmallCategory.add(cvo);
			}

		} finally {
			close();
		}

		return selectSmallCategory;
	}

	// 상품리스트 가져오기 
	@Override
	public List<ProductVO> selectProduct(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> selectProduct = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select * "
						+ " from "
						+ " ( "
						+ " select rownum as rno, productno, fk_categoryno, thumbnail_img, name, price, delivery_price, cnt, registerday, "
						+ " is_delete, major_category, small_category "
						+ " from "
						+ " ( "
						+ " 	with "
						+ " 	a as "
						+ " 	( "
						+ " 		select productno, thumbnail_img, fk_categoryno, name, price, delivery_price, cnt, "
						+ " 		to_char(registerday, 'yyyy-mm-dd') as registerday , is_delete "
						+ " 		from tbl_product "
						+ " 	), "
						+ " 	b as "
						+ " 	( "
						+ " 		select categoryno, major_category, small_category "
						+ " 		from tbl_category "
						+ " 	) "
						+ " 	select productno, fk_categoryno, thumbnail_img, name, price, delivery_price, cnt, registerday, a.is_delete, major_category, small_category "
						+ " 	from a join b  "
						+ " 	on a.fk_categoryno = b.categoryno "
						+ "	 	where productno is not null ";

			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String is_delete = paraMap.get("is_delete");
			String startprice = paraMap.get("startprice");
			String endprice = paraMap.get("endprice");
			String major_category = paraMap.get("major_category");
			String small_category = paraMap.get("small_category");
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) { 
				sql += " and " + searchType + " like '%' || ? || '%' ";
			}
			if(! major_category.isBlank() && !small_category.isBlank()) {
				sql += " and major_category = ? and small_category = ? ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql += " and registerday between to_date(?,'yyyy-mm-dd') "
					+  "                 and to_date(?,'yyyy-mm-dd') ";
			}
			if(!is_delete.isBlank()) {
				sql += " and a.is_delete = ? ";
			}
			if(!startprice.isBlank() && !endprice.isBlank()) {
				sql += " and price between ? and ? ";
			}
			
			sql  +=   "   order by registerday desc"
					+ " ) "
					+ " ) where rno between ? and ? ";
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo")); // 현재페이지
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			pstmt = conn.prepareStatement(sql);

			int index = 1;
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				pstmt.setString(index++, searchWord);
			}
			if(! major_category.isBlank() && !small_category.isBlank()) {
				pstmt.setString(index++, major_category);
				pstmt.setString(index++, small_category);
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			if(!is_delete.isBlank()) {
				pstmt.setString(index++, is_delete);
			}
			if(!startprice.isBlank() && !endprice.isBlank()) {
				pstmt.setString(index++, startprice);
				pstmt.setString(index++, endprice);
			}
		
			pstmt.setInt(index++, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
			pstmt.setInt(index++, (currentShowPageNo*sizePerPage));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO pvo = new ProductVO();
				CategoryVO cvo = new CategoryVO();
				
				pvo.setProductno(rs.getInt("productno"));
				pvo.setFk_categoryno(rs.getInt("fk_categoryno"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				pvo.setCnt(rs.getInt("cnt"));
				pvo.setRegisterday(rs.getString("registerday"));
				pvo.setIs_delete(rs.getInt("is_delete"));
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				pvo.setCategoryVO(cvo);
				
				selectProduct.add(pvo);
			}
			
		} finally {
			close();
		}

		return selectProduct;
	}

	// 총페이지수 알기
	@Override
	public int totalPage(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select ceil(count(*)/?) "
					+ " from "
					+ " ( "
					+ " 	with "
					+ " 	a as "
					+ " 	( "
					+ " 		select productno, thumbnail_img, fk_categoryno, name, price, delivery_price, cnt, registerday, is_delete "
					+ " 		from tbl_product "
					+ " 	), "
					+ " 	b as "
					+ " 	( "
					+ " 		select categoryno, major_category, small_category "
					+ " 		from tbl_category "
					+ " 	) "
					+ " 	select rownum as rno, productno, fk_categoryno, thumbnail_img, name, price, delivery_price, cnt, registerday, a.is_delete, major_category, small_category "
					+ " 	from a join b  "
					+ " 	on a.fk_categoryno = b.categoryno "
					+ "	 	where productno is not null ";
	
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String is_delete = paraMap.get("is_delete");
			String startprice = paraMap.get("startprice");
			String endprice = paraMap.get("endprice");
			String major_category = paraMap.get("major_category");
			String small_category = paraMap.get("small_category");
	
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) { 
				sql += " and " + searchType + " like '%' || ? || '%' ";
			}
			if(! major_category.isBlank() && !small_category.isBlank()) {
				sql += " and major_category = ? and small_category = ? ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql += " and registerday between to_date(?,'yyyy-mm-dd') "
					+  "                 and to_date(?,'yyyy-mm-dd') ";
			}
			if(!is_delete.isBlank()) {
				sql += " and a.is_delete = ? ";
			}
			if(!startprice.isBlank() && !endprice.isBlank()) {
				sql += " and price between ? and ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
			int index = 2;
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				pstmt.setString(index++, searchWord);
			}
			if(! major_category.isBlank() && !small_category.isBlank()) {
				pstmt.setString(index++, major_category);
				pstmt.setString(index++, small_category);
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			if(!is_delete.isBlank()) {
				pstmt.setString(index++, is_delete);
			}
			if(!startprice.isBlank() && !endprice.isBlank()) {
				pstmt.setString(index++, startprice);
				pstmt.setString(index++, endprice);
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			result = rs.getInt(1);
			
		} finally {
			close();
		}
			
		return result;
	}

	// 상품개수 알아오기
	@Override
	public int productCnt(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select count(*) "
						+ " from "
						+ " ( "
						+ " 	with "
						+ " 	a as "
						+ " 	( "
						+ " 		select productno, thumbnail_img, fk_categoryno, name, price, delivery_price, cnt, registerday, is_delete "
						+ " 		from tbl_product "
						+ " 	), "
						+ " 	b as "
						+ " 	( "
						+ " 		select categoryno, major_category, small_category "
						+ " 		from tbl_category "
						+ " 	) "
						+ " 	select rownum as rno, productno, fk_categoryno, thumbnail_img, name, price, delivery_price, cnt, registerday, a.is_delete, major_category, small_category "
						+ " 	from a join b  "
						+ " 	on a.fk_categoryno = b.categoryno "
						+ "	 	where productno is not null ";
	
			String searchType = paraMap.get("searchType");
			String searchWord = paraMap.get("searchWord");
			String startDate = paraMap.get("startDate");
			String endDate = paraMap.get("endDate");
			String is_delete = paraMap.get("is_delete");
			String startprice = paraMap.get("startprice");
			String endprice = paraMap.get("endprice");
			String major_category = paraMap.get("major_category");
			String small_category = paraMap.get("small_category");
	
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) { 
				sql += " and " + searchType + " like '%' || ? || '%' ";
			}
			if(! major_category.isBlank() && !small_category.isBlank()) {
				sql += " and major_category = ? and small_category = ? ";
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				sql += " and registerday between to_date(?,'yyyy-mm-dd') "
					+  "                 and to_date(?,'yyyy-mm-dd') ";
			}
			if(!is_delete.isBlank()) {
				sql += " and a.is_delete = ? ";
			}
			if(!startprice.isBlank() && !endprice.isBlank()) {
				sql += " and price between ? and ? ";
			}
			
			sql += " ) ";
			
			pstmt = conn.prepareStatement(sql);

			int index = 1;
			
			if(! searchType.isBlank() && ! searchWord.isBlank() ) {
				pstmt.setString(index++, searchWord);
			}
			if(! major_category.isBlank() && !small_category.isBlank()) {
				pstmt.setString(index++, major_category);
				pstmt.setString(index++, small_category);
			}
			if(!startDate.isBlank() && !endDate.isBlank()) {
				pstmt.setString(index++, startDate);
				pstmt.setString(index++, endDate);
			}
			if(!is_delete.isBlank()) {
				pstmt.setString(index++, is_delete);
			}
			if(!startprice.isBlank() && !endprice.isBlank()) {
				pstmt.setString(index++, startprice);
				pstmt.setString(index++, endprice);
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			result = rs.getInt(1);
			
		} finally {
			close();
		}
			
		return result;
	}

	// 상품 삭제하기 
	@Override
	public int deleteProduct(String deleteproductno) throws SQLException {
		
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " update tbl_product set is_delete = 1 "
						+ " where productno = ? "; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, deleteproductno);

			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return result;
	}

	// 상품재등록
	@Override
	public int recoverProduct(String recoverproductno) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql =  " update tbl_product set is_delete = 0 "
						+ " where productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, recoverproductno);

			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return result;
	}

	// 카테고리 번호 찾기 
	@Override
	public String searchCategoryNo(String major_category, String small_category) throws SQLException {
		
		String categoryno = "";

		try {
			
			conn = ds.getConnection();
			
			String sql  = " select categoryno "
						+ " from tbl_category "
						+ " where major_category = ? and small_category = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, major_category);
			pstmt.setString(2, small_category);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				categoryno = rs.getString("categoryno");
			}
		} finally {
			close();
		}
		
		return categoryno;
		
	}
	
	
	
	// 상품번호 채번하기 
	@Override
	public int getProductno() throws SQLException {
		
		int num = 0;
	      
		try {
	         conn = ds.getConnection();
	         
	         String sql = " select seq_productno.nextval AS pnum "
         				+ " from dual ";
	         
	         pstmt = conn.prepareStatement(sql);
	         rs = pstmt.executeQuery();
	         
             rs.next();
             num = rs.getInt(1);
	         
		} finally {
			close();
		}
		
		return num;
	}

	// 상품등록하기 
	@Override
	public int addProduct(Map<String, String> paraMap) throws SQLException {
	
		int result = 0;
		
		try {
			
			conn =ds.getConnection();
			
			String sql  = " insert into tbl_product "
						+ " values(?, ?, ?, ?, ?, "
						+ " 	   ?, sysdate, ?, ?, ?, 0, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("productno"));
			pstmt.setString(2, paraMap.get("categoryno"));
			pstmt.setString(3, paraMap.get("name"));
			pstmt.setString(4, paraMap.get("description"));
			pstmt.setString(5, paraMap.get("price"));
			pstmt.setString(6, paraMap.get("thumbnail_img"));
			pstmt.setString(7, paraMap.get("cnt"));
			pstmt.setString(8, paraMap.get("delivery_price"));
			pstmt.setString(9, paraMap.get("detail_html"));
			pstmt.setString(10,  paraMap.get("point_pct"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}

	// 옵션등록 
	@Override
	public int addOption(Map<String, String> paraMap) throws SQLException {

		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql  = " insert into tbl_option values(seq_optionno.nextval, ?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			
			pstmt.setString(1, paraMap.get("productno"));
			pstmt.setString(2, paraMap.get("optionName"));
			pstmt.setString(3, paraMap.get("colorCode"));
			pstmt.setString(4, paraMap.get("optionImg"));

			result = pstmt.executeUpdate();

		} finally {
			close();
		}

		return result;
	}

	// 이미지 등록하기 
	@Override
	public int addImage(Map<String, String> paraMap)throws SQLException {

		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " insert into tbl_product_img values(seq_product_imgno.nextval, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("productno"));
			pstmt.setString(2, paraMap.get("productImg"));
			
			result = pstmt.executeUpdate();

		} finally {
			close();
		}
		
		return result;
	}

	// 상품상세보기 
	@Override
	public List<ProductVO> detailProductList(String productno) throws SQLException {
		
		List<ProductVO> detailProductList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ " select productno, fk_categoryno, name, price, thumbnail_img, registerday, cnt, delivery_price, is_delete, point_pct "
						+ " from tbl_product "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select categoryno, major_category, small_category "
						+ " from tbl_category "
						+ " ) "
						+ " select productno, price, a.name as productname, thumbnail_img, registerday, cnt, delivery_price, is_delete, point_pct, "
						+ "       major_category, small_category "
						+ " from a join b "
						+ " on a.fk_categoryno = b.categoryno "
						+ " where productno = ? "
						+ " order by productno ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, productno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO pvo = new ProductVO();
				CategoryVO cvo = new CategoryVO();
				
				pvo.setProductno(rs.getInt("productno"));
				pvo.setName(rs.getString("productname"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				pvo.setRegisterday(rs.getString("registerday"));
				pvo.setCnt(rs.getInt("cnt"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				pvo.setIs_delete(rs.getInt("is_delete"));
				pvo.setPoint_pct(rs.getInt("point_pct"));
				pvo.setPrice(rs.getInt("price"));
				
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				pvo.setCategoryVO(cvo);
				
				detailProductList.add(pvo);
			}
			
		} finally {
			close();
		}
		
		return detailProductList;
	}

	// 상품 옵션리스트
	@Override
	public List<OptionVO> optionList(String productno) throws SQLException {
		
		List<OptionVO> optionList = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ " 	select color, optionno, fk_productno, name, img "
						+ " 	from tbl_option "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " 	select productno "
						+ " 	from tbl_product "
						+ " ) "
						+ " select fk_productno, color, productno, optionno, name, img "
						+ " from a join b "
						+ " on a.fk_productno = b.productno "
						+ " where productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, productno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OptionVO ovo = new OptionVO();
				
				ovo.setFk_productno(rs.getInt("fk_productno"));
				ovo.setColor(rs.getString("color"));
				ovo.setOptionno(rs.getInt("optionno"));
				ovo.setName(rs.getString("name"));
				ovo.setImg(rs.getString("img"));
				
				optionList.add(ovo);
			}
			
		} finally {
			close();
		}
		
		return optionList;
	}

	// 후기개수
	@Override
	public int reviewcnt(String productno) throws SQLException {
		
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ " select productno "
						+ " from tbl_product "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select fk_productno,optionno "
						+ " from tbl_option "
						+ " ), "
						+ " c as "
						+ " ( "
						+ " select reviewno, fk_optionno "
						+ " from tbl_review "
						+ " ) "
						+ " select count(*) as count "
						+ " from a join b "
						+ " on a.productno = b.fk_productno "
						+ " join c "
						+ " on b.optionno = c.fk_optionno "
						+ " where fk_productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, productno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt("count");
			}
			
		} finally {
			close();
		}
		
		return cnt;
	}

	// 한 컬렉션에 포함된 상품 검색
	@Override
	public List<ProductVO> selectColProductList(String collectionno) throws SQLException {
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select productno, name, price, thumbnail_img, cnt, delivery_price, to_char(registerday, 'yyyy-mm-dd') AS registerday, options_json, img, major_category "
					   + " from tbl_product p "
					   + " join "
					   + " ( "
					   + "     select fk_productno, "
					   + "    '[' || LISTAGG( "
					   + "        '{\"optionno\":' || optionno ||  "
					   + "        ',\"color\":\"' || cast(color as varchar2(10)) || '\"' ||  "
					   + "        ',\"img\":\"' || img || '\"}',  "
					   + "        ',' "
					   + "    ) WITHIN GROUP (ORDER BY optionno) || ']' AS options_json "
					   + "    from tbl_option "
					   + "    group by fk_productno "
					   + " ) o "
					   + " on p.productno = o.fk_productno "
					   + " join "
					   + " ( "
					   + "     select fk_productno, min(img) AS img "
					   + "     from tbl_product_img "
					   + "     group by fk_productno "
					   + " ) pimg "
					   + " on p.productno = pimg.fk_productno"
					   + " join tbl_category c "
					   + " on c.categoryno = p.fk_categoryno "
					   + " where p.is_delete = 0 and productno IN "
					   + " ( "
					   + "     select fk_productno "
					   + "     from tbl_col_product "
					   + "     where fk_collectionno = ? "
					   + " ) "
					   + " order by name ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, collectionno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO pvo = new ProductVO();
				
				pvo.setProductno(rs.getInt("productno"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setThumbnail_img(rs.getString("thumbnail_img"));
				pvo.setRegisterday(rs.getString("registerday"));
				pvo.setCnt(rs.getInt("cnt"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
				
				CategoryVO cvo = new CategoryVO();
				cvo.setMajor_category(rs.getString("major_category"));
				pvo.setCategoryVO(cvo);
				
				// 옵션을 JSON 배열 객체로 가져온다
				JSONArray jsonArr = new JSONArray(rs.getString("options_json"));
				
				List<OptionVO> optionList = new ArrayList<>();
				
				for(int i=0; i<jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					
					OptionVO ovo = new OptionVO();
					ovo.setOptionno(jsonObj.getInt("optionno"));
					ovo.setColor(jsonObj.getString("color"));
					ovo.setImg(jsonObj.getString("img"));
					optionList.add(ovo);
				}
				
				pvo.setOptionList(optionList);
				
				// 상세 이미지 미리보기를 가져온다
				List<ProductImgVO> pimgList = new ArrayList<>();
				
				ProductImgVO pimgvo = new ProductImgVO();
				pimgvo.setImg(rs.getString("img"));
				
				pimgList.add(pimgvo);
				
				pvo.setProductImgList(pimgList);
				
				productList.add(pvo);
			}
			
		} finally {
			close();
		}
		
		return productList;
	}

	// 주문에 필요한 정보를 가져오기 위한 메소드
	@Override
	public Map<String, String> getProductForOrder(String optionno) throws SQLException {
		Map<String, String> resultMap = new HashMap<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select p.name AS p_name, p.price, p.point_pct, p.delivery_price, p.cnt "
					   + "      , o.name AS o_name, o.img AS o_img "
					   + " from tbl_option o "
					   + " join tbl_product p "
					   + " on o.fk_productno = p.productno "
					   + " where optionno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, optionno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				resultMap.put("p_name", rs.getString("p_name"));
				resultMap.put("price", String.valueOf(rs.getInt("price")));
				resultMap.put("point_pct", String.valueOf(rs.getInt("point_pct")));
				resultMap.put("delivery_price", String.valueOf(rs.getInt("delivery_price")));
				resultMap.put("cnt", String.valueOf(rs.getInt("cnt")));

				resultMap.put("o_name", rs.getString("o_name"));
				resultMap.put("o_img", rs.getString("o_img"));
			}
			
		} finally {
			close();
		}
		return resultMap;
	}

	// 상품 총 판매수
	@Override
	public int productSalesCnt(String productno) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ " select productno, optionno "
						+ " from tbl_product a join tbl_option b "
						+ " on a.productno = b.fk_productno "
						+ " ), "
						+ " b as "
						+ " ( "
						+ " select fk_optionno, cnt, fk_orderno "
						+ " from tbl_order_detail "
						+ " ) "
						+ " select productno, nvl(sum(cnt), 0) "
						+ " from a left join b "
						+ " on a.optionno = b.fk_optionno "
						+ " where productno = ? "
						+ " group by productno ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, productno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n = rs.getInt(2);
			}
			
			
		} finally {
			close();
		}
		
		return n;
	}

	// 상품수정하기 기본정보
	@Override
	public ProductVO productList(String productno) throws SQLException {
		
		ProductVO pvo = new ProductVO();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select major_category, small_category, name, description, cnt, price, point_pct, delivery_price "
						+ " from tbl_product a join tbl_category b "
						+ " on a.fk_categoryno = b.categoryno "
						+ " where productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, productno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				CategoryVO cvo = new CategoryVO();
				
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				pvo.setCategoryVO(cvo);
				
				pvo.setName(rs.getString("name"));
				pvo.setDescription(rs.getString("description"));
				pvo.setCnt(rs.getInt("cnt"));
				pvo.setPrice(rs.getInt("price"));
				pvo.setPoint_pct(rs.getInt("point_pct"));
				pvo.setDelivery_price(rs.getInt("delivery_price"));
					
			}
			
			
		} finally {
			
		}
		
		
		return pvo;
	}
	
	// 카테고리 번호 찾기
	@Override
	public int selectCategoryno(String major_category, String small_category)  throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select categoryno "
						+ " from tbl_category "
						+ " where major_category = ? and small_category = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, major_category);
			pstmt.setString(2, small_category);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n = rs.getInt(1);
			}

		} finally {
			close();
		}

		return n;
	}

	@Override
	public int updateProduct(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql =  " update tbl_product set fk_categoryno = ?, name = ? , description = ? , cnt = ?, "
						+ " price = ?, delivery_price = ?, point_pct = ? "
						+ " where productno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("categoryno"));
			pstmt.setString(2, paraMap.get("name"));
			pstmt.setString(3, paraMap.get("description"));
			pstmt.setString(4, paraMap.get("cnt"));
			pstmt.setString(5, paraMap.get("price"));
			pstmt.setString(6, paraMap.get("delivery_price"));
			pstmt.setString(7, paraMap.get("point_pct"));
			pstmt.setString(8, paraMap.get("productno"));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		
		return n;
	}
}
