package category.controller;

import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import category.domain.CategoryVO;
import category.model.*;

public class AdminCategoryAdd extends AbstractController {

	CategoryDAO cdao = new CategoryDAO_imple();
	
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

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();
		HttpSession session = request.getSession();

		if ("GET".equalsIgnoreCase(method)) {
			List<CategoryVO> categoryList = cdao.CategorySelectAll();
			request.setAttribute("categoryList", categoryList);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/adminCategory/addCategory.jsp");

		} else {	// post\
			
			try {
				// 카테고리삭제하기 
				ServletContext svlCtx = session.getServletContext();
	            String uploadFileDir = svlCtx.getRealPath("/data/images");
	            
	            String major_category = request.getParameter("major_category");
				String small_category = request.getParameter("small_category");
				// System.out.println("확인이요" + major_category + small_category);

				// 카테고리등록
				String img = "";
				
				Part category_img = request.getPart("categoryimg");
				
				if(category_img.getHeader("Content-Disposition").contains("filename=")) {
					
					String fileName = extractFileName(category_img.getHeader("Content-Disposition")); // 이름만들기 메소드 
					
					if(category_img.getSize() > 0) {
						 String newFilename = fileName.substring(0, fileName.lastIndexOf(".")); // 확장자를 뺀 파일명 알아오기 
		    			 newFilename += "_"+String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
		    			 newFilename += System.nanoTime();
		    			 
		    			 newFilename += fileName.substring(fileName.lastIndexOf(".")); // 확장자 붙이기
		    			 
		    			 img = newFilename;
		    			 
		    			 // >>> 파일을 지정된 디스크 경로에 저장해준다. 이것이 바로 파일을 업로드 해주는 작업이다. <<<
		    			 category_img.write(uploadFileDir + File.separator + newFilename);
		    			 
		    			 // 임시데이터 제거하기 
		    			 category_img.delete();
					}
				} else {
					img =  request.getParameter(category_img.getName());
				}
				
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("major_category", major_category);
				paraMap.put("small_category", small_category);
				paraMap.put("img", img);

				int n = cdao.addCategory(paraMap); // 카테고리등록

				/*
				 * if(n==1) { // 새로고침하며 행이 추가되야함 String message = "새로운 카테고리 등록이 성공되었습니다!";
				 * String loc = request.getContextPath()+"/admin/category.gu";
				 * 
				 * request.setAttribute("message", message); request.setAttribute("loc", loc);
				 * 
				 * super.setRedirect(false); super.setViewPage("/WEB-INF/common/msg.jsp");
				 * 
				 * return; }
				 */
				JSONObject jsonObj = new JSONObject();  // {}
	            jsonObj.put("result", n);
	          
	            String json = jsonObj.toString(); // 문자열로 변환 
	            request.setAttribute("json", json);
	          
	            super.setRedirect(false);
	            super.setViewPage("/WEB-INF/common/jsonview.jsp");

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
