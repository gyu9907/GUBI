package member.controller;

import java.sql.SQLException;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberRegister extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String method = request.getMethod();
		
		if ("GET".equalsIgnoreCase(method)) {
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberRegister.jsp");
			
		} else {
			HttpSession session = request.getSession();
			boolean emailCheckOk = (boolean)session.getAttribute("emailCheckOk");
			
			if (!emailCheckOk) { // 해킹 방지용 세션 검사
				
				String message = "인증을 받지않고 바꾸려고 하면 안됩니다!";
				String loc = "javascript:history.back()";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				session.removeAttribute("emailCheckOk"); // 다쓰면 삭제
				
				return; // else 없으면 넣어줘야함!!!!!
			}
			
			// form 태그의 name 속성 받아옴
			String userid = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			String name = request.getParameter("name");
			String birth = request.getParameter("birth");
			String email = request.getParameter("email");
			
			String hp1 = request.getParameter("hp1");
			String hp2 = request.getParameter("hp2");
			String hp3 = request.getParameter("hp3");
			
			String postcode = request.getParameter("postcode");
			String address = request.getParameter("address");
			String detail_address = request.getParameter("detail_address");
			
			String tel = hp1 + hp2 + hp3; // 휴대폰 번호 합치기

			
			MemberVO member = new MemberVO();

			member.setUserid(userid);
			member.setPasswd(passwd);
			member.setName(name);
			member.setEmail(email);
			member.setBirth(birth);
			member.setEmail(email);
			member.setTel(tel);
			member.setPostcode(postcode);
			member.setAddress(address);
			member.setDetail_address(detail_address);
			
			////////////////////////////////////////////////////////////////

			// === 회원가입이 성공되어지면 자동으로 로그인 되도록 하겠다. === //
			try {
				int n = mdao.registerMember(member);
				
				if (n == 1) {
					// 회원가입 성공
					
					request.setAttribute("userid", member.getUserid());
					request.setAttribute("passwd", member.getPasswd());
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/common/memberRegister_after_autoLogin.jsp");
					return;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				String message = "회원가입 실패!";
				String loc = "javascript:history.back()";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}//end of try catch...
			
				
		}//end of if else..
		
		

	}//end of exe...

	
	
	
}//end of class...
