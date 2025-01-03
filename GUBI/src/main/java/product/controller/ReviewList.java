package product.controller;



import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import review.domain.ReviewVO;

public class ReviewList extends AbstractController {

	ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String productno = request.getParameter("productno");
		
		System.out.println("-------------리뷰조회-------------");
		List<ReviewVO> reviewList = pdao.selectReview(Integer.parseInt(productno));
		
		JSONArray jsonArr = new JSONArray(); 

	    if(reviewList.size() > 0) {
	    	
		    	for(ReviewVO rvo : reviewList) {
		    		
		    		JSONObject jsonObj = new JSONObject();
		    		jsonObj.put("title", rvo.getTitle());    
		    		jsonObj.put("score", rvo.getScore());    
		    		jsonObj.put("content", rvo.getContent());    
		    		jsonObj.put("img", rvo.getImg());    

	            jsonArr.put(jsonObj);
			}
	    }
	    
	    String json = jsonArr.toString();  
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}

}
