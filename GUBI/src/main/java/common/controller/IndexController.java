package common.controller;

import java.util.List;

import collection.domain.CollectionVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import collection.model.*;

public class IndexController extends AbstractController {
	
	private ProductDAO pdao = new ProductDAO_imple();
	private CollectionDAO cdao = new CollectionDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		List<ProductVO> productList = pdao.selectTop3Product();
		List<ProductVO> bestProductList = pdao.selectBestProd();
		String majorCategory = request.getParameter("majorCategory");
		List<CollectionVO> collectionList = cdao.selectCollectionProd(majorCategory);
		
		request.setAttribute("productList", productList);
		request.setAttribute("bestProductlist", bestProductList);
		request.setAttribute("collectionList", collectionList);
		
		System.out.println("collectionList :: " + collectionList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/index.jsp");
	}

}
