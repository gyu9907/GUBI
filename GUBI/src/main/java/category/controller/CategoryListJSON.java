package category.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import category.domain.CategoryVO;
import category.model.CategoryDAO;
import category.model.CategoryDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryListJSON extends AbstractController {

	private CategoryDAO cdao = new CategoryDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String major_category = request.getParameter("major_category");
		
		List<CategoryVO> categoryList = cdao.getCategoryList(major_category); 
		
		JSONArray jsonArr = new JSONArray(); 
		
		if(categoryList.size() > 0) {
	         
	         for(CategoryVO cvo : categoryList) {
	            
	            JSONObject jsonObj = new JSONObject(); 
	            
	            jsonObj.put("major_category", cvo.getMajor_category());  
	            jsonObj.put("small_category", cvo.getSmall_category());   
	            jsonObj.put("category_img", cvo.getCategory_img()); 
	            
	            jsonArr.put(jsonObj);
	            
	         }// end of for------------------------
	         
	      }// end of if-----------------------------
		
		
		String json = jsonArr.toString(); 

	      request.setAttribute("json", json);
	      
	    //  super.setRedirect(false);
	        super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}


}
