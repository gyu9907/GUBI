package product.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;

public class CartAdd extends AbstractController {
	
	ProductDAO pdao = new ProductDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// === 로그인 유무 검사하기 === //
	       /*if( !super.checkLogin(request) ) {*/
		if(false) {
	         // 로그인을 하지 않은 상태이라면 
	         
	         /*
	            사용자가 로그인을 하지 않은 상태에서 특정제품을 장바구니에 담고자 하는 경우 
	            사용자가 로그인을 하면 장바구니에 담고자 했던 그 특정제품 페이지로 이동하도록 해야 한다.
	            이와 같이 하려고 먼저, ProdView 클래스에서 super.goBackURL(request); 을 해두었음. 
	         */
	         
	         request.setAttribute("message", "장바구니에 담으려면 먼저 로그인 부터 하세요!!");
	         request.setAttribute("loc", "javascript:history.back()");
	         
	         super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         
	         return;
	      }
	      
	      else { // 로그인 되었을때
	    	 
	    	// 로그인을 한 상태이라면 
	    	// 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
          // 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
          // 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다.
	    	  
	    	  String method = request.getMethod();
	    	  
	    	  System.out.println("method :: "+ method);
	          
	          if("POST".equalsIgnoreCase(method)) {
	             // POST 방식이라면 
	          
	             String optionno = request.getParameter("optionno"); // 제품번호
	             String cnt = request.getParameter("cnt"); // 주문수량
	             
	            // HttpSession session = request.getSession();
	            // MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	            // String userid = loginuser.getUserid(); // 사용자ID
	             String userid = "eomjh";
	             
	             Map<String, String> paraMap = new HashMap<>();
	             paraMap.put("optionno", optionno);
	             paraMap.put("cnt", cnt);
	             paraMap.put("userid", userid);
	             
	             try {
	                int n = pdao.addCart(paraMap); // 장바구니에 해당사용자의 기존 제품이 없을경우 insert 하고,
	                                               // 장바구니에 해당사용자의 기존 제품이 있을경우 update 한다.
	                
	                if(n==1) {
	                   super.setRedirect(true);
	                   super.setViewPage(request.getContextPath()+"/product/productList.gu");
	                }
	             } catch(SQLException e) {
	                request.setAttribute("message", "장바구니 담기 실패!!");
	                request.setAttribute("loc", "javascript:history.back()");
	                
	                super.setRedirect(false);
	                super.setViewPage("/WEB-INF/common/msg.jsp");
	             }
	             
	          } else {
	              // GET 방식이라면
	              String message = "비정상적인 경로로 들어왔습니다";
	              String loc = "javascript:history.back()";
	                 
	              request.setAttribute("message", message);
	              request.setAttribute("loc", loc);
	                
	               super.setRedirect(false);   
	               super.setViewPage("/WEB-INF/common/msg.jsp");
	           }
	      }

	}

}
