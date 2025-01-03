<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>




<%-- ${pageContext.request.contextPath} --%>

<html>
<head>
<meta charset="UTF-8">
<title>회원가입 후 오토로그인</title>


<script type="text/javascript">
	window.onload = function () {
		
		
		alert("회원가입에 감사드립니다.");
		
		const frm = document.loginFrm;
		
		frm.action = "${pageContext.request.contextPath}/login/login.gu";
		frm.method = "post";
		frm.submit();
		
		
	}//end of window.onload...

</script>

</head>
<body>
	<form name="loginFrm">
		<input type="hidden" name="userid" value="${requestScope.userid}" />
		<input type="hidden" name="passwd" value="${requestScope.passwd}" />
	</form>


</body>
</html>












