package delivery.model;

import java.sql.SQLException;
import java.util.List;

import delivery.domain.DeliveryVO;

public interface DeliveryDAO {

   // 배송지 주소 등록하는 메소드     
   int register(DeliveryVO delivery) throws SQLException;
   
   // 배송지 리스트 가져오는 메소드
   public List<DeliveryVO> getDeliveryList(String userid) throws SQLException;
   
   // 특정 배송지 정보를 배송지 번호로 조회하는 메소드
   DeliveryVO getDeliveryByNo(String deliveryno) throws SQLException;

   // 배송지를 수정하는 메소드
   int updateDelivery(DeliveryVO delivery)throws SQLException;

   // 배송지를 삭제하는 메소드
   int DeliveryDelete(String[] deliverynoArr) throws SQLException;
  
   // 기본 배송지를 확인하는 메소드 
   DeliveryVO getDefaultDeliveryByUser(String parameter) throws SQLException;
   
   // 기본 배송지를 설정하면 이미 설정된 기본 배송지값을 0으로 변경하는 메소드 
   void set_Other_is_default_to_Null()throws SQLException;





  


}
