package product.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.*;

public class AdminProductUpdate extends AbstractController {

	ProductDAO pdao = new ProductDAO_imple();
	
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
		} 
		else {
			if("GET".equalsIgnoreCase(method)) {
				String productno = request.getParameter("productno");
				request.setAttribute("productno", productno);
				// System.out.println("productno"+productno);
				
				List<CategoryVO> selectMajorCategory = pdao.selectMajorCategory();
				request.setAttribute("selectMajorCategory", selectMajorCategory);
				
				ProductVO productList = pdao.productList(productno);
				request.setAttribute("productList", productList);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/admin/adminProduct/adminProductUpdate.jsp");
			}
			else { // post 상품 수정하기 update 
				String productno = request.getParameter("productno");
				// System.out.println(productno);
				String major_category = request.getParameter("major_category");
				String small_category = request.getParameter("small_category");
				String name = request.getParameter("name");
				String description = request.getParameter("description");
				String cnt = request.getParameter("cnt");
				String price = request.getParameter("price");
				String delivery_price = request.getParameter("delivery_price");
				String point_pct = request.getParameter("point_pct");
				System.out.println("productno"+productno);
				System.out.println("major_category"+major_category);
				System.out.println("small_category"+small_category);
				System.out.println("name"+name);
				System.out.println("description"+description);
				System.out.println("cnt"+cnt);
				System.out.println("price"+price);
				System.out.println("delivery_price"+delivery_price);
				System.out.println("point_pct"+point_pct);
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("productno", productno);
				paraMap.put("major_category", major_category);
				paraMap.put("small_category", small_category);
				paraMap.put("name", name);
				paraMap.put("description", description);
				paraMap.put("cnt", cnt);
				paraMap.put("price", price);
				paraMap.put("delivery_price", delivery_price);
				paraMap.put("point_pct", point_pct);
				
				// System.out.println(major_category + small_category);
				// 카테고리 번호찾기
				int selectCategoryno = pdao.selectCategoryno(major_category,small_category);
				paraMap.put("categoryno", Integer.toString(selectCategoryno));
				
				//System.out.println("selectCategoryno"+selectCategoryno);
				// 상품 수정하기
				int updateProduct = pdao.updateProduct(paraMap);
				System.out.println("updateProduct"+updateProduct);
				if(updateProduct == 1) {
					
					 JSONObject jsonObj = new JSONObject();  // {}
		             jsonObj.put("result", updateProduct);
		           
		             String json = jsonObj.toString(); // 문자열로 변환 
		             request.setAttribute("json", json);
		           
		             super.setRedirect(false);
		             super.setViewPage("/WEB-INF/jsonview.jsp");
		             
				} else {
					String message = "상품 수정이 실패되었습니다";
					String loc = request.getContextPath()+"/admin/productUpdate.gu";
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false); 
					super.setViewPage("/WEB-INF/common/msg.jsp");
					
					return;
					
				}

			}
			
		}

	}

}
