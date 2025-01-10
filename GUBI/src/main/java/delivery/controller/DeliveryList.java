package delivery.controller;

import java.util.List;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class DeliveryList extends AbstractController {

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
    	

        // 배송지 목록 조회
        List<DeliveryVO> deliveryList = ddao.getDeliveryList(userid);

        // 배송지 목록 설정
        if (deliveryList.isEmpty()) {
            request.setAttribute("message", "배송지 목록이 없습니다. 배송지를 추가하시겠습니까?");
            request.setAttribute("loc", request.getContextPath() + "/deliveryRegister.gu");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/common/msg.jsp");  // 메시지 페이지로 이동
            
        } else {
            request.setAttribute("deliveryList", deliveryList);
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/delivery/deliveryList.jsp");
        }

     
    }
}
