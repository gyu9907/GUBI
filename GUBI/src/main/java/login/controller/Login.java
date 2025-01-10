package login.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class Login extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if (!"POST".equals(method)) {
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/login.jsp");

			return;
		}
		// POST 방식으로 넘어왔다.
		String userid = request.getParameter("userid");
		String passwd = request.getParameter("passwd");
		// === 접속한 클라이언트의 IP 주소를 알아오는 것 ====
		String clientip = request.getRemoteAddr();
		// 먼저, C:\NCS\workspace_jsp\MyMVC\src\main\webapp\JSP 파일을 실행시켰을 때 IP 주소가 제대로
		// 출력되기위한 방법.txt 참조할 것!!!!!
		

		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		paraMap.put("clientip", clientip);

		MemberVO loginuser = mdao.login(paraMap); // 로그인 정보 가져옴
		if (loginuser != null) {
//			System.out.println("로그인 성공!!!");

			if (loginuser.getIdle() == 1) {// 마지막으로 로그인 한 것이 1년 이상 지난 경우, 탈퇴 유무 확인은 sql 에서 함!
				
//				System.out.println("~~~~~~확인용 휴면처리 대상입니다.");
//				String message = "로그인을 한지 1년이 지나서 휴면상태로 되었습니다.\\n휴면을 풀어주는 페이지로 이동합니다!!";
				
				// post 방식으로 보내주고 싶어서 json 객체로 만들어 보내려고 한다.
//				JSONObject jsonObj = new JSONObject();
//				
//				jsonObj.put("userid", loginuser.getUserid());
//				jsonObj.put("email", loginuser.getEmail());
				
//				String json = jsonObj.toString(); // 변환
				
//				request.setAttribute("json", json); // 담아줬다.
//				request.setAttribute("message", message);
//				request.setAttribute("action", request.getContextPath() + "/login/memberReactivation.gu");

				request.setAttribute("userid", loginuser.getUserid());
				request.setAttribute("email", loginuser.getEmail());
				request.setAttribute("passwd", passwd);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/login/memberReactivation.jsp");

				return; // 메소드 종료
				
			}//end of if (loginuser.getIdle() == 1) {}...

			// 휴면 대상이 아닌 회원으로 로그인 했을 경우

			HttpSession session = request.getSession();
			// WAS 메모리에 생성되어져 있는 session 을 불러오는 것이다.
			session.setAttribute("loginuser", loginuser); // 로그인 되어진 유저의 정보를 넣는다!
			// session(세션)에 로그인 되어진 사용자 정보인 loginuser 를 키이름을 "loginuser" 으로 저장시켜두는 것이다.

			if (loginuser.isRequirePwdChange()) { // 비밀번호 변경한지 3개월 이상된 경우

				String message = "비밀번호를 변경하신지 3개월이 지났습니다.\\n암호를 변경하는 페이지로 이동합니다!!";
				String loc = request.getContextPath() + "/login/passwdUpdate.gu";
				
				// 세션에 넣었으니 그 값을 이용해보자 이건 get 방식
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");

				return; // 메소드 종료

			} else {// 비밀번호 변경한지 3개월 미만인 경우, 완벽한 로그인
				
				// 로그인을 하면 시작페이지(index.up)로 가는 것이 아니라 로그인을 시도하려고 머물렀던 그 페이지로 가기 위한 것이다.
				if (session.getAttribute("goBackURL") != null) {
					super.setRedirect(true);
					super.setViewPage(request.getContextPath() + (String)session.getAttribute("goBackURL"));
					session.removeAttribute("goBackURL"); //
					return;
				}//end of if...
				
				super.setRedirect(true);
				super.setViewPage(request.getContextPath() + "/index.gu"); 
				                             // 아무래도 헤더에 보내줘야할 것이다!! 
				return;
				
			} // end of if else (loginuser.isRequirePwdChange()) { // 비밀번호 변경한지 3개월 이상된 경우 }...

		} else {
			String message = "로그인 실패";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

		} // end of if else (loginuser != null) { }...

		
		
		
		
		
	}// end of exe..

}// end of class...
