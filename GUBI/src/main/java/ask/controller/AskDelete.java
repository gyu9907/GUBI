package ask.controller;

import org.json.JSONObject;

import ask.model.AskDAO;
import ask.model.AskDAO_imple;
import common.controller.AbstractController;
import delivery.model.DeliveryDAO;
import delivery.model.DeliveryDAO_imple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class AskDelete extends AbstractController {

	 private AskDAO adao = new AskDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json; charset=UTF-8");
		// JSONObject 형식으로 바꾼다. 엄... 이거맞나 
		JSONObject json = new JSONObject();
		System.out.println(request.getMethod());

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			String[] asknoArr = request.getParameterValues("askno");
			
			// asknoArr 배열의 내용 출력
			if (asknoArr != null) {
			    System.out.println("문의 번호 배열:");
			    for (int i = 0; i < asknoArr.length; i++) {  
			        System.out.println(asknoArr[i]);  
			    }
			} else {
			    System.out.println("문의 번호가 없습니다.");
			}
			
			if (asknoArr == null || asknoArr.length == 0) {
				json.put("success", false);
				json.put("message", "삭제할 문의가 선택되지 않았습니다.");
			} else {
				int deletedCount = adao.AskDelete(asknoArr);
				if (deletedCount > 0) {
					json.put("success", true);
					json.put("message", deletedCount + "개의 문의가 성공적으로 삭제되었습니다."); 
				} else {
					json.put("success", false);
					json.put("message", "문의 삭제에 실패했습니다.");
				}
			}
		} else {
			json.put("success", false);
			json.put("message", "잘못된 접근 방식입니다.");
		}

		response.getWriter().print(json.toString());

	}
}
