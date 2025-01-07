package delivery.controller;

import java.util.List;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DeliveryList extends AbstractController {

    private DeliveryDAO ddao = new DeliveryDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
	    String userid = (String) session.getAttribute("userid");

	    if (userid == null) {
	        // 임시 아이디 설정 (테스트용)
	        session.setAttribute("userid", "mjhan");
	        userid = "mjhan";  // 임시 아이디 설정, 나중에 로그인 기능 구현시 제거
	    }
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
