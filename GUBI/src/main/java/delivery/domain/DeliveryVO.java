package delivery.domain;

public class DeliveryVO {
   
   private String fk_userid;       /* 회원 아이디 */
   private String delivery_name;   /* 배송지명 */
   private String receiver;         /* 수령인 */
   private String receiver_tel;     /* 수령인 전화번호 AES256 */
   private String postcode;        /* 우편번호 */
   private String address;         /* 주소 */
   private String detail_address;  /* 상세주소 */
   private String memo;            /* 배송시 요청사항 */
   private int is_default;     /* 기본 배송지 여부 */
   private int deliveryno;         /* 배송지 일련번호 */
   private int is_delete;          /* 삭제여부 */
   
   // Getter and Setter Methods

   public String getFk_userid() {
      return fk_userid;
   }
   public void setFk_userid(String fk_userid) {
      this.fk_userid = fk_userid;
   }

   public String getDelivery_name() {
      return delivery_name;
   }
   public void setDelivery_name(String delivery_name) {
      this.delivery_name = delivery_name;
   }

   public String getReceiver() {
      return receiver;
   }
   public void setReceiver(String receiver) {
      this.receiver = receiver;
   }

   public String getReceiver_tel() {
      return receiver_tel;
   }
   public void setReceiver_tel(String receiver_tel) {
      this.receiver_tel = receiver_tel;
   }

   public String getPostcode() {
      return postcode;
   }
   public void setPostcode(String postcode) {
      this.postcode = postcode;
   }

   public String getAddress() {
      return address;
   }
   public void setAddress(String address) {
      this.address = address;
   }

   public String getDetail_address() {
      return detail_address;
   }
   public void setDetail_address(String detail_address) {
      this.detail_address = detail_address;
   }

   public String getMemo() {
      return memo;
   }
   public void setMemo(String memo) {
      this.memo = memo;
   }

   public int getIs_default() {
      return is_default;
   }
   public void setIs_default(int is_default) {
      this.is_default = is_default;
   }

   public int getDeliveryno() {
      return deliveryno;
   }
   public void setDeliveryno(int deliveryno) {
      this.deliveryno = deliveryno;
   }

   public int getIs_delete() {
      return is_delete;
   }
   public void setIs_delete(int is_delete) {
      this.is_delete = is_delete;
   }
}
