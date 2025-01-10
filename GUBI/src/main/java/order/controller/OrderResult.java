package order.controller;

import common.controller.AbstractController;
import delivery.domain.DeliveryVO;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import order.domain.OrderVO;
import order.model.OrderDAO;
import order.model.OrderDAO_imple;

public class OrderResult extends AbstractController {

	DeliveryDAO ddao = new DeliveryDAO_imple();
	OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		
		String orderno = (String) session.getAttribute("orderno");
		session.removeAttribute("orderno");
//		String orderno = (String) request.getParameter("orderno"); // 테스트용
		
		if(orderno == null) {

			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/index.gu");

			return;
		}
		
		OrderVO ovo = odao.getOrder(orderno);
		
		DeliveryVO dvo = ddao.getDeliveryByNo(String.valueOf(ovo.getFk_deliveryno()));
		
		request.setAttribute("orderVO", ovo);
		request.setAttribute("deliveryVO", dvo);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/order/orderResult.jsp");

	}

}
