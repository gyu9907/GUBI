package collection.controller;

import java.util.List;

import category.domain.CategoryVO;
import collection.model.*;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class Collection extends AbstractController {
	
	private ProductDAO pdao = new ProductDAO_imple();
	private CollectionDAO coldao = new CollectionDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String selected_category = request.getParameter("selected_category");  // "SEATING" or "LIGHTING" or "TABLE"
		
		int totalCollectionCnt = coldao.totalCollectionCnt(selected_category);
		
		// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //
		List<CategoryVO> selectMajorCategory = pdao.selectMajorCategory();
		request.setAttribute("selectMajorCategory", selectMajorCategory);
		// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //
		
		request.setAttribute("totalCollectionCnt", totalCollectionCnt);
		request.setAttribute("selected_category", selected_category);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/collection/collection.jsp");
	}

}
