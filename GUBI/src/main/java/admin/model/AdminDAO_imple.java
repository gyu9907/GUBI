package admin.model;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import admin.domain.AdminVO;
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class AdminDAO_imple implements AdminDAO {
	
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes; // 양방향 암호화 객체 만들어야함

	// 생성자
	public AdminDAO_imple() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/semioracle");
			
			aes = new AES256(SecretMyKey.KEY);          
			// SecretMyKey.KEY 은 우리가 만든 암호화/복호화 키이다.
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}// end of public ProductDAO_imple() { }...

	// 자원반납 메소드
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

	}// end of close...
	
	

	
	// 관리자 로그인 메소드
	@Override
	public AdminVO adminlogin(Map<String, String> paraMap) throws SQLException {
		
		AdminVO admin = null;

		try {
			conn = ds.getConnection();

			String sql = " SELECT ADMINID, NAME, EMAIL "
					   + " FROM TBL_ADMIN "
					   + " WHERE ADMINID = ? AND PASSWD = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("adminid"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("adminpasswd")));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				admin = new AdminVO();
				
				admin.setAdminid(rs.getString("ADMINID"));
				admin.setName(rs.getString("name"));
				admin.setEmail(rs.getString("email"));
			}
			
		} finally {
			close();
		}
		
		return admin;
		
	}//end of public AdminVO adminlogin(Map<String, String> paraMap) throws SQLException {}...

	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class..


