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
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/review/askAnswer.css">
<!--  직접 만든 js -->
<script type="text/javascript" src="<%= ctx_Path%>/js/admin/askAnswer.js"></script>

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
							<li><a href="#">재고관리</a></li>	
						</ul>
					<li><a class="dropdown-item mb-3" href="#">Review / Question</a></li>
						<ul>	
							<li><a href="<%= ctx_Path%>/admin/ask.gu"">문의관리</a></li>
							<li><a href="<%= ctx_Path%>/admin/review.gu">리뷰관리</a></li>
						</ul>
				</ul>
			</div>
		</div>

	<!-- 본문 -->
	<div id="article" class="m-3 p-3">

	<p>Product > 문의관리</p>
	
	<form name="askFrm">
		<!-- 검색기능 -->
		<div id="membersearch">
			<h4 class="bold">리뷰 답변작성하기</h4>
			<hr style="border: 1px solid black; opacity:20%">
			<br>
		</div>	
		
		<div id="reviewContent">

			<div class="review">
				
				<div class="title"><input type="hidden" value="${requestScope.askno}" name="askno">${requestScope.askDetail.mvo.name}(${requestScope.askDetail.fk_userid})<span style="font-size:12pt;"> 님의 문의</span><span class="ragiday">${requestScope.askDetail.registerday}</span></div>
				<div class="askContent">${requestScope.askDetail.question}</div>

				<hr style="border: 1px solid black; opacity:20%">

				<div><i class="fa-regular fa-circle-user mr-2 lg"></i>${requestScope.askDetail.fk_adminid}</div>
				<div id="answer">관리자 답변
					<span class='answerday'>
						<c:if test="${not empty requestScope.askDetail.answerday}">${requestScope.askDetail.answerday}</c:if>
					</span>
				</div>

				<c:if test="${not empty requestScope.askDetail.answer}">
					<div class="existAnswer">
						${requestScope.askDetail.answer}
					</div>
					<button type="button" class="btn btn-dark"><a href="<%= ctx_Path%>/admin/ask.gu">목록으로</a></button>
				</c:if>
				
				<c:if test="${empty requestScope.askDetail.answer}"> 
					<div class="noexistAnswer">
						<textarea class="mt-1 mb-2" cols="148" rows="10" id="adminAnswer" placeholder="답변 작성하기"></textarea>
					</div>
					<div>
						<button type="button" class="answerbtn btn btn-dark">답변작성</button>
						<button type="reset" class="btn btn-light">초기화</button>
					</div>
				</c:if>

			</div>
		</div>
	</form>
</div>
</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  