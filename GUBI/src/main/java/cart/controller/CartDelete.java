package cart.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import cart.model.CartDAO;
import cart.model.CartDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;

public class CartDelete extends AbstractController {

	private CartDAO cdao = new CartDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONObject jsonObj = new JSONObject();
		
		if(!super.checkLogin(request)) { // 로그인 되어있지 않은 경우
			jsonObj.put("success", false);
			jsonObj.put("message", "로그아웃 되었습니다.");
		}
		else { // 로그인한 경우
			MemberVO loginuser = (MemberVO) request.getSession().getAttribute("loginuser");
			
			String cartno = request.getParameter("cartno");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("cartno", cartno);
			paraMap.put("fk_userid", loginuser.getUserid());
			
			try {
				int n = cdao.deleteCart(paraMap); // 장바구니 삭제
				
				if(n == 1) {
					jsonObj.put("success", true);
				}
				else {
					jsonObj.put("success", false);
					jsonObj.put("message", "장바구니 삭제를 실패했습니다.");
				}
			} catch (SQLException e) {
				jsonObj.put("success", false);
				jsonObj.put("message", "장바구니 삭제를 실패했습니다.");
			}
		}

		String json = jsonObj.toString();
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");

	}

}
