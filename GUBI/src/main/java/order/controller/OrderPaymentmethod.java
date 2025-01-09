package order.controller;

import java.util.List;
import java.util.Map;

import cart.domain.CartVO;
import cart.model.CartDAO;
import cart.model.CartDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class OrderPaymentmethod extends AbstractController {

	private CartDAO cartdao = new CartDAO_imple();
	private ProductDAO pdao = new ProductDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인하지 않은 경우, 이전 페이지로 돌아가기
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "로그인 후 이용 가능합니다.");
			request.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}
		
		// GET 방식으로는 접근 불가, 이전 페이지로 돌아가기
		if(!"POST".equalsIgnoreCase(request.getMethod())) {

			request.setAttribute("message", "잘못된 접근입니다.");
			request.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}
		
		// 넘겨받아야 할 정보 : 장바구니 번호 or 상품 번호와 개수
		
		// 출력할 정보 : 옵션 이미지, 옵션명, 상품명, 상품가격, 개수, 배송비, 포인트
		// 총액, 총 포인트
		
		String optionno = request.getParameter("optionno");         // 옵션 일련번호        (상품 페이지에서 바로 구매하기 버튼을 누른 경우)
		String[] cartno_arr = request.getParameterValues("cartno"); // 장바구니 일련번호 배열 (장바구니에서 넘어온 경우)

		if(optionno != null) { // 바로 주문하기로 넘어온 경우
			String cnt = request.getParameter("cnt");
			
			Map<String, String> resultMap = pdao.getProductForOrder(optionno);
			
			// 만약 재고가 부족한 경우
			if (Integer.parseInt(resultMap.get("cnt")) < Integer.parseInt(cnt)) {

				request.setAttribute("message", "상품의 재고가 부족합니다.");
				request.setAttribute("loc", "javascript:history.back()");

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");

				return;
			}
			
			int price = (int) (Integer.parseInt(resultMap.get("price")) * Integer.parseInt(cnt));
			int total_point = (int) ((Integer.parseInt(resultMap.get("price")) * Integer.parseInt(resultMap.get("point_pct")) / 100.0) * Integer.parseInt(cnt));
			int delivery_price = Integer.parseInt(resultMap.get("delivery_price"));
			
			request.setAttribute("optionno", optionno);
			request.setAttribute("o_img", resultMap.get("o_img"));
			request.setAttribute("p_name", resultMap.get("p_name"));
			request.setAttribute("o_name", resultMap.get("o_name"));
			request.setAttribute("cnt", cnt);
			request.setAttribute("price", price);
			
			request.setAttribute("total_price", price + delivery_price);
			request.setAttribute("total_point", total_point);
			request.setAttribute("total_delivery", delivery_price);
		}
		else if(cartno_arr != null) { // 장바구니에서 넘어온 경우
			
			List<CartVO> cartList = cartdao.getCartList(cartno_arr); // 상품 재고가 주문수량보다 많은 것만 가져온다.
			
			// 만약 개수가 안 맞는다면 재고가 부족한 상품이 있다는 의미이다.
			if(cartno_arr.length > cartList.size()) {
				request.setAttribute("message", "상품의 재고가 부족합니다.");
				request.setAttribute("loc", "javascript:history.back()");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
				
				return;
			}
			
			Map<String, String> resultMap = cartdao.getTotal(cartno_arr);
			
			request.setAttribute("cartList", cartList);
			
			request.setAttribute("total_price", resultMap.get("total_price"));
			request.setAttribute("total_point", resultMap.get("total_point"));
			request.setAttribute("total_delivery", resultMap.get("total_delivery"));
		}
		else {
			request.setAttribute("message", "잘못된 접근입니다.");
			request.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}
		
		request.setAttribute("deliveryno", request.getParameter("deliveryno"));
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/order/orderPaymentmethod.jsp");
	}

}
