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

// 상품별 리뷰 조회
public class ReviewList extends AbstractController {

	ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String productno = request.getParameter("productno");
		
		List<ReviewVO> reviewList = pdao.selectReview(Integer.parseInt(productno));
		
		JSONArray jsonArr = new JSONArray(); 

	    if(reviewList.size() > 0) {
	    	
		    	for(ReviewVO rvo : reviewList) {
		    		
		    		JSONObject jsonObj = new JSONObject();
		    		jsonObj.put("fk_userid", rvo.getFk_userid());    
		    		jsonObj.put("score", rvo.getScore());    
		    		jsonObj.put("name", rvo.getOptionvo().getName());    
		    		jsonObj.put("title", rvo.getTitle());    
		    		jsonObj.put("content", rvo.getContent());    
		    		jsonObj.put("img", rvo.getImg());    
		    		jsonObj.put("registerday", rvo.getRegisterday());    
		    		jsonObj.put("reviewno", rvo.getReviewno());    

	            jsonArr.put(jsonObj);
			}
	    }
	    
	    String json = jsonArr.toString();  
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}

}
