package admin.controller;

import java.util.HashMap;
import java.util.Map;

import admin.domain.AdminVO;
import admin.model.AdminDAO;
import admin.model.AdminDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminLogin extends AbstractController {

	private AdminDAO adao = new AdminDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();

		if (!"POST".equals(method)) {
			// post 아님

			String message = "올바른 방법으로 접속하지 않았습니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			return;
		}
		
		// POST 방식으로 넘어왔다.
		String adminid = request.getParameter("adminid");
		String adminpasswd = request.getParameter("adminpasswd");
		
		
		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("adminid", adminid);
		paraMap.put("adminpasswd", adminpasswd);

		AdminVO loginadmin = adao.adminlogin(paraMap); //관리자 로그인 정보 가져옴
		
		
		if (loginadmin != null) {

			HttpSession session = request.getSession();
			// WAS 메모리에 생성되어져 있는 session 을 불러오는 것이다.
			session.setAttribute("loginadmin", loginadmin); // 로그인 되어진 유저의 정보를 넣는다!

			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/index.gu");
			// 아무래도 헤더에 보내줘야할 것이다!!
			
		} else {

			String message = "로그인 실패";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

		} // end of if else (loginuser != null) { }...
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		


	}//end of exe...
	
	

}//end of class...
