package review.model;

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

import product.domain.OptionVO;
import review.domain.ReviewVO;
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
	
	// 내 리뷰 개수 

	@Override
	public int selectReviewCount(Map<String, String> paraMap) throws SQLException {
		
		int count = 0;

		try {
			conn = ds.getConnection();
			
			String sql = "SELECT COUNT(*) "
	                   + " FROM tbl_product P "
	                   + " JOIN tbl_option OPT ON P.productno = OPT.fk_productno "
	                   + " JOIN tbl_review R ON OPT.optionno = R.fk_optionno "
	                   + " WHERE r.fk_userid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userId"));

			rs = pstmt.executeQuery();
			
			rs.next();
	         
	        count = rs.getInt(1); 
	        
		} finally {
			close();
		}
		
		return count;
		
	}


	// 내 리뷰 조회  
	@Override
	public List<ReviewVO> selectReviewList(Map<String, String> paraMap) throws SQLException {
		List<ReviewVO> reviewList = new ArrayList<>();
		try {
			conn = ds.getConnection();
			
			String sql = " WITH ReviewCTE AS ( "
					+ "    SELECT row_number() over(order by reviewno desc) AS RNO , "
					+ "        r.reviewno,  "
					+ "        r.fk_optionno,  "
					+ "        r.fk_userid,  "
					+ "        r.score,  "
					+ "        OPT.name,  "
					+ "        p.productno,  "
					+ "        r.title,  "
					+ "        r.content,  "
					+ "        r.img,  "
					+ "        r.registerday "
					+ "         "
					+ "    FROM tbl_option OPT  "
					+ "    JOIN tbl_review r ON OPT.optionno = r.fk_optionno    "
					+ "    JOIN tbl_product p ON p.productno = OPT.fk_productno "
					+ "    WHERE r.fk_userid = ? "
					+ " ) "
					+ " SELECT  "
					+ "    reviewno,  "
					+ "    fk_optionno,  "
					+ "    fk_userid,  "
					+ "    score,  "
					+ "    name,  "
					+ "    productno,  "
					+ "    title,  "
					+ "    content,  "
					+ "    img,  "
					+ "    registerday "
					+ " FROM ReviewCTE "
					+ " WHERE rownum BETWEEN ? AND ? ";


			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userId"));
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ReviewVO rvo = new ReviewVO();
				
				rvo.setReviewno(rs.getInt("reviewno"));
				rvo.setFk_optionno(rs.getInt("fk_optionno"));
				rvo.setFk_userid(rs.getString("fk_userid"));
				rvo.setScore(rs.getInt("score"));
				
				OptionVO otpvo = new OptionVO();
				otpvo.setName(rs.getString("name"));
				otpvo.setFk_productno(rs.getInt("productno"));
				rvo.setOptionvo(otpvo);
				
				rvo.setTitle(rs.getString("title"));
				rvo.setContent(rs.getString("content"));
				rvo.setImg(rs.getString("img"));
				rvo.setRegisterday(rs.getString("registerday"));
				
				reviewList.add(rvo);
				
			}
			
		} finally {
			close();
		}
		
		return reviewList;
	}
	
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
