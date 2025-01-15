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

	private CategoryDAO cdao = new CategoryDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if(!super.checkAdmin(request) ) { // 관리자가 아닌 경우
			 // 로그인을 안한 경우 또는 일반사용자로 로그인 한 경우
	         String message = "관리자만 접근이 가능합니다.";
	         String loc = "/GUBI/index.gu";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         // super.setRedirect(false);
	         super.setViewPage("/WEB-INF/common/msg.jsp");
	         return;
		} 
		else {
			if("GET".equalsIgnoreCase(method)) {
				
				String major_category = request.getParameter("major_category");
				String categoryStatus = request.getParameter("categoryStatus");
				
				if(major_category == null &&
					!"SEATING".equalsIgnoreCase(major_category) &&
					!"LIGHTING".equalsIgnoreCase(major_category) &&
					!"TABLES".equalsIgnoreCase(major_category) ) {
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

			} 

		}

	}

}
