package category.model;

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
import util.check.Check;

public class CategoryDAO_imple implements CategoryDAO {
	
	private DataSource ds;  // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다. 
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// 생성자
	public CategoryDAO_imple() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semioracle");
		    
		} catch(NamingException e) {
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

	
	
	// 카테고리명 목록을 조회해오기
	@Override
	public List<CategoryVO> getCategoryList(String major_category) throws SQLException {
		
		List<CategoryVO> categoryList = new ArrayList<>(); 
	      
		try {
          conn = ds.getConnection();
          
          String sql = " WITH mainCategory AS ( "
          			 + "    SELECT categoryno, "
	          		 + "           major_category, "
	          		 + "           small_category, "
	          		 + "           category_img, "
	          		 + "           ROW_NUMBER() OVER (PARTITION BY major_category ORDER BY categoryno) AS row_num "
	          		 + "    FROM tbl_category "
	          		 + " ) "
	          		 + " SELECT major_category, category_img "
	          		 + " FROM mainCategory "
	          		 + " WHERE row_num = 2 ";
          
         if(!Check.isNullOrBlank(major_category)) {
        	 	
        	 	sql = " select small_category, category_img "
    	 			+ " from tbl_category "
    	 			+ " where major_category = ? AND is_delete = 0 "
    	 			+ " order by categoryno ";
        	 	
         }
                    
         pstmt = conn.prepareStatement(sql);
         
         if(!Check.isNullOrBlank(major_category)) {
        	 	pstmt.setString(1, major_category);
         }  
         
         rs = pstmt.executeQuery();
                  
         while(rs.next()) {
        	 
            CategoryVO cvo = new CategoryVO();
            
            if(!Check.isNullOrBlank(major_category)) {
        	 		cvo.setSmall_category(rs.getString(1));
            } else {
            		cvo.setMajor_category(rs.getString(1));
            }

            cvo.setCategory_img(rs.getString(2));
            
            categoryList.add(cvo);
         }// end of while(rs.next())----------------------------------
         
      } finally {
         close();
      }   
      
      return categoryList;
	
	} // end of public List<CategoryVO> getCategoryList() throws SQLException-----------

	
	// 헤더카테고리목록조회
	@Override
	public List<CategoryVO> getHeaderCategoryList() throws SQLException {
		List<CategoryVO> categoryList = new ArrayList<>(); 
		try {
	          conn = ds.getConnection();
	          
	          String sql = " SELECT major_category, "
		          		 + "        small_category "
		          		 + " FROM tbl_category "
		          		 + " WHERE is_delete = 0 ";
	         
	         pstmt = conn.prepareStatement(sql); 
	         rs = pstmt.executeQuery();
	         
	                  
	         while(rs.next()) {
	        	 
	            CategoryVO cvo = new CategoryVO();
	            cvo.setMajor_category(rs.getString("major_category"));
	            cvo.setSmall_category(rs.getString("small_category"));
	            
	            categoryList.add(cvo);
	         }// end of while(rs.next())----------------------------------
	         
	      } finally {
	         close();
	      }   
	      
	      return categoryList;
	}

		

	// 카테고리 목록 select 
	@Override
	public List<CategoryVO> CategorySelectAll() throws SQLException {
		
		List<CategoryVO> categoryList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select categoryno, major_category, small_category "
						+ " from tbl_category "
						+ " where is_delete = 0 "
						+ " order by categoryno, major_category ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				CategoryVO cvo = new CategoryVO();
				
				cvo.setCategoryno(rs.getInt("categoryno"));
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				
				categoryList.add(cvo);
			}
			
		} finally {
			close();
		}
		
		return categoryList;
	}

	// 카테고리 추가
	@Override
	public int addCategory(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " insert into tbl_category values(seq_categoryno.nextval, ? , ? , 0, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("major_category"));
			pstmt.setString(2, paraMap.get("small_category"));
			pstmt.setString(3, paraMap.get("img"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return result;
	}

	// 카테고리삭제
	@Override
	public int deleteCategory(String categoryno) throws SQLException {
		
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " update tbl_category set is_delete = 1 "
					   + " where categoryno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(categoryno));
			
			result = pstmt.executeUpdate();
				
		}finally {
			close();
		}
		
		return result;
	}

	// 옵션검색 카테고리조회
	@Override
	public List<CategoryVO> selectCategory(Map<String, String> paraMap) throws SQLException {
		
		List<CategoryVO> categoryList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql =  " with "
						+ " a as "
						+ " ( "
						+ "    select fk_categoryno, count(*) as productcnt "
						+ "    from tbl_product "
						+ "    group by fk_categoryno "
						+ " ), "
						+ " b as "
						+ " ( "
						+ "    select categoryno, major_category, small_category, is_delete, category_img "
						+ "    from tbl_category "
						+ " ) "
						+ " select categoryno, major_category, small_category, is_delete, nvl(productcnt, 0) as productcnt, category_img "
						+ " from a right join b "
						+ " on a.fk_categoryno = b.categoryno "
						+ " where categoryno is not null ";

			String major_category = paraMap.get("major_category");
			String categoryStatus = paraMap.get("categoryStatus");
			
			if(! major_category.isBlank()) {
				sql += " and major_category = ? ";
			}
			if(! categoryStatus.isBlank()) {
				sql += " and is_delete = ? ";
			}
			sql += " order by categoryno ";
			
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			
			if(! major_category.isBlank()) {
				pstmt.setString(index++, major_category);
			}
			if(! categoryStatus.isBlank()) {
				pstmt.setString(index++, categoryStatus);
			}

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				CategoryVO cvo = new CategoryVO();
				
				cvo.setCategoryno(rs.getInt("categoryno"));
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				cvo.setIs_delete(rs.getInt("is_delete"));
				cvo.setProductcnt(rs.getInt("productcnt"));
				cvo.setCategory_img(rs.getString("category_img"));
				
				categoryList.add(cvo);
			}

		} finally {
			close();
		}
		
		return categoryList;
	}

	// 카테고리개수
	@Override
	public int categoryCnt(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql =  " select count(*) as cnt "
						+ " from "
						+ " ( "
						+ " with "
						+ " a as "
						+ " ( "
						+ "    select fk_categoryno, count(*) as productcnt "
						+ "    from tbl_product "
						+ "    group by fk_categoryno "
						+ " ), "
						+ " b as "
						+ " ( "
						+ "    select categoryno, major_category, small_category, is_delete "
						+ "    from tbl_category "
						+ " ) "
						+ " select categoryno, major_category, small_category, is_delete, nvl(productcnt, 0) as productcnt "
						+ " from a right join b "
						+ " on a.fk_categoryno = b.categoryno "
						+ " where categoryno is not null ";
			
			String major_category = paraMap.get("major_category");
			String categoryStatus = paraMap.get("categoryStatus");
			
			if(! major_category.isBlank()) {
				sql += " and major_category = ? ";
			}
			if(! categoryStatus.isBlank()) {
				sql += " and is_delete = ? ";
			}
			sql += " ) ";
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			
			if(! major_category.isBlank()) {
				pstmt.setString(index++, major_category);
			}
			if(! categoryStatus.isBlank()) {
				pstmt.setString(index++, categoryStatus);
			}

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n = rs.getInt("cnt");
			}
			
		} finally {
			close();
		}
		return n;
	}

	// 카테고리 중복검사 
	@Override
	public boolean categoryDuplicateCheck(String smallCategory) throws SQLException {
		
		boolean isExist = false;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select small_category "
						+ " from tbl_category "
						+ " where small_category = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, smallCategory);
			
			rs = pstmt.executeQuery();
			
			isExist = rs.next(); // 중복이면 true
			
		} finally {
			close();
		}

		return isExist;
	}

	// 수정 가능한 카테고리 목록 ( is_delete = 0)
	@Override
	public List<CategoryVO> updateCategorySelect() throws SQLException {
		
		List<CategoryVO> updateCategorySelect = new ArrayList<>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " select categoryno, major_category, small_category, is_delete, category_img "
						+ " from tbl_category "
						+ " where is_delete = 0 "
						+ " order by categoryno, major_category ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CategoryVO cvo = new CategoryVO();
				
				cvo.setCategoryno(rs.getInt("categoryno"));
				cvo.setMajor_category(rs.getString("major_category"));
				cvo.setSmall_category(rs.getString("small_category"));
				cvo.setIs_delete(rs.getInt("is_delete"));
				cvo.setCategory_img(rs.getString("category_img"));
				
				updateCategorySelect.add(cvo);
			}
			
		} finally {
			close();
		}
		
		
		return updateCategorySelect;
	}

	// 카테고리 대분류 조회하기
	@Override
	public List<CategoryVO> majorCategory() throws SQLException {
		
		List<CategoryVO> majorCategory = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select distinct major_category "
					   + " from tbl_category "; 
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CategoryVO cvo = new CategoryVO();
				
				cvo.setMajor_category(rs.getString("major_category"));
				
				majorCategory.add(cvo);
			}
			
		} finally {
			close();
		}
		
		return majorCategory;
	}

	// 카테고리수정하기
	@Override
	public int updateCategory(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql =  " update tbl_category set major_category = ? , small_category = ?, category_img =? "
						+ " where categoryno = ?";
			
			pstmt = conn.prepareStatement(sql);
				
			pstmt.setString(1, paraMap.get("majorCategory"));
			pstmt.setString(2, paraMap.get("smallCategory"));
			pstmt.setString(3, paraMap.get("img"));
			
			pstmt.setString(4, paraMap.get("categoryno"));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		
		return n;
	}
}
