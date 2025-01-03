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

public class AdminOrder extends AbstractController {

	OrderDAO odao = new OrderDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if("GET".equalsIgnoreCase(method)) {
			
			String searchType = request.getParameter("searchType");
			String searchWord = request.getParameter("searchWord");
			
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			
			String orderStatus = request.getParameter("orderStatus");
			String sizePerPage = "10";
			String currentShowPageNo = request.getParameter("currentShowPageNo");
				
			if(searchType == null &&
				!"orderno".equals(searchType) &&
				!"userid".equals(searchType) &&
				!"name".equals(searchType) &&
				!"tel".equals(searchType) ) {
				searchType = "orderno";
			}
			if(searchWord == null) {
				searchWord = "";
			}
			if(startDate == null || startDate.isBlank()) {
				startDate = "";
	    	}
			if(endDate == null) {
				endDate = "";
	    	}
			if(orderStatus == null) {
				orderStatus = "";
			}
			if(currentShowPageNo == null) {
				currentShowPageNo = "1";
			}
			
			// System.out.println(searchType+ ", "+ currentShowPageNo + ","+ searchWord+ ", "+startDate+ ", "+endDate+ ", "+orderStatus);

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);
			paraMap.put("startDate", startDate);
			paraMap.put("endDate", endDate);
			paraMap.put("orderStatus", orderStatus);
			paraMap.put("currentShowPageNo", currentShowPageNo);
			paraMap.put("sizePerPage", sizePerPage);
			
			// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지수 알아오기 //
			int totalPage = odao.getTotalPage(paraMap);
			
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
	        
	        // *** [맨처음][이전] 만들기 *** //
	        pageBar += "<li class='page-item'><a class='page-link' href='order.gu?searchType="+searchType+"&searchWord="+searchWord+"&startDate="+startDate+"&endDate="+endDate+"&orderStatus="+orderStatus+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
	        
		   	if(pageNo != 1) {
		   		pageBar += "<li class='page-item'><a class='page-link' href='order.gu?searchType="+searchType+"&searchWord="+searchWord+"&startDate="+startDate+"&endDate="+endDate+"&orderStatus="+orderStatus+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
			}
			
		   	
		    while( !(loop > blockSize || pageNo > totalPage) ) { 
	       	 
	       	if(pageNo == Integer.parseInt(currentShowPageNo)) {
	           	 pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
	           	 
	       	}
	       	else {
	           	 pageBar += "<li class='page-item'><a class='page-link' href='order.gu?searchType="+searchType+"&searchWord="+searchWord+"&startDate="+startDate+"&endDate="+endDate+"&orderStatus="+orderStatus+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
	       	}
	       	 
	       	 loop++; 		 
	       	 pageNo++;	
	        }// end of while()-----------------------------
		    
		    // *** [다음][마지막] 만들기 *** //
	        if(pageNo <= totalPage) {
	       	 pageBar += "<li class='page-item'><a class='page-link' href='order.gu?searchType="+searchType+"&searchWord="+searchWord+"&startDate="+startDate+"&endDate="+endDate+"&orderStatus="+orderStatus+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
	        }
	        
	        pageBar += "<li class='page-item'><a class='page-link' href='order.gu?searchType="+searchType+"&searchWord="+searchWord+"&startDate="+startDate+"&endDate="+endDate+"&orderStatus="+orderStatus+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";
	        
			
			try {
				// 주문목록 조회하기
				List<OrderVO> orderList =  odao.selectOrderList(paraMap);
				int orderCnt = odao.orderCnt(paraMap);
				List<OrderVO> statusCnt = odao.statusCnt();

				request.setAttribute("orderList", orderList);
				request.setAttribute("orderCnt", orderCnt);
				request.setAttribute("statusCnt", statusCnt);
				request.setAttribute("searchType", searchType);
				request.setAttribute("searchWord", searchWord);
				request.setAttribute("startDate", startDate);
				request.setAttribute("endDate", endDate);
				request.setAttribute("orderStatus", orderStatus);
				request.setAttribute("currentShowPageNo", currentShowPageNo);
				request.setAttribute("sizePerPage", sizePerPage);
				request.setAttribute("pageBar", pageBar);

				super.setRedirect(false);
		        super.setViewPage("/WEB-INF/admin/adminOrder/adminOrder.jsp");
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			// post 일때 
		}
	
        
	}

}
