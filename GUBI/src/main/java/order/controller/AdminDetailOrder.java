package order.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import order.domain.OrderVO;
import order.model.*;

public class AdminDetailOrder extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String orderno = request.getParameter("orderno");
		
		// 주문목록 상세조회하기
		List<OrderVO> detailOrderList = odao.detailOrderList(orderno);

		JSONArray jsonArr = new JSONArray();
		
		for(OrderVO ovo : detailOrderList) {
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("orderno", ovo.getOrderno());
			jsonObj.put("userid",ovo.getFk_userid());
			jsonObj.put("totalcnt", ovo.getTotal_cnt());
			jsonObj.put("totalprice", ovo.getTotal_price());
			jsonObj.put("status", ovo.getStatus());
			jsonObj.put("orderday", ovo.getOrderday());
			jsonObj.put("color", ovo.getOpvo().getColor());
			jsonObj.put("optionimg", ovo.getOpvo().getImg());
			jsonObj.put("optionname",ovo.getOpvo().getName());
			jsonObj.put("optionno", ovo.getOpvo().getOptionno());
			jsonObj.put("productname", ovo.getPvo().getName());
			jsonObj.put("thumbnail_img", ovo.getPvo().getThumbnail_img());
			jsonObj.put("price", ovo.getPvo().getPrice());
			jsonObj.put("cnt", ovo.getOdvo().getCnt());
			jsonObj.put("username", ovo.getMvo().getName());
			jsonObj.put("delivery_price", ovo.getPvo().getDelivery_price());
			jsonObj.put("productno", ovo.getPvo().getProductno());
			
			jsonArr.put(jsonObj);
		}
		
		String json = jsonArr.toString();

		request.setAttribute("json", json);
		
		super.setRedirect(false);
        super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}

}
