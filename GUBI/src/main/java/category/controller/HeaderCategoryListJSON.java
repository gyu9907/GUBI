package category.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

import category.domain.CategoryVO;
import category.model.CategoryDAO;
import category.model.CategoryDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HeaderCategoryListJSON extends AbstractController {

	CategoryDAO cdao = new CategoryDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<CategoryVO> categoryList = cdao.getHeaderCategoryList();
		System.out.println(categoryList);

		JSONArray jsonArr = new JSONArray(); 
		
		if(categoryList.size() > 0) {
	         
	         for(CategoryVO cvo : categoryList) {
	            
	            JSONObject jsonObj = new JSONObject(); 
	            
	            jsonObj.put("major_category", cvo.getMajor_category());  
	            jsonObj.put("small_category", cvo.getSmall_category());   
	            
	            jsonArr.put(jsonObj);
	            
	         }// end of for------------------------
	         
	      }// end of if-----------------------------
		
		
		String json = jsonArr.toString(); 
	      
		request.setAttribute("json", json);
		
		//super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");
		 
	}

}
