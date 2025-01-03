package login.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class PasswdUpdate extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if (!"POST".equalsIgnoreCase(method)) {
			// post 아님
			
			// 로그인에서 바로 넘어왔다면 get 방식을 이용해서 바로 넘어왔을 것이다.
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/passwdUpdate.jsp");
			
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
			
			String userid = request.getParameter("userid");
			String new_passwd = request.getParameter("passwd");

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("new_passwd", new_passwd);
			
			// 기존 비밀번호랑 새 비밀번호가 다른지 확인 
			boolean isPasswdExist = false;
			
			isPasswdExist = mdao.passwdExist(paraMap); // true = 같은 비밀번호 
			
			if (isPasswdExist) {
				String message = "기존 비밀번호와 일치합니다! 새로운 비밀번호를 입력해주세요!";
				String loc = request.getContextPath() + "/login/passwdUpdate.gu";
				
				// 세션에 넣었으니 그 값을 이용해보자 이건 get 방식
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
				
				return;
				
			}//end of if (!isPasswdExist) {}...
			

			int n = 0;
			try {
				n = mdao.passwdUpdate(paraMap);
				
			} catch (Exception e) {
				e.printStackTrace();
			} // end of try catch..

			// 비밀번호 변경 성공 시 로그아웃을 하고 새롭게 다시 로그인 하라고 떠야한다.
			if (n == 1) {
				request.setAttribute("userid", userid);
				request.setAttribute("new_passwd", new_passwd);
				
				// 로그아웃 클래스 안쓰고 직접 처리해줬다.
//				HttpSession session = request.getSession(); // 세션불러오기
				session.invalidate();
				
				String message = "비밀번호가 변경됐습니다! 다시 로그인해주세요.";
				String loc = request.getContextPath() + "/login/login.gu";
				
				// 세션에 넣었으니 그 값을 이용해보자 이건 get 방식
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
			
			} else { // 비밀번호 변경 실패 시
				
				String message = "서버 문제가 발생했습니다. 나중에 다시 시도해주십시오.";
				String loc = request.getContextPath() + "/index.gu";
				
				// 세션에 넣었으니 그 값을 이용해보자 이건 get 방식
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

			}//end of if else...(n == 1)
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

		}//end of if else... POST...
		

	}//end of exe...

	
}//end of class...










