package ask.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import ask.model.AskDAO;
import ask.model.AskDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import util.url.GetURL;

public class Ask extends AbstractController {

	private AskDAO adao = new AskDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if (!"POST".equals(method)) { // get 방식
			
			if (!super.checkLogin(request)) { // 로그인을 안했으면 바로 로그인으로 보내버린다.
				String message = "문의를 하시려면 먼저 로그인 해주십시오.";
				String loc = request.getContextPath()+"/login/login.gu";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				// 돌아갈 url 세션값 넣어주기
				HttpSession session = request.getSession();
				session.setAttribute("goBackURL", GetURL.getCurrentURL(request)); 
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
				return;
			}//end of 
			
			// 임시로 보내준 것, 일단 productno 를 받아와야 넣기가 가능
			String productno = request.getParameter("productno");
			request.setAttribute("productno", productno);
			
			// 로그인 했으면 바로 들어온다.
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/ask/ask.jsp");

		} else { // post 방식

			// 먼저 json 객체 선언
			JSONObject jsonObj = new JSONObject(); // {}

			// 문의하기의 전제조건은 먼저 로그인을 해야 하는 것이다.
			if (super.checkLogin(request)) { // 로그인을 했으면

				String userid = request.getParameter("userid"); // 유저아이디
				String productno = request.getParameter("productno");
				String is_hide = request.getParameter("is_hide"); // checkbox 유무 0 1
				
				if (is_hide == null) {
					is_hide = "0"; // 체크박스 체크안하면 null 인데 0 이면 공개글이다.
				}//
				
				String QnA_passwd = request.getParameter("QnA_passwd"); // input 태그는 "" 로 보내준다! 비밀번호
				String QnA_type = request.getParameter("QnA_type"); // select 태그 0 1 2 값
				String question = request.getParameter("question"); // text area 태그
				
				// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** //
				question = question.replaceAll("<", "&lt;");
				question = question.replaceAll(">", "&gt;");

				// 입력한 내용에서 엔터는 <br>로 변환시키기
				question = question.replaceAll("\r\n", "<br>");
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("userid", userid);
				paraMap.put("productno", productno);
				paraMap.put("is_hide", is_hide);
				paraMap.put("QnA_passwd", QnA_passwd);
				paraMap.put("QnA_type", QnA_type);
				paraMap.put("question", question); // 질문 내용
				
				try {
					// 문의하기 insert 하기
					int n = adao.insertAsk(paraMap); // 성공하면 1
					
					if (n == 1) {
						
						String message = "문의 등록이 완료됐습니다!";
						String loc = request.getContextPath()+"/index.gu"; // 원래 마이페이지로 가야한다.
						
						jsonObj.put("n", n);
						jsonObj.put("message", message);
						jsonObj.put("loc", loc);
						
						String json = jsonObj.toString();
						
						request.setAttribute("json", json);
						
						super.setRedirect(false);
						super.setViewPage("/WEB-INF/common/jsonview.jsp");
						return;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
					String message = "서버 문제로 인하여 작동이 불가합니다. 나중에 다시 시도해주십시오.";
					String loc = request.getContextPath() + "/index.gu"; // 시작페이지로 이동한다.

					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					// 부모창 새로고침 및 팝업창 닫기 위한 용도로 반환해주는 값

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/msg.jsp");

				} // end of try catch...

			} else {
				// 로그인을 안 했으면, json 객체에 로그인하라고 말해주고 로그인창으로 이동시키라고 넣어주자.
				String message = "먼저 로그인을 하세요!!";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
			}

		} // end of if (!"POST".equals(method)) {}...

	}// end of exe...

}// end of class..
