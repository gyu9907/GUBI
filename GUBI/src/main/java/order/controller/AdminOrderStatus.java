package order.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import order.domain.OrderVO;
import order.model.*;

public class AdminOrderStatus extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if("GET".equalsIgnoreCase(method)) {
			
			String status = request.getParameter("status");
			System.out.println("status"+status);
			
			String sizePerPage = "10";
			String currentShowPageNo = request.getParameter("currentShowPageNo");
			
			if(currentShowPageNo == null) {
				currentShowPageNo = "1";
			}

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("sizePerPage",sizePerPage);
			paraMap.put("currentShowPageNo", currentShowPageNo);
			paraMap.put("status", status);

			// 페이징 하기 위해서 페이지 넘버
			int totalPage = odao.StatusTotalPage(paraMap);
			System.out.println("totalPage"+totalPage);
						
			try {
		   		 if(Integer.parseInt(currentShowPageNo) > totalPage || 
		   			Integer.parseInt(currentShowPageNo)	<= 0 ) {
		   			 currentShowPageNo = "1";
		       		 paraMap.put("currentShowPageNo", currentShowPageNo);
		   		 }
		   	} catch(NumberFormatException e) {
			   		 currentShowPageNo = "1";
			   		 paraMap.put("currentShowPageNo", currentShowPageNo);
		   	}
			
			String pageBar = "";
		   	 
		   	int blockSize = 10;
		    // blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
		   	 
		   	int loop = 1;
		    // loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다.
		   	 
		   	// ==== !!! 다음은 pageNo 구하는 공식이다. !!! ==== // 
	        int pageNo  = ( (Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1; 
	    	// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
	        
	        // *** [맨처음][이전] 만들기 *** //
	        pageBar += "<li class='page-item'><a class='page-link' href='orderStatus.gu?&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
	        
		   	if(pageNo != 1) {
		   		pageBar += "<li class='page-item'><a class='page-link' href='orderStatus.gu?&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
			}
			
		    while( !(loop > blockSize || pageNo > totalPage) ) { 
	       	 
	       	if(pageNo == Integer.parseInt(currentShowPageNo)) {
	           	 pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
	       	}
	       	else {
	           	 pageBar += "<li class='page-item'><a class='page-link' href='member.gu?&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
	       	}
	       	 
		       	 loop++; 		 
		       	 pageNo++;	
	        }// end of while()-----------------------------
		    
		    // *** [다음][마지막] 만들기 *** //
	        if(pageNo <= totalPage) {
	       	 	pageBar += "<li class='page-item'><a class='page-link' href='orderStatus.gu?&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
	        }
	        
	        pageBar += "<li class='page-item'><a class='page-link' href='orderStatus.gu?&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";
	        
			try {
				// 상태별 주문목록
				List<OrderVO> statusList = odao.statusList(paraMap);
				System.out.println("statusList.size()"+statusList.size());
				// status
				List<OrderVO> statusCnt = odao.statusCnt();
				
				request.setAttribute("statusList", statusList);
				request.setAttribute("sizePerPage", sizePerPage);
				request.setAttribute("currentShowPageNo", currentShowPageNo);
				request.setAttribute("statusCnt", statusCnt);
				request.setAttribute("pageBar", pageBar);
				
			
				super.setRedirect(false);
		        super.setViewPage("/WEB-INF/admin/adminOrder/adminOrderStatus.jsp");
		        
		        
			} catch(SQLException e) {
				e.printStackTrace();
			}

		} else {
			
		}

	}

}