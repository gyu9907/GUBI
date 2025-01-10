package order.controller;

import java.util.List;
import java.util.Map;

import cart.domain.CartVO;
import cart.model.CartDAO;
import cart.model.CartDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.check.Check;

public class OrderPayment extends AbstractController {

	private CartDAO cartdao = new CartDAO_imple();
	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인하지 않은 경우, 팝업을 닫기
		if (!super.checkLogin(request)) {
			request.setAttribute("message", "로그인 후 이용 가능합니다.");
			request.setAttribute("loc", "javascript: self.close();");

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			return;
		}
		
		// GET 방식으로는 접근 불가, 이전 페이지로 돌아가기
		if (!"POST".equalsIgnoreCase(request.getMethod())) {

			request.setAttribute("message", "잘못된 접근입니다.");
			request.setAttribute("loc", "javascript: self.close();");

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			return;
		}
		
		String use_point = request.getParameter("use_point");
		
		// 만약 사용 포인트가 비어있다면 0으로 설정
		if(Check.isNullOrBlank(use_point)) {
			use_point = "0";
		}
		
		int usePoint = Integer.parseInt(use_point);
		
		MemberVO loginuser = (MemberVO) request.getSession().getAttribute("loginuser");
		int memberPoint = loginuser.getPoint();
		
		if(usePoint > memberPoint) {
			request.setAttribute("message", "보유 포인트보다 많은 포인트를 사용할 수 없습니다.");
			request.setAttribute("loc", "javascript: self.close();");

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");

			return;
		}
		

		String optionno = request.getParameter("optionno");
		String[] cartno_arr = request.getParameterValues("cartno");

		String order_name = ""; // 주문 이름
		int amount_paid = 0;    // 결제 금액
		
		if(optionno != null) { // 바로 주문하기로 넘어온 경우
			String cnt = request.getParameter("cnt");

			Map<String, String> resultMap = pdao.getProductForOrder(optionno);
			
			// 만약 재고가 부족한 경우
			if (Integer.parseInt(resultMap.get("cnt")) < Integer.parseInt(cnt)) {

				request.setAttribute("message", "상품의 재고가 부족합니다.");
				request.setAttribute("loc", "javascript: self.close();");

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");

				return;
			}
			
			int price = (int) (Integer.parseInt(resultMap.get("price")) * Integer.parseInt(cnt));
			int delivery_price = Integer.parseInt(resultMap.get("delivery_price"));
			
			order_name = resultMap.get("p_name");
			amount_paid = price + delivery_price - usePoint;
		}
		else if(cartno_arr != null) { // 장바구니에서 넘어온 경우
			
			List<CartVO> cartList = cartdao.getCartList(cartno_arr); // 상품 재고가 주문수량보다 많은 것만 가져온다.
			
			// 만약 개수가 안 맞는다면 재고가 부족한 상품이 있다는 의미이다.
			if(cartno_arr.length > cartList.size()) {
				
				System.out.println("cartno_arr.length "+cartno_arr.length);
				System.out.println("cartList.size() "+cartList.size());
				
				request.setAttribute("message", "상품의 재고가 부족합니다.");
				request.setAttribute("loc", "javascript: self.close();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
				
				return;
			}
			
			Map<String, String> resultMap = cartdao.getTotal(cartno_arr);
			
			int total_cnt = 0;
			
			for(int i=0; i<cartList.size(); i++) {
				total_cnt += cartList.get(i).getCnt();
			}
			
			order_name = cartList.get(0).getProductVO().getName() + " 외 " + total_cnt + "개";
			amount_paid = Integer.parseInt(resultMap.get("total_price")) - usePoint;
		}
		else {
			request.setAttribute("message", "잘못된 접근입니다.");
			request.setAttribute("loc", "javascript: self.close();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}
		
		// 결제 테스트를 위해 값을 10000원 이해로 낮춤
		while(amount_paid > 10000) {
			amount_paid = amount_paid/10;
		}

		// 결제 테스트를 위해 값을 1000원 으로 낮춤
//		amount_paid = 1000;
		
		request.setAttribute("order_name", order_name);
		request.setAttribute("amount_paid", amount_paid);
		request.setAttribute("use_point", use_point);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/order/orderPayment.jsp");

	}

}
