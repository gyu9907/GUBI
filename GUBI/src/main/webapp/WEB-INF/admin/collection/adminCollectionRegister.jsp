<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> 
<title>관리자 컬렉션등록</title>

<!-- jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<!-- jQueryUI CSS 및 JS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script> 
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/css/bootstrap.min.css" type="text/css">
<!-- Bootstrap js -->
<script src="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" type="text/javascript"></script>
<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<!-- Quill 텍스트 에디터 stylesheet -->
<link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet" />
<!-- Quill 텍스트 에디터 library -->
<script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>
<script src="https://cdn.jsdelivr.net/npm/quill-resize-module@2.0.4/dist/resize.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/quill-resize-module@2.0.4/dist/resize.min.css" rel="stylesheet">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/collection/adminCollectionRegister.css">
<!-- 직접 만든 js -->
<script type="text/javascript">
const ctxPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/admin/adminCollectionRegister.js"></script>

</head>

<body>
	<%-- ****** 회원상세보기 모달 시작 ****** --%>
	<div class="modal fade" id="addProductModal" data-backdrop="static"> 
		<div class="modal-dialog modal-dialog-centered AddProductModal">
			<div class="modal-content">
			
				<!-- Modal header -->
				<div class="modal-header">
					<span class="modal-title" style="font-weight: bold;">상품추가</span>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				
				<!-- Modal body -->
				<div class="modal-body">
					<%-- 상품 검색기능 --%>
					<form name="productSearchFrm">
						<div>
							<h6>상품검색</h6>
							
							<table class="table table-sm">
								<thead class="thead-light">
									<tr>
										<th class="th"><span>검색어</span></th>
										<td colspan="3">
											<select name="searchType">
												<option value="name">상품명</option>
												<option value="productno">상품코드</option>
											</select>
											<input type="text" name="searchWord"/>
										</td>
									</tr>
									<tr>
										<th class="th"><span>카테고리</span></th>
										<td colspan="3">
											<select name="major_category" id="major_category">
												<option value="" selected>Major Category</option>
												<c:forEach var="major" items="${requestScope.selectMajorCategory}">
												<option value="${major.major_category}">${major.major_category}</option>
												</c:forEach>
											</select>
											<select name="small_category" id="small_category">
												<option value="" selected>Small Category</option>
											</select>
										</td>
									</tr>
									<tr>
										<th class="th"><span>기간검색</span></th>
										<td colspan="3">
											<input type="date" placeholder="달력추가" name="startDate"/>&nbsp;-
											<input type="date" placeholder="달력추가" name="endDate"/>
											<button type="button" class="datebtn btn btn-light">오늘</button>
											<button type="button" class="datebtn btn btn-light">어제</button>
											<button type="button" class="datebtn btn btn-light">일주일</button>
											<button type="button" class="datebtn btn btn-light">지난달</button>
											<button type="button" class="datebtn btn btn-light">1개월</button>
											<button type="button" class="datebtn btn btn-light">3개월</button>
											<button type="button" class="datebtn btn btn-light">전체</button>
										</td>
									</tr>
									<tr>
										<th class="th"><span>정렬</span></th>
										<td id="radio">
											<label><span class="mr-1">최신순</span><input type="radio" value="latest" name="sortby" id="t" class="mr-3" checked/></li></label>
											<label><span class="mr-1">인기순</span><input type="radio" value="bestItem" name="sortby" id="t" class="mr-3"/></li></label>
										</td>
										<th class="th"><span>재고</span></th>
										<td><input type="text" name="startCnt" style="width:100px;" /><span>개 이상~&nbsp;</span><input type="text" name="endCnt" style="width:100px;"/><span>개 이하</span></td>
									</tr>
									<tr>
										<th class="th"><span>색상</span></th>
										<td style="display: flex;">
											<c:forEach var="color" items="${requestScope.colorList}" varStatus="status">
												<div style="border: solid 2px #eee; border-radius: 100%; display: inline-blick; margin: 0 4px;">
													<input type="checkbox" name="color" id="color${status.index}" value="${color}" style="display: none;"/>
													<label for="color${status.index}" style=" display: block; background-color: ${color}; width:20px; height:20px; margin: 4px; border-radius: 100%;"></label>
												</div>
											</c:forEach>
										</td>
										<th class="th"><span>상품가격</span></th>
										<td><input type="text" name="startPrice" style="width:100px;" /><span>원 이상~&nbsp;</span><input type="text" name="endPrice" style="width:100px;"/><span>원 이하</span></td>
									</tr>
								</thead>
							</table>

							<div id="searchbutton">
								<button id="searchBtn" type="button" class="btn btn-light" onclick="searchProduct('${pageContext.request.contextPath}', 1)">검색</button>
								<button type="reset" class="btn btn-light">초기화</button>
							</div>
						</div>
						<input type='hidden' name='currentShowPageNo' value='1'/>
					</form>

					<hr style="border: solid 1px black; opacity:20%">

					<!-- 조회목록 -->
					<div class="p-3">
						<div id="top" class="mb-2">
							<span id="totalCnt">전체 : 0건 조회</span>
						</div>
						
						<div>
							<div id="productupdate">
								<button type="button" class="btn btn-light add-product-btn"><span>선택상품추가</span></button>
							</div>

							<div style="position: relative;">
								<div class="toast align-items-center" id="addProductToast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="2000" style="position: absolute; display: none;">
									<div class="d-flex">
										<div class="toast-body">
											선택한 상품이 추가되었습니다.
										</div>
									</div>
								</div>
							</div>

							<table class="table table-sm mt-2">
								<thead class="thead-light">
								<tr>
									<th><input type="checkbox" class="check-all-item" /></th>
									<th>번호</th>
									<th>이미지</th>
									<th>상품코드</th>
									<th>카테고리</th>
									<th>상품명</th>
									<th>등록일</th>
									<th>재고</th>
									<th>판매여부</th>
									<th>가격</th>
								</tr>
								</thead>
								<tbody id="searchProductResult">
									<tr class="table-empty"><th colspan="10" style="text-align: center;">검색된 상품이 없습니다.</th></tr>
								</tbody>
							</table>
						</div>
						
                        <div id="page" class="justify-content-center">
                            <ul class="pagination pagination-sm justify-content-center" style="margin:20px 0">
                              </ul>
                        </div>
					</div>
				</div>
				
				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-light add-product-btn" id="" data-dismiss="modal">선택상품추가 후 닫기</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">닫기</button>
				</div>

			</div>
		</div>
	</div>
	<%-- ****** 회원상세보기 모달 끝 ****** --%>

	<div>
		<%-- 관리자메뉴--%>
		<div id="header">   
			<img id="logo" src="${pageContext.request.contextPath}/images/ui/logo/gubi_logo.png"/>  
		</div>
		<div id="adminmenu">
			<ul class="nav nav-tabs">
				<li class="nav-item">
					<a class="nav-link" data-toggle="tab" href="#home">HOME</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" data-toggle="tab" href="#menu0">MEMBER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" data-toggle="tab" href="#menu1">ORDER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" data-toggle="tab" href="#menu2">CATEGORY</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" data-toggle="tab" href="#menu3">PRODUCT</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link active" data-toggle="tab" href="#menu4">COLLECTION</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" data-toggle="tab" href="#menu5">STATUS</a>
				</li>
			</ul>
		</div><%-- header--%>

		<div id="contents">
			<%-- 사이드 메뉴 --%>
			<div id="sidemenu" class="bg-light p-3">
				<i class="fa-solid fa-layer-group fa-3x d-flex justify-content-center mt-4"></i>
				<h3>Collection</h3>
				<div id="menulist">
					<ul>
						<li><a class="dropdown-item mb-3" href="#">Collection</a>
							<ul>
								<li><a href="${pageContext.request.contextPath}/admin/collection.gu">전체컬렉션관리</a></li>
								<li><a class="selected">컬렉션등록</a></li>
							</ul>
						</li>
					</ul>
				</div>
			</div>

			<%-- 본문 --%>
			<div id="article" class="m-3 p-3">
				<p>COLLECTION > 컬렉션 등록</p>

				<h4 class="bold">컬렉션 등록</h4>
				<hr style="border: 1px solid black; opacity:20%">
				<br>

				<form name="registerFrm">
					<h6 class="h6 mb-3 font-weight-bold">기본정보</h6>

					<table class="DefaultInfoTable table table-sm">
						<thead class="thead-light">
							<tr>
								<th class="th"><label for="name">컬렉션명 *</label></th>
								<td>
									<input id="name" name="name" class="required" type="text" maxlength="20"/>
									<span class="error">컬렉션명은 필수입력 사항입니다.</span>
								</td>
							</tr>
							<tr>
								<th class="th"><label for="designer">디자이너</label></th>
								<td>
									<input id="designer" name="designer" type="text" maxlength="20"/>
								</td>
							</tr>
							<tr>
								<th class="th"><label for="thumbnail">미리보기 이미지 *</label></th>
								<td>
									<label for="thumbnail_img"><img class="preview thumbnail_img" /></label><br>
									<input id="thumbnail_img" name="thumbnail_img" type="file" class="img_file required" accept=".jpeg, .png, .webp"/>
									<span class="error">미리보기 이미지는 필수입력 사항입니다.</span>
								</td>
							</tr>
							<tr>
								<th class="th"><label for="fullscreen_img">대표 이미지/영상 *</label></th>
								<td>
									<label for="fullscreen_img">
										<img class="preview fullscreen_img" />
										<video autoplay loop playsinline muted class="preview" style="display:none"></video>
									</label><br>
									<input id="fullscreen_img" name="fullscreen_img" class="required" type="file" accept=".jpeg, .png, .webp, .mp4"/>
									<span class="error">대표 이미지는 필수입력 사항입니다.</span>
								</td>
							</tr>
						</thead>
					</table>

					<br>

					<h6 class="h6 mb-3 font-weight-bold">상품정보</h6>
			
					<div id="productupdate">
						<button type="button" class="btn btn-light" data-toggle="modal" data-target="#addProductModal" onclick="javascript:searchProduct('${pageContext.request.contextPath}', 1)"><span>상품추가</span></button>
						<button type="button" class="btn btn-danger" id="removeProductBtn"><span>- 선택상품삭제</span></button>
					</div>

					<div style="position: relative;">
						<div class="toast align-items-center" id="deleteProductToast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="2000" style="position: absolute; display: none;">
							<div class="d-flex">
								<div class="toast-body">
									선택한 상품이 삭제되었습니다.
								</div>
							</div>
						</div>
					</div>
					
					<table class="table table-sm mt-2">
						<thead class="thead-light">
						  <tr>
							<th><input type="checkbox" class="check-all-item" /></th>
							<th>번호</th>
							<th>이미지</th>
							<th>상품코드</th>
							<th>카테고리</th>
							<th>상품명</th>
							<th>등록일</th>
							<th>재고</th>
							<th>판매여부</th>
							<th>가격</th>
						  </tr>
						</thead>
						<tbody id="collectionProductsTbody">
							<tr class="table-empty"><th colspan="10" style="text-align: center;">추가된 상품이 없습니다.</th></tr>
						</tbody>
					</table>
						<!-- <div class="CollectionRegisterProduct_container">
							<ul>
								<li class="my-2">
									<input type="hidden" name="fk_productno" value=""/>
									<div class="ProductImg_container mr-1"><img/></div>
									<div class="ProductInfo_container">
										<div class="ProductName mb-2">Bagdad Portable Lamp</div>
										<div class="ProductOption my-1">International Orange</div>
										<div class="ProductPrice">$399</div>
									</div>
									<button type="button" class="ProductDelete"><i class="fa-solid fa-xmark"></i></button>
								</li>
								<li class="my-2">
									<input type="hidden" name="fk_productno" value=""/>
									<div class="ProductImg_container mr-1"><img/></div>
									<div class="ProductInfo_container">
										<div class="ProductName mb-2">Bagdad Portable Lamp</div>
										<div class="ProductOption my-1">International Orange</div>
										<div class="ProductPrice">$399</div>
									</div>
									<button type="button" class="ProductDelete"><i class="fa-solid fa-xmark"></i></button>
								</li>
							</ul>
							<div align="center">
								<button type="button" class="AddButton">상품 추가</button>
							</div>
						</div> -->
			
						<h6 class="h6 mb-3 font-weight-bold">상세설명</h6>
						
						<ul class="nav nav-tabs mb-2" id="EditorNav">
							<li class="nav-item">
								<a class="nav-link active" data-toggle="tab" href="#ImageEditor">Image Editor</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" data-toggle="tab" href="#TextEditor">Text Editor</a>
							</li>
						</ul>

						<div class="tab-content">
						
							<div class="tab-pane show active" id="ImageEditor">
				
								<div id="productupdate">
									<button type="button" class="AddImgBtn btn btn-light"><span>이미지추가</span></button>
									<button type="button" class="RemoveImgBtn btn btn-danger"><span>- 선택이미지삭제</span></button>
								</div>
								
								<input type="hidden" name="detailCnt" value="1"/>
	
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
											<th class="th"><label>이미지 1 *</label></th>
											<td>
												<label for="img1"><img class="preview" /></label>
												<div>
													<label>이미지 *</label> <span class="error">이미지는 필수입력 사항입니다.</span>
													<input id="img1" name="img1" type="file" class="img_file required" accept=".bmp, .jpg, .jpeg, .png, .webp"/>
													
													<label>제목 *</label> <span class="error">제목은 필수입력 사항입니다.</span>
													<input class="description_title required" name="description_title1" class="required" type="text" maxlength="100"/>
													
													<label>설명 *</label> <span class="error">설명은 필수입력 사항입니다.</span>
													<textarea class="description_detail required" name="description_detail1" class="required" rows="6" cols="70" maxlength="1000"></textarea>
												</div>
											</td>
										</tr>
									</thead>
								</table>
							</div>
	
							<%-- Quill 텍스트 에디터 container --%>
							<div class="tab-pane" id="TextEditor">
								<div id="editor">
	
								</div>
							</div>
						</div>
			
						<div class="mt-5 mb-4" align="center">
							<button type="button" class="CollectionRegisterButton">컬렉션 등록</button>
						</div>
					</form>
	
			</div> <%-- end article --%>
		</div>
	</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  