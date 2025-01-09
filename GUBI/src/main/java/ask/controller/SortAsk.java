package ask.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ask.domain.AskVO;
import ask.model.AskDAO;
import ask.model.AskDAO_imple;
import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class SortAsk extends AbstractController {

	private AskDAO adao = new AskDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 어차피 get 방식		
		
		String userid = null;
		if (super.checkLogin(request)) { // 로그인된 경우
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
			
			userid = loginuser.getUserid(); // 로그인 안하면 null
		}
		
		String QnA_type = request.getParameter("QnA_type"); // 선택유형 -1 0 1 2
		// ALL 이면 -1로 만들어주자
		QnA_type = String.valueOf((Integer.parseInt(QnA_type)-1)); // 선택유형 -1 0 1 2
		
		String productno = request.getParameter("productno");
		
		
		System.out.println("userid => "+userid);
		System.out.println("QnA_type => "+QnA_type);
//		System.out.println("productno => "+productno);
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("QnA_type", QnA_type);
		paraMap.put("productno", productno);
		
		try {
			// 분류에 따른 리스트 뽑아주기 메소드
			List<AskVO> askList = adao.sortAsk(paraMap); // 성공하면 
			
			System.out.println("askList => "+askList);
			
			JSONArray jsonArr = new JSONArray(); 
			
			if (!askList.isEmpty()) {
				
				for (AskVO avo : askList) {
					
					JSONObject jsonObj = new JSONObject(); // {}
					
					jsonObj.put("fk_userid", avo.getFk_userid());
					
					// 정말로 로그인한 유저 아이디 가져온 값의 로그인 아이디가 같은지 확인
					if (avo.getFk_userid().equals(userid)) {
						jsonObj.put("is_loginuser", true);
					} else {
						jsonObj.put("is_loginuser", false);
					}
					
					jsonObj.put("fk_productno", avo.getFk_productno());
					
					String subject = avo.getQuestion(); // 제목유형을 만들어줬다.
					if (subject.length() < 20) {
						jsonObj.put("subject", subject);
					
					} else {
						String subject_str = subject.substring(0, 20)+"...";
						jsonObj.put("subject",subject_str);
					}
					jsonObj.put("question", avo.getQuestion());
//					System.out.println("답변 널값이면 => "+avo.getAnswer());
					jsonObj.put("answer", avo.getAnswer());
					jsonObj.put("is_hide", avo.getIs_hide());
					jsonObj.put("registerday", avo.getRegisterday());
					jsonObj.put("answerday", avo.getAnswerday());
					jsonObj.put("ask_category", avo.getAsk_category());
					
					jsonArr.put(jsonObj);
					
				}//end of for...

				String json = jsonArr.toString();
				
				request.setAttribute("json", json);
//				System.out.println("확인용 json => "+json);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/jsonview.jsp");
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
		
		
		
		
		
		
		
		
		
		
		
		

	}//end of exe..

	
}//end of class...
