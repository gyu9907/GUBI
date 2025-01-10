package member.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import member.model.*;

public class AdminMember extends AbstractController {
	
	MemberDAO mdao = new MemberDAO_imple();

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
				
				String searchSelect = request.getParameter("searchSelect");
				String searchWord = request.getParameter("searchWord");
				String dateSelect = request.getParameter("dateSelect");
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				String status = request.getParameter("status");


				String sizePerPage = "10"; // 몇명씩 보여줄건가
				String currentShowPageNo = request.getParameter("currentShowPageNo"); // 현재 페이지
				
				if(searchSelect == null &&
		    	  (!"userid".equals(searchSelect) &&
		    	   !"name".equals(searchSelect) &&
		    	   !"tel".equals(searchSelect) ) ) {
					 	searchSelect = "userid";
		    	}	
				if(searchWord == null) {
					searchWord = "";
			 	}
				if(dateSelect == null &&
		    	  (!"registerday".equals(searchSelect) &&
		    	   !"loginday".equals(searchSelect) ) ) {
		    		  dateSelect = "registerday";
				}	
				if(startDate == null) {
					startDate = "";
		    	}
				if(endDate == null) {
					endDate = "";
		    	}
				if(status == null) {
					status = "0";
		    	}
				if(currentShowPageNo == null) {
					currentShowPageNo = "1";
				}
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("searchSelect", searchSelect);
				paraMap.put("searchWord", searchWord);
				paraMap.put("dateSelect", dateSelect);
				paraMap.put("startDate", startDate);
				paraMap.put("endDate", endDate);
				paraMap.put("status",status);
				
				paraMap.put("sizePerPage", sizePerPage);
				paraMap.put("currentShowPageNo", currentShowPageNo);
				
				
				// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지수 알아오기 //
				int totalPage = mdao.getTotalPage(paraMap);
					
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
		        pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchSelect="+searchSelect+"&searchWord="+searchWord+"&dateSelect="+dateSelect+"&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
		        
			   	if(pageNo != 1) {
			   		pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchSelect="+searchSelect+"&searchWord="+searchWord+"&dateSelect="+dateSelect+"&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
				}
				
			   	
			    while( !(loop > blockSize || pageNo > totalPage) ) { 
		       	 
		       	if(pageNo == Integer.parseInt(currentShowPageNo)) {
		           	 pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
		           	 
		       	}
		       	else {
		           	 pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchSelect="+searchSelect+"&searchWord="+searchWord+"&dateSelect="+dateSelect+"&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
		       	}
		       	 
		       	 loop++; 		 
		       	 pageNo++;	
		        }// end of while()-----------------------------
			    
			    // *** [다음][마지막] 만들기 *** //
		        if(pageNo <= totalPage) {
		       	 pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchSelect="+searchSelect+"&searchWord="+searchWord+"&dateSelect="+dateSelect+"&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
		        }
		        
		        pageBar += "<li class='page-item'><a class='page-link' href='member.gu?searchSelect="+searchSelect+"&searchWord="+searchWord+"&dateSelect="+dateSelect+"&status="+status+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";
			
		       // String currentURL = MyUtil.getCurrentURL(request);
		        
				try {
					// 페이징 처리한 조건검색 멤버목록
					List<MemberVO> optionMemberList = mdao.optionMemberList(paraMap);
					int optionMembercnt = mdao.optionMembercnt(paraMap);

					request.setAttribute("memberList",optionMemberList);
					request.setAttribute("memberCnt", optionMembercnt);
					
					request.setAttribute("searchSelect", searchSelect);
					request.setAttribute("searchWord", searchWord);
					request.setAttribute("dateSelect", dateSelect);
					request.setAttribute("startDate", startDate);
					request.setAttribute("endDate", endDate);
					request.setAttribute("status", status);
					request.setAttribute("pageBar", pageBar);
					request.setAttribute("currentShowPageNo", currentShowPageNo);
					request.setAttribute("sizePerPage", sizePerPage);
					
					super.setRedirect(false);
		            super.setViewPage("/WEB-INF/admin/adminMember/adminMember.jsp");
		             
				} catch(SQLException e) {
					e.printStackTrace();
				}
				
			}
			else { // status 값을 1로 수정하기 (회원탈퇴)
				String[] userid = request.getParameterValues("userid");
				System.out.println(userid);
				
				String status = request.getParameter("status");
				System.out.println(status);
				int n = 0;
				
				if(userid != null) {
					if(status.equals("0")) {
						for(String deleteuserid : userid) {
							
							n = mdao.deleteMember(deleteuserid); // 회원탈퇴
						}
					}
					else {
						for(String recoveruserid : userid) {
							
							n = mdao.recoverMember(recoveruserid) + 1; // 회원복구
						}
					}
					
				}
				else {
					// 삭제할 회원 없음 
				}

				if(n==1) {
					
					String message = "회원 상태 [탈퇴] 로 변경되었습니다";
					String loc = request.getContextPath()+"/admin/member.gu";
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false); 
					super.setViewPage("/WEB-INF/common/msg.jsp");
					
					return;
				}
				if(n==2) {
					String message = "회원 상태 [일반] 으로 변경되었습니다";
					String loc = request.getContextPath()+"/admin/member.gu";
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false); 
					super.setViewPage("/WEB-INF/common/msg.jsp");
					
					return;
				}
		
			}
			
		}

	}

}
