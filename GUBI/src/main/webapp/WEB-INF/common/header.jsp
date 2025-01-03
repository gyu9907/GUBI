<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="<%= ctxPath%>/css/common/common.css">
<link rel="stylesheet" href="<%= ctxPath%>/css/product/product_list.css">

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script> 

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/common/common.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Bentham&family=Newsreader:ital,opsz,wght@0,6..72,200..800;1,6..72,200..800&display=swap"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Bentham&family=Newsreader:ital,opsz,wght@0,6..72,200..800;1,6..72,200..800&family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap"
	rel="stylesheet">

<title>GUBI</title>

</head>
<body>

    <div class="header_wrapper" >
        <ul class="nav_wrapper" id="nav_wrapper">
            <li class="nav_list1">
                <ul class="header_menu_list">
                    <li class="header_menu" id="product_menu"><a href="<%= ctxPath %>/product/productList.gu">Product</a></li>
                    <li class="header_menu"><a href="">Collections</a></li>
                    <%--<li class="header_menu"><a href="">GUBI's Story</a></li> --%>
                </ul>   
            </li>
            <li class="nav_list2">
                <div class="logo_wrapper">
                    <a href="<%= ctxPath%>/index.gu">
                        <img src="<%= ctxPath%>/images/common/logo-b.png" />
                    </a>
                </div>
            </li>
            <li class="nav_list3">
                <ul>
                    <li class="header_icon"><a href=""><i class="fa-solid fa-arrow-right-from-bracket fa-rotate-180 fa-lg"></i></a></li>
                    <li class="header_icon"><a href=""><i class="fa-solid fa-user"></i></a></li>
                    <li class="header_icon"><a href=""><i class="fa-solid fa-2em fa-magnifying-glass"></i></a></li>
                    <li class="header_icon"><a href=""><i class="fa-solid fa-location-dot"></i></a></li>
                    <li class="header_icon"><a href=""><i class="fa-solid fa-cart-shopping"></i></a></li>
                </ul>   
            </li>
        </ul>
        <div class="menu_container" id="header_dropdown_menu">
            <ul class="major_categry_list">
                <li>
                		<a href="<%= ctxPath%>/product/productList.gu?major_category=SEATING">SEATING</a>
                		<ul class="small_categry_list" id="seating_list"></ul>
                	</li>
                	<li>
                		<a href="<%= ctxPath%>/product/productList.gu?major_category=LIGHTING">LIGHTING</a>
                		<ul class="small_categry_list" id="lighting_list"></ul>
                	</li>
                	<li>
                		<a href="<%= ctxPath%>/product/productList.gu?major_category=TABLES">TABLES</a>
                		<ul class="small_categry_list" id="tables_list"></ul>
                	</li>
            </ul>
           
            <div class="product-grid">
                <!-- 제품 카드 1 -->
                <a href="#">
                    <div class="product-card">
                        <div class="product-image"> 
                            <img src="<%= ctxPath%>/images/main/collection2.png">
                        </div>
                        <div class="product-info">
                            <p>COPPIA TABLE LAMP</p>
                            <div class="price">A new sculptural twin-bulb table lamp designed for GUBI by Vibeke Fonnesberg Schmidt. Featuring two discs of Fonnesberg Schmidt’s signature plexiglass housed in a curving metal case, the elegantly industrial design represents both a new material and a new designer for GUBI.</div>
                        </div>
                    </div>
                </a>

                <!-- 제품 카드 2 -->
                <a href="#">
                    <div class="product-card">
                        <div class="product-image"> 
                            <img src="<%= ctxPath%>/images/main/collection2.png">
                        </div>
                        <div class="product-info">
                            <p>COPPIA TABLE LAMP</p>
                            <div class="price">A new sculptural twin-bulb table lamp designed for GUBI by Vibeke Fonnesberg Schmidt. Featuring two discs of Fonnesberg Schmidt’s signature plexiglass housed in a curving metal case, the elegantly industrial design represents both a new material and a new designer for GUBI.</div>
                        </div>
                    </div>
                </a>
                
            </div>
        </div>
    </div>