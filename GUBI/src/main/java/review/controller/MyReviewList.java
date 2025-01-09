package review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import review.domain.ReviewVO;
import review.model.ReviewDAO;
import review.model.ReviewDAO_imple;

public class MyReviewList extends AbstractController {
	
	ReviewDAO rdao = new ReviewDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

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
