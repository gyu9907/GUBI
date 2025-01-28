package order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import cart.domain.CartVO;
import cart.model.CartDAO;
import cart.model.CartDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import order.domain.OrderDetailVO;
import order.domain.OrderVO;
import order.model.*;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.check.Check;

public class OrderRegister extends AbstractController {

	private CartDAO cartdao = new CartDAO_imple();
	private ProductDAO pdao = new ProductDAO_imple();
	private OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// GET 방식으로는 접근 불가, 이전 페이지로 돌아가기
		if (!"POST".equalsIgnoreCase(request.getMethod())) {

			request.setAttribute("message", "잘못된 접근입니다.");
			request.setAttribute("loc", "javascript:history.back()");

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
		
		String userid = request.getParameter("userid"); // userid를 파라미터로 받는 이유 : 로그인이 결제 중 풀려도 완료된 결제는 진행되어야 함

		String optionno = request.getParameter("optionno");         // 옵션 일련번호        (상품 페이지에서 바로 구매하기 버튼을 누른 경우)
		String[] cartno_arr = request.getParameterValues("cartno"); // 장바구니 일련번호 배열 (장바구니에서 넘어온 경우)
		int deliveryno = Integer.parseInt(request.getParameter("deliveryno")); // 배송지 일련번호
		
		OrderVO ovo = new OrderVO();
		
		int orderno = odao.getOrderno();
		
		int total_point = 0;    // 총 적립 포인트
		int total_delivery = 0; // 총 배송비
		int total_price = 0;    // 총 결제금액
		int total_cnt = 0;      // 총 수량
		
		if(optionno != null) { // 바로 주문하기로 넘어온 경우
			
			String cnt = request.getParameter("cnt");
			
			Map<String, String> resultMap = pdao.getProductForOrder(optionno);
			
			int price = (int) (Integer.parseInt(resultMap.get("price")) * Integer.parseInt(cnt));
			total_point = (int) ((Integer.parseInt(resultMap.get("price")) * Integer.parseInt(resultMap.get("point_pct")) / 100.0) * Integer.parseInt(cnt));
			total_delivery = Integer.parseInt(resultMap.get("delivery_price"));
			total_price = price + total_delivery;
			total_cnt = Integer.parseInt(cnt);
			
			// 주문 상세
			OrderDetailVO odvo = new OrderDetailVO();
			odvo.setFk_optionno(Integer.parseInt(optionno));
			odvo.setCnt(Integer.parseInt(cnt));
			odvo.setPrice(price);
			odvo.setCartno(-1); // 장바구니 일련번호가 없으므로 -1
			
			List<OrderDetailVO> orderDetailList = new ArrayList<>();
			orderDetailList.add(odvo);
			ovo.setOrderDetailList(orderDetailList);
		}
		else if(cartno_arr != null) { // 장바구니에서 넘어온 경우
			
			List<CartVO> cartList = cartdao.getCartList(cartno_arr);
			
			Map<String, String> resultMap = cartdao.getTotal(cartno_arr);

			// 주문 상세 리스트
			List<OrderDetailVO> orderDetailList = new ArrayList<>();
			
			for(CartVO cvo : cartList) {
				total_cnt += cvo.getCnt();
				
				OrderDetailVO odvo = new OrderDetailVO();
				
				odvo.setFk_optionno(cvo.getFk_optionno());
				odvo.setCnt(cvo.getCnt());
				odvo.setPrice(cvo.getProductVO().getPrice());
				odvo.setCartno(cvo.getCartno()); // 장바구니에서 삭제하기 위해 저장
				
				orderDetailList.add(odvo);
			}
			
			ovo.setOrderDetailList(orderDetailList);
			
			total_point = Integer.parseInt(resultMap.get("total_point"));
			total_delivery = Integer.parseInt(resultMap.get("total_delivery"));
			total_price = Integer.parseInt(resultMap.get("total_price"));
		}
		else {
			request.setAttribute("message", "잘못된 접근입니다.");
			request.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			
			return;
		}
		
		ovo.setOrderno(orderno);
		ovo.setFk_userid(userid);
		ovo.setFk_deliveryno(deliveryno);
		ovo.setTotal_price(total_price);
		ovo.setUse_point(usePoint);
		ovo.setReward_point(total_point);
		ovo.setDelivery_price(total_delivery);
		ovo.setTotal_cnt(total_cnt);

		int n = odao.insertOrder(ovo);
		
		if(n == 1) {
			// 로그인 되어있는 경우 세션에서 포인트 차감
			if (super.checkLogin(request)) {
				HttpSession session = request.getSession();
				MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
				
				loginuser.setPoint(loginuser.getPoint() - usePoint); // 회원 포인트 차감
				session.setAttribute("loginuser", loginuser);
				session.setAttribute("orderno", String.valueOf(orderno));
			}

			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("message", "결제가 완료되었습니다.");
			jsonObj.put("loc", request.getContextPath() + "/order/orderResult.gu");
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			
			return;
		}
//		else if (n == -1) {
//			JSONObject jsonObj = new JSONObject();
//			
//			jsonObj.put("message", "주문하신 상품의 재고가 부족합니다.");
//			jsonObj.put("loc", "javascript:history.back()");
//			
//			String json = jsonObj.toString();
//			
//			request.setAttribute("json", json);
//			
//			super.setRedirect(false);
//			super.setViewPage("/WEB-INF/common/jsonview.jsp");
//			
//			return;
//		}
		else {
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("message", "주문 처리 중 오류가 발생했습니다.");
			jsonObj.put("loc", "javascript:history.back()");
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			
			return;
		}
	}

}
