package delivery.model;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
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
import delivery.domain.DeliveryVO;
import util.security.AES256;
import util.security.SecretMyKey;


public class DeliveryDAO_imple implements DeliveryDAO {
   
   private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
   private Connection conn;
   private PreparedStatement pstmt;
   private ResultSet rs;
   private AES256 aes;

   // 생성자
   public DeliveryDAO_imple() {

      try {
         Context initContext = new InitialContext();
         Context envContext = (Context) initContext.lookup("java:/comp/env");
         ds = (DataSource) envContext.lookup("jdbc/myoracle");

         aes = new AES256(SecretMyKey.KEY);

      } catch (NamingException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
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

   
   
   // 배송지 주소를 등록하는 메소드 
   @Override
   public int register(DeliveryVO delivery) throws SQLException {
       int result = 0;
       try {
           conn = ds.getConnection();


           
           String sql = " INSERT INTO tbl_delivery " 
                      + " (deliveryno, fk_userid, delivery_name, is_default, receiver, receiver_tel, postcode, address, detail_address, memo, is_delete) " 
        		      + " VALUES (SEQ_DELIVERYNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
           pstmt = conn.prepareStatement(sql);

           
           
           pstmt.setString(1, delivery.getFk_userid());
           
           // 디버깅용으로 getReceiver() 값 확인
           System.out.println("Receiver value: " + delivery.getReceiver());
           
			// delivery_name이 null이거나 비어 있다면, receiver를 기본값으로 설정.
			/*
			 * String deliveryName = (delivery.getDelivery_name() != null &&
			 * !delivery.getDelivery_name().isEmpty()) ? delivery.getDelivery_name() :
			 * delivery.getReceiver(); // delivery_name이 없으면 receiver 사용
			 * 
			 * // 디버깅용 로그 System.out.println("Delivery Name: " + deliveryName);
			 * 
			 * 
			 * // 배송지 이름을 설정하고, 수령인이 널값이 아니면 정상적으로 처리 if (delivery.getReceiver() == null ||
			 * delivery.getReceiver().isEmpty()) {
			 * System.out.println("Receiver is required but missing!"); // 수령인 필드가 널값 이라면
			 * 예외를 던질 수 있음 (하 ㅈㄴ어렵네) throw new
			 * IllegalArgumentException("Receiver name is required but missing."); }
			 *
             *
        		// 정상적인 값일 때만 setString 호출
        	*	pstmt.setString(2, deliveryName); // delivery_name
              */
           
           pstmt.setString(2, delivery.getDelivery_name());
           
           pstmt.setInt(3, delivery.getIs_default());
           pstmt.setString(4, delivery.getReceiver());
           pstmt.setString(5, aes.encrypt(delivery.getReceiver_tel())); // 전화번호 암호화
           pstmt.setString(6, delivery.getPostcode());
           pstmt.setString(7, delivery.getAddress());
           pstmt.setString(8, delivery.getDetail_address());
           pstmt.setString(9, delivery.getMemo());
           pstmt.setInt(10, delivery.getIs_delete());
           
           // 데이터베이스에 업데이트 실행
           result = pstmt.executeUpdate();

      } catch (SQLException e) {

         // SQL 예외 처리
         e.printStackTrace();
         throw new SQLException("배송지 등록 실패: " + e.getMessage());

      } catch (GeneralSecurityException | UnsupportedEncodingException e) {

         // 암호화 관련 예외 처리
         e.printStackTrace();
         throw new SQLException("암호화 처리 실패: " + e.getMessage());
        
       } finally {
         
           close(); // 자원 반환을 close() 메소드를 이용해 처리
       }

     return result;
   }

   // 배송지 리스트 가져오는 메소드
	@Override
	public List<DeliveryVO> getDeliveryList(String userid) throws SQLException {

		List<DeliveryVO> deliveryList = new ArrayList<>();
		try {
			conn = ds.getConnection();

            String sql = " SELECT deliveryno, delivery_name, receiver, receiver_tel, postcode, "
                       + " address, detail_address, memo, is_default "
                       + " FROM TBL_DELIVERY "
                       + " WHERE fk_userid = ? AND is_delete = 0 ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				DeliveryVO delivery = new DeliveryVO();
				delivery.setDeliveryno(rs.getInt("deliveryno"));
				delivery.setDelivery_name(rs.getString("delivery_name"));
				delivery.setReceiver(rs.getString("receiver"));
				// 전화번호 복호화 처리 (복호화 시 예외 처리 추가)
				String encryptedPhone = rs.getString("Receiver_tel");

				if (encryptedPhone != null && !encryptedPhone.trim().isEmpty()) {
					try {
						delivery.setReceiver_tel(aes.decrypt(encryptedPhone));
					} catch (GeneralSecurityException | UnsupportedEncodingException e) {
						e.printStackTrace(); // 복호화 실패시 에러....??
					}
				}

				delivery.setPostcode(rs.getString("postcode"));
				delivery.setAddress(rs.getString("address"));
				delivery.setDetail_address(rs.getString("detail_address"));
				delivery.setMemo(rs.getString("memo"));
				delivery.setIs_default(rs.getInt("is_default"));

				// 리스트에 추가
				deliveryList.add(delivery);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 복호화 실패시 예외 처리
			throw new SQLException("복호화에 실패했습니다.");
		} finally {
			close(); // 데이터베이스 연결 종료
		}

		return deliveryList;

   } // end of public class MypageDAO_imple implements MypageDAO{}-------------------------------------------------

   
   
   
  // 특정 배송지 정보를 배송지 번호로 조회하는 메소드
   @Override
   public DeliveryVO getDeliveryByNo(String deliveryno) throws SQLException {
	 
	 
	   
       try {
           conn = ds.getConnection();

           String sql = " SELECT deliveryno, delivery_name, receiver, receiver_tel, postcode, address, "
                      + " detail_address, memo, is_default "
                      + " FROM tbl_delivery "
                      + " WHERE deliveryno = ? ";

           pstmt = conn.prepareStatement(sql);
           // deliveryno가 숫자 형식인지를 체크
           try {
               pstmt.setInt(1, Integer.parseInt(deliveryno)); // 숫자 값으로 변환
           } catch (NumberFormatException e) {
               // 유효하지 않은 숫자 형식일 경우 처리
               throw new IllegalArgumentException("유효한 배송지 번호를 입력해야 합니다.");
           }

           rs = pstmt.executeQuery();

           if (rs.next()) {
               DeliveryVO delivery = new DeliveryVO();
               delivery.setDeliveryno(rs.getInt("deliveryno"));
               delivery.setDelivery_name(rs.getString("delivery_name"));
               delivery.setReceiver(rs.getString("receiver"));
               delivery.setReceiver_tel(aes.decrypt(rs.getString("Receiver_tel")));
               delivery.setPostcode(rs.getString("postcode"));
               delivery.setAddress(rs.getString("address"));
               delivery.setDetail_address(rs.getString("detail_address"));
               delivery.setMemo(rs.getString("memo"));
               delivery.setIs_default(rs.getInt("is_default"));

               return delivery;
              
           }
       } catch (GeneralSecurityException | UnsupportedEncodingException e) {
    	   e.printStackTrace();
           // 복호화 실패시 예외 처리
           throw new SQLException("복호화에 실패했습니다.");
       } finally {
           close(); // 데이터베이스 연결 종료
       }
       return null; // 배송지가 없으면 null 반환
   }// end of public DeliveryVO getDeliveryByNo(String deliveryno) throws SQLException {}-----------------------------------------------------


   
   
   
	// 배송지를 수정하는 메소드
	@Override
	public int updateDelivery(DeliveryVO delivery) throws SQLException {
 
		int result = 0;
		
	    try {
	        conn = ds.getConnection();

	        String sql = " update tbl_delivery "
	                   + " set delivery_name = ?, receiver = ?, receiver_tel = ?, postcode = ?, address = ?, "
	                   + " detail_address = ?, memo = ?, is_default = ? "
	                   + " WHERE deliveryno = ? ";

	        pstmt = conn.prepareStatement(sql);

	       
	        // 디버깅용 출력
	        System.out.println("Updating delivery with deliveryno: " + delivery.getDeliveryno());
	        System.out.println("Receiver: " + delivery.getReceiver());
	        
	        pstmt.setString(1, delivery.getDelivery_name());
	        pstmt.setString(2, delivery.getReceiver());
	        // 만약에 배송지를 수정에 성공하면 배송지 목록에 보여지는 전화번호는 복호화된 번호가 들어가야한다. 하지만, 수정해서 db에 들어간 수령인 연락처는 암호화가 되어야한다.
	        pstmt.setString(3, aes.encrypt(delivery.getReceiver_tel())); // 배송지명 암호화
	        pstmt.setString(4, delivery.getPostcode());
	        pstmt.setString(5, delivery.getAddress());
	        pstmt.setString(6, delivery.getDetail_address());
	        pstmt.setString(7, delivery.getMemo());
	        pstmt.setInt(8, delivery.getIs_default());
	        pstmt.setInt(9, delivery.getDeliveryno());
	        result = pstmt.executeUpdate();  

            // 기본 배송지인 경우 표시 처리
            if (delivery.getIs_default() == 1) {
                delivery.setDelivery_name(delivery.getDelivery_name() + " (기본 배송지)");
            }
	        // 수정 결과 로그 출력
	        System.out.println("Rows updated: " + result);
	        
	        
	    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
	        System.out.println("Error during updateDelivery: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        close(); 
	    }

	    return result; 
	    
	}// end of public int updateDelivery(DeliveryVO delivery) throws SQLException {}----------------------
	
	// 기본 배송지를 설정하면 이미 설정된 기본 배송지값을 0으로 변경하는 메소드 
	@Override
	public void set_Other_is_default_to_Null() throws SQLException {
		
		try  {
			 conn = ds.getConnection();
			
		 String sql = " update tbl_delivery set is_default = 0 "
		 		    + " where is_default = 1 ";
		 
		    	pstmt = conn.prepareStatement(sql);
		    	
		        pstmt.executeUpdate();  // 기본 배송지 값을 모두 0으로 설정
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally  {
		    	close();		   
		    }
	}// end of public void set_Other_is_default_to_Null() throws SQLException {}----------------------------------------

	
	// 배송지 삭제 메소드 
	@Override
	public int DeliveryDelete(String[] deliverynoArr) {
	    int deletedCount = 0;

	    try {
	        conn = ds.getConnection();
	        String sql = "delete from tbl_delivery where deliveryno = ?";

	        pstmt = conn.prepareStatement(sql);

	        for (String deliveryno : deliverynoArr) {
	            pstmt.setInt(1, Integer.parseInt(deliveryno));
	            deletedCount += pstmt.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	       
	        try {
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return deletedCount;
	
	}// public int DeliveryDelete(String[] deliverynoArr) {
	
	
	

	// 기본 배송지를 확인하는 메소드
		@Override
		public DeliveryVO getDefaultDeliveryByUser(String userid) throws SQLException {
		    DeliveryVO delivery = null;

		    try {
		        conn = ds.getConnection();

		        String sql = " select * "
		        		   + " from tbl_delivery where fk_userid = ? and is_default = 1 and is_delete = 0 ";
		        pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, userid);

		        rs = pstmt.executeQuery();

		        if (rs.next()) {
		            delivery = new DeliveryVO();
		            delivery.setDeliveryno(rs.getInt("deliveryno"));
		            delivery.setReceiver(rs.getString("receiver"));
		            delivery.setDelivery_name(rs.getString("delivery_name"));
		            delivery.setPostcode(rs.getString("postcode"));
		            delivery.setAddress(rs.getString("address"));
		            delivery.setDetail_address(rs.getString("detail_address"));
		            delivery.setMemo(rs.getString("memo"));
		            delivery.setIs_default(rs.getInt("is_default"));
		        }

		    } finally {
		        close();
		    }

		    return delivery;
		
	}

	

}

