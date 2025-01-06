package collection.controller;

import java.sql.SQLException;

import org.json.JSONObject;

import collection.model.CollectionDAO;
import collection.model.CollectionDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminCollectionDelete extends AbstractController {

	private CollectionDAO coldao = new CollectionDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" or "POST"
		
		if(!"POST".equalsIgnoreCase(method)) { // GET 방식
			// TODO 오류메시지 보내기
		}
		else { // POST 방식
			String[] collectionno_arr = request.getParameterValues("collectionno");
			
			if(collectionno_arr == null) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("isDeleted", false);
				
				String json = jsonObj.toString();
				
				request.setAttribute("json", json);
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/jsonview.jsp");
				return;
			}
			
			try {
				int deleteCnt = 0;
				
				for(int i=0; i<collectionno_arr.length; i++) {
					int n = coldao.updateIsDeleteCollection(collectionno_arr[i]);
					
					if(n == 1) {
						deleteCnt++;
					}
				}

				JSONObject jsonObj = new JSONObject();
				
				if(collectionno_arr.length == deleteCnt) { // 모두 삭제 성공한 경우
					jsonObj.put("isDeleted", true);
				}
				else {
					jsonObj.put("isDeleted", false);
				}
				
				String json = jsonObj.toString();
				
				request.setAttribute("json", json);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/jsonview.jsp");
				
			} catch (SQLException e) {
				e.printStackTrace();
				
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("isDeleted", false);
				
				String json = jsonObj.toString();
				
				request.setAttribute("json", json);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/jsonview.jsp");
			}
		}

	}

}
