package member.controller;

import java.util.Map;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import order.model.*;

public class MyPage extends AbstractController {

    private OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

        // 로그인하지 않은 경우 처리
        if (!super.checkLogin(request)) {
           
        	super.goBackURL(request);
        	
        	request.setAttribute("message", "로그인 후 이용 가능합니다.");
            request.setAttribute("loc", request.getContextPath() + "/login/login.gu");
           
            super.setViewPage("/WEB-INF/common/msg.jsp");
          
            return;
        }

        // 현재 로그인된 사용자의 ID 가져오기
        String userid = loginuser.getUserid();

        // 주문 상태 가져오기
        Map<String, Integer> orderStatusMap = odao.getOrderStatusByUserId(userid);
        request.setAttribute("orderStatusMap", orderStatusMap);

        // 자신의 마이페이지인지 확인
        String pageOwnerId = request.getParameter("userid");
        if (pageOwnerId == null || pageOwnerId.equals(userid)) {
            // 자신의 마이페이지
            request.setAttribute("loginuser", loginuser);
            super.setViewPage("/WEB-INF/member/myPage.jsp");
        } else {
            // 다른 사용자의 마이페이지에 접근 시도
            request.setAttribute("message", "다른 사용자의 마이페이지는 들어올 수 없습니다.");
            request.setAttribute("loc", "javascript:history.back()");
            super.setViewPage("/WEB-INF/common/msg.jsp");
        }
    }
}
