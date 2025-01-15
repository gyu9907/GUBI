package product.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class ProductListTotalCount extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 총 상품 개수 조회
		String majorCname = request.getParameter("majorCname"); 
		String smallCname = request.getParameter("smallCname"); 
		// 무료배송인지
		String freeshipping = request.getParameter("freeshipping"); 
		
		
		
		System.out.println("major_category ::: " + majorCname);
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("majorCname", majorCname);
		paraMap.put("smallCname", smallCname);
		paraMap.put("freeshipping", freeshipping);
		
		// === Ajax(JSON)를 사용하여 상품목록 "마우스 스크롤" 방식으로 페이징 처리해서 보여주겠다. === //
		int totalProductCount = pdao.totalProductCount(paraMap); // 모든상품의 개수를 알아온다.
		
		 System.out.println("~~~~ 확인용 totalProductCount : " + totalProductCount);

		
		 JSONObject jsonObj = new JSONObject(); // {}
		 jsonObj.put("totalProductCount", totalProductCount); // { totalProductCount: 0 }
		 
		request.setAttribute("json", jsonObj);
	      
	    super.setRedirect(false);
	    super.setViewPage("/WEB-INF/common/jsonview.jsp"); 

	}


}
