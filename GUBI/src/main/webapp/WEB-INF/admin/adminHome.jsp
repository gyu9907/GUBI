<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    
<%
	String ctx_Path = request.getContextPath();
	//	   /GUBI
%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="<%= ctx_Path%>/css/common/common.css">

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<!-- jquery js-->
<script type="text/javascript" src="<%= ctx_Path%>/js/jquery-3.7.1.min.js"></script>

<!-- Bootstrap -->
<script type="text/javascript" src="<%= ctx_Path%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="<%= ctx_Path%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" type="text/css">

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctx_Path%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctx_Path%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script> 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctx_Path%>/js/common/common.js"></script>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<title>GUBI</title>
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/adminHome.css">
</head>
<body>
<div class="">

        <!-- 관리자메뉴-->
        <div id="header">   
            <a href="<%= ctx_Path%>/admin/admin.gu">
	 			<img id="logo" src="<%= ctx_Path%>/image/logo.png" onclick="<%= ctx_Path%>/admin/admin.gu" style="width:72px;"/>
	  		</a>
        </div><!-- header-->

        <div id="article" class="bg-light">
            <div id="adminmenu" class="bg-white">
                    <!-- Links 추가하기 -->
                    <ul class="navbar-nav">
                        <li class="li dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/admin.gu">HOME</a>
                        </li>
                        <li class="li dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/member.gu">MEMBER</a>
                        </li>
                        <li class="li dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/order.gu">ORDER</a>
                        </li>
                        <li class="li dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/category.gu">CATEGORY</a>
                        </li>
                        <li class="li dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/product.gu">PRODUCT</a>
                        </li>
                        <li class="li dropdown-item">
                        <a class="nav-link">STATUS</a>
                        </li>
                    </ul>
            </div> <!-- adminmenu-->
    
            <div id="contents">

                <div id="todayissue" class="border rounded-sm">
                    <span class="today">Today</span>

                    <hr style="border: solid 1px black; opacity:100%">

                    <div id="card" class="row justify-content-around mt-4">
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Visitor</h5>
                            <i class="icon fa-solid fa-house fa-4x mb-3 mt-1" style="color:#222831"></i>
                            <h3 class="card-text mt-4">${requestScope.visitor[0]}</h3>
                            </div>
                        </div>
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Register</h5>
                            <i class="icon fa-solid fa-user fa-4x mb-3 mt-1" style="color:#222831"></i>
                            <h3 class="card-text mt-4">${requestScope.register[0]}</h3>
                            </div>
                        </div>
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Purchase</h5>
                            <i class="icon fa-solid fa-cart-shopping fa-4x mb-3 mt-1" style="color:#222831"></i>
                            <h3 class="card-text mt-4">${requestScope.purchase[0]}</h3>
                            </div>
                        </div>
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Sales</h5> 
                            <i class="fa-solid fa-sack-dollar fa-4x mb-3 mt-1" style="color:#222831"></i>
                            <h3 class="card-text mt-4"><fmt:formatNumber value="${requestScope.sales[0]}" pattern="#,###"/> ₩</h3>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="week">
                    <div id="money" class="row justify-content-between mb-3">
                        <div class="col"><h6>Weekly Visitor</h6><h4>${requestScope.visitor[1]}</h4></div>
                        <div class="col"><h6>Weekly Register</h6><h4>${requestScope.register[1]}</h4></div>
                        <div class="col"><h6>Weekly Purchase</h6><h4>${requestScope.purchase[1]}</h4></div>
                        <div class="col"><h6>Weekly Sales</h6><h4><fmt:formatNumber value="${requestScope.sales[1]}" pattern="#,###"/> ₩</h4></div>
                    </div>
                </div>
                <div id="month">
                    <div id="money" class="row justify-content-between mb-3">
                        <div class="col"><h6>Monthly Visitor</h6><h4>${requestScope.visitor[2]}</h4></div>
                        <div class="col"><h6>Monthly Register</h6><h4>${requestScope.register[2]}</h4></div>
                        <div class="col"><h6>Monthly Purchase</h6><h4>${requestScope.purchase[2]}</h4></div>
                        <div class="col"><h6>Monthly Sales</h6><h4><fmt:formatNumber value="${requestScope.sales[2]}" pattern="#,###"/> ₩</h4></div>
                    </div>
                </div>
                

            <div id="info">
                <div id="tables" ><!-- 최근주문, 최근가입-->
                    <!-- 최근주문내역-->
                    <div id="allorder" class="border rounded-sm">
                        <div id="ordertitle">
                            <div class="title mr-2">Recent Orders</div>
                            <button type="button" class="viewbtn btn" onclick="location.href='<%= ctx_Path%>/admin/order.gu'"><span>+ more</span></button>
                        </div>
                        <div>
                            <table class="table table-sm mt-2">
                                <thead>
                                    <tr>
                                    <th>주문번호</th>
                                    <th>주문자아이디</th>
                                    <th>주문수량</th>
                                    <th>총주문액</th>
                                    <th>주문일시</th>
                                    </tr>
                                </thead>
                                <c:forEach var="ovo" items="${requestScope.orderlist}">
	                                 <tbody>
	                                    <tr>
	                                    <td>${ovo.orderno}</td>
	                                    <td>${ovo.fk_userid}</td>
	                                    <td>${ovo.total_cnt}</td>
	                                    <td><fmt:formatNumber value="${ovo.total_price}" pattern="#,###"/>원</td>
	                                    <td>${ovo.orderday}</td>
	                                    </tr>
	                                </tbody>
                                </c:forEach>
                            </table>
                        </div>
                        <div id="pageBar">
					       <nav>
					          <ul class="pagination">${requestScope.pageBar}</ul>
					       </nav>
				    	</div>
                    </div><!-- allorder -->
                    
                    
                    
                    <!-- 최근회원가입-->
                    <div id="registerlist" class="border rounded-sm">
                        <div id="registertitle">
                            <div class="title mr-2">Recent Register</div>
                            <button type="button" class="viewbtn btn" onclick="location.href='<%= ctx_Path%>/admin/member.gu'"><span>+ more</span></button>
                        </div>
                        <div>
                            <table class="table table-sm mt-2">
                                <thead>
                                    <tr>
                                    <th>회원번호</th>
                                    <th>이름</th>
                                    <th>아이디</th>
                                    <th>접속횟수</th>
                                    <th>가입일시</th>
                                    </tr>
                                </thead>
                                <c:forEach var="mvo" items="${requestScope.registerlist}">
	                                <tbody>
	                                    <tr>
	                                    <td>1001</td>
	                                    <td>${mvo.userid}</td>
	                                    <td>${mvo.name}</td>
	                                    <td>${mvo.logincnt}</td>
	                                    <td>${mvo.registerday}</td>
	                                    </tr>
	                                </tbody>
                                </c:forEach>
                            </table>
                        </div>
                        <div id="pageBar2">
					       <nav>
					          <ul class="pagination">${requestScope.pageBar2}</ul>
					       </nav>
				    	</div>
                    </div> <!--registerlist -->
                </div>
                    
                    
                    
                <!-- 최근리뷰-->
                <div id="review" class="border rounded-sm">
                    <div id="reviewtitle">
                        <div class="title mr-2">Recent Reviews</div>
                        <button type="button" class="viewbtn btn" onclick="location.href='<%= ctx_Path%>/admin/review.gu'"><span>+ more</span></button>
                    </div>
                    <div>
                        <table class="table table-hover mt-2 overflow-auto">
                            <thead>
                                <tr>
                                <th>상품번호</th>
                                <th>상품명</th>
                                <th>아이디</th>
                                <th>작성내용</th>
                                <th>작성일시</th>
                                </tr>
                            </thead>
                            <c:forEach var="rvo" items="${requestScope.reviewList}">
                            <tbody>
                                <tr>
                                <td>${rvo.reviewno}</td>
                                <td>${rvo.pvo.name}</td>
                                <td>${rvo.fk_userid}</td>
                                <td>${rvo.title}</td>
                                <td>${rvo.registerday}</td>
                                </tr>
                            </tbody>
                            </c:forEach>
                        </table>
                    </div>
                </div> <!-- review -->
            </div>
            
            
            
        </div><!-- contents-->
        </div><!-- article-->
        
    </div><!-- container-fluid -->
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  