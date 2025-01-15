package order.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;
import order.domain.OrderVO;
import order.model.OrderDAO;
import order.model.OrderDAO_imple;

public class OrderStatusUpdate extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	private OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		
		// 로그인하지 않은 경우
		if(!super.checkLogin(request)) {
			jsonObj.put("message", "로그인 후 이용 가능합니다.");
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			
			return;
		}
		
		// GET 방식으로는 접근 불가, 이전 페이지로 돌아가기
		if(!"POST".equalsIgnoreCase(request.getMethod())) {
			jsonObj.put("message", "잘못된 접근입니다.");
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			
			return;
		}
		
		MemberVO loginuser = (MemberVO) request.getSession().getAttribute("loginuser");
		
		String orderno = request.getParameter("orderno");
		String status = request.getParameter("status");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("orderno", orderno);
		paraMap.put("status", status);
		paraMap.put("userid", loginuser.getUserid());
		
		int n = odao.updateOrderStatus(paraMap);
		
		if(n==1) {
			switch (status) {
			case "3":
				jsonObj.put("message", "주문이 취소되었습니다.");
				break;
			case "6":
				OrderVO ovo = odao.getOrder(orderno);
				
				loginuser.setPoint(loginuser.getPoint() + ovo.getReward_point()); // 보상 포인트 추가
				
				jsonObj.put("message", "구매확정이 완료되었습니다. "
				          + new DecimalFormat("#,###").format(ovo.getReward_point())
				          + " 포인트가 지급되었습니다.");
				break;
			case "7":
				jsonObj.put("message", "환불신청이 접수되었습니다.");
				break;

			default:
				jsonObj.put("message", "주문상태 변경 중 오류가 발생했습니다.");
				break;
			}
		}
		else {
			jsonObj.put("message", "주문상태 변경 중 오류가 발생했습니다.");
		}
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");

	}

}
