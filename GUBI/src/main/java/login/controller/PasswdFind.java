package login.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class PasswdFind extends AbstractController {

//	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if (!"POST".equalsIgnoreCase(method)) {
			// post 아님
			
			// 로그인에서 바로 넘어왔다면 get 방식을 이용해서 바로 넘어왔을 것이다.
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/passwdFind.jsp");
			
		} else {
		
			String userid = request.getParameter("userid");
			String email = request.getParameter("email");
			
			// 받아온 값 다시 넣어줘서 보내주자
			request.setAttribute("userid", userid);
			request.setAttribute("email", email);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/passwdUpdate.jsp");
			
		}//end of if else... POST...
		
		
		
		
		

	}//end of exe...

	
}//end of class...
