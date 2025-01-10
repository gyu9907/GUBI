package delivery.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class DeliveryRegister extends AbstractController {
   
    private DeliveryDAO ddao = new DeliveryDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
   

        // 로그인하지 않은 경우 처리
        if (!super.checkLogin(request)) {
        	 goBackURL(request);

            request.setAttribute("message", "로그인 후 이용 가능합니다.");
            request.setAttribute("loc", request.getContextPath() + "/login/login.gu");

            super.setViewPage("/WEB-INF/common/msg.jsp"); // 메시지 페이지로 이동
            return; // 함수 종료
        }
        
        // 세션에서 userid 가져오기
        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
        String userid = loginuser.getUserid();
 

        // 로그인 되어 있으면, 이후 배송지 등록 작업 진행
        String method = request.getMethod(); // "GET" or "POST"

        if ("GET".equals(method)) {
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/delivery/deliveryRegister.jsp"); // 배송지 등록 폼 페이지로 이동
        } else { // POST method
            // 데이터 매핑 전, request에서 수령인 확인
            String receiver = request.getParameter("receiver");
            String delivery_name = request.getParameter("delivery_name"); // 배송지명
            if (delivery_name == null || delivery_name.isEmpty()) {
                delivery_name = receiver; // 수령인이 기본 배송지명
            }

            String hp1 = request.getParameter("hp1");
            String hp2 = request.getParameter("hp2");
            String hp3 = request.getParameter("hp3");
            String postcode = request.getParameter("postcode");
            String address = request.getParameter("address");
            String detail_address = request.getParameter("detail_address");
            String memo = request.getParameter("memo");

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
            String isDefaultStr = request.getParameter("is_default");
            int isDefault = 0;
            if ("true".equals(isDefaultStr)) {
                isDefault = 1; // true이면 1로 설정
            }

            delivery.setIs_default(isDefault);

            String message = "";
            String loc = "";

            try {
                int n = ddao.register(delivery);
                if (n == 1) {
                    message = "배송지 등록이 완료되었습니다.";
                    loc = request.getContextPath() + "/member/myPage.gu"; // 마이페이지로 이동
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
            super.setViewPage("/WEB-INF/common/msg.jsp");
        }
    }

    }

