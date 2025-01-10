package category.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import category.model.*;

public class AdminCategoryDelete extends AbstractController {

	CategoryDAO cdao = new CategoryDAO_imple();
	
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
		
		else { // 관리자인경우
			
			if("POST".equalsIgnoreCase(method)) {
				
				String categoryno = request.getParameter("categoryno");
				
				if(categoryno != null && !categoryno.isEmpty()) {
					int b = cdao.deleteCategory(categoryno); // 카테고리삭제
					
					if(b==1) {
						String message = "카테고리가 삭제되었습니다";
						String loc = request.getContextPath()+"/admin/categoryAdd.gu";
						
						request.setAttribute("message", message);
						request.setAttribute("loc", loc);
						
						super.setRedirect(false); 
						super.setViewPage("/WEB-INF/common/msg.jsp");
						
						return;
					}
				
			   }

			} // end of if("POST".equalsIgnoreCase(method))
		}
		
		
		
		
		
	}
}
