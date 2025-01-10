<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>

<html>
<head>

<title>MY PAGE</title> 
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/member/myPage.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 


<%-- 직접 만든 JS --%>
  <script type="text/javascript" src="<%= ctxPath%>/js/member/myPage.js"></script>

</head>
<body>

  <div class="sidebar">
    <div class="sub-title"><a href="${pageContext.request.contextPath}/member/myPage.gu">마이페이지</a></div>
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
<div class="container">

	<div class="main-title">MY PAGE</div>
  


	<div class="content">
		
        <div class="sub-title">${loginuser.name}님 반갑습니다.</div>
    	<hr>
    	
	    <div class="summaryContainer">
	      <div class="item">
	          <div class="number">welcome</div>
	         <div>${loginuser.name}님 반갑습니다.</div>  <%-- userid 컬럼명이 들어가야 한다. --%>
	        </div>
	        <div class="item">
	          <div class="number"><fmt:formatNumber value="${loginuser.point}" pattern="#,###" />P</div>
	          <div>총 포인트</div>
	                                      
	        </div>
	        <div class="item">
	          <div class="number"><fmt:formatNumber value="${loginuser.point}" pattern="#,###" />P</div>
	          <div>사용가능 포인트</div>
	        </div>
	    </div>
	    
	    <div class="sub-title">주문처리 현황</div>
	    
		<div class="shippingStatusContainer">
		    <div class="status">
		        <c:forEach var="entry" items="${orderStatusMap}">
		            <div class="item">
		                <div>
		                    <div class="text">${entry.key}</div> <!-- 상태 코드 표시 -->
		                    <div class="black number">
		                        <!-- 주문 상태 텍스트 표시 -->
		                        
		                        ${entry.value}
		                    </div>
		                </div>
		            </div>
		        </c:forEach>
		    </div>
		</div>
    <!-- 4. 최근 주문 내역 -->
	</div>
	
</div>

</body>
</html>