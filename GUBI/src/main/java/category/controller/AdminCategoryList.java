package category.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import category.domain.CategoryVO;
import category.model.CategoryDAO;
import category.model.CategoryDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminCategoryList extends AbstractController {

	CategoryDAO cdao = new CategoryDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if("GET".equalsIgnoreCase(method)) {
			String major_category = request.getParameter("major_category");
			String categoryStatus = request.getParameter("categoryStatus");
			
			if(major_category == null &&
				!"seating".equalsIgnoreCase(major_category) &&
				!"lighting".equalsIgnoreCase(major_category) &&
				!"tables".equalsIgnoreCase(major_category) ) {
				major_category = "";
			}
			if(categoryStatus == null) {
				categoryStatus = "";
			}
			
			System.out.println(major_category +categoryStatus );
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("major_category", major_category);
			paraMap.put("categoryStatus", categoryStatus);
			
			try {
				List<CategoryVO> categoryList = cdao.selectCategory(paraMap);
				int categoryCnt = cdao.categoryCnt(paraMap);
				
				request.setAttribute("categoryList", categoryList);
				request.setAttribute("categoryCnt", categoryCnt);
				
				request.setAttribute("major_category", major_category);
				request.setAttribute("categoryStatus", categoryStatus);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/admin/adminCategory/categoryList.jsp");	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			
		}
			
			
			
		

		
	}

}
