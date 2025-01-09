package member.controller;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmailAuth extends AbstractController {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if ("POST".equalsIgnoreCase(method)) {
			
			String email_auth_text = request.getParameter("email_auth_text");
			
			boolean isExists = false; // 초기값 설정
			
			JSONObject jsonObj = new JSONObject(); // 선언
			
			// 세션 가져오기
			HttpSession session = request.getSession(); // 세션 불러오기
			String certification_code = (String)session.getAttribute("certification_code");
					
			// 맞으면 바꿔줌
			if (certification_code.equals(email_auth_text)) {
				isExists = true;
				session.setAttribute("emailCheckOk", "true"); 
				// 유효성 검사에서, 정말로 이메일인증이 확실한지 체크하는 용도로 넣어줌
				
				System.out.println(session.getAttribute("emailCheckOk"));
				
				// !!!! 중요 !!!! //
		        // !!!! 세션에 저장된 인증코드 삭제하기 !!!! //
				session.removeAttribute("certification_code"); // 인증코드만 삭제한다!!!
			}
			
			jsonObj.put("isExists", isExists);

			// 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}"
			String json = jsonObj.toString(); // 스트링 타입으로 바꿔준다!
			System.out.println("json" + json); // 있으면 true 나옴

			request.setAttribute("json", json);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp"); // 포장해서 jsp 파일로 보냄
			
		} else {// get
			String message = "옳바른 방법으로 접속하지 않았습니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			return;
		}//end of if..
		

	}//end of exe...

	
	
}//end of class...
