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

public class DeliveryListPopup extends AbstractController {

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
    	
    	HttpSession session = request.getSession();
    	MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
    	String userid = loginuser.getUserid();

        // 배송지 삭제 요청 처리
        //String[] deliverynoArr = request.getParameter("deliveryno").split("\\,");

        
     // if (deliverynoArr != null) {
         //  for (String no : deliverynoArr) {
             //  System.out.println("전달된 배송지 번호: " + no); // 디버깅용 출력
          //  }
     //   }

        // 배송지 목록 조회
        List<DeliveryVO> deliveryList = ddao.getDeliveryList(userid);

        // 배송지 목록 설정
        if (deliveryList.isEmpty()) {
            request.setAttribute("message", "배송지 목록이 없습니다. 먼저 배송지를 추가해주세요.");
            request.setAttribute("loc", request.getContextPath() + "/delivery/deliveryRegisterPopup.gu");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/common/msg.jsp");  // 메시지 페이지로 이동
            
        } else {
            request.setAttribute("deliveryList", deliveryList);
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/delivery/deliveryListPopup.jsp");
        }

     
    }
}
