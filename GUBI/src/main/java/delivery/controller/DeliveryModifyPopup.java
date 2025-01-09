package delivery.controller;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class DeliveryModifyPopup extends AbstractController {

    private DeliveryDAO ddao = new DeliveryDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	if(!super.checkLogin(request)) {
			request.setAttribute("message", "세션이 만료되었습니다. 다시 로그인해주세요.");
			request.setAttribute("loc", "javascript: self.close();");
			
			super.goBackURL(request);
    		
    		super.setRedirect(false);
    		super.setViewPage("/WEB-INF/common/msg.jsp");
    		return;
    	}
    	
        String method = request.getMethod(); // "GET" 또는 "POST"
        String deliveryno = request.getParameter("deliveryno"); // query parameter로 전달된 deliveryno
        if (deliveryno == null || deliveryno.isBlank()) { 
        	super.setRedirect(true);
        	super.setViewPage(request.getContextPath() + "/delivery/deliveryListPopup.gu "); // 배송지 목록 페이지로 이동
        	return;
        }
        System.out.println("Received deliveryno: " + deliveryno);
        if ("GET".equals(method)) {
            // 수정 화면 (GET 요청)
            DeliveryVO delivery = ddao.getDeliveryByNo(deliveryno);  // 기존 배송지 정보 가져오기
            
            if (delivery == null) { 
            	super.setRedirect(true);
            	super.setViewPage(request.getContextPath() + "/delivery/deliveryListPopup.gu "); // 수정 페이지로 이동
            } else {
            	 request.setAttribute("delivery", delivery);
             	 super.setRedirect(false);
                 super.setViewPage("/WEB-INF/delivery/deliveryModifyPopup.jsp"); // 수정 페이지로 이동
            }
            
           

        } else { // "POST" 요청
        	
        	    if (deliveryno == null || deliveryno.isEmpty()) {
        	        throw new IllegalArgumentException("배송지 번호가 제공되지 않았습니다.");       	        
        	    }
             // 확인용 디버깅
        	 System.out.println("URL: " + request.getRequestURI() + "?deliveryno=" + request.getParameter("deliveryno"));
        	 
             String receiver = request.getParameter("receiver");
             String delivery_name = request.getParameter("delivery_name");
             String hp1 = request.getParameter("hp1");
             String hp2 = request.getParameter("hp2");
             String hp3 = request.getParameter("hp3");
             String postcode = request.getParameter("postcode");
             String address = request.getParameter("address");
             String detail_address = request.getParameter("detail_address");
             String memo = request.getParameter("memo");

             String receiver_tel = hp1 + hp2 + hp3;  // 전화번호 조합
             boolean is_default = "on".equals(request.getParameter("is_default"));  // 체크박스를 확인하여 기본 배송지 설정
             
             // 기본 배송지가 체크되었을 경우 다른 배송지는 기본 배송지 설정을 해제해야 하므로 해당 로직 추가
             if (is_default) {
                 
                 ddao.set_Other_is_default_to_Null();  // 기본 배송지를 설정하면 이미 설정된 기본 배송지값을 0으로 변경하는 메소드
             }

             
             DeliveryVO delivery = new DeliveryVO();
             delivery.setDeliveryno(Integer.parseInt(deliveryno));  // 배송지 번호를 설정
             delivery.setReceiver(receiver);
             delivery.setDelivery_name(delivery_name);
             delivery.setReceiver_tel(receiver_tel);
             delivery.setPostcode(postcode);
             delivery.setAddress(address);
             delivery.setDetail_address(detail_address);
             delivery.setMemo(memo);
             delivery.setIs_default(is_default ? 1 : 0);  // 기본 배송지 여부 설정!! 여기서 문제가 생긴듯 함 

             int result = ddao.updateDelivery(delivery);  // DAO를 사용하여 배송지 수정
             
             String message;
             String loc;
             if (result > 0) {
                 message = "배송지 정보 수정이 완료되었습니다.";
                 loc = request.getContextPath() + "/deliveryRegisterPopup.gu"; // 성공 후 이동할 페이지
             } else {
                 message = "배송지 수정에 실패했습니다.";
                 loc = "javascript:history.back()";  // 실패 시 이전 페이지로 돌아가도록
             }

             request.setAttribute("message", message);
             request.setAttribute("loc", loc);

             super.setRedirect(false);
             super.setViewPage("/WEB-INF/common/msg.jsp");  // 메시지 페이지로 이동
         }
     }
 }