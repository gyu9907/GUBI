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
<title>관리자 카테고리</title>
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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/category/categoryList.css">

<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

</head>

<script type="text/javascript">
$(document).ready(function(){

	$("select#major_category").val("${requestScope.major_category}");
	$("input:radio[value='${requestScope.categoryStatus}']").prop("checked", true);
	
});

function searchCategory(){
	const frm = document.categorySelectFrm;
    frm.action = "category.gu";
    //frm.method = "get";
    frm.submit();
}
</script>
<body>

	

	<!-- 관리자메뉴-->
	<div id="header">   
		  <a href="<%= ctx_Path%>/admin/admin.gu">
		 	<img id="logo" src="<%= ctx_Path%>/image/logo.png" onclick="<%= ctx_Path%>/admin/admin.gu"/>
		  </a>
	</div><!-- header-->

	<div id="adminmenu">
		<ul class="nav nav-tabs">
			<li class="nav-item">
				<a class="nav-link" href="<%= ctx_Path%>/admin/admin.gu">HOME</a>
			  </li>
			<li class="nav-item">
			  <a class="nav-link"  href="<%= ctx_Path%>/admin/member.gu">MEMBER</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link" href="<%= ctx_Path%>/admin/order.gu">ORDER</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link active" href="<%= ctx_Path%>/admin/category.gu">CATEGORY</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link" href="<%= ctx_Path%>/admin/product.gu">PRODUCT</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#menu4">STATUS</a>
			</li>
		  </ul>
	</div>

	<div id="contents">
	
		<!-- 사이드 메뉴 -->
		<div id="sidemenu" class="bg-light p-3">
			<i class="fa-solid fa-list fa-3x d-flex justify-content-center mt-4"></i>
			<h3>CATEGORY</h3>
			<div id="menulist">
				<ul>
					<li><a class="dropdown-item mb-3" href="#">Category</a></li>
						<ul>
							<li><a href="<%= ctx_Path%>/admin/category.gu">카테고리 조회</a></li>
							<li><a href="<%= ctx_Path%>/admin/categoryAdd.gu">카테고리 등록</a></li>
							<li><a href="<%= ctx_Path%>/admin/categoryUpdate.gu">카테고리 수정</a></li>
						</ul>
				</ul>
			</div>
		</div>

		<!-- 본문 -->
		<div id="article" class="m-3 p-3">

			<p>Category > 카테고리 조회</p>
		
			<form name="categorySelectFrm">
			<!-- 검색기능 -->
			<div id="categorysearch">
				<h4 class="bold">카테고리 관리</h4>
				<hr style="border: 1px solid black; opacity:20%">
				<br>
				<h6>&nbsp;&nbsp;카테고리 조회</h6>
				
				<table class="table table-sm">
					<thead class="thead-light">
						<tr>
							<th class="th">카테고리 소속</th> <!-- 대분류 의자,책상,조명 등 -->
							<td>
								<select name="major_category" id="category">
									<option value="" disabled selected>대분류</option>
									<option value="SEATING" id="seating">SEATING</option>
									<option value="LIGHTING" id="lighting">LIGHTING</option>
									<option value="TABLES" id="tables">TABLES</option>
								</select>
							</td>
						</tr>
						<tr>
							<th class="th">삭제유무</th> <!-- 소분류 거실의자, 야외의자 등 -->
							<td id="radio">
								<label><span class="mr-1">전체</span><input type="radio" value="" name="categoryStatus" class="mr-3" checked/></li></label>
								<label><span class="mr-1">등록된 카테고리</span><input type="radio" value="0" name="categoryStatus"  class="mr-3"/></li></label>
								<label><span class="mr-1">삭제된 카테고리</span><input type="radio" value="1" name="categoryStatus"  class="mr-3"/></li></label>
							</td>
						</tr>
					</thead>
				</table>

				<div id="searchbutton">
					<button type="button" class="btn btn-light" onclick="searchCategory()">검색</button>
					<button type="reset" class="btn btn-light">초기화</button>
				</div>
			</div>
			</form>
			
				<hr style="border: solid 1px black; opacity:20%">

				<div id="categoryall">
					
					<table class="table">
						<thead class="bg-light">
							<th>카테고리 LIST ( ${requestScope.categoryCnt} )</th>
						</thead>
						<c:if test="${not empty requestScope.categoryList}">
							<c:forEach var="catevo" items="${requestScope.categoryList}">
								<tbody>
									<tr>
									<td>
										<span>${catevo.categoryno}&nbsp;&nbsp;&nbsp;</span>
										<span><img src="<%= ctx_Path%>/data/images/${catevo.category_img}" />&nbsp;</span>
										<span>${catevo.major_category} / ${catevo.small_category}</span>
										<span class="ml-1">( ${catevo.productcnt} )</span>
									</td>
									</tr>
								</tbody>
							</c:forEach>
						</c:if>
						
						<c:if test="${empty requestScope.categoryList}"> <!-- 카테고리가 있을 경우 -->
								<tbody>
									<tr>
										<td>카테고리가 없습니다 ...</td>
									</tr>
								</tbody>
						</c:if>
						
					</table>
				</div>
		</div> 
	</div>
</div>
</body>
</html>