package product.controller;

import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.OptionVO;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class ProductDetail extends AbstractController {
	
	ProductDAO pdao = new ProductDAO_imple();
	
	public void redirect(HttpServletRequest request) {
		super.setRedirect(true);
		super.setViewPage(request.getContextPath()+"/product/productList.gu");
	}
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(!super.checkLogin(request)) {
			super.goBackURL(request);
		}
		
		String productno = request.getParameter("productno");
		
		try {
			Integer.parseInt(productno);
		} catch(NumberFormatException e) {
			redirect(request);
			return;
		}
		
		// 제품번호로 해당 제품의 상세정보를 조회해오기
		ProductVO pvo = pdao.selectDetailByProductno(productno);
        List<OptionVO> optionList = pdao.selectOptionList(productno);
		
		if(pvo == null) {
	       redirect(request); 
	       return;
		}
	    
        request.setAttribute("pvo", pvo); 
        request.setAttribute("category_name", pvo.getCategoryVO().getSmall_category()); 
        request.setAttribute("optionList", optionList);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/product/productDetail.jsp");

	}

}
