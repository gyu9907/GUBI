package product.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.check.Check;

public class AdminProductSearchJSON extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		
		String major_category = request.getParameter("major_category");
		String small_category = request.getParameter("small_category");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
//		String is_delete = request.getParameter("is_delete");
		String is_delete = "0";
		String startPrice = request.getParameter("startPrice");
		String endPrice = request.getParameter("endPrice");
		String[] arrColor = request.getParameterValues("color");
		String sortby = request.getParameter("sortby");
		String startCnt = request.getParameter("startCnt");
		String endCnt = request.getParameter("endCnt");

		String colors = "";
		if(arrColor != null) {
			colors = String.join(",", arrColor);
		}
		
		if(searchType == null ||
			!("name".equals(searchType) ||
			"productno".equals(searchType))) {
			searchType = "";
		}
		
		if(searchWord == null) {
			searchWord = "";
		}
		
		if(sizePerPage == null ||
			(!"10".equals(sizePerPage) && 
			!"5".equals(sizePerPage) &&
			!"3".equals(sizePerPage))) {
			sizePerPage = "10";
		}
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		
		if(Check.isNullOrBlank(sortby)) {
			sortby = "latest";
		}
		
		// 테스트용 출력
//		System.out.println("searchType : "+searchType);
//		System.out.println("searchWord : "+searchWord);
//		System.out.println("major_category : "+major_category);
//		System.out.println("small_category : "+small_category);
//		System.out.println("startDate : "+startDate);
//		System.out.println("endDate : "+endDate);
//		System.out.println("is_delete : "+is_delete);
//		System.out.println("startPrice : "+startPrice);
//		System.out.println("endPrice : "+endPrice);
//		System.out.println("currentShowPageNo : "+currentShowPageNo);
//		System.out.println("colors : "+colors);
//		System.out.println("startCnt : "+startCnt);
//		System.out.println("endCnt : "+endCnt);
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("currentShowPageNo", currentShowPageNo);
		paraMap.put("sizePerPage", sizePerPage); // 한페이지당 보여줄 행의 개수
		
		paraMap.put("major_category", major_category);
		paraMap.put("small_category", small_category);
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		paraMap.put("is_delete", is_delete);
		paraMap.put("startPrice", startPrice);
		paraMap.put("endPrice", endPrice);
		paraMap.put("colors", colors);
		paraMap.put("sortby", sortby);
		paraMap.put("startCnt", startCnt);
		paraMap.put("endCnt", endCnt);
		
		
		/*** 페이지 바 만들기 시작 ***/
		int totalProductCnt = pdao.getTotalProductCnt(paraMap);
		int totalPage = (int) Math.ceil((double) totalProductCnt/Integer.parseInt(sizePerPage));
		
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

		pageBar += "<li class='page-item'><a class='page-link' href='javascript:searchProduct(\""+request.getContextPath()+"\", 1)'>&laquo;</a></li>";

        // 첫번째 페이지 바에서는 다음 버튼을 표시하지 않는다.
		if (pageNo > 1) {
			pageBar += "<li class='page-item'><a class='page-link' href='javascript:searchProduct(\""+request.getContextPath()+"\", "+(pageNo-1)+")'>&lt;</a></li>";
		}

        while(!(loop > blockSize || pageNo > totalPage)) {
        	
        	if(pageNo == Integer.parseInt(currentShowPageNo)) {
	        	pageBar += "<li class='page-item active'><a class='page-link' href='javascript:searchProduct(\""+request.getContextPath()+"\", "+pageNo+")'>"+pageNo+"</a></li>";
        	}
        	else {
	        	pageBar += "<li class='page-item'><a class='page-link' href='javascript:searchProduct(\""+request.getContextPath()+"\", "+pageNo+")'>"+pageNo+"</a></li>";
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
			pageBar += "<li class='page-item'><a class='page-link' href='javascript:searchProduct(\""+request.getContextPath()+"\", "+pageNo+")'>&gt;</a></li>";
        }
		pageBar += "<li class='page-item'><a class='page-link' href='javascript:searchProduct(\""+request.getContextPath()+"\", "+totalPage+")'>&raquo;</a></li>";
		/*** 페이지 바 만들기 끝 ***/
		
		try {
			List<ProductVO> productList = pdao.selectProducts(paraMap);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productList", productList);
			jsonObj.put("pageBar", pageBar);
			jsonObj.put("sizePerPage", sizePerPage);
			jsonObj.put("currentShowPageNo", currentShowPageNo);
			jsonObj.put("totalProductCnt", totalProductCnt);
			
			String json = jsonObj.toString();
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/error.gu");
		}

	}

}
