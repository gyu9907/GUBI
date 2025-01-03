<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctx_Path = request.getContextPath();
    //    /MyMVC
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 주문상태변경</title>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> 


<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<!-- jquery js-->
<script type="text/javascript" src="<%= ctx_Path%>/js/jquery-3.7.1.min.js"></script>
<!-- Bootstrap js-->
<script type="text/javascript" src="<%= ctx_Path%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="<%= ctx_Path%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" type="text/css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/order/adminOrderStatus.css">

<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script type="text/javascript">
$(document).ready(function(){

});
</script>

</head>
<body>
	<div>
		<!-- 관리자메뉴-->
		<div id="header">   
			<a href="<%= ctx_Path%>/admin/admin.gu">
		 		<img id="logo" src="<%= ctx_Path%>/image/logo.png" onclick="<%= ctx_Path%>/admin/admin.gu"/>
		  	</a>
		</div>
		
		
		<!-- 관리자상단메뉴-->
		<div id="adminmenu">
			<ul class="nav nav-tabs">
				<li class="nav-item">
					<a class="nav-link" href="<%= ctx_Path%>/admin/admin.gu">HOME</a>
				  </li>
				<li class="nav-item">
				  <a class="nav-link" href="<%= ctx_Path%>/admin/member.gu">MEMBER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link active"  href="<%= ctx_Path%>/admin/order.gu">ORDER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" href="<%= ctx_Path%>/admin/category.gu">CATEGORY</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="<%= ctx_Path%>/admin/product.gu">PRODUCT</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" href="#menu3">STATUS</a>
				</li>
			  </ul>
		</div>

		<div id="contents">
			<!-- 사이드 메뉴 -->
			 
			<div id="sidemenu" class="bg-light p-3">
				<i class="fa-solid fa-receipt fa-3x d-flex justify-content-center mt-4"></i>
				<h3>ORDER</h3>
				<div id="menulist">
					<ul>
						<li><a class="dropdown-item mb-3" href="#">Order</a></li>
						<ul>
							<li><a href="<%= ctx_Path%>/admin/order.gu">주문리스트 (전체)</a></li>
							
							<c:forEach var="status" items="${requestScope.statusCnt}">
								<c:if test="${status.status < 6}">
									<li>
									<c:choose>
										<c:when test='${status.status == 1}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=1" data-value="${status.status}">결제대기</a></c:when>
										<c:when test='${status.status == 2}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=2" data-value="${status.status}">주문완료</a></c:when>
										<c:when test='${status.status == 3}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=3" data-value="${status.status}">배송중</a></c:when>
										<c:when test='${status.status == 4}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=4" data-value="${status.status}">배송완료</a></c:when>
										<c:when test='${status.status == 5}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=5" data-value="${status.status}">구매확정</a></c:when>
									</c:choose>
									<button type="button" value="1" class="btn btn-info btn-sm">
										<span>${status.statusCnt}</span>
									</button>
									</li>
								</c:if>
							</c:forEach>
						</ul>
						
						<li><a class="dropdown-item mb-3" href="#">Cancle</a></li>
						<ul>
							<c:forEach var="status" items="${requestScope.statusCnt}">
								<c:if test="${status.status > 5}">
									<li>
									<c:choose>
										<c:when test='${status.status == 6}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=6" data-value="${status.status}">주문취소</a></c:when>
										<c:when test='${status.status == 7}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=7" data-value="${status.status}">환불접수</a></c:when>
										<c:when test='${status.status == 8}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=8" data-value="${status.status}">환불완료</a></c:when>
									</c:choose>
									<button type="button" value="1" class="btn btn-success btn-sm">
									<span>${status.statusCnt}</span></button></li>
								</c:if>
							</c:forEach>
						</ul>
					</ul>
				</div>
			</div>

			<!-- 본문 -->
			<div id="article" class="m-3 p-3">
			<p>Order > 결제대기</p> 
			
				<form name="orderSearchFrm">
					<!-- 검색기능 -->
					<div id="membersearch">
						<h4 class="bold">주문완료</h4>
						<hr style="border: 1px solid black; opacity:20%">

				<!-- orderlist -->
				<div id="orderlist" class="p-3">
					<div id="top">
						<span>전체 : ${requestScope.orderCnt}건 조회</span>&nbsp;&nbsp;
						<!-- <span>총 주문액 : 원</span>  -->
					</div>
					
					<div>
						<table class="table table-sm mt-3">
							<thead class="thead-light">
							  <tr>
								<th>no</th>
								<th>주문번호</th>
								<th>주문일시</th>
								<th>주문수량</th>
								<th>주문상태</th>
								<th>주문자</th>
								<th>주문자아이디</th>
								<th>연락처</th>
								<th>총주문액</th>
							  </tr>
							</thead>
							<c:if test="${not empty requestScope.statusList}">
								<c:forEach var="ordervo" items="${requestScope.statusList}" varStatus="status">
								<tbody>
								  <tr id="ordertr" data-id="${ordervo.orderno}" data-toggle="modal" data-target="#detailOrder${ordervo.orderno}">
								  <fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}"></fmt:parseNumber>
				    			  <fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
								  	<td>${(requestScope.orderCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
									<td>${ordervo.orderno}</td>
									<td>${ordervo.orderday}</td>
									<td>${ordervo.total_cnt}</td>	
									<td>
									<c:choose>
				    					<c:when test='${ordervo.status == 1}'>결제대기</c:when>
				    					<c:when test='${ordervo.status == 2}'>주문완료</c:when>
				    					<c:when test='${ordervo.status == 3}'>주문취소</c:when>
				    					<c:when test='${ordervo.status == 4}'>배송중</c:when>
				    					<c:when test='${ordervo.status == 5}'>배송완료</c:when>
				    					<c:when test='${ordervo.status == 6}'>구매확정</c:when>
				    					<c:when test='${ordervo.status == 7}'>환불접수</c:when>
				    					<c:when test='${ordervo.status == 8}'>환불완료</c:when>
				    				</c:choose>
									</td>
									<td>${ordervo.name}</td>
									<td>${ordervo.fk_userid}</td>
									<td>${ordervo.tel}</td>
									<td><fmt:formatNumber value="${ordervo.total_price}" pattern="#,###"/></td>
								  </tr>
								</tbody>
								</c:forEach>
							</c:if>
							<c:if test="${empty requestScope.statusList}">
								<tbody>
									<tr><td>주문 내역이 없습니다...</td></tr>
								</tbody>
							</c:if>
						</table>
					</div>
				</div>
				<c:if test="${not empty requestScope.statusList}">
					<div id="pageBar">
				       <nav>
				          <ul class="pagination">${requestScope.pageBar}</ul>
				       </nav>
		    		</div>
				</c:if>
				
	
			</div> <!-- end article -->
		</div>
	</div>
</body>
</html>
</div>