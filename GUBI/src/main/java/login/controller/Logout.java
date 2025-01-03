package login.controller;

import admin.domain.AdminVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class Logout extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse respone) throws Exception {
		
		// 로그아웃 처리하기
		HttpSession session = request.getSession(); // 세션불러오기
		
		/////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////
		
		// 첫번째 방법 : 세션을 그대로 존재하게끔 해두고, 세션에 저장되어진 어떤 값(지금은 로그인 되어진 회원객체)을 삭제하기
//		session.removeAttribute("loginuser");
//		
//		super.setRedirect(true);
//		super.setViewPage(request.getContextPath()+"/index.up");
		
		// 두번째 방법 : WAS 메모리 상에서 세션에 저장된 모든 데이터를 삭제하는 것
		// 이게 더 많이 쓰임
		
		// 관리자가 아닌 일반 사용자로 들어와서 돌아갈 페이지가 있다라면 돌아갈 페이지로 돌아간다.
		// 돌아갈 페이지가 없거나 또는 관리자로 로그아웃을 하면 무조건 /MyMVC/index.up 페이지로 돌아간다.
		AdminVO loginadmin = (AdminVO)session.getAttribute("loginadmin");
		
		if (session.getAttribute("goBackURL") != null && "admin".equals(loginadmin.getAdminid())) {
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + (String) session.getAttribute("goBackURL"));
//			System.out.println("~~~ 확인용 goBackURL => " + request.getContextPath() + (String)session.getAttribute("goBackURL"));
			session.removeAttribute("goBackURL"); //
			return;
		} // end of if...
		
		
		
		session.invalidate();
		
		super.setRedirect(true);
		super.setViewPage(request.getContextPath()+"/index.gu");
		
		
		

	}//end of execute...

	
	
	
}//end of class...










