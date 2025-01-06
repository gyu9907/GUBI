<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<title>컬렉션 등록 완료</title>

<!-- jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/css/bootstrap.min.css" >

<style>

body {
	position: relative;
	height: 100vh;
}

.container {
	position: absolute;
	top: 50%;
	left: 50%;
    transform: translate(-50%, -50%);
	text-align: center;
}

button.btn {
	padding: 5px;
	font-size: 11pt;
}
#viewCollection {
	background-color: black;
	color: white;
}
#viewCollection:hover {
	background-color: white;
	color: black;
}

</style>

<script type="text/javascript">
if(${empty sessionScope.collectionno}) {
	location.href = "${pageContext.request.contextPath}/admin/collectionRegister.gu";
}
$(document).ready(function() {
	$("#registerCollection").on("click", function() {
		location.href = "${pageContext.request.contextPath}/admin/collectionRegister.gu";
	});
	
	$("#viewCollection").on("click", function() {
		location.href = "${pageContext.request.contextPath}/collection/collectionDetail.gu?collectionno=${sessionScope.collectionno}";
	});
});
</script>
<c:remove var="collectionno" scope="session" /> <%-- 세션 삭제 --%>
</head>
<body>
	<div class="container row">
		<div class="offset-4 col-4 mb-5"><h4>컬렉션 등록이 완료되었습니다.</h4></div>
		<div class="btn-container offset-3 col-6 row">
			<button type="button" class="offset-1 col-4 btn btn-light" id="registerCollection">새로운 컬렉션<br>등록하기</button>
			<button type="button" class="offset-2 col-4 btn btn-light" id="viewCollection">등록한 컬렉션<br>확인하기</button>
		</div>
	</div>
</body>
</html>