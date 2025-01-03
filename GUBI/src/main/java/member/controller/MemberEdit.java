package member.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberEdit extends AbstractController {

	MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();

		if (!"POST".equals(method)) { // get 방식

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberEdit.jsp");

		} else { // post 방식
			
			// 내정보를 수정하기 위한 전제조건은 먼저 로그인을 해야 하는 것이다.
			if (super.checkLogin(request)) { // 로그인을 했으면

				String userid = request.getParameter("userid");
				String passwd = request.getParameter("passwd");
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String hp1 = request.getParameter("hp1");
				String hp2 = request.getParameter("hp2");
				String hp3 = request.getParameter("hp3");
				String postcode = request.getParameter("postcode");
				String address = request.getParameter("address");
				String detail_address = request.getParameter("detail_address");
				
				String tel = hp1+hp2+hp3;
				
				// 담아
				MemberVO member = new MemberVO();
				member.setUserid(userid); // 키값
				
				member.setPasswd(passwd);
				member.setName(name);
				member.setEmail(email);
				member.setTel(tel);
				member.setPostcode(postcode);
				member.setAddress(address);
				member.setDetail_address(detail_address);
				
				try {
					// 회원정보 수정
					int n = mdao.memberEdit(member);
					
					if (n == 1) {
						// !!!! session 에 저장된 loginuser 를 변경된 사용자의 정보값으로 변경해주어야 한다. !!!!
						HttpSession session = request.getSession();
						MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
						
						loginuser.setPasswd(passwd);
						loginuser.setName(name);
						loginuser.setEmail(email);
						loginuser.setTel(tel);
						loginuser.setPostcode(postcode);
						loginuser.setAddress(address);
						loginuser.setDetail_address(detail_address);
						
						String message = "회원정보 수정 성공!!";
						String loc = request.getContextPath() + "/index.gu"; // 시작페이지로 이동한다.

						request.setAttribute("message", message);
						request.setAttribute("loc", loc);
						// 부모창 새로고침 및 팝업창 닫기 위한 용도로 반환해주는 값

						super.setRedirect(false);
						super.setViewPage("/WEB-INF/common/msg.jsp");

					}//end of if...
				} catch (SQLException e) {
					e.printStackTrace();
					String message = "서버 문제로 인하여 회원정보 수정이 불가합니다. 나중에 다시 시도해주십시오.";
					String loc = request.getContextPath() + "/index.gu"; // 시작페이지로 이동한다.
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					// 부모창 새로고침 및 팝업창 닫기 위한 용도로 반환해주는 값

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/msg.jsp");
					
				}//end of try catch...
				
			} else {
				// 로그인을 안 했으면, 보통은 의도적으로 뚫고 들어온 경우
				String message = "회원정보를 수정하기 위해서는 먼저 로그인을 하세요!!";
				String loc = "javascript:history.back()";

				request.setAttribute("message", message);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
			
			
			
		}//end of if (!"POST".equals(method)) {}...
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}//end of exe...

	
}//end of class...





