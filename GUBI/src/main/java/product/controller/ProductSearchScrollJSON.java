package product.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.check.Check;

public class ProductSearchScrollJSON extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String start = request.getParameter("start");
		String len = request.getParameter("len");

		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		String major_category = request.getParameter("major_category");
		String small_category = request.getParameter("small_category");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
//		String is_delete = request.getParameter("is_delete");
		String is_delete = "0";
		String startPrice = request.getParameter("startPrice");
		String endPrice = request.getParameter("endPrice");
		String[] arrColor = request.getParameterValues("color");
		String sortby = request.getParameter("sortby");
		String startCnt = request.getParameter("startCnt");
		String endCnt = request.getParameter("endCnt");

		String colors = "";
		if(arrColor != null) {
			colors = String.join(",", arrColor);
		}
		
		if(Check.isNullOrBlank(start)) {
			start = "1";
		}
		if(Check.isNullOrBlank(len)) {
			len = "16";
		}
		
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) - 1);
		
		if(searchType == null ||
			!("name".equals(searchType) ||
			"productno".equals(searchType))) {
			searchType = "name";
		}
		
		if(searchWord == null) {
			searchWord = "";
		}
		
		if(Check.isNullOrBlank(sortby)) {
			sortby = "latest";
		}
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("start", start);
		paraMap.put("end", end);
		
		paraMap.put("major_category", major_category);
		paraMap.put("small_category", small_category);
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		paraMap.put("is_delete", is_delete);
		paraMap.put("startPrice", startPrice);
		paraMap.put("endPrice", endPrice);
		paraMap.put("colors", colors);
		paraMap.put("sortby", sortby);
		paraMap.put("startCnt", startCnt);
		paraMap.put("endCnt", endCnt);
		
		int totalProductCnt = pdao.getTotalProductCnt(paraMap);
		
		try {
			List<ProductVO> productList = pdao.getProductListWithScroll(paraMap);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productList", productList);
			jsonObj.put("totalProductCnt", totalProductCnt);
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/error.gu");
		}
	}

}
