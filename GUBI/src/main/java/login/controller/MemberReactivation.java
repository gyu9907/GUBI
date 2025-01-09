package login.controller;



import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberReactivation extends AbstractController {

	MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String method = request.getMethod();
		
		if ("GET".equalsIgnoreCase(method)) {
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/index.jsp");
			
		} else {
			HttpSession session = request.getSession();
			String emailCheckOk = (String) session.getAttribute("emailCheckOk");

			if (emailCheckOk == null) { // 해킹 방지용 세션 검사

				String message = "인증을 받지않고 바꾸려고 하면 안됩니다!";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
				
				return; // else 없으면 넣어줘야함!!!!!
			}
			
			session.removeAttribute("emailCheckOk"); // 다쓰면 삭제
			
			
			String userid = request.getParameter("userid");
			String email = request.getParameter("email");
			String passwd = request.getParameter("passwd");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("email", email);
			
			// 회원 계정 복구 메소드 실행 mdao
			int n = mdao.memberReactivation(paraMap);
			
			String message = "";
			String loc = "";
			
			if (n == 1) {
				request.setAttribute("userid", userid);
				request.setAttribute("passwd", passwd);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/memberReactivation_after_autoLogin.jsp"); // 자동로그인
//				return;
				
			} else {
				message = "서버 문제로 휴면계정 해제가 안됐습니다. 잠시후 시도해주세요.";
				loc = request.getContextPath() + "/index.gu";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
			}
			
			
			
		}//end of if else..

	}//end of exe...

	
	
}//end of class...













