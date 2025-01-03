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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/category/addCategory.css">

<script type="text/javascript" src="<%= ctx_Path%>/js/admin/CategoryAdd.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>

<script type="text/javascript">
$(document).ready(function(){
	
	let category_check = false;
	
	// 카테고리 중복검사하기 
    $("span#categorycheck").click(function(){
    	category_check = true;
        $.ajax({
            url:"categoryDuplicate.gu",
            data:{"smallCategory":$("input#small_category").val().trim()}, 
            type:"post",
            dataType:"json",
            success:function(json){
          
                if(json.isExists) {
                    //  입력한 smallCategory 가 이미 사용중이라면
                    $("span#categorycheckresult").html($("input#small_category").val() + " 은 이미 사용중 이므로 다른 카테고리명 입력하세요").css({"color":"red"});
                    $("input#small_category").val(""); 
                }
                else { //  입력한 smallCategory 가 존재하지 않는 경우라면
                    if($("input#small_category").val().trim() === "") {
                        $("span#categorycheckresult").html("공백은 등록 불가능 합니다").css({"color":"navy"});
                    } else {
                        $("span#categorycheckresult").html($("input#small_category").val() + " 은 등록가능 합니다").css({"color":"navy"});

                    }
                }
            },                   
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });
    });
	
    $("input#small_category").bind("change", function(){
    	category_check = false;
    });   
    // 카테고리 중복검사하기  끝 //
	
});
</script>
<body>

	
<div id="categoryAddForm">
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
				  <a class="nav-link" href="<%= ctx_Path%>/admin/member.gu">MEMBER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link"  href="<%= ctx_Path%>/admin/order.gu">ORDER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link active" href="<%= ctx_Path%>/admin/category.gu">CATEGORY</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="<%= ctx_Path%>/admin/product.gu">PRODUCT</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" href="#menu3">STATUS</a>
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

			<p>Category > 카테고리 등록</p>
			
			<form name="categoryFrm" enctype="multipart/form-data">
			<!-- 검색기능 -->
			<div id="categorysearch">
				<h4 class="bold">카테고리 관리</h4>
				<hr style="border: 1px solid black; opacity:20%">
				<br>
				<h6>&nbsp;&nbsp;카테고리 등록</h6>
				
				<table class="table table-sm">
					<thead class="thead-light">
						<tr>
							<th class="th">카테고리 소속</th> <!-- 대분류 의자,책상,조명 등 -->
							<td>
								<select name="major_category" id="category">
									<option value="" disabled selected>대분류</option>
									<option value="SEATING" class="cateoption" id="seating">SEATING</option>
									<option value="LIGHTING" class="cateoption" id="lighting">LIGHTING</option>
									<option value="TABLES" class="cateoption" id="tables">TABLES</option>
								</select>
							</td>
						</tr>
						<tr>
							<th class="th">카테고리명</th> <!-- 소분류 거실의자, 야외의자 등 -->
							<td>
								<input type="text" id="small_category" name="small_category" />
								<span class="btn btn-light" id="categorycheck">카테고리 중복검사</span>
								<span id="categorycheckresult"></span>
							</td>
						</tr>
						<tr>
							<th class="th">카테고리 이미지</th> <!-- 소분류 거실의자, 야외의자 등 -->
							<td>
								<input type="file" class="category_img" />
							</td>
						</tr>
						<tr>
							<th class="th">이미지 미리보기</th> <!-- 소분류 거실의자, 야외의자 등 -->
							<td>
								<img id="viewcategoryimg" class="mr-2" />
							</td>
						</tr>
					</thead>
				</table>

				<div id="searchbutton">
					<button  type="button" id="cateAdd" value="등록" class="btn btn-light">등록</button>
				</div>
			</div>
			</form>
			
				<hr style="border: solid 1px black; opacity:20%">

				<div id="categoryall">
				
					<div id="categorydelete" class="mb-2">
						<button type="button" name="deletemember" value="deletemember" class="btn btn-danger" onclick="deleteCategory()" style="height:30px; width:120px;"><p style="font-size:8pt;">- 카테고리삭제하기</p></button>
					</div>
					
					<table class="table">
						<thead class="bg-light">
							<th>등록된 카테고리</th>
						</thead>
						<c:if test="${not empty requestScope.categoryList}"> <!-- 카테고리가 있을 경우 -->
							<c:forEach var="catevo" items="${requestScope.categoryList}">
								<tbody>
									<tr>
										<td>
											<span><input type="checkbox" name="deleteCategoryCheck" class="mr-3" value="${catevo.categoryno}"/></span>
											<span>${catevo.categoryno}&nbsp;&nbsp;&nbsp;</span>
											<span>${catevo.major_category} / ${catevo.small_category}</span>
											<span class="ml-1">( 2 )</span>
										</td>
									</tr>
								</tbody>
							</c:forEach>
						</c:if>
					</table>
				</div><!-- categoryall -->
		</div> <!-- end article -->
	</div>
</div>


	<form name="deleteCategoryFrm">
		
	</form>
</body>
</html>