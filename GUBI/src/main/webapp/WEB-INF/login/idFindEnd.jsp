<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>




<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />



<%-- 그냥 css 공유하자 --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login/idFind.css" />


<script type="text/javascript">

	// 로그인 페이지로
	function goLoginPage() {

		alert("로그인 페이지로 이동합니다!"); // 메세지 출력해주기
		location.href = "${pageContext.request.contextPath}/login/login.gu"; // 페이지 이동

	}//end of function goLoginPage() {}...

	// 비밀번호 찾기 페이지로
	function gopasswdFindPage() {
		alert("비밀번호 페이지로 이동합니다!");
		location.href = "${pageContext.request.contextPath}/login/passwdFind.gu"; // 페이지 이동

	}//end of function goLoginPage() {}...
</script>




<div class="container mt-5" style="border: solid 0px red;">

        <h4>아이디 찾기 출력</h4>


        <div class="row justify-content-center">

            <%-- 성명 --%>

            <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0; text-align: center; font-size: 18pt; color: red;">
                <span id="idResult">${requestScope.name}님의 아이디는 ${requestScope.userid} 입니다.</span>
            </div>
            <div class="w-100"></div>
            <div style="margin-bottom: 10%;"></div>



            <%--  바로 로그인, 비밀번호 찾기  버튼 --%>
            <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">바로 로그인 하시겠습니까?</div>
            <div class="w-100"></div>
            <div class="col-lg-5 col-md-7">
                <input type="button" class="btnstyle" value="로그인 하러가기" onclick="goLoginPage()" />
            </div>
            <div class="w-100"></div>

            <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">비밀번호를 잊으셨습니까?</div>
            <div class="w-100"></div>
            <div class="col-lg-5 col-md-7">
                <input type="button" class="btnstyle" value="비밀번호 찾기" onclick="gopasswdFindPage()" />
            </div>

            <div style="margin-bottom: 10%;"></div>


        </div>

    </div>




<jsp:include page="/WEB-INF/common/footer.jsp" />



