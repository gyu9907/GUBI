<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String ctxPath = request.getContextPath();
%>
<script type="text/javascript">
    var ctxPath = "${pageContext.request.contextPath}";  // JSP에서 컨텍스트 경로를 가져와서 ctxPath에 할당
</script>
<html>
<head>

<title>배송지 목록</title>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css"
	href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">


<!-- Optional JavaScript -->
<script type="text/javascript"
	src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript"
	src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>


<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/delivery/deliveryListPopup.js"></script>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/delivery/deliveryList.css" />

<style type="text/css">
.container {
    max-width: 98%; /* 컨테이너 넓이를 적당히 설정 */
    min-width: 800px;
}
button#submit-btn,
button#delete-btn {
	width: 120px;
}
button#modify-btn {
	width: 80px;
    padding: 3px 5px;
	border: solid 1px #ccc;
	color: rgb(85, 85, 85);
	background-color: white;
	font-size: 10pt;
	border-radius: 5px;
	transition: all 0.3s;
}
button#modify-btn:hover {
	border: solid 1px rgb(85, 85, 85);
}

.button#submit-btn {
	min-width: 80px;
    margin: 10px 0;
    border: solid 1px black;
    border-radius: 5px;
    color: white;
    background-color: black;
    padding: 12px 45px;
    font-size: 10pt;
    transition: 0.3s;
}


 
 /* Secure Checkout 버튼 */
button#goCheckoutBtn {
    display: block;
    width: 100%;
    margin: 10px 0;
    border: solid 1px black;
    border-radius: 5px;
    color: white;
    background-color: black;
    padding: 12px 45px;
    font-size: 10pt;
    transition: 0.3s;
}

button#goCheckoutBtn:hover {
    color: black;
    background-color: white;
}


.button#cancel-btn {
	display: block;
    width: 50%;
    margin: 10px 0;
    border: solid 1px black;
    border-radius: 5px;
    color: white;
    background-color: black;
    padding: 12px 45px;
    font-size: 10pt;
    transition: 0.3s;
	
} 

</style>

</head>
<body data-ctxPath="<%= request.getContextPath() %>">


		<div class="container mt-4">
			<h2>배송지 관리</h2>

			<c:if test="${not empty deliveryList}">
				<p>배송 주소록</p>
				<hr class="custom-hr">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th><input type="checkbox" name="selectall"
								value="selectall" /></th>
							<th>배송지명</th>
							<th>수령인</th>
							<th>연락처</th>
							<th>주소</th>
							<th>기본 배송지</th>
							<th>수정</th>
						</tr>
					</thead>
					<tbody>
					<form name="deliveryDeleteForm">
						<c:forEach var="delivery" items="${deliveryList}">
							<tr data-deliveryno="${delivery.deliveryno}">
								<td>
                                <input type="checkbox" name="deliveryno" value="${delivery.deliveryno}" />
                                </td>                               
								<td>${delivery.delivery_name}</td>
								<td>${delivery.receiver}</td>
								<td>${fn:substring(delivery.receiver_tel, 0, 3)}-${fn:substring(delivery.receiver_tel, 3, 7)}-${fn:substring(delivery.receiver_tel, 7, 11)}</td>
								<td>${delivery.address}${delivery.detail_address}</td>
								<td>${delivery.is_default == 1 ? '기본배송지' : ''}</td>
								<td><button type="button" id="modify-btn">수정하기</button></td>
							</tr>
						</c:forEach>
					</form>	
					</tbody>
				</table>
			</c:if>
			<hr class="custom3-hr">

			<button type="button" id="submit-btn">배송지 등록</button>
			<button type="button" id="delete-btn">배송지 삭제</button>

		</div>


	<!--블러 처리 콘텐츠 영역 끝--->

</body>
</html>