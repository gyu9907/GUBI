package common.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class IndexController extends AbstractController {
	
	ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<ProductVO> productList = pdao.selectTop3Product();
		
		request.setAttribute("productList", productList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/index.jsp");
	}

}
