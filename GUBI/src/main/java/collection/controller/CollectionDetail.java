package collection.controller;

import java.util.List;

import collection.domain.CollectionVO;
import collection.model.*;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.check.Check;

public class CollectionDetail extends AbstractController {

	private CollectionDAO coldao = new CollectionDAO_imple();
	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String collectionno = request.getParameter("collectionno");
		
		// 컬렉션 번호가 없으면
		if(Check.isNullOrBlank(collectionno)) {
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/collection/collection.gu");
			return;
		}
		
		// 컬렉션 번호가 숫자인지 확인
		try {
			Integer.parseInt(collectionno);
		} catch (NumberFormatException e) {
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/collection/collection.gu");
			return;
		}
		
		// 컬렉션 정보 조회
		CollectionVO collectionVO = coldao.getCollectionDetail(collectionno);
		
		// 해당하는 컬렉션이 없으면
		if(collectionVO == null) {
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/collection/collection.gu");
			return;
		}
		
		// 컬렉션에 포함된 상품 목록 조회
		List<ProductVO> productList = pdao.selectColProductList(collectionno);
		
		request.setAttribute("collectionVO", collectionVO);
		request.setAttribute("productList", productList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/collection/collectionDetail.jsp");
	}

}
