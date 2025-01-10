package ask.controller;

import java.util.List;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import ask.domain.AskVO;
import ask.model.*;

public class AskList extends AbstractController {

    private AskDAO adao = new AskDAO_imple();

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

        // 로그인된 사용자의 ID 가져오기
       // String userid = loginuser.getUserid();

        // 문의 내역 조회
        List<AskVO> askList = adao.getAskList(userid);

        // 문의 목록 설정
        if (askList.isEmpty()) {
            request.setAttribute("message", "문의 내역이 없습니다. 문의를 추가하시겠습니까?");
            request.setAttribute("loc", request.getContextPath() + "/ask.gu");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/common/msg.jsp");  // 메시지 페이지로 이동
        } else {
            request.setAttribute("askList", askList);
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/ask/askList.jsp");
        }
    }
}
