package delivery.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DeliveryRegister extends AbstractController {
   
    private DeliveryDAO ddao = new DeliveryDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String method = request.getMethod(); // "GET" or "POST"
        
        if ("GET".equals(method)) {
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/delivery/deliveryRegister.jsp"); // 나중에 WEB-INF 경로로 바꿔야함.
        } else { // POST method
                 
            // 데이터 매핑 전, request에서 수령인 확인
            String receiver = request.getParameter("receiver");
            System.out.println("[DEBUG] Receiver (from request): " + receiver);
        
            String delivery_name = request.getParameter("delivery_name"); // 배송지명
            // 배송지명을 입력하지 않으면 수령인 성명이 기본값
            if (delivery_name == null || delivery_name.isEmpty()) {
                delivery_name = receiver; // 수령인이 기본 배송지명
            }
            // 임시로 하드코딩한 사용자 아이디
            HttpSession session = request.getSession();

            if (session.getAttribute("userid") == null) {
            	// 테스트용으로 임시 사용자 아이디를 세션에 넣어버리기 김규빈 방식임
                session.setAttribute("userid", "gyu9907");  // 임시 아이디 설정
            }
            String userid = (String) session.getAttribute("userid");  // 세션에서 userid 가져오기
            
            
            

            String hp1 = request.getParameter("hp1"); // 010
            String hp2 = request.getParameter("hp2"); // 1234
            String hp3 = request.getParameter("hp3"); // 5678
            String postcode = request.getParameter("postcode"); // 우편번호
            String address = request.getParameter("address"); // 주소
            String detail_address = request.getParameter("detail_address"); // 상세주소
            String memo = request.getParameter("memo"); // 배송시 요청사항
        
            String receiver_tel = hp1 + hp2 + hp3;
    
            DeliveryVO delivery = new DeliveryVO();
    
            delivery.setFk_userid(userid);	
            delivery.setDelivery_name(delivery_name);
            delivery.setReceiver(receiver);
            delivery.setReceiver_tel(receiver_tel);
            delivery.setPostcode(postcode);
            delivery.setAddress(address);
            delivery.setDetail_address(detail_address);
            delivery.setMemo(memo);
            
            // is_default 값 처리
            String isDefaultStr = request.getParameter("is_default"); // "true" or "false"
            int isDefault = 0; // 기본값을 0으로 설정
            if ("true".equals(isDefaultStr)) {
                isDefault = 1; // true이면 1로 설정
            }

            // deliveryVO에 is_default 값 설정
            delivery.setIs_default(isDefault);
        
            // 데이터 매핑 후, VO에서 수령인 확인
            System.out.println("[DEBUG] Receiver (from DeliveryVO): " + delivery.getReceiver());

            // 배송지 변경이 성공되어지면 "배송지 변경" 이라는 alert 를 띄우고 마이페이지로 이동
            String message = "";
            String loc = "";
        
            try {
                // 배송지 등록 처리
                int n = ddao.register(delivery);
                if (n == 1) {
                    message = "배송지 등록이 완료되었습니다.";
                    loc = request.getContextPath() + "/myPage.gu"; // 마이페이지로 이동
                } else {
                    message = "배송지 등록이 실패했습니다.";
                    loc = "javascript:history.back()"; // 실패 시 이전 페이지로 돌아감
                }
            } catch (SQLException e) {
                e.printStackTrace();
                message = "배송지 등록 중 오류가 발생했습니다.";
                loc = "javascript:history.back()"; // 오류 발생 시 이전 페이지로 돌아감
            }
    
            request.setAttribute("message", message);
            request.setAttribute("loc", loc);
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
        }
    }// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {}--------------------------------------------------
}// end of class-------------------------------

