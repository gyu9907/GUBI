package category.controller;

import org.json.JSONObject;

import category.model.CategoryDAO;
import category.model.CategoryDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminCategoryDuplicate extends AbstractController {
	
	CategoryDAO cdao = new CategoryDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method =	request.getMethod(); 
		
		if("POST".equalsIgnoreCase(method)) {
			
			String smallCategory = request.getParameter("smallCategory"); // ajax 에서 설정한 key 값 잘 확인하기 !! 

			boolean isExists = cdao.categoryDuplicateCheck(smallCategory);
			
			JSONObject jsonObj = new JSONObject(); 
			jsonObj.put("isExists", isExists);     // {"isExists":true}  또는  {"isExists":false} 으로 만들어준다.
			
			String json = jsonObj.toString(); 
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			
		}// end of if("POST".equalsIgnoreCase(method)){}---------
		

	}

}
