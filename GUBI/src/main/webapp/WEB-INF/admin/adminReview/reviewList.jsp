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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/review/reviewList.css">
<!--  직접 만든 js -->
<script type="text/javascript" src="<%= ctx_Path%>/js/admin/reviewList.js"></script>

<script type="text/javascript">

$(document).ready(function(){

	document.title="관리자 리뷰목록";
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
         ,maxDate: "D" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)
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

	$("select[name='searchType']").val("${requestScope.searchType}").prop("selected",true);
	$("input:text[name='searchWord']").val("${requestScope.searchWord}");
	
	$("input:text[name='startDate']").val("${requestScope.startDate}");
	$("input:text[name='endDate']").val("${requestScope.endDate}");
	
	
	$("input:text[name='searchWord']").bind("keydown", function(e){		
		if(e.keyCode == 13) {
			goSearchMember();
		}
	});
	
});



function reviewSearch() {
	const frm = document.reviewFrm;
    frm.action = "review.gu";
    frm.method = "get";
    frm.submit();
}


function reviewDetail() {
	
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
							<li><a href="#">전체상품관리</a></li>
							<li><a href="<%= ctx_Path%>/admin/addProduct.gu">상품등록</a></li>
						</ul>
					<li><a class="dropdown-item mb-3" href="#">Review / Question</a></li>
						<ul>	
							<li><a href="<%= ctx_Path%>/admin/ask.gu">문의관리</a></li>
							<li><a href="<%= ctx_Path%>/admin/review.gu" style="color:black">리뷰관리</a></li>
						</ul>
				</ul>
			</div>
		</div>

	<!-- 본문 -->
	<div id="article" class="m-3 p-3">

	<p>Product > 리뷰관리</p>
	
	<form name="reviewFrm">
		<!-- 검색기능 -->
		<div id="membersearch">
			<h4 class="bold">전체 리뷰목록</h4>
			<hr style="border: 1px solid black; opacity:20%">
			<br>
			<h6>&nbsp;&nbsp;기본검색</h6>
			
			<table class="table table-sm">
				<thead class="thead-light">
					<tr>
						<th class="th"><span>검색어</span></th>
						<td>
							<select name="searchType">
								<option value="" disabled selected>검색어</option>
								<option value="name">작성자명</option>
								<option value="userid">아이디</option>
							</select>
							<input type="text" name="searchWord" />
						</td>
					</tr>
					<tr>
						<th class="th"><span>기간검색</span></th>
						<td>
							<input type="text" class="mr-1" name="startDate" id="datepicker1" />
								<input type="text" class="mr-1" name="endDate" id="datepicker2" />
							<button type="button" class="datebtn btn btn-light">오늘</button>
							<button type="button" class="datebtn btn btn-light">어제</button>
							<button type="button" class="datebtn btn btn-light">일주일</button>
							<button type="button" class="datebtn btn btn-light">1개월</button>
							<button type="button" class="datebtn btn btn-light">3개월</button>
						</td>
					</tr>
				</thead>
			</table>

			<div id="searchbutton">
				<button type="button" class="btn btn-light" onclick="reviewSearch()">검색</button>
				<button type="reset" class="btn btn-light">초기화</button>
			</div>
		</div>
	</form>

	<hr style="border: solid 1px black; opacity:20%">

			<!-- 조회목록 -->
			<div id="memberlist" class="p-3">
				<div id="top">
					<span>전체 : ${requestScope.reviewCnt}건 조회</span>
				</div>
				
				<div>
					<table class="table table-sm mt-3">
						<thead class="thead-light">
						  <tr> 					
							<th>no</th>
							<th>리뷰번호</th>
							<th>상품명</th>
							<th>리뷰제목</th>
							<th>리뷰내용</th>
							<th>작성자명</th>
							<th>아이디</th>
							<th>리뷰등록일</th>
							<th>별점</th>
						  </tr>
						</thead>
						<c:if test="${not empty requestScope.reviewList}">
							<c:forEach var="rvo" items="${requestScope.reviewList}" varStatus="status">
								<tbody>
								  <tr id="reviewDetailTr" data-id="${rvo.reviewno}" data-toggle="modal" data-target="#detailReview${rvo.reviewno}"/>
				    				<fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
									<td>${(requestScope.reviewCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
									<td class="reviewno">${rvo.reviewno}</td>
									<td>${rvo.pvo.name}</td>
									<td>${rvo.title}</td>
									<td>${rvo.content}</td>
									<td>${rvo.mvo.name}</td>
									<td>${rvo.fk_userid}</td>
									<td>${rvo.registerday}</td>
									<td>${rvo.score}</td>
								  </tr>
								</tbody>
								
								<%-- ****** 회원상세보기 모달 시작 ****** --%>
								<div class="modal fade" id="detailReview${rvo.reviewno}" data-backdrop="static"> 
									<div class="modal-dialog modal-dialog-centered">
										
										  <div class="modal-content">
											
											<!-- Modal header -->
											<div class="modal-header">
											  <span class="modal-title" style="font-weight: bold;"></span><span>${rvo.mvo.name} 님의 상세리뷰</span>
											  <button type="button" class="close" data-dismiss="modal">&times;</button>
											  
											</div>
											
											<!-- 리뷰이미지가 없을 경우 -->
											<c:if test="${empty rvo.img}">
												<!-- Modal body -->
												<div class="modal-body">
													<div class="pname">${rvo.pvo.name}
													<span>
													<fmt:parseNumber var="score" value="${rvo.score}"></fmt:parseNumber>
														<c:forEach  var="i" begin="1" end="5">
															<c:choose>
																<c:when test="${i <= score}">
																	<i class="fa-solid fa-star" style="color: #FFD700;"></i>
																</c:when>
																<c:otherwise>
																	<i class="fa-solid fa-star" style="color: #E0E0E0;"></i>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</span>
													</div>
													<div class="regiday">작성일 ${rvo.registerday}</div>
													<div class="content">${rvo.content}</div>
													<div></div>
												</div>
											</c:if>
											
											<!-- 리뷰이미지가 존재할 경우 -->
											<c:if test="${not empty rvo.img}">
												<!-- Modal body -->
												<div class="modal-body">
													
													<div class="pname">Product ${rvo.pvo.name} / 옵션작성
													<span>
													<fmt:parseNumber var="score" value="${rvo.score}"></fmt:parseNumber>
														<c:forEach  var="i" begin="1" end="5">
															<c:choose>
																<c:when test="${i <= score}">
																	<i class="fa-solid fa-star" style="color: #FFD700;"></i>
																</c:when>
																<c:otherwise>
																	<i class="fa-solid fa-star" style="color: #E0E0E0;"></i>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</span>
													</div>
													<div class="img">
														<img src="<%= ctx_Path%>/data/images/${rvo.img}"/>
													</div>
													
													<hr style="border: 1px solid black; opacity:20%">
													
													<div class="regiday">작성일 ${rvo.registerday}</div>
													<div class="content">${rvo.content}</div>
												</div>
											</c:if>
											
											<!-- Modal footer -->
											<div class="modal-footer">
											  <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
											</div>
										  </div>
										</div>
								 </div>
								 <%-- ****** 회원상세보기 모달 끝 ****** --%>	
							</c:forEach>
						</c:if>
						
						<c:if test="${empty requestScope.reviewList}">
							<tr><td>등록된 리뷰가 없습니다...</td></tr>
						</c:if>
						
					</table>
				</div>
			</div>
		<c:if test="${not empty requestScope.reviewList}">
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
</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  