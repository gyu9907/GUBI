package admin.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import order.domain.OrderVO;
import review.domain.ReviewVO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import admin.model.*;

public class AdminIndex extends AbstractController {
	
	AdminDAO adao = new AdminDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// *** 통계 *** // 
		// 방문자통계
		List<String> visitor = adao.visitorCnt();
		request.setAttribute("visitor", visitor);
		
		// 회원가입통계
		List<String> register = adao.registerCnt();
		request.setAttribute("register", register);
		
		// 구매수통계
		List<String> purchase = adao.purchaseCnt();
		request.setAttribute("purchase", purchase);
		
		// 판매금액통계
		List<String> sales = adao.salesCnt();
		request.setAttribute("sales", sales);
		
		
		// *** 테이블 *** // 
		// ***** 최근주문 ***** ////////////////////////////////////////////////////// 
		String sizePerPage = "10"; // 몇명씩 보여줄건가
		String currentShowPageNo = request.getParameter("currentShowPageNo"); // 현재 페이지
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sizePerPage", sizePerPage);
		
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}

		// 주문 페이지 수 
		int totalPage = adao.orderTotalPage(paraMap);

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
		
		paraMap.put("currentShowPageNo", currentShowPageNo);
		
		String pageBar = "";
		
	   	int blockSize = 5;
	    // blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
	   	 
	   	int loop = 1;
	    // loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다.
	   	 
	   	// ==== !!! 다음은 pageNo 구하는 공식이다. !!! ==== // 
        int pageNo  = ( (Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1;
    	// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
        
     	// *** [맨처음][이전] 만들기 *** //
        pageBar += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
        
	   	if(pageNo != 1) {
	   		pageBar += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}

	    while( !(loop > blockSize || pageNo > totalPage) ) { 
       	 
       	if(pageNo == Integer.parseInt(currentShowPageNo)) {
       		pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
       	}
       	else {
       		pageBar += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
       	}
       	 
       	 loop++; 		 
       	 pageNo++;	
        }// end of while()-----------------------------
	    
	    // *** [다음][마지막] 만들기 *** //
        if(pageNo <= totalPage) {
        	pageBar += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
        }
        
        pageBar += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";

        try {
        	List<OrderVO> orderlist = adao.orderlist(paraMap);

        	request.setAttribute("orderlist", orderlist);
        	request.setAttribute("pageBar", pageBar);
        	request.setAttribute("sizePerPage", sizePerPage);
        	request.setAttribute("currentShowPageNo", currentShowPageNo);
        	
        } catch(SQLException e) {
			e.printStackTrace();
		}
        
        
        // ***** 최근주문 ***** //////////////////////////////////////////////////////  
     	String sizePerpage = "10"; // 몇명씩 보여줄건가
     	String currentShowPageno = request.getParameter("currentShowPageno"); // 현재 페이지 

     	Map<String, String> paraMap2 = new HashMap<>();
     	paraMap2.put("sizePerpage", sizePerpage);
     	
		if(currentShowPageno == null) {
			currentShowPageno = "1";
		}
		System.out.println("currentShowPageno: " + currentShowPageno);

     	//  최근회원가입 페이지 수 
     	int regiPage = adao.registerTotalPage(paraMap2);
     	System.out.println("regiPage"+regiPage);

     	try {
	   		 if(Integer.parseInt(currentShowPageno) > regiPage || 
	   			Integer.parseInt(currentShowPageno)	<= 0 ) {
	   			 currentShowPageno = "1";
	       		 paraMap2.put("currentShowPageno", currentShowPageno);
	   		 }
	   	} catch(NumberFormatException e) {
	   		currentShowPageno = "1";
	   		paraMap2.put("currentShowPageno", currentShowPageno);
	   	}
     	
     	paraMap2.put("currentShowPageno", currentShowPageno);

     	int loop2 = 1;
	    // loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다.
     	int blockSize2 = 5;
     	int pageno  = ( (Integer.parseInt(currentShowPageno) - 1)/blockSize2 ) * blockSize2 + 1;
     	System.out.println("pageno"+ pageno);
     	
     	String pageBar2 = "";
     	// *** [맨처음][이전] 만들기 *** //
        pageBar2 += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerpage="+sizePerpage+"&currentShowPageno=1'><<</a></li>";
        
	   	if(pageno != 1) {
	   		pageBar2 += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerpage="+sizePerpage+"&currentShowPageno="+(pageno-1)+"'>[이전]</a></li>";
		}

	    while( !(loop2 > blockSize2 || pageno > regiPage) ) { 
       	 
       	if(pageno == Integer.parseInt(currentShowPageno)) {
           	 pageBar2 += "<li class='page-item active'><a class='page-link' href='#'>"+pageno+"</a></li>";
       	}
       	else {
             pageBar2 += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerpage="+sizePerpage+"&currentShowPageno="+pageno+"'>"+pageno+"</a></li>";
       	}
       	 
       	 loop2++; 		 
       	 pageno++;	
        }// end of while()-----------------------------
	    
	    // *** [다음][마지막] 만들기 *** //
        if(pageno <= regiPage) {
       	 pageBar2 += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerpage="+sizePerpage+"&currentShowPageno="+pageno+"'>[다음]</a></li>";
        }
        
         pageBar2 += "<li class='page-item'><a class='page-link' href='admin.gu?&sizePerpage="+sizePerpage+"&currentShowPageno="+regiPage+"'>>></a></li>";
        
        try {
        	List<MemberVO> registerlist = adao.registerlist(paraMap2);
        	
        	request.setAttribute("pageBar2", pageBar2);
        	request.setAttribute("registerlist", registerlist);
        	request.setAttribute("currentShowPageno", currentShowPageno);
        	
        } catch(SQLException e) {
			e.printStackTrace();
		}

		// 최근리뷰
		List<ReviewVO> reviewList = adao.reviewList();
		request.setAttribute("reviewList", reviewList);
		

		super.setRedirect(false);
        super.setViewPage("/WEB-INF/admin/adminHome.jsp");
	}

}
