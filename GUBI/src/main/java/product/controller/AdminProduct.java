package product.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.*;

public class AdminProduct extends AbstractController {
	
	private ProductDAO pdao = new ProductDAO_imple();

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
		} 
		else {
			if("GET".equalsIgnoreCase(method)) {
				
				String searchType = request.getParameter("searchType");
				String searchWord = request.getParameter("searchWord");
				
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				
				String is_delete = request.getParameter("is_delete");
				
				String startprice = request.getParameter("startprice");
				String endprice = request.getParameter("endprice");
				String small_category = request.getParameter("small_category");
				
				// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- // 
				List<CategoryVO> selectMajorCategory = pdao.selectMajorCategory();
				request.setAttribute("selectMajorCategory", selectMajorCategory);
				String major_category = request.getParameter("major_category");
				// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //

				String sizePerPage = "15"; // 몇개씩 보여줄건가
				String currentShowPageNo = request.getParameter("currentShowPageNo"); // 현재 페이지
				

				if(searchType == null &&
				   !"name".equals("searchType") &&
				   !"productno".equals("searchType") ) {
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
				if(is_delete == null) {
					is_delete = "";
				}
				if(startprice == null) {
					startprice = "";
				}
				if(endprice == null) {
					endprice = "";
				}
				if(major_category == null ||
					!"LIGHTING".equals(major_category) &&
					!"SEATING".equals(major_category) && 
					!"TABLES".equals(major_category) ) {
					major_category = "";
				}
				if(small_category == null) {
					small_category = "";
				}
				if(currentShowPageNo == null) {
					currentShowPageNo = "1";
				}
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("searchType", searchType);
				paraMap.put("searchWord", searchWord);
				paraMap.put("startDate", startDate);
				paraMap.put("endDate", endDate);
				paraMap.put("is_delete", is_delete);
				paraMap.put("startprice",startprice);
				paraMap.put("endprice", endprice);
				paraMap.put("major_category", major_category);
				paraMap.put("small_category", small_category);
				
				paraMap.put("sizePerPage", sizePerPage);
				paraMap.put("currentShowPageNo", currentShowPageNo);
				
				int totalPage = pdao.totalPage(paraMap);
				// System.out.println("totalPage 확인 "+totalPage);
				
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
		        pageBar += "<li class='page-item'><a class='page-link' href='product.gu?searchType="+searchType+"&searchWord="+searchWord+"&major_category="+major_category+"&small_category="+small_category+"&startDate="+startDate+"&endDate="+endDate+"&is_delete="+is_delete+"&startprice="+startprice+"&endprice="+endprice+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'><<</a></li>";
		        
			   	if(pageNo != 1) {
			   		pageBar += "<li class='page-item'><a class='page-link' href='product.gu?searchType="+searchType+"&searchWord="+searchWord+"&major_category="+major_category+"&small_category="+small_category+"&startDate="+startDate+"&endDate="+endDate+"&is_delete="+is_delete+"&startprice="+startprice+"&endprice="+endprice+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
				}
				
			   	
			    while( !(loop > blockSize || pageNo > totalPage) ) { 
		       	 
		       	if(pageNo == Integer.parseInt(currentShowPageNo)) {
		           	 pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
		           	 
		       	}
		       	else {
		           	 pageBar += "<li class='page-item'><a class='page-link' href='product.gu?searchType="+searchType+"&searchWord="+searchWord+"&major_category="+major_category+"&small_category="+small_category+"&startDate="+startDate+"&endDate="+endDate+"&is_delete="+is_delete+"&startprice="+startprice+"&endprice="+endprice+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
		       	}
		       	 
		       	 loop++; 		 
		       	 pageNo++;	
		        }// end of while()-----------------------------
			    
			    // *** [다음][마지막] 만들기 *** //
		        if(pageNo <= totalPage) {
		       	 pageBar += "<li class='page-item'><a class='page-link' href='product.gu?searchType="+searchType+"&searchWord="+searchWord+"&major_category="+major_category+"&small_category="+small_category+"&startDate="+startDate+"&endDate="+endDate+"&is_delete="+is_delete+"&startprice="+startprice+"&endprice="+endprice+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
		        }
		        
		        pageBar += "<li class='page-item'><a class='page-link' href='product.gu?searchType="+searchType+"&searchWord="+searchWord+"&major_category="+major_category+"&small_category="+small_category+"&startDate="+startDate+"&endDate="+endDate+"&is_delete="+is_delete+"&startprice="+startprice+"&endprice="+endprice+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>>></a></li>";
			
				try {
					List<ProductVO> selectProduct = pdao.selectProduct(paraMap);
					int productCnt = pdao.productCnt(paraMap);
					System.out.println(productCnt);
					request.setAttribute("selectProduct", selectProduct);
					request.setAttribute("productCnt", productCnt);
					request.setAttribute("searchType", searchType);
					request.setAttribute("searchWord", searchWord);
					request.setAttribute("startDate", startDate);
					request.setAttribute("endDate", endDate);
					request.setAttribute("is_delete", is_delete);
					request.setAttribute("startprice", startprice);
					request.setAttribute("endprice", endprice);
					request.setAttribute("major_category", major_category);
					request.setAttribute("small_category", small_category);
					request.setAttribute("pageBar", pageBar);
					request.setAttribute("sizePerPage",sizePerPage);
					request.setAttribute("currentShowPageNo", currentShowPageNo);
					
					super.setRedirect(false);
			        super.setViewPage("/WEB-INF/admin/adminProduct/adminProduct.jsp");
			        
				} catch(SQLException e) {
					e.printStackTrace();
				}
				
			} else { // post
				String productno[] = request.getParameterValues("productno");
				String is_delete = request.getParameter("is_delete");

				int n = 0;
				
				if(productno.length > 0) {
					if(is_delete.equals("0")) {
						for(String deleteproductno : productno) {
							n = pdao.deleteProduct(deleteproductno);
							System.out.println("n" + n);
						}
					}
					else {
						for(String recoverproductno : productno) {
								
							n = pdao.recoverProduct(recoverproductno) + 1;
						}
					}
				} else { // 삭제할 회원 없음
					
				}
				
				if(n==1) {
					String message = "상품이 [삭제] 되었습니다";
					String loc = request.getContextPath()+"/admin/product.gu";
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
					super.setRedirect(false); 
					super.setViewPage("/WEB-INF/common/msg.jsp");
					
					return;
				}
				if(n==2) {
					String message = "상품이 [재등록] 되었습니다";
					String loc = request.getContextPath()+"/admin/product.gu";
					
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
