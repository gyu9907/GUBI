package member.controller;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class IdDuplicateCheck extends AbstractController {
	
	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse respone) throws Exception {
		
		String method = request.getMethod();
		
		if ("POST".equals(method)) {
			String userid = request.getParameter("userid");
//			System.out.println("userid  "+userid);
			

			boolean isExists = mdao.idDuplicateCheck(userid);
			
			JSONObject jsonObj = new JSONObject(); // {}
			// {"isExists":"김태희", "age":35, "address":"서울시"} 처럼
			// {"isExists":true} 또는 {"isExists":false}
			// 자바에서 자바스크립트 객체 형태로 값을 바꿔주는 추가 패키지
			jsonObj.put("isExists", isExists);
//			jsonObj.put("name", "김태희"); // {"isExists":true ,"name":"김태희"} 처럼 추가된다
			
			// 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}"
			String json = jsonObj.toString(); // 스트링 타입으로 바꿔준다!
//			System.out.println("json"+ json); // 있으면 true 나옴
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp"); //포장해서 jsp 파일로 보냄
//			super.setViewPage("/WEB-INF/jsonview.jsp"); // js\member\memberRegister.js
		
		} else { // get 방식
			String message = "옳바른 방법으로 접속하지 않았습니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			return;
		} 
		
		
	}//end of execute()...

}//end of class...
