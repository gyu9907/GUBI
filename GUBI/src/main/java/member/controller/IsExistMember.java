package member.controller;

import java.sql.SQLException;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class IsExistMember extends AbstractController {

	MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if (!"POST".equals(method)) { // get 방식

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/index.jsp");

		} else { // post 방식

			// 본인인증 받기 위한 전제조건은 먼저 로그인을 해야 하는 것이다.
			if (super.checkLogin(request)) { // 로그인을 했으면

				String userid = request.getParameter("userid");
				String passwd = request.getParameter("passwd");

				// 담아
				MemberVO member = new MemberVO();
				member.setUserid(userid); 
				member.setPasswd(passwd);
				
				try {
					// 회원 본인인증
					boolean isExists = mdao.memberIsExist(member); // 존재하면 true

					JSONObject jsonObj = new JSONObject(); // {}

					jsonObj.put("isExists", isExists);

					// 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}"
					String json = jsonObj.toString(); // 스트링 타입으로 바꿔준다!

					request.setAttribute("json", json);

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/jsonview.jsp"); // 포장해서 jsp 파일로 보냄

				} catch (SQLException e) {
					e.printStackTrace();
					String message = "서버 문제로 인하여 본인인증이 불가합니다. 나중에 다시 시도해주십시오.";
					String loc = request.getContextPath() + "/index.gu"; // 시작페이지로 이동한다.

					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					// 부모창 새로고침 및 팝업창 닫기 위한 용도로 반환해주는 값

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/msg.jsp");

				} // end of try catch...

			} else {
				// 로그인을 안 했으면, 보통은 의도적으로 뚫고 들어온 경우
				String message = "먼저 로그인을 하세요!!";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
			}

		} // end of if (!"POST".equals(method)) {}...

	}// end of exe...

}// end of class...
