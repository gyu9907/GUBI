package product.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.OptionVO;
import product.domain.ProductVO;
import product.model.*;

public class AdminDetailProduct extends AbstractController {
	
	ProductDAO pdao = new ProductDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String productno = request.getParameter("productno");
		
		// 상품정보
		List<ProductVO> detailProductList = pdao.detailProductList(productno);
		// 옵션정보
		List<OptionVO> optionList = pdao.optionList(productno);
		// 후기개수
		int reviewcnt = pdao.reviewcnt(productno);
		// 판매개수
		int productSalesCnt = pdao.productSalesCnt(productno);
		// System.out.println("productSalesCnt"+productSalesCnt);

		JSONArray jsonArr = new JSONArray();
		JSONArray optionArr = new JSONArray();
		
		for(ProductVO pvo : detailProductList) {
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("productno", pvo.getProductno());
			jsonObj.put("productname", pvo.getName());
			jsonObj.put("thumbnail_img", pvo.getThumbnail_img());
			jsonObj.put("registerday", pvo.getRegisterday());
			jsonObj.put("cnt", pvo.getCnt());
			jsonObj.put("price", pvo.getPrice());
			jsonObj.put("delivery_price", pvo.getDelivery_price());
			jsonObj.put("is_delete", pvo.getIs_delete());
			jsonObj.put("major_category", pvo.getCategoryVO().getMajor_category());
			jsonObj.put("small_category", pvo.getCategoryVO().getSmall_category());
			
			jsonArr.put(jsonObj);
		}
		for(OptionVO ovo: optionList) {
			JSONObject optionObj = new JSONObject();
			optionObj.put("optionno", ovo.getOptionno());
			optionObj.put("optionname", ovo.getName());
			optionObj.put("img", ovo.getImg());
			optionObj.put("color", ovo.getColor());
			optionObj.put("fk_productno", ovo.getFk_productno());
			
			optionArr.put(optionObj);
		}
		
		JSONObject json = new JSONObject();
		
		json.put("product", jsonArr);
		json.put("option", optionArr);
		json.put("reviewcnt", reviewcnt);
		json.put("productSalesCnt", productSalesCnt);
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(json.toString());
		
		//super.setRedirect(false);
        //super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
