<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../adminHeader.jsp" />	

<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/collection/adminCollection.css">
<!-- 직접 만든 js -->
<script type="text/javascript">
document.title="관리자 컬렉션관리";
const ctxPath = "${pageContext.request.contextPath}";
$(document).ready(function() {
	$(".nav-link.COLLECTION").addClass("active");
});
</script>
<script src="${pageContext.request.contextPath}/js/admin/adminCollection.js"></script>
		
	<div>
		<div id="contents">
			<!-- 사이드 메뉴 -->
			<div id="sidemenu" class="bg-light p-3">
				<i class="fa-solid fa-layer-group fa-3x d-flex justify-content-center mt-4"></i>
				<h3>Collection</h3>
				<div id="menulist">
					<ul>
						<li><a class="dropdown-item mb-3" href="#">Collection</a>
							<ul>
								<li><a class="selected">전체컬렉션관리</a></li>
								<li><a href="${pageContext.request.contextPath}/admin/collectionRegister.gu">컬렉션등록</a></li>
							</ul>
						</li>
					</ul>
				</div>
			</div>

			<!-- 본문 -->
			<div id="article" class="m-3 p-3">

					<p>COLLECTION > 전체 컬렉션 관리</p>

					<!-- 검색기능 -->
					<div id="searchContainer">
						<h4 class="bold">전체 컬렉션관리</h4>
						<hr style="border: 1px solid black; opacity:20%">
						<br>


						<h6>&nbsp;&nbsp;컬렉션검색</h6>
						
						<form name="searchCollectionFrm">
						<table class="table table-sm">
							<thead class="thead-light">
								<tr>
									<th class="th"><span>검색어</span></th>
									<td>
										<select name="searchType">
											<option value="name">컬렉션명</option>
											<option value="collectionno">컬렉션코드</option>
										</select>
										<input type="text" name="searchWord"/>
									</td>
								</tr>
								<tr>
									<th class="th"><span>기간검색</span></th>
									<td>
										<input type="date" placeholder="달력추가" name="startDate"/>&nbsp;-
										<input type="date" placeholder="달력추가" name="endDate"/>
										<button type="button" class="datebtn btn btn-light">오늘</button>
										<button type="button" class="datebtn btn btn-light">어제</button>
										<button type="button" class="datebtn btn btn-light">일주일</button>
										<button type="button" class="datebtn btn btn-light">1개월</button>
										<button type="button" class="datebtn btn btn-light">3개월</button>
										<button type="button" class="datebtn btn btn-light">전체</button>
									</td>
								</tr>
							</thead>
						</table>

						<div id="searchbutton">
							<button type="button" id="searchCollectionBtn" class="btn btn-light" onclick="searchCollection('${pageContext.request.contextPath}', 1)">검색</button>
							<button type="reset" class="btn btn-light">초기화</button>
						</div>
						<input type='hidden' name='currentShowPageNo' value='1'/>
						</form>

						<br>

						<h6>&nbsp;&nbsp;포함상품검색</h6>
						
						<form name="searchByProductFrm">
						<div>
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
											<button type="button" class="datebtn btn btn-light">1개월</button>
											<button type="button" class="datebtn btn btn-light">3개월</button>
											<button type="button" class="datebtn btn btn-light">전체</button>
										</td>
									</tr>
									
									<tr>
										<th class="th"><span>재고</span></th>
										<td><input type="text" name="startCnt" style="width:100px;" /><span>개 이상~&nbsp;</span><input type="text" name="endCnt" style="width:100px;"/><span>개 이하</span></td>
										<th class="th"><span>상품가격</span></th>
										<td><input type="text" name="startPrice" style="width:100px;" /><span>원 이상~&nbsp;</span><input type="text" name="endPrice" style="width:100px;"/><span>원 이하</span></td>
									</tr>
									<tr>
										<th class="th"><span>색상</span></th>
										<td colspan="3" style="display: flex;">
											<c:forEach var="color" items="${requestScope.colorList}" varStatus="status">
												<div style="border: solid 2px #eee; border-radius: 100%; display: inline-blick; margin: 0 4px;">
													<input type="checkbox" name="color" id="color${status.index}" value="${color}" style="display: none;"/>
													<label for="color${status.index}" style=" display: block; background-color: ${color}; width:20px; height:20px; margin: 4px; border-radius: 100%;"></label>
												</div>
											</c:forEach>
										</td>
									</tr>
								</thead>
							</table>

							<div id="searchbutton">
								<button type="button" id="searchByProductBtn" class="btn btn-light" onclick="searchByProduct('${pageContext.request.contextPath}', 1)">검색</button>
								<button type="reset" class="btn btn-light">초기화</button>
							</div>
						</div>
						<input type='hidden' name='currentShowPageNo' value='1'/>
						</form>
					</div>

					<hr style="border: solid 1px black; opacity:20%">

					<!-- 조회목록 -->
					<div class="p-3">
						<div id="top">
							<span id="totalCnt">전체 : 0건 조회</span>
						</div>
						
						<form name="collectionListFrm">
						<div>
							<div id="productupdate">
								<button type="button" class="btn btn-danger" id="deleteCollection"><span>- 컬렉션삭제</span></button>
							</div>
							<table class="table table-sm mt-3">
								<thead class="thead-light">
								  <tr>
									<th><input type="checkbox" class="check-all-item" /></th>
									<th>번호</th>
									<th>미리보기 이미지</th>
									<th>컬렉션코드</th>
									<th>컬렉션명</th>
									<th>등록일</th>
									<th>포함상품</th>
									<th>컬렉션수정</th>
								  </tr>
								</thead>
								<tbody id="searchResult">
									<tr class="table-empty"><th colspan="10" style="text-align: center;">등록된 컬렉션이 없습니다.</th></tr>
								</tbody>
							</table>
						</div>
						</form>
						
                        <div id="page" class="justify-content-center">
                            <ul class="pagination pagination-sm justify-content-center" style="margin:20px 0">
                              </ul>
                        </div>
					</div>
	
			</div> <!-- end article -->
		</div>
	</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  