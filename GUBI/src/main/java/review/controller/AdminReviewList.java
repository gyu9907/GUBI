package review.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import review.domain.ReviewVO;
import review.model.*;

public class AdminReviewList extends AbstractController {

	ReviewDAO rdao = new ReviewDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
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
		else {
			if("GET".equalsIgnoreCase(method)) {
				
				String searchType = request.getParameter("searchType");
				String searchWord = request.getParameter("searchWord");
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				
				String sizePerPage = "10"; // 몇명씩 보여줄건가
				String currentShowPageNo = request.getParameter("currentShowPageNo"); // 현재 페이지
				
				if(searchType == null &&
					!"name".equalsIgnoreCase(searchType) &&
					!"userid".equalsIgnoreCase(searchType)) {
					searchType = "";
				}
				if(searchWord == null) {
					searchWord = "";
				}
				if(startDate == null) {
					startDate = "";
		    	}
				if(endDate == null) {
					endDate = "";
		    	}
				if(currentShowPageNo == null) {
					currentShowPageNo = "1";
				}
					
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("searchType",searchType);
				paraMap.put("searchWord", searchWord);
				paraMap.put("startDate",startDate);
				paraMap.put("endDate",endDate);
				paraMap.put("sizePerPage", sizePerPage);
				paraMap.put("currentShowPageNo", currentShowPageNo);
				
				
				// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지수 알아오기 //
				int totalPage = rdao.getTotalPage(paraMap);
				
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
		        pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchType="+searchType+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
		        
			   	if(pageNo != 1) {
			   		pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchType="+searchType+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
				}
				
			   	
			    while( !(loop > blockSize || pageNo > totalPage) ) { 
		       	 
		       	if(pageNo == Integer.parseInt(currentShowPageNo)) {
		           	 pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
		           	 
		       	}
		       	else {
		           	 pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchType="+searchType+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
		       	}
		       	 
		       	 loop++; 		 
		       	 pageNo++;	
		        }// end of while()-----------------------------
			    
			    // *** [다음][마지막] 만들기 *** //
		        if(pageNo <= totalPage) {
		       	 pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchType="+searchType+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
		        }
		        
		        pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchType="+searchType+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";
			
		        
		        try {
		        	// 리뷰목록
		        	List<ReviewVO> reviewList = rdao.reviewList(paraMap);
		        	// 조건검색한 리뷰수
		        	int reviewCnt = rdao.reviewCnt(paraMap);
		        	
		        	request.setAttribute("reviewList", reviewList);
		        	request.setAttribute("reviewCnt", reviewCnt);
		        	request.setAttribute("pageBar", pageBar);
		
		        	request.setAttribute("sizePerPage", sizePerPage);
		        	request.setAttribute("currentShowPageNo", currentShowPageNo);
		        	
		        	request.setAttribute("searchType", searchType);
		        	request.setAttribute("searchWord", searchWord);
		        	request.setAttribute("startDate", startDate);
		        	request.setAttribute("endDate", endDate);
		        	
		        	super.setRedirect(false);
					super.setViewPage("/WEB-INF/admin/adminReview/reviewList.jsp");	
		        	
		        } catch(SQLException e) {
		        	e.printStackTrace();
		        }

			} 
		}
	}

}
