package review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import product.domain.ProductVO;
import review.domain.ReviewVO;
import review.model.ReviewDAO;
import review.model.ReviewDAO_imple;

public class MyReviewDisplayJSON extends AbstractController {

	ReviewDAO rdao = new ReviewDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		String start = request.getParameter("start"); 
		String len = request.getParameter("len");
		
		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("userId", loginuser.getUserid());
		paraMap.put("start", start);
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) - 1);
		paraMap.put("end", end);
		
		List<ReviewVO> reviewList = rdao.selectReviewList(paraMap);
		
		
		 JSONArray jsonArr = new JSONArray(); 

		    if(reviewList.size() > 0) {
		    	
			    	for(ReviewVO rvo : reviewList) {
			    		
			    		JSONObject jsonObj = new JSONObject();
			    		jsonObj.put("reviewno", rvo.getReviewno());
			    		jsonObj.put("fk_optionno", rvo.getFk_optionno());
			    		jsonObj.put("fk_userid", rvo.getFk_userid());
			    		jsonObj.put("score", rvo.getScore());
			    		jsonObj.put("productno", rvo.getOptionvo().getFk_productno());
			    		jsonObj.put("optionname", rvo.getOptionvo().getName());
			    		jsonObj.put("title", rvo.getTitle());
			    		jsonObj.put("content", rvo.getContent());
			    		jsonObj.put("img", rvo.getImg());
			    		jsonObj.put("registerday", rvo.getRegisterday());
		            jsonArr.put(jsonObj);
				}
		    }
		    
		    String json = jsonArr.toString(); 
		    
		    System.out.println("---------------- json [MyReviewDisplayJSON] 조회-----------");
		    System.out.println(json);
		    
		    request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}

}
