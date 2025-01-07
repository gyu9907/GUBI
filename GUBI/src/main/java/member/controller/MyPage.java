package member.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class MyPage extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

	    // 테스트용 사용자
	    HttpSession session = request.getSession();
	    MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

	    if (loginuser == null) {
	        // 세션에 사용자 정보가 없는 경우 테스트용 사용자 생성
	        loginuser = new MemberVO();
	        loginuser.setUserid("mjhan");
	        loginuser.setName("한민정");
	       
	        
	        // 세션에 로그인 사용자 정보 추가
	        session.setAttribute("loginuser", loginuser);
	    }
	    // 임시 테스트 아이디 값 loginuser와 ㄹㅇ 로그인 값인 userid는 서로 다르므로 임시 로그인 아이디로는 마이페이지에 들어갈 수 없다! 그러므로 userid를 loginuser와 값이 같게 바꿔준다!
	    // 로그인 기능이 되면 임시 로그인 테스트는 없앨것!
	    String userid = request.getParameter("userid");
	    if (userid == null) {
	        userid = loginuser.getUserid();  // 로그인한 사용자의 ID를 기본값으로 설정
	    }

	    // 내정보를 수정하기 위한 전제조건은 로그인이 된 상태
	    if (super.checkLogin(request)) {
	        // 로그인을 했으면
	      //  String userid = request.getParameter("userid");

	        if (loginuser.getUserid().equals(userid)) {
	            // 로그인한 사용자가 자신의 정보를 수정하는 경우

	            request.setAttribute("loginuser", loginuser);
	            super.setViewPage("/WEB-INF/member/myPage.jsp");
	        } else {
	            // 로그인한 사용자가 다른 사용자의 마이페이지에 접근하는 경우
	            String message = "다른 사용자의 마이페이지는 들어올 수 없습니다.";
	            String loc = "javascript:history.back()";

	            request.setAttribute("message", message);
	            request.setAttribute("loc", loc);
	            super.setViewPage("/WEB-INF/msg.jsp");
	        }

	    } else {
	        // 로그인을 안 했으면
	        String message = "마이페이지에 들어가기 위해서는 로그인이 필요합니다.";
	        String loc = "javascript:history.back()";

	        request.setAttribute("message", message);
	        request.setAttribute("loc", loc);
	        super.setViewPage("/WEB-INF/msg.jsp");
	    }
	}
}