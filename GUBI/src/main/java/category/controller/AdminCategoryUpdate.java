package category.controller;

import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import category.domain.CategoryVO;
import category.model.*;
import java.io.*;

public class AdminCategoryUpdate extends AbstractController {

	private CategoryDAO cdao = new CategoryDAO_imple();
	
	private String extractFileName(String partHeader) {
	      for(String cd : partHeader.split("\\;")) {
	         if(cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", ""); 
	            int index = fileName.lastIndexOf(File.separator); // File.separator 란? OS가 Windows 이라면 \ 이고, OS가 Mac, Linux, Unix 이라면 / 을 말하는 것이다.       
	            return fileName.substring(index + 1);
	         }
	      }
	      return null;
	}// end of private String extractFileName(String partHeader)-------------------
	
	
	
	// 카테고리 수정하기 
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		HttpSession session = request.getSession();
		
		if("GET".equals(method)) {
			
			List<CategoryVO> updateCategorySelect = cdao.updateCategorySelect();
			request.setAttribute("updateCategorySelect", updateCategorySelect);
			
			List<CategoryVO> majorCategory = cdao.majorCategory();
			request.setAttribute("majorCategory", majorCategory);
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/adminCategory/categoryUpdate.jsp");
		}
		else { // 수정하기 클릭한 경우
			
			ServletContext svlCtx = session.getServletContext();
            String uploadFileDir = svlCtx.getRealPath("/data/images");
			
			String categoryno = request.getParameter("categoryno");
			String majorCategory = request.getParameter("majorCategory");
			String smallCategory = request.getParameter("smallCategory");
			String img = ""; // 파일명 
//			String filename = request.getParameter("categoryimg");

			Part categoryImgPart = request.getPart("categoryimg"); // 파일(새로운 이미지)일까 아닐까(기존이미지명) 
			
			if(categoryImgPart.getHeader("Content-Disposition").contains("filename=")) { // part 가 파일인가 ? 
				
			    String fileName = extractFileName(categoryImgPart.getHeader("Content-Disposition")); // 이름만들기 메소드 
				
				if(categoryImgPart.getSize() > 0) { // 파일이 존재하는가 
					
					 String newFilename = fileName.substring(0, fileName.lastIndexOf(".")); // 확장자를 뺀 파일명 알아오기 
	    			 newFilename += "_"+String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
	    			 newFilename += System.nanoTime();
	    			 
	    			 newFilename += fileName.substring(fileName.lastIndexOf(".")); // 확장자 붙이기
	    			 
	    			 img = newFilename; // 파일명 저장하기 
	    			 
	    			 // >>> 파일을 지정된 디스크 경로에 저장해준다. 이것이 바로 파일을 업로드 해주는 작업이다. <<<
	    			 categoryImgPart.write(uploadFileDir + File.separator + newFilename);
	    			 
	    			 // 임시데이터 제거하기 
	    			 categoryImgPart.delete();
	    			
				}
			}
			else {
				img =  request.getParameter(categoryImgPart.getName());
					// request.getParameter("categoryimg");
			}
		
			Map<String, String> paraMap = new HashMap<>();
	        paraMap.put("categoryno", categoryno);
	        paraMap.put("majorCategory", majorCategory);
	        paraMap.put("smallCategory", smallCategory);
	        paraMap.put("img", img);
	        
	        // 수정 sql 
			int n = cdao.updateCategory(paraMap);
			
			JSONObject jsonObj = new JSONObject();  // {}
            jsonObj.put("result", n);
          
            String json = jsonObj.toString(); // 문자열로 변환 
            request.setAttribute("json", json);
          
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/common/jsonview.jsp");
		}
	

	}

}
