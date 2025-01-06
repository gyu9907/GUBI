<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Administrator</title>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<!-- jquery js-->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<!-- Bootstrap js-->
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/css/bootstrap.min.css" type="text/css">

</head>

<body>
	<!-- 관리자메뉴-->
	<div id="header">   
		 <a href="${pageContext.request.contextPath}/admin/admin.gu">
		 	<img id="logo" src="${pageContext.request.contextPath}/image/logo.png" onclick="${pageContext.request.contextPath}/admin/admin.gu"/>
		 </a>
	</div><!-- header-->
		
	
	<div id="adminmenu">
		<ul class="nav nav-tabs">
			<li class="nav-item">
			  <a class="nav-link HOME" href="${pageContext.request.contextPath}/admin/admin.gu">HOME</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link MEMBER" href="${pageContext.request.contextPath}/admin/member.gu">MEMBER</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link ORDER" href="${pageContext.request.contextPath}/admin/order.gu">ORDER</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link CATEGORY" href="${pageContext.request.contextPath}/admin/category.gu">CATEGORY</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link PRODUCT" href="${pageContext.request.contextPath}/admin/product.gu">PRODUCT</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link COLLECTION" href="${pageContext.request.contextPath}/admin/collection.gu">COLLECTION</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link STATISTICS" href="${pageContext.request.contextPath}/admin/statistics.gu">STATISTICS</a>
			</li>
		  </ul>
	</div>