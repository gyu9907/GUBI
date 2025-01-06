<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" />	
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product/product_search.css" />

<!-- jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/css/bootstrap.min.css" type="text/css">
<!-- Bootstrap js -->
<script src="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" type="text/javascript"></script>
<!-- 사용자 정의 js -->
<script type="text/javascript">
document.title = "Search";
const ctxPath = "${pageContext.request.contextPath}";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/product/productSearch.js"></script>
	<div class="productList_wrapper container py-5">
		<div class="productGrid_wrapper">
		
			<form name="searchFrm">
				<input type="hidden" name="start"/>
				<input type="hidden" name="len"/>
				<div class="py-5">
					<input type="text" name="searchWord" id="searchWord" placeholder="Type to search" />
				</div>
				<div class="flex">
					<div>
						<ul class="nav nav-pills">
						  <li class="nav-item">
						    <a class="nav-link active" data-toggle="pill" href="#category">Category</a>
						  </li>
						  <li class="nav-item">
						    <a class="nav-link" data-toggle="pill" href="#price">Price</a>
						  </li>
						  <li class="nav-item">
						    <a class="nav-link" data-toggle="pill" href="#color">Color</a>
						  </li>
						</ul>
					</div>
	
					<div class="filter-dropdown">
						<select id="sortby" name="sortby">
							<option value="latest">latest</option>
							<option value="bestItem">popular</option>
						</select>
					</div>
				</div>
				
				
				<div class="tab-content py-3">
				  <div class="tab-pane container active" id="category">
				  	<h4>Category</h4>
					<select name="major_category" id="major_category">
						<option value="" selected>Major Category</option>
						<c:forEach var="major" items="${requestScope.selectMajorCategory}">
							<option value="${major.major_category}">${major.major_category}</option>
						</c:forEach>
					</select>
					<select name="small_category" id="small_category">
						<option value="" selected>Small Category</option>
						
					</select>
				  </div>
				  <div class="tab-pane container fade" id="price">
				  	<h4>Price</h4>
				      ₩&nbsp;<input type="number" name="startPrice" maxlength="10"/>&nbsp;&nbsp;-&nbsp;
				      ₩&nbsp;<input type="number" name="endPrice" maxlength="10"/>
				  </div>
				  <div class="tab-pane container fade" id="color">
				    <h4>Color</h4>
					<div style="display: flex;">
						<c:forEach var="color" items="${requestScope.colorList}" varStatus="status">
							<div style="border: solid 2px #eee; border-radius: 100%; display: inline-blick; margin-right: 8px;">
								<input type="checkbox" name="color" id="color${status.index}" value="${color}" style="display: none;"/>
								<label for="color${status.index}" style=" display: block; background-color: ${color}; width:20px; height:20px; margin: 4px; border-radius: 100%;"></label>
							</div>
						</c:forEach>
					</div>
				  </div>
				</div>
				
			</form>
		
			<div>
				<div class="product-grid">
					<%-- 상품 검색결과 공간 --%>
				</div>
			</div>
		</div>
	</div>

	<!-- <script>
      // 요소를 선택
      const element = document.getElementById("imageElement");

      // hover 상태에서 이미지 변경
      element.addEventListener("mouseover", function () {
        element.style.backgroundImage = 'url("image2.jpg")'; // hover 시 이미지 변경
      });

      // hover에서 벗어났을 때 원래 이미지로 돌아오기
      element.addEventListener("mouseout", function () {
        element.style.backgroundImage = 'url("image1.jpg")'; // 기본 이미지로 돌아가기
      });
    </script> -->
  <jsp:include page="../common/footer.jsp" />
