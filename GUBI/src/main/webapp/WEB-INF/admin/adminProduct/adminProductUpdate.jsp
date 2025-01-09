<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctx_Path = request.getContextPath();
    //    /MyMVC
%>

<jsp:include page="/WEB-INF/admin/adminHeader.jsp" /> 

<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/product/adminProductUpdate.css">
<!--  직접 만든 js -->
<script type="text/javascript" src="<%= ctx_Path%>/js/admin/adminProductUpdate.js"></script>

<script type="text/javascript">
	document.title="관리자 상품수정";
	$(".nav-link.PRODUCT").addClass("active"); // 메뉴엑티브
</script>

<body>
<div>
	
	<div id="contents">
		<!-- 사이드 메뉴 -->
		<div id="sidemenu" class="bg-light p-3">
			<i class="fa-solid fa-bag-shopping fa-3x d-flex justify-content-center mt-4"></i>
			<h3>Product</h3>
			<div id="menulist">
				<ul>
					<li><a class="dropdown-item mb-3" href="#">Product</a></li>
					<ul>
						<li><a href="<%= ctx_Path%>/admin/product.gu">전체상품관리</a></li>
						<li><a href="#">상품등록</a></li>
					</ul>
					<li><a class="dropdown-item mb-3" href="#">Review / Question</a></li>
					<ul>	
						<li><a href="<%= ctx_Path%>/admin/ask.gu">문의관리</a></li>
						<li><a href="<%= ctx_Path%>/admin/review.gu">리뷰관리</a></li>
					</ul>
				</ul>
			</div>
		</div>

	<!-- 본문 -->
	<div id="article" class="m-3 p-3">

	<p>Product > 상품수정</p>
	
	<form name="productUpdateFrm">
		<!-- 검색기능 -->
		<div id="addProduct">
			<h4 class="bold">상품수정하기</h4>	
			<hr style="border: 1px solid black; opacity:20%">
			<br>
			<h6>&nbsp;&nbsp;상품수정</h6>
			
			<table id="table" class="table table-sm">
				<thead class="thead-light">
					<tr>
						<th class="th"><span>카테고리</span></th>
						<input type="hidden" class="productno" name="productno" value="${requestScope.productno}">
						<td>
							<select id="major_category" name="major_category" class="infoData">
								<option value="" disabled selected>대분류</option>
								<c:forEach var="major" items="${requestScope.selectMajorCategory}">
									<option value="${major.major_category}">${major.major_category}</option>
								</c:forEach>
							</select>
							<select name="small_category" id="small_category" class="infoData">
								<option value="" disabled selected>소분류</option>
							</select>
							<span class="error">카테고리는 필수선택 사항입니다.</span>
						</td>
					</tr>
					<tr>
						<th class="th"><span>상품명</span></th>
						<td>
							<input type="text" id="name" name="name" class="infoData"/>
							<span class="error">상품명은 필수입력 사항입니다.</span>
						</td>
					</tr>
					<tr>
						<th class="th"><span>상품설명</span></th>
						<td>
							<textarea id="description" class="infoData" name="description" cols="57" rows="8"></textarea>
							<span class="error">상품설명은 필수입력 사항입니다.</span>
						</td>
					</tr>
					<tr>
						<th class="th"><span>수량</span></th>
						<td><input type="text" name="cnt" id="cnt" class="infoData"/>
							<span class="error">수량은 필수입력 사항입니다.</span>
						</td>
					</tr>
					<tr>
						<th class="th"><span>가격</span></th>
						<td><input type="number" name="price" id="price" class="infoData" maxlength="10" />
							<span class="error">가격은 필수입력 사항입니다.</span>
						</td>
						
					</tr>
					<tr>
						<th class="th"><span>배송비</span></th>
						<td><input type="text" name="delivery_price" id="delivery_price" class="infoData" />
							<span class="error">배송비는 필수입력 사항입니다.</span>
						</td>
					</tr>
					
					<tr>
						<th class="th"><span>포인트</span></th>
						<td><input type="text" name="point_pct" id="point_pct" class="infoData"/>
							<span class="error">포인트는 필수입력 사항입니다.</span>
						</td>
					</tr>
				</thead>
			</table>
			
			  <div id="regibutton">
					<button type="button" class="updatebtn btn btn-success">상품수정</button>
					<button type="reset" class="btn btn-light">초기화</button>
			  </div>
		</div>
	</form>
	
	<form action="productUpdate">
		<input type="hidden" class="major" value="${productList.categoryVO.major_category}">
		<input type="hidden" class="small" value="${productList.categoryVO.small_category}">
		<input type="hidden" class="pname" value="${productList.name}">
		<input type="hidden" class="description" value="${productList.description}">
		<input type="hidden" class="cnt" value="${productList.cnt}">
		<input type="hidden" class="price" value="${productList.price}">
		<input type="hidden" class="point_pct" value="${productList.point_pct}">
		<input type="hidden" class="delivery_price" value="${productList.delivery_price}">
	</form>
</div>
</div>
</div>

<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  