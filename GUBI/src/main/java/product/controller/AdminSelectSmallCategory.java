package product.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class AdminSelectSmallCategory extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String major_category = request.getParameter("major_category");
		
		List<CategoryVO> selectSmallCategory = pdao.selectSmallCategory(major_category);
		
		JSONArray jsonArr = new JSONArray();
		
		for(CategoryVO cvo : selectSmallCategory) {
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("small_category", cvo.getSmall_category());
			
			jsonArr.put(jsonObj);
			//System.out.println("jsonObj"+jsonObj);
		}
		
		String json = jsonArr.toString();

		request.setAttribute("json", json);
		
		super.setRedirect(false);
        super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}

}
