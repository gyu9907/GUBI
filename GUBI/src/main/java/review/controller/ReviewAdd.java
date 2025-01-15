package review.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import member.domain.MemberVO;
import review.model.ReviewDAO;
import review.model.ReviewDAO_imple;

public class ReviewAdd extends AbstractController {
	
	private ReviewDAO rdao = new ReviewDAO_imple();
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

		HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
        String userid = loginuser.getUserid(); // 사용자ID

		
		try {
		
			String method = request.getMethod();
			if("POST".equalsIgnoreCase(method)) {
				ServletContext svlCtx = session.getServletContext();
	            String uploadFileDir = svlCtx.getRealPath("/data/images");

	            System.out.println("svlCtx :: " + svlCtx);
	            System.out.println("uploadFileDir :: " + uploadFileDir);
				
	            String img = null;
	             
	            Part part = request.getPart("img");

	            if(part.getHeader("Content-Disposition").contains("filename=")) {
	            	String fileName = extractFileName(part.getHeader("Content-Disposition"));
	            	
	            	if(part.getSize() > 0) {
	            		
	            		String newFilename = fileName.substring(0, fileName.lastIndexOf(".")); // 확장자를 뺀 파일명 알아오기
	                        
	                    newFilename += "_"+String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance()); 
	                    newFilename += System.nanoTime();
	                    newFilename += fileName.substring(fileName.lastIndexOf(".")); // 확장자 붙이기
	                        
	                    part.write(uploadFileDir + File.separator + newFilename);
	                    
	                    part.delete();
	                    
	                    System.out.println("part.getName:: " + part.getName());
	                    System.out.println("newFilename:: " + newFilename);

	                    if("img".equals(part.getName())) {
	                    	img = newFilename;
	                    }
	            	}
	            } else { // form 태그에서 전송되어온 것이 파일이 아닐 경우
//	            	String formValue = request.getParameter(part.getName());
	            }
	            String optionno = request.getParameter("optionno");
	    		String title = request.getParameter("title");
	    		String content = request.getParameter("content");
	    		content = content.replaceAll("<", "&lt;");
	    		content = content.replaceAll(">", "&gt;");
	    		content = content.replaceAll("\r\n", "<br>");
	    		String score = request.getParameter("score");
		    	
		    		
	    		Map<String,String> paraMap = new HashMap<>();
	    		
	    		paraMap.put("userid", userid);
	    		paraMap.put("optionno", optionno);
	    		paraMap.put("title", title);
	    		paraMap.put("content", content);
	    		paraMap.put("score", score);
	    		paraMap.put("img", img); // 생성한 새로운 파일명 세팅
	
				int n = rdao.addReview(paraMap);
						
				if(n==1) {
					JSONObject jsonObj = new JSONObject();  // {}
					jsonObj.put("result", n);
					
					String json = jsonObj.toString(); // 문자열로 변환 
					request.setAttribute("json", json);
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/jsonview.jsp");
				}
			}
				
		} catch(SQLException e) {
			request.setAttribute("message", "리뷰 작성 실패!!");
            request.setAttribute("loc", "javascript:history.back()");
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/common/msg.jsp");
		}
		
	}
}


