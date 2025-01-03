<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<jsp:include page="../common/header.jsp" />	
<script type="text/javascript" src="<%= ctxPath%>/js/product/categoryListJSON.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/js/product/productList.js"></script>
      
	<div class="productList_wrapper">
      <div class="header-container">
        <!-- 타이틀 영역 -->
        <div class="title-section">
          <p class="title" id="category_title">ALL PRODUCTS</p>
          <p>
            GUBI is where the beauty of history meets the thrill of right now.
            With sculptural signature pieces we bring substance and character to
            the homes and spaces where design lovers live and meet
          </p>
        </div>
        <!-- 카테고리 영역 -->
        <div class="category-section">
          <ul class="category-list" id="category-list"></ul>
        </div>
      </div>
      
      <div class="productGrid_wrapper">
        <!-- 필터 드롭다운 영역 -->
        <div class="category-filter">
          <div class="free_shipping">
          
          <!-- 총 상품 수 display none  -->
	      <span id="totalProductCount" style="display: none" ></span>
	      <span id="countProduct" style="display: none">0</span>
          <!-- 총 상품 수 display none  -->
          
            <span class="switch-label">Free Shipping</span>
            <label class="switch">
              <input type="checkbox" name="freeShipping"/>
              <span class="slider"></span>
            </label>
          </div>
          <div class="filter-dropdowns">
            <!-- 네 번째 드롭다운 -->
            <div class="filter-dropdown">
              <select id="collection" name="collection">
                <option value="">Collection</option>
              	<c:forEach var="colvo"  items="${requestScope.collectionList}">
              		<option value="${colvo.name}">${colvo.name}</option>
              	</c:forEach>
              </select>
            </div>

            <!-- 다섯 번째 드롭다운 -->
            <div class="filter-dropdown">
              <select id="sortby" name="sortby">
                <option value="latest">latest</option>
                <option value="bestItem">popular</option>
              </select>
            </div>
          </div>
        </div>

        <!-- 제품 목록 -->
          <div class="product-grid" id="product-grid"></div>

      </div>
    </div>
  <jsp:include page="../common/footer.jsp" />