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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/product/adminProduct.css">
<!--  직접 만든 js -->
<script type="text/javascript" src="<%= ctx_Path%>/js/admin/adminProduct.js"></script>

<script type="text/javascript">

$(document).ready(function(){

	document.title="관리자 상품목록";
	$(".nav-link.PRODUCT").addClass("active"); // 메뉴엑티브
	
	$.datepicker.setDefaults({
		 dateFormat: 'yy-mm-dd' //Input Display Format 변경
         ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
         ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
         ,changeYear: true //콤보박스에서 년 선택 가능
         ,changeMonth: true //콤보박스에서 월 선택 가능                
         ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
         ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
         ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
         ,buttonText: "선택" // 버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
         ,yearSuffix: "년" // 달력의 년도 부분 뒤에 붙는 텍스트
         ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
         ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
         ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
         ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
         ,minDate: "-1Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
         ,maxDate: "+1D" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)
	  });
	
   $(function() {
       $("#datepicker1, #datepicker2").datepicker({
           dateFormat: 'yymmdd'
       });
   });
	
   $("input#datepicker1").bind("keyup", e => {
       $(e.target).val("").next().show();
   }); // 생년월일에 키보드로 입력하는 경우 

   $("input#datepicker1").bind("change", e => {
       if ($(e.target).val() != "") {
           $(e.target).next().hide();
       }
   }); // 생년월일에 마우스로 값을 변경하는 경우
   $("input#datepicker2").bind("keyup", e => {
       $(e.target).val("").next().show();
   }); // 생년월일에 키보드로 입력하는 경우 

   $("input#datepicker2").bind("change", e => {
       if ($(e.target).val() != "") {
           $(e.target).next().hide();
       }
   }); // 생년월일에 마우스로 값을 변경하는 경우
	
//    $("select[name='searchType']").bind("change", e => {
//        if ($(e.target).val("${requestScope.searchType}").prop("selected",true)) {
//        }
//    }); // 생년월일에 마우스로 값을 변경하는 경우

    $("select[name='searchType']").val("${requestScope.searchType}");
	// 검색어
	$("select[name='searchType']").val("${requestScope.searchType}");
	$("input:text[name='searchWord']").val("${requestScope.searchWord}");
	// 카테고리 
	$("select[name='major_category']").val("${requestScope.major_category}");
	selectSmallCategory();
	$("select[name='small_category']").val("${requestScope.small_category}");
	// 기간검색
	$("input:text[name='startDate']").val("${requestScope.startDate}");
	$("input:text[name='endDate']").val("${requestScope.endDate}");
	// 상품가격
	$("input:text[name='startprice']").val("${requestScope.startprice}");
	$("input:text[name='endprice']").val("${requestScope.endprice}");	
	
	$("input:radio[value='${requestScope.is_delete}']").prop("checked", true);
});

function deleteProduct() {
	
	if($("input:checkbox[name='updateCheck']:checked").length == 0) {
		alert("삭제할 상품을 선택하세요");
		return;
	}
	
	$("input:checkbox[name='updateCheck']:checked").each((index, elmt) => {
		$(document.deleteFrm).append("<input type='hidden' name='productno' value='"+ $(elmt).val() +"'/>") // product 를 넘김
		$(document.deleteFrm).append("<input type='hidden' name='is_delete' value='0'/>") // is_delete 값을 0으로 넘김
	});

 	const frm = document.deleteFrm;
    frm.action = "product.gu";
    frm.method = "post";
    frm.submit();
}


function recoverProduct() {
	
	if($("input:checkbox[name='updateCheck']:checked").length == 0) {
		alert("재등록할 상품을 선택하세요");
		return;
	}
	
	$("input:checkbox[name='updateCheck']:checked").each((index, elmt) => {
		$(document.recoverFrm).append("<input type='hidden' name='productno' value='"+ $(elmt).val() +"'/>") // product 를 넘김
		$(document.recoverFrm).append("<input type='hidden' name='is_delete' value='1'/>") // is_delete 값을 1으로 넘김
	});

	const frm = document.recoverFrm;
				frm.action = "product.gu";
			    frm.method = "post";
			    frm.submit();
}

// 전체선택하기 / 전체해제하기
function onecheck() { // this.checked 는 해당요소가 체크 되었는지를 의미 (boolean 타입)
						  // true 면 check 상태, false 면 해제된 상태
	const boxlist = document.querySelectorAll("input[name='updateCheck']"); // 전체 체크박스 
	const checkedboxlist = document.querySelectorAll("input[name='updateCheck']:checked"); // 선택 체크박스 
	const selectAll = document.querySelector("input[name='allcheckbox']"); // 전체체크박스

	if(boxlist.length === checkedboxlist.length) {
		selectAll.checked = true;
	}
	else {
		selectAll.checked = false;
	}
}

function allCheck(selectAll) {

	const checkboxes = document.getElementsByName('updateCheck');

	checkboxes.forEach((checkbox) => {
		checkbox.checked = selectAll.checked
	});
}
</script>
</head>

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
							<li><a href="#" style="color:black">전체상품관리</a></li>
							<li><a href="<%= ctx_Path%>/admin/addProduct.gu">상품등록</a></li>
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

	<p>Product > 전체상품관리</p>
	
	<form name="productFrm">
		<!-- 검색기능 -->
		<div id="membersearch">
			<h4 class="bold">전체 상품관리</h4>
			<hr style="border: 1px solid black; opacity:20%">
			<br>
			<h6>&nbsp;&nbsp;기본검색</h6>
			
			<table class="table table-sm">
				<thead class="thead-light">
					<tr>
						<th class="th">검색어</th>
						<td>
							<select name="searchType">
								<option value="" disabled selected>검색어</option>
								<option value="name">상품명</option>
								<option value="productno">상품코드</option>
							</select>
							<input type="text" name="searchWord" />
						</td>
					</tr>
					<tr>
						<th class="th">카테고리</th>
						<td>
							<select name="major_category" id="major_category">
								<option value="" disabled selected>대분류</option>
								<c:forEach var="major" items="${requestScope.selectMajorCategory}">
									<option value="${major.major_category}">${major.major_category}</option>
								</c:forEach>
							</select>
							<select name="small_category" id="small_category">
								<option value="" disabled selected>소분류</option>
								
							</select>
						</td>
					</tr>
					<tr>
						<th class="th">기간검색</th>
						<td>
							<input type="text" class="mr-1" name="startDate" id="datepicker1" readonly />
							<input type="text" class="mr-1" name="endDate" id="datepicker2" readonly />
							<button type="button" class="datebtn btn btn-light">오늘</button>
							<button type="button" class="datebtn btn btn-light">어제</button>
							<button type="button" class="datebtn btn btn-light">일주일</button>
							<button type="button" class="datebtn btn btn-light">1개월</button>
							<button type="button" class="datebtn btn btn-light">3개월</button>
						</td>
					</tr>
					<tr>
						<th class="th">판매여부</th>
						<td id="radio">
							<label><span class="mr-1">전체</span><input type="radio" value="" name="is_delete" id="t" class="mr-3"/></li></label>
							<label><span class="mr-1">판매중</span><input type="radio" value="0" name="is_delete" id="t" class="mr-3"/></li></label>
							<label><span class="mr-1">품절</span><input type="radio" value="1" name="is_delete" id="t" class="mr-3"/></li></label>
						</td>	
					</tr>
					<tr>
						<th class="th">상품가격</th>
						<td><input type="text" name="startprice" style="width:100px;" /><span>&nbsp;원 이상~&nbsp;</span><input type="text" name="endprice" style="width:100px;"/><span>&nbsp;원 이하</span></td>
					</tr>
				</thead>
			</table>

			<div id="searchbutton">
				<button type="button" class="btn btn-light" onclick="productSearch()">검색</button>
				<button type="reset" class="btn btn-light">초기화</button>
			</div>
		</div>
	</form>

	<hr style="border: solid 1px black; opacity:20%">

			<!-- 조회목록 -->
			<div id="memberlist" class="p-3">
				<div id="top">
					<span>전체 : ${requestScope.productCnt}건 조회</span>
				</div>
				
				<div>
				<c:if test="${requestScope.is_delete == '0'}">
					<div id="productupdate">
						<button type="button" class="btn btn-danger" onclick="deleteProduct()"><span>- 상품삭제</span></button>
					</div>
				</c:if>
				<c:if test="${requestScope.is_delete == '1'}">
					<div id="productupdate">
						<button type="button" class="btn btn-success" onclick="recoverProduct()"><span>+ 상품재등록</span></button>
					</div>
				</c:if>
					
					<table class="table tabletr table-sm mt-3">
						<thead class="thead-light">
						  <tr> 	
							<c:if test="${requestScope.is_delete eq '0' or requestScope.is_delete eq '1'}">
								<th><input type="checkbox" name="allcheckbox" onclick="allCheck(this)"/></th>
							</c:if>	
							<th>no</th>
							<th>상품번호</th>
							<th>이미지</th>
							<th>카테고리번호</th>
							<th>카테고리</th>
							<th>상품명</th>
							<th>등록일</th>
							<th>재고</th>
							<th>판매여부</th>
							<th>가격</th>
							<th>배송비</th>
						  </tr>
						</thead>
						<c:if test="${not empty requestScope.selectProduct}">
							<c:forEach var="pvo" items="${requestScope.selectProduct}" varStatus="status">
								<tbody>
								  <tr id="tr" data-id="${pvo.productno}" data-toggle="modal" data-target="#detailProduct${pvo.productno}">
								  	<fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}"></fmt:parseNumber>
				    				<fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
				    				<c:if test="${requestScope.is_delete eq '0' or requestScope.is_delete eq '1'}">
										<th><input type="checkbox" name="updateCheck" value="${pvo.productno}" onclick="onecheck()"/></th>
									</c:if>
									<td>${(requestScope.productCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
									<td name="productno">${pvo.productno}</td>
									<td><img src="<%= ctx_Path%>/data/images/${pvo.thumbnail_img}" style="width:30px;"/></td>
									<td>${pvo.fk_categoryno}</td>
									<td>${pvo.categoryVO.major_category} \ ${pvo.categoryVO.small_category}</td>
									<td>${pvo.name}</th>
									<td>${pvo.registerday}</th>
									<td>${pvo.cnt}</td>
									<td>${pvo.is_delete}</td>
									<td><fmt:formatNumber value="${pvo.price}" pattern="#,###"/></td>
									<td><fmt:formatNumber value="${pvo.delivery_price}" pattern="#,###"/></td>
								  </tr>
								</tbody>
								<!-- modal 만들기 -->
								<div class="modal fade" id="detailProduct${pvo.productno}" data-backdrop="static">
									<div class="modal-dialog modal-dialog-centered modal-lg">
									  <div class="modal-content">
										<!-- Modal header -->
										<div class="modal-header">
										  <h5 class="modal-title">상품 상세보기</h5>
										  <button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<!-- Modal body -->
										<div class="modal-body">
													
										</div>
										<!-- Modal footer -->
										<div class="modal-footer">
										  <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
										</div>
									  </div>
									</div>
								</div>
								<!-- modal 만들기 -->
							</c:forEach>
						</c:if>
						<c:if test="${empty requestScope.selectProduct}">
							<tr><td>등록된 상품이 없습니다...</td></tr>
						</c:if>
					</table>
				</div>
			</div>
		<c:if test="${not empty requestScope.selectProduct}">
			<div id="pageBar">
		       <nav>
		          <ul class="pagination">${requestScope.pageBar}</ul>
		       </nav>
	    	</div>
    	</c:if>
		</div> <!-- end article -->
	</div>
</div>


	<form name="deleteFrm">
		
	</form>	
	<form name="recoverFrm">
		
	</form>	
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  