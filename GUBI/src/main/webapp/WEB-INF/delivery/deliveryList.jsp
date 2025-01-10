<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />

<%
    String ctxPath = request.getContextPath();
%>
<script type="text/javascript">
    var ctxPath = "${pageContext.request.contextPath}";  // JSP에서 컨텍스트 경로를 가져와서 ctxPath에 할당
</script>


<title>::: 배송지 목록 :::</title>
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
<script type="text/javascript" src="<%= ctxPath%>/js/delivery/deliveryList.js"></script>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/delivery/deliveryList.css" />

	<div id="content">
		<!-- 콘텐츠 영역 시작 -->
<div class="sidebar2">
    <h2><a href="${pageContext.request.contextPath}/member/myPage.gu">마이페이지</a></h2>
    <hr>
    <div class="section">
        <h3>나의 쇼핑 정보</h3>
        <ul>
            <li><a href="${pageContext.request.contextPath}/member/memberOrderList.gu?status=order">주문/배송</a></li>
            <li><a href="${pageContext.request.contextPath}/member/memberOrderList.gu?status=refund">취소/반품/교환</a></li>
        </ul>
    </div>
    <div class="section">
        <h3>나의 활동 정보</h3>
        <ul>
            <li><a href="${pageContext.request.contextPath}/member/memberEdit.gu">회원정보 수정</a></li>
            <li><a href="${pageContext.request.contextPath}/member/memberDelete.gu">회원 탈퇴</a></li>
            <li><a href="${pageContext.request.contextPath}/delivery/deliveryList.gu">배송지 관리</a></li>
            <li><a href="${pageContext.request.contextPath}/review/myReviewList.gu">나의 리뷰</a></li>
            <li><a href="#">1:1 문의</a></li>
        </ul> 
    </div>
  </div>


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
						</tr>
					</thead>
					<tbody>
					<form name="deliveryDeleteForm">
						<c:forEach var="delivery" items="${deliveryList}">
							<tr data-deliveryno="${delivery.deliveryno}"
								onclick="opendeliveryModifyPage(this)">
								<td>
                                <input type="checkbox" name="deliveryno" value="${delivery.deliveryno}" onclick="event.stopPropagation();" />
                                </td>                               
								<td>${delivery.delivery_name}</td>
								<td>${delivery.receiver}</td>
								<td>${delivery.receiver_tel}</td>
								<td>${delivery.address}${delivery.detail_address}</td>
								<td>${delivery.is_default == 1 ? '기본배송지' : ''}</td>
							</tr>
						</c:forEach>
					</form>	
					</tbody>
				</table>
			</c:if>
			<hr class="custom3-hr">

			<button type="button" id="submit-btn" onclick="goSubmit()">배송지 등록</button>
			<button type="button" id="delete-btn" onclick="goDelete()">배송지 삭제</button>

		</div>


	</div>
	<!--블러 처리 콘텐츠 영역 끝--->