package delivery.controller;

import org.json.JSONObject;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetDeliveryJSON extends AbstractController {

	DeliveryDAO ddao = new DeliveryDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		
		if(!super.checkLogin(request)) {
			jsonObj.put("mesage", "로그인이 필요한 서비스입니다.");
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			return;
		}
		
		String method = request.getMethod();
		if(!"POST".equalsIgnoreCase(method)) {
			jsonObj.put("mesage", "올바르지 않은 접근입니다.");
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			return;
		}
		
		String deliveryno = request.getParameter("deliveryno");
		
		DeliveryVO deliveryVO =  ddao.getDeliveryByNo(deliveryno);
		
		jsonObj.put("deliveryno", deliveryVO.getDeliveryno());
		jsonObj.put("receiver", deliveryVO.getReceiver());
		jsonObj.put("receiver_tel", deliveryVO.getReceiver_tel());
		jsonObj.put("postcode", deliveryVO.getPostcode());
		jsonObj.put("address", deliveryVO.getAddress());
		jsonObj.put("detail_address", deliveryVO.getDetail_address());
		jsonObj.put("memo", deliveryVO.getMemo());
		
		String json = jsonObj.toString();

		request.setAttribute("json", json);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");

	}

}
