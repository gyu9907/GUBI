package review.controller;

import java.io.File;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import review.model.ReviewDAO;
import review.model.ReviewDAO_imple;

public class ReviewDelete extends AbstractController {
	
	private ReviewDAO rdao = new ReviewDAO_imple();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		HttpSession session = request.getSession();
		
		if("POST".equalsIgnoreCase(method)) { 
	        
			String reviewno = request.getParameter("reviewno");
		   String filename = request.getParameter("img");
		   ServletContext svlCtx = session.getServletContext();
		   String uploadFileDir = svlCtx.getRealPath("/data/images");
		   File file = new File(uploadFileDir + File.separator + filename);

	        
			
			int n = rdao.reviewDelete(reviewno); 
			 
			if(n == 1) {
				// 파일이 존재하는지 확인하고 삭제 시도
				if (file.exists()) {
					boolean isDeleted = file.delete();
					if (isDeleted) {
						System.out.println("파일이 성공적으로 삭제되었습니다.");
					} else {
						System.out.println("파일 삭제에 실패했습니다.");
					}
				} else {
					System.out.println("파일이 존재하지 않습니다.");
				}
			}
			
			JSONObject jsobj = new JSONObject(); // {}
			jsobj.put("n", n); // {"n":1}
			
			String json = jsobj.toString(); // "{"n":1}"
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/jsonview.jsp");
			
		}


	}

}
