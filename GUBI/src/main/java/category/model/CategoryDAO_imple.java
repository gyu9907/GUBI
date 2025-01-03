package category.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import category.domain.CategoryVO;
import util.check.Check;

public class CategoryDAO_imple implements CategoryDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public CategoryDAO_imple() {

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
    	 			+ " where major_category = ? "
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
	
	
	
	
	
	
	
	
	
}
