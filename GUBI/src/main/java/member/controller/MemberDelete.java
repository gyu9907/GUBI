package member.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class MemberDelete extends AbstractController {

	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();

		if (!"POST".equals(method)) { // get 방식

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberDelete.jsp");

		} else { // post 방식
			// 내정보를 수정하기 위한 전제조건은 먼저 로그인을 해야 하는 것이다.
			if (super.checkLogin(request)) { // 로그인을 했으면

				
				
				
				
				
				
				
				
				
			} else {
				// 로그인을 안 했으면, 보통은 의도적으로 뚫고 들어온 경우
				String message = "회원정보를 수정하기 위해서는 먼저 로그인을 하세요!!";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}//end of if else (super.checkLogin(request)) {}...

		}//end of if else (!"POST".equals(method)) {}...
		
		
		
		
		
		
		
		
		

	}//end of exe...

	
}//end of class...
