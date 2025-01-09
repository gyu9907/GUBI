package cart.controller;

import java.util.List;
import java.util.Map;

import cart.domain.CartVO;
import cart.model.CartDAO;
import cart.model.CartDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;

public class Cart extends AbstractController {

	private CartDAO cartdao = new CartDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인하지 않은 경우
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "로그인 후 이용 가능합니다.");
			request.setAttribute("loc", request.getContextPath() + "/login/login.gu");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}

		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		List<CartVO> cartList = cartdao.getCartList(loginuser.getUserid()); // 상품 재고가 주문수량보다 많은 것만 가져온다.
		
		
		Map<String, String> resultMap = cartdao.getTotal(loginuser.getUserid());
		
		request.setAttribute("cartList", cartList);
		
		request.setAttribute("total_price", resultMap.get("total_price"));
		request.setAttribute("total_point", resultMap.get("total_point"));
		request.setAttribute("total_delivery", resultMap.get("total_delivery"));
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/cart/cart.jsp");

	}

}
