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
<title>관리자 주문관리</title>
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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/order/adminOrder.css">

<script type="text/javascript" src="<%= ctx_Path%>/js/admin/adminOrder.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	
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

	$("select[name='searchType']").val("${requestScope.searchType}");
	$("input:text[name='searchWord']").val("${requestScope.searchWord}");
	$("input:text[name='startDate']").val("${requestScope.startDate}");
	$("input:text[name='endDate']").val("${requestScope.endDate}");
	$("input:radio[value='${requestScope.orderStatus}']").prop("checked",true);
	
	
});

function searchOrder() {
	// alert("검색누름");
	const frm = document.orderSearchFrm;
    frm.action = "order.gu";
    //frm.method = "get";
    frm.submit();
}

</script>

</head>
<body>
	<div>
		<!-- 관리자메뉴-->
		<div id="header">   
			<a href="<%= ctx_Path%>/admin/admin.gu">
		 		<img id="logo" src="<%= ctx_Path%>/image/logo.png" onclick="<%= ctx_Path%>/admin/admin.gu"/>
		  	</a>
		</div>
		
		
		<!-- 관리자상단메뉴-->
		<div id="adminmenu">
			<ul class="nav nav-tabs">
				<li class="nav-item">
					<a class="nav-link" href="<%= ctx_Path%>/admin/admin.gu">HOME</a>
				  </li>
				<li class="nav-item">
				  <a class="nav-link" href="<%= ctx_Path%>/admin/member.gu">MEMBER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link active"  href="<%= ctx_Path%>/admin/order.gu">ORDER</a>
				</li>
				<li class="nav-item">
				  <a class="nav-link" href="<%= ctx_Path%>/admin/category.gu">CATEGORY</a>
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
				<i class="fa-solid fa-receipt fa-3x d-flex justify-content-center mt-4"></i>
				<h3>ORDER</h3>
				<div id="menulist">
					<ul>
						<li><a class="dropdown-item mb-3" href="#">Order</a></li>
						<ul>
							<li><a href="#">주문리스트 (전체)</a></li>
							
							<c:forEach var="status" items="${requestScope.statusCnt}">
								<c:if test="${status.status < 6}">
									<li id="statusmenu">
									<c:choose>
										<c:when test='${status.status == 1}'><a class="menu" href="<%= ctx_Path%>/admin/orderStatus.gu" data-value="${status.status}">결제대기</a></c:when>
										<c:when test='${status.status == 2}'><a class="menu"href="<%= ctx_Path%>/admin/orderStatus.gu"  data-value="${status.status}">주문완료</a></c:when>
										<c:when test='${status.status == 3}'><a class="menu"href="<%= ctx_Path%>/admin/orderStatus.gu"  data-value="${status.status}">배송중</a></c:when>
										<c:when test='${status.status == 4}'><a class="menu"href="<%= ctx_Path%>/admin/orderStatus.gu"  data-value="${status.status}">배송완료</a></c:when>
										<c:when test='${status.status == 5}'><a class="menu"href="<%= ctx_Path%>/admin/orderStatus.gu"  data-value="${status.status}">구매확정</a></c:when>
									</c:choose>
									<button type="button" value="1" class="btn btn-info btn-sm">
									<span>${status.statusCnt}</span></button></li>
								</c:if>
							</c:forEach>
						</ul>
						
						<li><a class="dropdown-item mb-3" href="#">Cancle</a></li>
						<ul>
							<c:forEach var="status" items="${requestScope.statusCnt}">
								<c:if test="${status.status > 5}">
									<li>
									<c:choose>
										<c:when test='${status.status == 6}'><a href="<%= ctx_Path%>/admin/orderStatus.gu" data-value="${status.status}">주문취소</a></c:when>
										<c:when test='${status.status == 7}'><a href="<%= ctx_Path%>/admin/orderStatus.gu" data-value="${status.status}">환불접수</a></c:when>
										<c:when test='${status.status == 8}'><a href="<%= ctx_Path%>/admin/orderStatus.gu" data-value="${status.status}">환불완료</a></c:when>
									</c:choose>
									<button type="button" value="1" class="btn btn-success btn-sm">
									<span>${status.statusCnt}</span></button></li>
								</c:if>
							</c:forEach>
						</ul>
					</ul>
				</div>
			</div>

			<!-- 본문 -->
			<div id="article" class="m-3 p-3">
				<p>Order > 주문리스트</p>
				
				<form name="orderSearchFrm">
					<!-- 검색기능 -->
					<div id="membersearch">
						<h4 class="bold">주문리스트(전체)</h4>
						<hr style="border: 1px solid black; opacity:20%">
						<br>
						<h6>&nbsp;&nbsp;기본검색</h6>
						
						<table class="table table-sm">
							<thead class="thead-light">
								<tr>
									<th class="th"><span>검색어</span></th>
									<td>
										<select name="searchType">
											<option value="orderno">주문번호</option>
											<option value="userid">회원아이디</option>
											<option value="name">주문자명</option>
											<option value="tel">핸드폰번호</option>
										</select>
										<input type="text" name="searchWord" />
									</td>
								</tr>
								<tr>
									<th class="th"><span>주문일시</span></th>
									<td>
										<input type="text" class="mr-1" name="startDate" id="datepicker1" size="10" />
										<input type="text" class="mr-1" name="endDate" id="datepicker2" size="10"/>
										<button type="button" class="datebtn btn btn-light">오늘</button>
										<button type="button" class="datebtn btn btn-light">어제</button>
										<button type="button" class="datebtn btn btn-light">일주일</button>
										<button type="button" class="datebtn btn btn-light">1개월</button>
										<button type="button" class="datebtn btn btn-light">3개월</button>
									</td>
								</tr>
								<tr>
									<th class="th"><span>주문상태</span></th>
									<td id="radio">
										<label><span class="mr-1">전체</span><input type="radio" value="" name="orderStatus"      class="mr-3" checked/></li></label>
										<label><span class="mr-1">결제대기</span><input type="radio" value="1" name="orderStatus"  class="mr-3"/></li></label>
										<label><span class="mr-1">주문완료</span><input type="radio" value="2" name="orderStatus"  class="mr-3"/></li></label>
										<label><span class="mr-1">주문취소</span><input type="radio" value="3" name="orderStatus"  class="mr-3"/></li></label>
										<label><span class="mr-1">배송중</span><input type="radio" value="4" name="orderStatus"    class="mr-3"/></li></label>
										<label><span class="mr-1">배송완료</span><input type="radio" value="5" name="orderStatus"  class="mr-3"/></li></label>
										<label><span class="mr-1">구매확정</span><input type="radio" value="6" name="orderStatus"  class="mr-3"/></li></label>
										<label><span class="mr-1">환불접수</span><input type="radio" value="7" name="orderStatus"  class="mr-3"/></li></label>
										<label><span class="mr-1">환불완료</span><input type="radio" value="8" name="orderStatus"  class="mr-3"/></li></label>
									</td>	
								</tr>
							</thead>
						</table>

						<div id="searchbutton">
							<button type="button" class="btn btn-light" onclick="searchOrder()">검색</button>
							<button type="reset" class="btn btn-light">초기화</button>
						</div>
					</div>
				</form>
					
				<hr style="border: solid 1px black; opacity:20%">

				<!-- 조회목록 -->
				<div id="memberlist" class="p-3">
					<div id="top">
						<span>전체 : ${requestScope.orderCnt}건 조회</span>&nbsp;&nbsp;
						<!-- <span>총 주문액 : 원</span>  -->
					</div>
					
					<div>
						<table class="table table-sm mt-3">
							<thead class="thead-light">
							  <tr>
								<th>no</th>
								<th>주문번호</th>
								<th>주문일시</th>
								<th>주문수량</th>
								<th>주문상태</th>
								<th>주문자</th>
								<th>주문자아이디</th>
								<th>연락처</th>
								<th>총주문액</th>
							  </tr>
							</thead>
							<c:if test="${not empty requestScope.orderList}">
								<c:forEach var="ordervo" items="${requestScope.orderList}" varStatus="status">
								<tbody>
								  <tr id="ordertr" data-id="${ordervo.orderno}" data-toggle="modal" data-target="#detailOrder${ordervo.orderno}">
								  <fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}"></fmt:parseNumber>
				    			  <fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
								  	<td>${(requestScope.orderCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
									<td>${ordervo.orderno}</td>
									<td>${ordervo.orderday}</td>
									<td>${ordervo.total_cnt}</td>	
									<td>
									<c:choose>
				    					<c:when test='${ordervo.status == 1}'>결제대기</c:when>
				    					<c:when test='${ordervo.status == 2}'>주문완료</c:when>
				    					<c:when test='${ordervo.status == 3}'>주문취소</c:when>
				    					<c:when test='${ordervo.status == 4}'>배송중</c:when>
				    					<c:when test='${ordervo.status == 5}'>배송완료</c:when>
				    					<c:when test='${ordervo.status == 6}'>구매확정</c:when>
				    					<c:when test='${ordervo.status == 7}'>환불접수</c:when>
				    					<c:when test='${ordervo.status == 8}'>환불완료</c:when>
				    				</c:choose>
									</td>
									<td>${ordervo.name}</td>
									<td>${ordervo.fk_userid}</td>
									<td>${ordervo.tel}</td>
									<td><fmt:formatNumber value="${ordervo.total_price}" pattern="#,###"/></td>
								  </tr>
								</tbody>

								<!-- modal 만들기 -->
								<div class="modal fade" id="detailOrder${ordervo.orderno}" data-backdrop="static">
									<div class="modal-dialog modal-dialog-centered modal-lg">
									  <div class="modal-content">
										<!-- Modal header -->
										<div class="modal-header">
										  <h5 class="modal-title">주문 상세 정보</h5>
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
								</c:forEach>
							</c:if>
							<c:if test="${empty requestScope.orderList}">
								<tbody>
									<tr><td>주문 내역이 없습니다...</td></tr>
								</tbody>
							</c:if>
						</table>
					</div>
				</div>
				<c:if test="${not empty requestScope.orderList}">
					<div id="pageBar">
				       <nav>
				          <ul class="pagination">${requestScope.pageBar}</ul>
				       </nav>
		    		</div>
				</c:if>
				
	
			</div> <!-- end article -->
		</div>
	</div>
</body>
</html>
</div>