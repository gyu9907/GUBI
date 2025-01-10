package member.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberDelete extends AbstractController {

	MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인하지 않은 경우 처리
        if (!super.checkLogin(request)) {
           
        	super.goBackURL(request);
        	
        	request.setAttribute("message", "로그인 후 이용 가능합니다.");
            request.setAttribute("loc", request.getContextPath() + "/login/login.gu");
           
            super.setViewPage("/WEB-INF/common/msg.jsp");
          
            return;
        }
		
		String method = request.getMethod();

		if (!"POST".equals(method)) { // get 방식

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberDelete.jsp");

		} else { // post 방식
			// 내정보를 수정하기 위한 전제조건은 먼저 로그인을 해야 하는 것이다.
			if (super.checkLogin(request)) { // 로그인을 했으면

				// 필요한 값만 가져옴
				String userid = request.getParameter("userid");
				System.out.println("userid => "+ userid);
				try {
					
					int n = mdao.deleteMember(userid);
					
					if (n == 1) {
						// 회원 탈퇴 성공
						
						// 저장된 세션 모두 비우기
						HttpSession session = request.getSession();
						session.invalidate();
						
						super.setRedirect(false);
						super.setViewPage("/WEB-INF/common/aftermemberDelete.jsp");
						// 회원 탈퇴 후 마지막 말을 전하는 페이지
						return;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					String message = "서버 문제로 인하여 회원탈퇴가 불가합니다. 나중에 다시 시도해주십시오.";
					String loc = request.getContextPath() + "/index.gu"; // 시작페이지로 이동한다.

					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					// 부모창 새로고침 및 팝업창 닫기 위한 용도로 반환해주는 값

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/msg.jsp");
					
				}//end of try catch...
				
				
				
				
				
				
			} else {
				// 로그인을 안 했으면, 보통은 의도적으로 뚫고 들어온 경우
				String message = "먼저 로그인을 하세요!!";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/common/msg.jsp");
			}//end of if else (super.checkLogin(request)) {}...

		}//end of if else (!"POST".equals(method)) {}...
		
		
		
		
		
		
		
		
		

	}//end of exe...

	
}//end of class...
