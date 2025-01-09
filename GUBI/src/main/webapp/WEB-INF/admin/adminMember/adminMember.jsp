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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/member/adminMember.css">

<script type="text/javascript">
$(document).ready(function() {
	
	document.title="관리자 회원목록";
	$(".nav-link.MEMBER").addClass("active"); // 메뉴엑티브

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
	
    $("select[name='searchType']").bind("change", e => {
        if ($(e.target).val("${requestScope.searchType}").prop("selected",true)) {
        }
    }); // 생년월일에 마우스로 값을 변경하는 경우
   
	$("select[name='searchSelect']").val("${requestScope.searchSelect}");
	$("input:text[name='searchWord']").val("${requestScope.searchWord}");

	$("select[name='dateSelect']").val("${requestScope.dateSelect}");
	$("input:text[name='startDate']").val("${requestScope.startDate}");
	$("input:text[name='endDate']").val("${requestScope.endDate}");

	$("input:radio[value='${requestScope.status}']").prop("checked", true);

	$("input:text[name='searchWord']").bind("keydown", function(e){		
		if(e.keyCode == 13) {
			goSearchMember();
		}
	});
	
	const now = new Date(); // 현재날짜
	var year = now.getFullYear();
	var month = (now.getMonth() + 1).toString().padStart(2, '0');
	var day = now.getDate().toString().padStart(2, '0');
	
	const yyyymmdd = year+month+day; // 오늘날짜
	const yesterday = year+month+(now.getDate()-1).toString().padStart(2, '0')	// 어제
	const weekday = year+month+(now.getDate()-7).toString().padStart(2, '0')	// 일주일전
	const monthday = year+(now.getMonth() + 1).toString().padStart(2, '0')-1+day		// 한달전
	const threemonthday = year+month+day// 세달전
	
	// 오늘 
	$("button.todaybtn").on("click",function(){	
		//alert("오늘버튼클릭");
		
		$("input:text[name='startDate']").val(yyyymmdd);
		$("input:text[name='endDate']").val(yyyymmdd);
	});
	// 어제 
	$("button.yesterdaybtn").on("click",function(){
		 //alert("어제버튼클릭");
	
		$("input:text[name='startDate']").val(yesterday);
		$("input:text[name='endDate']").val(yyyymmdd);
	});
	// 일주일전 
	$("button.weekbtn").on("click",function(){

		$("input:text[name='startDate']").val(weekday);
		$("input:text[name='endDate']").val(yyyymmdd);
	});
	// 한달전 
	$("button.monthbtn").on("click",function(){
		alert("저번달버튼클릭");

		$("input:text[name='startDate']").val(monthday);
		$("input:text[name='endDate']").val(yyyymmdd);
	});
	// 세달전 
	$("button.threemonthbtn").on("click",function(){
		alert("세달전버튼클릭");
		$("input:text[name='startDate']").val();
		$("input:text[name='endDate']").val(yyyymmdd);
	});
	

});

// 검색 버튼 클릭 시 함수 호출 
function goSearchMember() {
	const frm = document.memberFrm;
			    frm.action = "member.gu";
			    //frm.method = "get";
			    frm.submit();
}

function deleteMember() {
	
	if($("input:checkbox[name='deleteCheck']:checked").length == 0) {
		alert("삭제할 회원을 선택하세요");
		return;
	}
	
	$("input:checkbox[name='deleteCheck']:checked").each((index, elmt) => {
		$(document.deleteFrm).append("<input type='hidden' name='userid' value='"+ $(elmt).val() +"'/>")
		$(document.deleteFrm).append("<input type='hidden' name='status' value='0'/>")
	});

	if(confirm("정말로 회원을 삭제하시겠습니까?")) {
		const frm = document.deleteFrm;
	    frm.action = "member.gu";
	    frm.method = "post";
	    frm.submit();
	} else {
		alert("삭제가 취소되었습니다.")
	}

}

function recoverMember() {

	if($("input:checkbox[name='deleteCheck']:checked").length == 0) {
		alert("복구할 회원을 선택하세요");
		return;
	}
	
	$("input:checkbox[name='deleteCheck']:checked").each((index, elmt) => {
		$(document.recoverFrm).append("<input type='hidden' name='userid' value='"+ $(elmt).val() +"'/>")
		$(document.recoverFrm).append("<input type='hidden' name='status' value='1'/>")
	});
	
	
	if(confirm("정말로 회원을 복구하시겠습니까?")) {
		const frm = document.recoverFrm;
	    frm.action = "member.gu";
	    frm.method = "post";
	    frm.submit();
	} else {
		alert("복구가 취소되었습니다.")
	}

}

// 전체선택하기 / 전체해제하기
function onecheck() { // this.checked 는 해당요소가 체크 되었는지를 의미 (boolean 타입)
						  // true 면 check 상태, false 면 해제된 상태
	const boxlist = document.querySelectorAll("input[name='deleteCheck']"); // 전체 체크박스 
	const checkedboxlist = document.querySelectorAll("input[name='deleteCheck']:checked"); // 선택 체크박스 
	const selectAll = document.querySelector("input[name='allcheckbox']"); // 전체체크박스

	if(boxlist.length === checkedboxlist.length) {
		selectAll.checked = true;
	}
	else {
		selectAll.checked = false;
	}
}

function allCheck(selectAll) {

	const checkboxes = document.getElementsByName('deleteCheck');

	checkboxes.forEach((checkbox) => {
		checkbox.checked = selectAll.checked
	});
}
</script>

	<div>

	<div id="contents">
		
		<!-- 사이드 메뉴 -->
		<div id="sidemenu" class="bg-light p-3">
			<i class="fa-solid fa-users fa-3x d-flex justify-content-center mt-4"></i>
			<h3>MEMBER</h3>
			<div id="menulist">
				<ul>
					<li><a class="dropdown-item mb-3" href="#">Member</a></li>
						<ul>
							<li><a href="#" style="color:black">회원정보관리</a></li>
						</ul>
				</ul>
			</div>
		</div>

		<!-- 본문 -->
		<div id="article" class="m-3 p-3">
		<p>Member > 회원정보관리</p>
				
		<form name="memberFrm">
			<!-- 검색기능 -->
			<div id="membersearch">
				<h4 class="bold">회원정보관리</h4>
				<hr style="border: 1px solid black; opacity:20%">
				<br>
				<h6>&nbsp;&nbsp;기본검색</h6>
		
				<table class="table table-sm">
					<thead class="thead-light">
						<tr>
							<th class="th">검색어</th>
							<td>
								<select id="searchSelect" name="searchSelect">
									<option value="userid">아이디</option>
									<option value="name">회원명</option>
									<option value="tel">핸드폰</option>
								</select>
								<input type="text" name="searchWord" size="15"/>
							</td>
						</tr>
						<tr>
							<th class="th">기간검색</th>
							<td>
								<select name="dateSelect">
									<option value="registerday" checked>가입날짜</option>
									<option value="loginday">최근접속</option>
								</select>
								<input type="text" class="mr-1" name="startDate" id="datepicker1" size="10" />
								<input type="text" class="mr-1" name="endDate" id="datepicker2" size="10" />
								<button type="button" class="datebtn todaybtn btn btn-light" value="1">오늘</button>
								<button type="button" class="datebtn yesterdaybtn btn btn-light" value="2">어제</button>
								<button type="button" class="datebtn weekbtn btn btn-light" value="3">일주일</button>
								<button type="button" class="datebtn monthbtn btn btn-light" value="4">1개월</button>
								<button type="button" class="datebtn threemonthbtn btn btn-light" value="5">3개월</button>
							</td>
						</tr>
						<tr>
							<th class="th">가입여부</th>
							<td id="radio">
								<label><span class="mr-1">전체</span><input type="radio" value="" name="status" id="all" class="mr-3"/></label>
								<label><span class="mr-1">일반회원</span><input type="radio" value="0" name="status" id="basic" class="mr-3"/></label>
								<label><span class="mr-1">탈퇴회원</span><input type="radio" value="1" name="status" id="leave" class="mr-3"/></label>
							</td>	
						</tr>
					</thead>
				</table>

				<div id="searchbutton">
					<button type="button" class="btn btn-light" onclick="goSearchMember()">검색</button>
					<button type="reset" class="btn btn-light">초기화</button>
				</div>
			</div>
		</form>

		<hr style="border: solid 1px black; opacity:20%">

		<!-- 조회목록 -->
		<div id="memberlist" class="p-3">
			<div id="top">
				<div class="pb-1">총 회원수 : ${requestScope.memberCnt}명</div>
			</div>
			
			<div>

				<c:if test="${requestScope.status eq '0'}">
					<div id="productupdate">
						<button type="button" name="deletemember" value="deletemember" class="btn btn-danger" onclick="deleteMember()" style="height:30px; width:110px;"><p style="font-size:8pt;">- 회원삭제하기</p></button>
					</div>
				</c:if>
				<c:if test="${requestScope.status eq '1'}">
					<div id="productupdate">
						<button type="button" name="recovermember" value="recovermember" class="btn btn-success" onclick="recoverMember()" style="height:30px; width:110px;"><p style="font-size:8pt;">+ 회원복구하기</p></button>
					</div>
				</c:if>
			    
				<table class="table table-sm mt-3">
					<thead class="thead-light">
					  <tr>
					  	<c:if test="${requestScope.status eq '0' or requestScope.status eq '1'}">
					  		<th><input type="checkbox" name="allcheckbox" onclick="allCheck(this)"/></th>
					  	</c:if>
						<th>번호</th>
						<th>회원명</th>
						<th>아이디</th>
						<th>핸드폰</th>
						<th>가입일자</th>
						<th>최근접속일자</th>
						<th>탈퇴유무</th>
						<th>구매수</th>
						<th>로그인</th>
						<th>포인트</th>
						<th>상세보기</th>
					  </tr>
					</thead>
					
					<c:if test="${not empty requestScope.memberList}">
			    		<c:forEach var="membervo" items="${requestScope.memberList}" varStatus="status">
				    		<tr>
				    		<fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}"></fmt:parseNumber>
				    		<fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
				    			<c:if test="${requestScope.status eq '0' or requestScope.status eq '1'}">
				    			<td><input type="checkbox" name="deleteCheck" value="${membervo.userid}" onclick="onecheck()"/></td>			
			    				</c:if>			
				    			<td>${(requestScope.memberCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
								<td>${membervo.name}</td>
								<td>${membervo.userid}</td>
								<td>${membervo.tel}</td>
								<td>${membervo.registerday}</td>
								<td>${membervo.loginday}</td>
								<td>
									<c:choose>
				    					<c:when test='${membervo.status == 0}'>가입회원</c:when>
				    					<c:otherwise>탈퇴회원</c:otherwise>
				    				</c:choose>
			    				</td>
								<td>${membervo.ordercnt}</td>
								<td>${membervo.logincnt}</td>
								<td>${membervo.point}</td>
								<td><button type="button" class="viewdetail btn btn-light" data-toggle="modal" data-target="#detailMember${status.index}"><p>viewDetail</p></button></td>
							</tr>
							
							
							<%-- ****** 회원상세보기 모달 시작 ****** --%>
							<div class="modal fade" id="detailMember${status.index}" data-backdrop="static"> 
								<div class="modal-dialog modal-dialog-centered">
									
									  <div class="modal-content">
										
										<!-- Modal header -->
										<div class="modal-header">
										  <span class="modal-title" style="font-weight: bold;">${membervo.name}</span><span> 님의 상세정보</span>
										  <button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										
										<!-- Modal body -->
										<div class="modal-body">
											<div class="one">
												<div>성명</div><div>${membervo.name}</div><br>
											</div>
											<div class="one">
												<div>아이디</div><div>${membervo.userid}</div><br>
											</div>
											<div class="one">
												<div>전화번호</div><div>${membervo.tel}</div><br>
											</div>
											<div class="one">
												<div>이메일</div><div>${membervo.email}</div><br>
											</div>
											<div class="one">
												<div>주소</div><div>${membervo.fulladdress}</div><br>
											</div>
											<div class="one"> 
												<div>가입일자</div><div>${membervo.registerday}</div><br>
											</div>
											<div class="one">
												<div class="lastragiday">마지막 접속일</div><div>${membervo.loginday}</div><br>
											</div>
											<div class="one">
												<div>탈퇴유무</div>
												<div>
													<c:choose>
														<c:when test="${membervo.status == '0'}">가입회원</c:when>
														<c:otherwise>탈퇴회원</c:otherwise>
													</c:choose><br>
												</div>			 
											</div>
											<div class="one">
												<div>휴면상태</div>
												<div>
													<c:choose>
														<c:when test="${membervo.status == '0'}">정상회원</c:when>
														<c:otherwise>휴면회원</c:otherwise>
													</c:choose><br>
												</div>			 
											</div>
											<div class="one">
												<div>구매수</div><div>${membervo.ordercnt}</div><br>
											</div>
											<div class="one">
												<div>로그인수</div><div>${membervo.logincnt}</div><br>
											</div>
											<div class="one">
												<div>포인트</div><div>${membervo.point}</div><br>
											</div>
										</div>
										
										<!-- Modal footer -->
										<div class="modal-footer">
										  <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
										</div>
									  </div>
									</div>
							 </div>
							 </c:forEach>
							<%-- ****** 회원상세보기 모달 끝 ****** --%>	
			    	</c:if>
  
				    	
			    	<c:if test="${empty requestScope.memberList}">
			    	 <tr>
			               <td colspan="5">데이터가 존재하지 않습니다.</td>
			         </tr>
					</c:if>
				</table>
			</div>
		</div>
		<c:if test="${not empty requestScope.memberList}">
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