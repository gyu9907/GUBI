<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<link rel="stylesheet" href="<%= ctxPath%>/css/product/product_detail.css">

<jsp:include page="../common/bootstrap.jsp" />	
<jsp:include page="../common/header.jsp" />	

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/product/productDetail.js"></script>
  <div class="productDetail_wrapper">
  	<div class="container">
  		<div class="product-image">
  			<img id="product_image" src="<%= ctxPath%>/data/images/${requestScope.optionList[0].img}" />
  		</div>
  		<div class="product-details">
  			<h2>${requestScope.pvo.name}</h2>
  			<p class="product_desc" id="product_desc">${requestScope.pvo.description}</p>
  			<span id="read_more" style="cursor: pointer">...Read More</span>
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
  						<fmt:formatNumber value="${requestScope.pvo.price * (requestScope.pvo.point_pct / 100)}" type="number" groupingUsed="true" />
  					P
  					</span>
  				</li>
  			</ul>
  		</div>
  		
  		<div class="spacing"></div>
  		
  		<div class="price-info">
  			<p class="price">&#8361; 
  				<fmt:formatNumber value="${requestScope.pvo.price}" type="number" groupingUsed="true" />
  			</p>
  		</div>
  		<form name="addCartFrm" >
	  		<input type="hidden" id="spinner" name="cnt" value="1">
	  		<input type="hidden" id="spinner" name="optionno" value="${requestScope.optionList[0].optionno}">
	  		<div class="button-wrapper">
	  			<button type="button" onclick="addCart()">Add Cart</button>
	  			<button>Order</button>
	  		</div>
  		</form>
  	</div>
  	
  	<div class="clearfix"></div>
  </div>
  <div class="detail_container">
  	<c:import url="/data/html/${requestScope.pvo.detail_html}" charEncoding="UTF-8"/>
  </div>
  
  <hr class="border-divider" />
    <!-- 리뷰 작성 폼 -->
    <div class="review-section">
      <div class="review-list" id="reviewList">
        <!-- 리뷰 항목 -->
  <%--    <div class="review-item">
          <p class="review-author">han min jeong</p>
          <p class="review-rating">⭐⭐⭐⭐☆</p>
          <p class="review-comment">
            The lamp is absolutely beautiful and adds so much charm to my
            living room.
          </p>
        </div>
        --%>
      </div>

      <hr class="divider" />

      <form class="review-form" id="review-form">

        <div class="form-group">
          <label for="review-rating">Rating</label>
          <select id="review-rating" name="review-rating" required>
            <option value="5">★★★★★</option>
            <option value="4">★★★★☆</option>
            <option value="3">★★★☆☆</option>
            <option value="2">★★☆☆☆</option>
            <option value="1">★☆☆☆☆</option>
          </select>
        </div>
        <div class="form-group">
          <label for="review-message">Review</label>
          <textarea
            id="review-message"
            name="review-message"
            rows="4"
            placeholder="Write your review here"
            required
          ></textarea>
        </div>
        <div class="button-wrapper">
          <button type="submit">Write</button>
        </div>
      </form>
    </div>
    
  </div>
  <jsp:include page="../common/footer.jsp" />