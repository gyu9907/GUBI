package login.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class IdFind extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if (!"POST".equalsIgnoreCase(method)) {
			// post 아님
			
			// 로그인에서 바로 넘어왔다면 get 방식을 이용해서 바로 넘어왔을 것이다.
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/idFind.jsp");
			
		} else {
			// post 방식
			HttpSession session = request.getSession();
			boolean emailCheckOk = (boolean)session.getAttribute("emailCheckOk");
			
			if (!emailCheckOk) { // 해킹 방지용 세션 검사
				
				String message = "인증을 받지않고 바꾸려고 하면 안됩니다!";
				String loc = "javascript:history.back()";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				session.removeAttribute("emailCheckOk"); // 다쓰면 삭제
				
				return; // else 없으면 넣어줘야함!!!!!
			}
			
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("name", name);
			paraMap.put("email", email);
			
			String userid = null;
			try {
				userid = mdao.idFind(paraMap);
				
			} catch (Exception e) {
				e.printStackTrace();
			} // end of try catch..
			
			if (userid != null) {
				// 이제 idFindEnd.jsp 출력 해줘야함 
				request.setAttribute("name", name);
				request.setAttribute("userid", userid);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/login/idFindEnd.jsp");
				
			} else { // 검색 실패 시
				String message = "해당 성명과 이메일로 가입된 계정이 없습니다. 다시 시도해주십시오.";
				String loc = request.getContextPath() + "/login/idFind.gu";
				
				// 세션에 넣었으니 그 값을 이용해보자 이건 get 방식
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
				
			}//end of if else...
			
			

			
		}//end of if else... POST...
		
		
		
		
		
		
		
		
		
		

	}//end of exe...

	
}//end of class..
