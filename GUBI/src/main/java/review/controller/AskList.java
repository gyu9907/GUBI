package review.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import review.domain.AskVO;
import review.model.*;

public class AskList extends AbstractController {

	private ReviewDAO rdao = new ReviewDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String ask_category = request.getParameter("category");
		// System.out.println("category"+ask_category); // null 이면 전체조회
		String sizePerPage = "10"; // 몇명씩 보여줄건가
		String currentShowPageNo = request.getParameter("currentShowPageNo"); // 현재 페이지
		

		if(!super.checkAdmin(request) ) { // 관리자가 아닌 경우
			 // 로그인을 안한 경우 또는 일반사용자로 로그인 한 경우
	         String message = "관리자만 접근이 가능합니다.";
	         String loc = "/GUBI/index.gu";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         // super.setRedirect(false);
	         super.setViewPage("/WEB-INF/common/msg.jsp");
	         
	         return;
		} 
		
		if(ask_category == null &&
				   ! "0".equalsIgnoreCase(ask_category) &&
				   ! "1".equalsIgnoreCase(ask_category) &&
				   ! "2".equalsIgnoreCase(ask_category) ) {
					ask_category = "";
				}
				if(currentShowPageNo == null) {
					currentShowPageNo = "1";
				}
				
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("ask_category", ask_category);
				paraMap.put("sizePerPage", sizePerPage);
				paraMap.put("currentShowPageNo",currentShowPageNo);
				
				// 페이징 처리하기 
				int totalPage = rdao.askTotalPage(paraMap);
				
				
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
		       pageBar += "<li class='page-item'><a class='page-link' href='ask.gu?category="+ask_category+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
		       
			   if(pageNo != 1) {
			   		pageBar += "<li class='page-item'><a class='page-link' href='ask.gu?category="+ask_category+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
			   }
				
			   	
			   while( !(loop > blockSize || pageNo > totalPage) ) { 
		      	 
		       if(pageNo == Integer.parseInt(currentShowPageNo)) {
		          	 pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
		          	 
		      	}
		      	else {
		          	 pageBar += "<li class='page-item'><a class='page-link' href='ask.gu?category="+ask_category+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
		      	}
		      	 
		      	 loop++; 		 
		      	 pageNo++;	
		       }// end of while()-----------------------------
			    
			    // *** [다음][마지막] 만들기 *** //
		       if(pageNo <= totalPage) {
		      	 pageBar += "<li class='page-item'><a class='page-link' href='ask.gu?category="+ask_category+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
		       }
		       
		       pageBar += "<li class='page-item'><a class='page-link' href='ask.gu?category="+ask_category+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";
			
					
				try {
					// 페이징처리한 문의목록 구하기 
					List<AskVO> askList = rdao.askList(paraMap);
					// 문의개수
					int askCnt = rdao.askCnt(ask_category);
					
					request.setAttribute("askList", askList);
					request.setAttribute("askCnt", askCnt);
					request.setAttribute("sizePerPage", sizePerPage);
					request.setAttribute("pageBar", pageBar);
					request.setAttribute("currentShowPageNo", currentShowPageNo);
					request.setAttribute("sizePerPage", sizePerPage);
					request.setAttribute("ask_category", ask_category);
					
					
				} catch(SQLException e) {
		        	e.printStackTrace();
		        }
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/admin/adminReview/askList.jsp");	
		
		
	}

}
