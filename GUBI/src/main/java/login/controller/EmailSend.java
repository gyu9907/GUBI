package login.controller;



import org.json.JSONObject;

import common.controller.AbstractController;
import common.controller.GoogleMail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.security.RandomEmailCode;

public class EmailSend extends AbstractController {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if ("POST".equals(method)) {
			String email = request.getParameter("email");
			
			boolean sendMailSuccess = false; // 메일이 정상적으로 전송되었는지 유무를 알아오기 위한 용도

			JSONObject jsonObj = new JSONObject(); // {}
				
			try {
				RandomEmailCode rd = new RandomEmailCode();
				// 랜덤코드 생성
				String certification_code = rd.makeRandomCode();

				GoogleMail mail = new GoogleMail();

				mail.send_certification_code(email, certification_code); // 이메일 보내기
				sendMailSuccess = true; // 매일전송 성공!
				
				// 세션에 저장해서 인증할 때 쓰자!
				HttpSession session = request.getSession();
				session.setAttribute("certification_code", certification_code);
				
				
			} catch (Exception e) {
				// 이메일 전송 실패시
				sendMailSuccess = false; // 매일전송 실패
			} // end of try catch...

			// 이메일 발송여부도 넣어주자
			jsonObj.put("sendMailSuccess", sendMailSuccess);
			
			// 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}"
			String json = jsonObj.toString(); // 스트링 타입으로 바꿔준다!
			System.out.println("json" + json); // 있으면 true 나옴

			request.setAttribute("json", json);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp"); // 포장해서 jsp 파일로 보냄
			
		} else { // get 방식
			
			String message = "옳바른 방법으로 접속하지 않았습니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			
		}//end of if else...
		

	}//end of exe..

}//end of class..
