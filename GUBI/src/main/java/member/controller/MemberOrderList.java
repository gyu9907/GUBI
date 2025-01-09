package member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import order.domain.OrderVO;
import order.model.OrderDAO;
import order.model.OrderDAO_imple;

public class MemberOrderList extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
	
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
		
		MemberVO loginuser = (MemberVO) request.getSession().getAttribute("loginuser");

		String sizePerPage = request.getParameter("sizePerPage");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		
		String status = request.getParameter("status");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		if(sizePerPage == null ||
			(!"10".equals(sizePerPage) && !"5".equals(sizePerPage) && !"3".equals(sizePerPage))) {
			sizePerPage = "10";
		}
		if (currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		if(status == null) {
			status = "order";
		}
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("userid", loginuser.getUserid());
		paraMap.put("status", status);
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		paraMap.put("currentShowPageNo", currentShowPageNo);
		paraMap.put("sizePerPage", sizePerPage); // 한페이지당 보여줄 행의 개수
		
		/*** 페이지 바 만들기 시작 ***/
		int totalOrderCnt = odao.getTotalOrder(paraMap);
		int totalPage = (int) Math.ceil((double) totalOrderCnt/Integer.parseInt(sizePerPage));
		
		// 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 totalPage 값 보다 더 큰값을 입력하여 장난친 경우
		// currentShowPageNo 에 0 또는 음수를 입력하여 장난친 경우
		// currentShowPageNo 에 숫자가 아닌 문자열을 입력하여 장난친 경우
		// 아래처럼 막아준다.
		try {
			if(Integer.parseInt(currentShowPageNo) > totalPage ||
			   Integer.parseInt(currentShowPageNo) <= 0) {
				currentShowPageNo = "1";
				paraMap.put("currentShowPageNo", currentShowPageNo);
			}
		} catch(NumberFormatException e) {
			currentShowPageNo = "1";
			paraMap.put("currentShowPageNo", currentShowPageNo);
		}
		String pageBar = "";
		
		int blockSize = 10; // 블럭(토막)당 보여지는 페이지 번호의 개수
        
        int loop = 1; // 1 부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도
		
		// pageNo 구하는 공식을 사용
        int pageNo  = ( (Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1; // 페이지바에서 보여지는 첫번째 번호

		pageBar += "<li class='page-item'><a class='page-link' href='"+request.getContextPath()+"/member/memberOrderList.gu?currentShowPageNo=1&status="+status+"'>&laquo;</a></li>";

        // 첫번째 페이지 바에서는 다음 버튼을 표시하지 않는다.
		if (pageNo > 1) {
			pageBar += "<li class='page-item'><a class='page-link' href='"+request.getContextPath()+"/member/memberOrderList.gu?currentShowPageNo="+(pageNo-1)+"&status="+status+"'>&lt;</a></li>";
		}

        while(!(loop > blockSize || pageNo > totalPage)) {
        	
        	if(pageNo == Integer.parseInt(currentShowPageNo)) {
	        	pageBar += "<li class='page-item active'><a class='page-link' href='"+request.getContextPath()+"/member/memberOrderList.gu?currentShowPageNo="+pageNo+"&status="+status+"'>"+pageNo+"</a></li>";
        	}
        	else {
	        	pageBar += "<li class='page-item'><a class='page-link' href='"+request.getContextPath()+"/member/memberOrderList.gu?currentShowPageNo="+pageNo+"&status="+status+"'>"+pageNo+"</a></li>";
        	}
        	
        	loop++;   // 1  2  3  4  5  6  7  8  9  10
        	
        	pageNo++; // 1  2  3  4  5  6  7  8  9  10
                      // 11 12 13 14 15 16 17 18 19 20
            		  // 21 22 23 24 25 26 27 28 29 30
            		  // 31 32 33 34 35 36 37 38 39 40
            		  // 41 42
        	
        }// end of while()------------------------------
        
        // 마지막 페이지 바에서는 다음 버튼을 표시하지 않는다.
        if(pageNo <= totalPage) {
			pageBar += "<li class='page-item'><a class='page-link' href='"+request.getContextPath()+"/member/memberOrderList.gu?currentShowPageNo="+pageNo+"&status="+status+"'>&gt;</a></li>";
        }
		pageBar += "<li class='page-item'><a class='page-link' href='"+request.getContextPath()+"/member/memberOrderList.gu?currentShowPageNo="+totalPage+"&status="+status+"'>&raquo;</a></li>";
		/*** 페이지 바 만들기 끝 ***/
		
		List<OrderVO> orderList = odao.getOrderList(paraMap);
		
		request.setAttribute("orderList", orderList);
		request.setAttribute("pageBar", pageBar);
		request.setAttribute("currentShowPageNo", currentShowPageNo);
		request.setAttribute("status", status);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/member/memberOrderList.jsp");

	}

}
