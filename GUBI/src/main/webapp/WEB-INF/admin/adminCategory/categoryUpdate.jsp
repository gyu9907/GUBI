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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/category/categoryUpdate.css">

<script type="text/javascript" src="<%= ctx_Path%>/js/admin/categoryUpdate.js"></script>


</head>

<script type="text/javascript">
$(document).ready(function(){
	document.title="관리자 카테고리수정";
	$(".nav-link.CATEGORY").addClass("active"); // 메뉴엑티브
});
</script>

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
						<li><a href="<%= ctx_Path%>/admin/categoryUpdate.gu" style="color:black">카테고리 수정</a></li>
					</ul>
				</ul>
			</div>
		</div>

		<!-- 본문 -->
		<div id="article" class="m-3 p-3">

			<p>Category > 카테고리 수정</p>
		
			<!-- 검색기능 -->
			<div id="categorysearch">
				<h4 class="bold">카테고리 수정</h4>
				<hr style="border: 1px solid black; opacity:20%">
				<br>
				<h6>&nbsp;&nbsp;카테고리 조회</h6>
			</div>
			
			<hr style="border: solid 1px black; opacity:20%">

			<div id="categoryall">
			<form name="updateCategory" enctype="multipart/form-data">
			
				<table class="table">
					<thead class="bg-light">
						<tr>
							<td>수정 가능한 카테고리</td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty requestScope.updateCategorySelect}">
							<c:forEach var="cvo" items="${requestScope.updateCategorySelect}">
								<tr id="updateCategoryTr" data-id="${cvo.categoryno}" data-toggle="modal" data-target="#detailOrder${cvo.categoryno}">
									<td>
										<span id="categoryno" >${cvo.categoryno}&nbsp;&nbsp;&nbsp;</span>
										<span><img src="<%= ctx_Path%>/data/images/${cvo.category_img}" id="categoryimg"/>&nbsp;&nbsp;</span>
										<span id="majorCategory">${cvo.major_category}</span>
										<span id="smallCategory"> / ${cvo.small_category}</span>
									</td>
								</tr>
								
								<!-- modal 만들기 -->
								
								<div class="modal fade" id="detailOrder${cvo.categoryno}" data-backdrop="static">
									<div class="modal-dialog modal-dialog-centered modal-lg">
									  <div class="modal-content">
										<!-- Modal header -->
										<div class="modal-header">
										  <h5 class="modal-title">카테고리 수정하기</h5>
										  <button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<!-- Modal body -->
										<div class="modal-body">
											<div class="head"><span>Category no${cvo.categoryno}</span></div>
											<div>
												<div class="title">카테고리 대분류</div>
												<div>
													<select name="major_category" id="major_category">
														<option value="" disabled>대분류</option>
														<option value="SEATING" id="seating">SEATING</option>
														<option value="LIGHTING" id="lighting">LIGHTING</option>
														<option value="TABLES" id="tables">TABLES</option>
													</select>
												</div>
											</div>
											<div>
												<div class="title">카테고리 소분류</div>
												<div>
													<input type="text" id="small_category" maxLength="30"/>
													<span class="error1">카테고리 소분류는 필수입력 사항입니다.</span>
												</div> 
											</div>
											<div>
												<div class="title">카테고리 이미지</div>
												<div id="image">
												<img src="" id="viewcategoryimg" class="mr-2" />
													<!--  <input type="text" id="categoryimg" name="categoryimg" readonly /> -->
													 <input type="file" id="newcategoryimg" class="" />
													 <span class="error2">카테고리 이미지는 필수입력 사항입니다.</span>
												</div>
											</div>
										</div>
										<!-- Modal footer -->
										<div class="modal-footer">
										<button type="button" class="btn btn-dark" id="updataCategory">수정하기</button>
										  <button type="button" class="btn btn-light" data-dismiss="modal">Close</button>
										</div>
									  </div>
									</div>
								</div>
								
							</c:forEach>
						</c:if>
					</tbody>
				</table>
				
			</form>
			</div>
		</div> 
	</div>

<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  