package review.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.check.Check;

public class ReviewDAO_imple implements ReviewDAO {
	
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// 생성자
	public ReviewDAO_imple() {

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

	
	// 리뷰를 작성하여 insert 하는 메소드 
	@Override
	public int addReview(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " insert into tbl_review(reviewno, fk_userid, fk_optionno, title, content, score, img, registerday) "
					   + " values(seq_reviewno.nextval, ? , ? , ? , ? , ? , ? , default ) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setInt(2, Integer.parseInt(paraMap.get("optionno")));
			pstmt.setString(3, paraMap.get("title"));
			pstmt.setString(4, paraMap.get("content"));
			pstmt.setString(5, paraMap.get("score"));
			pstmt.setString(6, paraMap.get("img"));
			
			//System.out.println(sql);
			//System.out.println(paraMap);
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return n;
	}

	// 리뷰 수정하는 메소드
	@Override
	public int reviewEdit(Map<String, String> paraMap) throws SQLException {
		int n = 0;
		
		System.out.println(paraMap);
		
		int reviewno = Integer.parseInt(paraMap.get("reviewno"));
		String title = paraMap.get("title");
		int score = Integer.parseInt(paraMap.get("score"));
		String content = paraMap.get("content");
		String img = paraMap.get("img");
		
		try {
			conn = ds.getConnection();
			
			String sql = " UPDATE tbl_review "
					   + " SET title = ?, score = ?, content = ?";
					
			 if (!Check.isNullOrBlank(img)) {
				 sql += ", img = ? ";
			 }
			sql += " WHERE reviewno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setInt(2, score);
			pstmt.setString(3, content);
			if (!Check.isNullOrBlank(img)) {
				pstmt.setString(4, img);
				pstmt.setInt(5, reviewno);
			} else {
				pstmt.setInt(4, reviewno);
			}
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		
		return n;
	}

	
	// 상품을 구매한 유저가 작성한 리뷰 삭제하는 메소드
	@Override
	public int reviewDelete(String reviewno) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " delete from tbl_review "
					   + " where reviewno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(reviewno));
			
			n = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		
		return n;
	}

	

}
