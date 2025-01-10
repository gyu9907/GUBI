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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/review/askList.css">
<!--  직접 만든 js -->
<script type="text/javascript" src="<%= ctx_Path%>/js/admin/askList.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	document.title="관리자 문의답변";
	$(".nav-link.PRODUCT").addClass("active"); // 메뉴엑티브
	
	
	
});
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
							<li><a href="<%= ctx_Path%>/admin/ask.gu" style="color:black">문의관리</a></li>
							<li><a href="<%= ctx_Path%>/admin/review.gu">리뷰관리</a></li>
						</ul>
				</ul>
			</div>
		</div>

	<!-- 본문 -->
	<div id="article" class="m-3 p-3">

	<p>Product > 문의관리</p>

	<!-- 검색기능 -->
	<div id="membersearch">
		<h4 class="bold">전체 문의목록</h4>
	</div>
	

	<hr style="border: solid 1px black; opacity:20%">
	
		<!-- 조회목록 -->
		<div id="askList" class="">
		
			<div id="top">
				<span class="mb-4">전체 : ${requestScope.askCnt}건 조회</span>
			</div>
			
			<div class="askCategory">
				<span><a href="<%= ctx_Path%>/admin/ask.gu" style="${empty requestScope.ask_category ? 'color: black;' : ''}">전체보기</a></span>
				<span><a href="<%= ctx_Path%>/admin/ask.gu?category=0" style="${requestScope.ask_category eq '0' ? 'color: black;' : ''}">상품문의</a></span>
				<span><a href="<%= ctx_Path%>/admin/ask.gu?category=1" style="${requestScope.ask_category eq '1' ? 'color: black;' : ''}">배송문의</a></span>
				<span><a href="<%= ctx_Path%>/admin/ask.gu?category=2" style="${requestScope.ask_category eq '2' ? 'color: black;' : ''}">기타문의</a></span>
			</div>
			
			<div>
				<table class="table table-sm mt-3">
					<thead class="bg-light">
					  <tr> 					
						<th>no</th>
						<th>문의번호</th>
						<th>카테고리</th>
						<th>질문</th>
						<th>회원아이디</th>
						<th>작성자명</th>
						<th>작성일자</th>
						<th>공개여부</th>
					  </tr>
					</thead>
					<c:if test="${not empty requestScope.askList}">
						<c:forEach var="avo" items="${requestScope.askList}" varStatus="status">
							<tbody>
							   <tr id="askTr" onclick="location.href='<%= ctx_Path%>/admin/askAnswer.gu?askno=${avo.askno}'">
							    <fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}"></fmt:parseNumber>
			    				<fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
								<td>${(requestScope.askCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
								<td class="reviewno">${avo.askno}</td>
								<td>${avo.category} </td>
								<td class='answertd'>${avo.question} 
								<c:choose>
									<c:when test='${not empty avo.answer}'><div class="answer">답변완료</div></c:when>
									<c:when test='${empty avo.answer}'></c:when>
								</c:choose>

								</td>
								<td>${avo.mvo.userid}</td>
								<td>${avo.mvo.name}</td>
								<td>${avo.registerday}</td>
								<td>
									<c:choose>
										<c:when test='${avo.is_hide == 0}'><i class="pl-3 fa-solid fa-lock-open"></i></c:when>
										<c:when test='${avo.is_hide == 1}'><i class="pl-3 fa-solid fa-lock"></i></c:when>
									</c:choose>
								</td>
							  </tr>
							</tbody>
						</c:forEach>
					</c:if>
					
					<c:if test="${empty requestScope.askList}">
						<tr><td>등록된 문의가 없습니다...</td></tr>
					</c:if>
					
				</table>
			</div>
		</div>
	<c:if test="${not empty requestScope.askList}">
		<div id="pageBar">
	       <nav>
	          <ul class="pagination">${requestScope.pageBar}</ul>
	       </nav>
    	</div>
   	</c:if>
	</div> <!-- end article -->
</div>
</div>

<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />    