package review.controller;

import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import review.model.ReviewDAO;
import review.model.ReviewDAO_imple;

public class MyReviewList extends AbstractController {
	
	private ReviewDAO rdao = new ReviewDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 로그인하지 않은 경우
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "로그인 후 이용 가능합니다.");
			request.setAttribute("loc", request.getContextPath() + "/login/login.gu");
			
			super.goBackURL(request);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("userId", loginuser.getUserid());

		int totalCount = rdao.selectReviewCount(paraMap);
		
		request.setAttribute("totalCount", totalCount);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/review/myReviewList.jsp");
	}

}
