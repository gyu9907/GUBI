<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<!DOCTYPE html>
<html>
<head>

<title>구비사이트</title> 

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">

<!-- 직접 만든 CSS -->
<%-- <link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/template/template.css" /> --%>

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script> 

<%-- 직접 만든 JS --%>
<%-- <script type="text/javascript" src="<%= ctxPath%>/js/template/template.js"></script> --%>

<script type="text/javascript">
	sessionStorage.setItem("contextpath", "${pageContext.request.contextPath}");
	
	// 로그아웃 함수
	function goLogOut() {
		
		 // 로그아웃을 처리해주는 페이지로 이동
	    location.href = "${pageContext.request.contextPath}/login/logout.gu";
		
	}//end of function goLogOut() {}...
	
	
	// 회원정보 수정 이동 함수
	function gomemberEditPage() {
		location.href = "${pageContext.request.contextPath}/member/memberEdit.gu";
		
	}//end of function goMypage() {}...
	
	// 회원 탈퇴로 이동 함수
	function gomemberDeletePage() {
		location.href = "${pageContext.request.contextPath}/member/memberDelete.gu";
		
	}//end of function goMypage() {}...
	
</script>

</head>
<body>

   <%-- <%@ include file="/WEB-INF/login/login.jsp" %> --%>
   
   <c:if test="${empty sessionScope.loginuser && empty sessionScope.loginadmin}">
   		<a href="${pageContext.request.contextPath}/login/login.gu">로그인 하기 전</a>
   </c:if>


	<c:if test="${not empty sessionScope.loginuser}">
		<div id="after_loginuser">${(sessionScope.loginuser).name} 접속중...</div>
		<button type="button" id="memberEdit" onclick="gomemberEditPage()">회원정보 변경(임시로 만든 구멍)</button>
		<button type="button" id="memberDelete" onclick="gomemberDeletePage()">회원탈퇴(임시로 만든 구멍)</button>
		<button id="logout" onclick="goLogOut()">로그아웃</button>
		<%-- <div id="adsd">${(sessionScope.loginuser).tel} 접속중...</div> --%>
	</c:if>
	
	
	<c:if test="${not empty sessionScope.loginadmin}">
		<div id="after_loginadmin">${(sessionScope.loginadmin).name} 접속중</div>
		<button id="logout" onclick="goLogOut()">로그아웃</button>
	</c:if>
	
	
	
	
	
	
	
	
	
	
	