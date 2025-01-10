package product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class ProductDisplayJSON extends AbstractController {
	
	private ProductDAO pdao = new ProductDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String majorCname = request.getParameter("majorCname"); 
		String smallCname = request.getParameter("smallCname"); 
		String freeshipping = request.getParameter("freeshipping"); 
		String sortby = request.getParameter("sortby"); 
		String collection = request.getParameter("collection"); 
		String start = request.getParameter("start"); 
		String len = request.getParameter("len");
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("majorCname", majorCname);
		paraMap.put("smallCname", smallCname);
		paraMap.put("freeshipping", freeshipping);
		paraMap.put("sortby", sortby);
		paraMap.put("collection", collection);
		paraMap.put("start", start);
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) - 1);
		paraMap.put("end", end);
		
		List<ProductVO> productList;
		
		if(!"".equalsIgnoreCase(collection)) {
			productList = pdao.selectProductByCollection(paraMap); 
		} else {
			productList  = pdao.selectCnoProduct(paraMap); 
		}
		
	    JSONArray jsonArr = new JSONArray(); 

	    if(productList.size() > 0) {
	    	
		    	for(ProductVO pvo : productList) {
		    		
		    		JSONObject jsonObj = new JSONObject(); 
		    		jsonObj.put("productno", pvo.getProductno());    
		    		jsonObj.put("name", pvo.getName());  
		    		jsonObj.put("price", pvo.getPrice());  
		    		jsonObj.put("thumbnail_img", pvo.getThumbnail_img()); 
		    		jsonObj.put("registerday", pvo.getRegisterday());  

		    		
	            jsonArr.put(jsonObj);
			}
	    }
	    
	    String json = jsonArr.toString(); 
	    
	    System.out.println("---------------- json [ProductDisplayJSON] 조회-----------");
	    System.out.println(json);
	    
	    request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}
	

}
	
