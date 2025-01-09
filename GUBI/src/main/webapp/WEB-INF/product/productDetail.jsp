<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<jsp:include page="../common/header.jsp" />	
<jsp:include page="../common/bootstrap.jsp" />	
<link rel="stylesheet" href="<%= ctxPath%>/css/product/product_detail.css">
<link rel="stylesheet" href="<%= ctxPath%>/css/review/review.css">

<%-- 직접 만든 JS --%>
<script>
    // JSP 표현식을 통해 sessionScope 값을 JavaScript 변수로 설정
    var loginuser = '<%= session.getAttribute("loginuser")%>';
</script>
<script type="text/javascript" src="<%= ctxPath%>/js/product/productDetail.js"></script>


  <div class="productDetail_wrapper">
  	<div class="container">
  		<div class="product-image">
  			<img id="product_image" src="<%= ctxPath%>/data/images/${requestScope.optionList[0].img}" />
  		</div>
  		<div class="product-details">
  			<h2>${requestScope.pvo.name}</h2>
  			<p class="product_desc" id="product_desc">${requestScope.pvo.description}</p>
  			<span id="read_more" style="font-size: 14px; color: #666; cursor: pointer">...Read More</span>
  			<hr />
  			<div class="shade">
  				<ul>
	  				<li class="cate">
	  					<span>${requestScope.category_name} Shade:</span>
	  					<span id="prod_opt_name">
	  						${requestScope.optionList[0].name}
	  					</span>
	  				</li>
  				</ul>
  				<div class="cate-button">
  					<c:forEach var="option" items="${requestScope.optionList}">
  						<button class="color-button" data-img="${option.img}" data-name="${option.name}" data-optionno="${option.optionno}">
  							<div style="background-color: ${option.color}"></div>
  						</button>
  					</c:forEach>
  				</div>
  			</div>
  		
  		<hr />
  		
  		<div class="base">
  			<ul>
  				<li class="cate">
  					<span>Delivery Price</span>
  					<span>&#8361;
  						<fmt:formatNumber value="${requestScope.pvo.delivery_price}" type="number" groupingUsed="true" />
  					</span>
  				</li>
  				<li class="cate">
  					<span>Point</span>
  					<span>
  						<fmt:formatNumber value="${requestScope.pvo.price * (requestScope.pvo.point_pct / 100)}" type="number" groupingUsed="true" />P
  					</span>
  				</li>
  			</ul>
  		</div>
  		
  		<!-- <div class="spacing"></div> -->
  		
  		<c:if test="${requestScope.pvo.cnt != 0}">
	  		<form name="addCartFrm" >
		  		<div class="price-info">
		  		<input class="product-cnt" type="number" id="spinner" name="cnt" value="1">
		 			<p class="price">&#8361; 
		 				<fmt:formatNumber value="${requestScope.pvo.price}" type="number" groupingUsed="true" />
		 			</p>
		 		</div>
		  		<input type="hidden" id="spinner" name="prodCount" value="${requestScope.pvo.cnt}">
		  		<input type="hidden" id="spinner" name="optionno" value="${requestScope.optionList[0].optionno}">
		  		<div class="button-wrapper">
		  			<button type="button" onclick="addCart()">Add Cart</button>
		  			<button type="button" onclick="goOrderPage()">Order</button>
		  		</div>
	  		</form>
  		</c:if>
  		<c:if test="${requestScope.pvo.cnt == 0}">
  			<div class="price-info">
	 			<p class="price">&#8361; 
	 				<fmt:formatNumber value="${requestScope.pvo.price}" type="number" groupingUsed="true" />
	 			</p>
	 		</div>
  		</c:if>
  	</div>
  	
  	<div class="clearfix"></div>
  </div>
  <div class="detail_container">
  	<c:import url="/data/html/${requestScope.pvo.detail_html}" charEncoding="UTF-8"/>
  </div>
  
	<hr class="border-divider" />
    <!-- 리뷰 작성 폼 -->
    <div class="review-section" id="reivewDiv">
		<div class="review-list" id="reviewList">
        <!-- 리뷰 항목 -->
		</div>
		
		
		<!-- 리뷰 작성 버튼 -->
		<%-- <c:forEach var="option" items="${requestScope.optionList}">
			<div class="button-wrapper">
				<button type="button" class="reviewBtn" data-toggle="modal" data-target="#addReviewModal" data-value="${option.optionno}">
					리뷰 작성하기 ${option.optionno}
				</button>
			</div>
		</c:forEach> --%>
		
		<!-- 리뷰 작성 모달 -->
		<%-- <jsp:include page="../review/reviewRegister.jsp" /> --%>
	</div>
</div>
<jsp:include page="../common/footer.jsp" />