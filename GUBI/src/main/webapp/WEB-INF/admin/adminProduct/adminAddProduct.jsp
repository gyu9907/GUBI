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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/product/adminAddProduct.css">
<!--  직접 만든 js -->
<script type="text/javascript" src="<%= ctx_Path%>/js/admin/adminAddProduct.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	document.title="관리자 상품추가";
	$(".nav-link.PRODUCT").addClass("active"); // 메뉴엑티브
	
});
</script>
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
						<li><a href="#" style="color:black">상품등록</a></li>
					</ul>
					<li><a class="dropdown-item mb-3" href="#">Review	 / Question</a></li>
					<ul>	
						<li><a href="<%= ctx_Path%>/admin/ask.gu">문의관리</a></li>
						<li><a href="<%= ctx_Path%>/admin/review.gu">리뷰관리</a></li>
					</ul>
				</ul>
			</div>
		</div>

	<!-- 본문 -->
	<div id="article" class="m-3 p-3">

	<p>Product > 상품등록</p>
	
	<form name="productAddsFrm" enctype="multipart/form-data">
		<!-- 검색기능 -->
		<div id="addProduct">
			<h4 class="bold">상품등록하기</h4>	
			<hr style="border: 1px solid black; opacity:20%">
			<br>
			<h6>&nbsp;&nbsp;상품등록</h6>
			
			<table id="table" class="table table-sm">
				<thead class="thead-light">
					<tr>
						<th class="th"><span>카테고리</span></th>
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
					<tr>
						<th class="th"><span>대표이미지</span></th>
						<td><input type="file" id="thumbnail_img" name="thumbnail_img" class="thumbnail_img infoData" accept=".bmp, .jpg, .jpeg, .png, .webp"/>
							<span class="error">대표이미지는 필수입력 사항입니다.</span>
						</td>
					</tr>
					<tr>
						<th class="th"><span>대표이미지 미리보기 </span></th>
						<td class="preview"><img id="previewImg" /></td>
					</tr>
				</thead>
			</table>

			<br>
			
			<h6>&nbsp;&nbsp;옵션 추가하기</h6>
			<button type="button" class="addoption btn btn-danger mb-2"><p>옵션 추가</p></button>
			
			<div style="position: relative;">
		         <div class="toast align-items-center" id="addOptionToast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="1000" style="position: absolute;">
		            <div class="d-flex">
		               <div class="toast-body">
		                  옵션이 추가되었습니다.
		               </div>
		            </div>
		         </div>
		     </div>	
		     
			<table id="table" class="option table table-sm">
				<thead class="thead thead-light">
					<tr>
						<th class="th"><span>옵션 등록</span></th>
						<td id="option">
							<input type="file" class="infoData optionimg mr-2" id="optionimg" name="optionimg" placeholder="옵션이미지" />
							<input type="text" name="optionname" id="optionname" class="infoData mr-2"  placeholder="옵션명" /><span class="error">옵션명은 필수입력 사항입니다.</span><br>
							<div id="colorSelect" class="mt-2">
								<input type="text" name="colorCode"  id="colorCode1" class="code infoData mr-2"   placeholder="컬러코드(선택 시 자동입력)" />	
								<div id="circlecolor"><div id="color" class="color1" value="#674636"></div></div>
								<div id="circlecolor"><div id="color" class="color2" value="#ECDFCC"></div></div>
								<div id="circlecolor"><div id="color" class="color3" value="#FCDE70"></div></div>
								<div id="circlecolor"><div id="color" class="color4" value="#F7B5CA"></div></div>
								<div id="circlecolor"><div id="color" class="color5" value="#9ABF80"></div></div>
								<div id="circlecolor"><div id="color" class="color6" value="#1F509A"></div></div>
								<div id="circlecolor"><div id="color" class="color7" value="#C4D7FF"></div></div>
								<div id="circlecolor"><div id="color" class="color8" value="#F5F7F8"></div></div>
								<div id="circlecolor"><div id="color" class="color9" value="#DDDDDD"></div></div>
								<div id="circlecolor"><div id="color" class="color10" value="#31363F"></div></div>
								<input type="color" id="color" style="width:30px;">
							</div>
						</td>
					</tr>	
				</thead>
			</table>

			<br>
			<br>
			<h6>&nbsp;&nbsp;이미지 추가하기</h6>
		     <div id="productupdate">
	         <button type="button" class="AddImgBtn btn btn-light mb-2"><p>이미지추가</p></button>
	         <button type="button" class="RemoveImgBtn btn btn-danger mb-2"><p>- 선택이미지삭제</p></button>
	      	 </div>

		     <div style="position: relative;">
		         <div class="toast align-items-center" id="addImgToast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="1000" style="position: absolute;">
		            <div class="d-flex">
		               <div class="toast-body">
		                  이미지가 추가되었습니다.
		               </div>
		            </div>
		         </div>
		     </div>
		      
		     <table class="ImageTable table table-sm mt-2">
		         <thead class="thead-light">
		            <tr>
		               <th class="th"><span>이미지 1 *</span></th>
		               <td>
		                  <label for="img1"><img class="preview"/></label>
		                  <div>
		                     <label>이미지 *</label><span class="error">이미지는 필수입력 사항입니다.</span>
		                     <input id="img1" class="img_file infoData" name="img1" type="file" accept=".bmp, .jpg, .jpeg, .png, .webp"/>
		
		                     <label>제목 *</label> <span class="error">제목은 필수입력 사항입니다.</span>
		                     <input class="infoData description_title" class="required"  name="description_title1" type="text" maxlength="100"/>
		
		                     <label>설명 *</label> <span class="error">설명은 필수입력 사항입니다.</span>
		                     <textarea class="infoData description_detail required" name="description_detail1" class="required" rows="6" cols="70" maxlength="1000"></textarea>
		                     
		                  </div>
		               </td>
		            </tr>
		         </thead>
		      </table>

			  <div id="regibutton">
					<button type="button" class="ragibtn btn btn-success">상품등록</button>
					<button type="reset" class="btn btn-light">초기화</button>
			  </div>
		</div>
	</form>

</div>
</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  