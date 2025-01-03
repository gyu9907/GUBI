package product.controller;

import java.util.List;

import collection.domain.CollectionVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

// 상품목록 페이지에서 필요한 정보 조회 컨트롤러
// 총 상품 개수 조회 및 콜렉션 리스트 조회 : requestScope로 전달 예정
public class ProductList extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 콜렉션목록 조회
        List<CollectionVO> collectionList = pdao.selectCollectionList();
        request.setAttribute("collectionList", collectionList);
	      
	    super.setRedirect(false);
	    super.setViewPage("/WEB-INF/product/productList.jsp"); 
	}

}
