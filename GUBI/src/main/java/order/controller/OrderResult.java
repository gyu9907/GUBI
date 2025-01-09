package order.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import order.domain.OrderVO;
import order.model.OrderDAO;
import order.model.OrderDAO_imple;

public class OrderResult extends AbstractController {

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
		
		request.setAttribute("orderVO", ovo);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/order/orderResult.jsp");

	}

}
