package review.controller;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import review.domain.AskVO;
import review.model.*;

public class AskAnswer extends AbstractController {

	ReviewDAO rdao = new ReviewDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();
		
		if("GET".equalsIgnoreCase(method)) {
			
			String askno = request.getParameter("askno");
			
			AskVO askDetail = rdao.detailAsk(askno);
			request.setAttribute("askDetail", askDetail);
			request.setAttribute("askno", askno);
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/adminReview/askAnswer.jsp");
			
		} else { // 답변 작성시
			String answer = request.getParameter("answer");
			String askno = request.getParameter("askno");
			
			
			// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** // 
			answer = answer.replaceAll("<", "&lt;");
			answer = answer.replaceAll(">", "&gt;");
			// 입력한 내용에서 엔터는 <br>로 변환시키기
			answer = answer.replaceAll("\r\n", "<br>");	

			int answerAdd = rdao.answerAdd(answer, askno);
			
			JSONObject jsonObj = new JSONObject(); 
			jsonObj.put("answerAdd", answerAdd);
			
			String json = jsonObj.toString(); 
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");

		}

	}

}
