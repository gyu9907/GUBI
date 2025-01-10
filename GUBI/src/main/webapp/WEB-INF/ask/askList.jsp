<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../common/header.jsp" />

<%
    String ctxPath = request.getContextPath();
%>



<script>
    const contextPath = '${pageContext.request.contextPath}';  // JSP에서 컨텍스트 경로를 가져와서 ctxPath에 할당
    const ctxPath = '<%= request.getContextPath() %>';
</script>  
<html>
<head>

<title>::: 문의 내역 :::</title>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css"
	href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">


<!-- Optional JavaScript -->
<script type="text/javascript"
	src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript"
	src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>


<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/ask/askList.js"></script>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/ask/askList.css" />

</head>
<body data-ctxPath="<%= request.getContextPath() %>">


<div class="sidebar2">
    <h2><a href="<%=ctxPath%>/member/myPage.gu">마이페이지</a></h2>
    <hr>
    <div class="section">
        <h3>나의 쇼핑 정보</h3>
        <ul>
            <li><a href="#">주문/배송</a></li>
            <li><a href="#">취소/반품/교환</a></li>
        </ul>
    </div>
    <div class="section">
        <h3>나의 활동 정보</h3>
        <ul>
            <li><a href="#">회원정보 및 탈퇴</a></li>
            <li><a href="<%= ctxPath%>/delivery/deliverList.gu">배송지 목록</a></li>
            <li><a href="#">포인트</a></li>
            <li><a href="#">나의 리뷰</a></li>
            <li><a href="#">1:1 문의</a></li>
        </ul> 
    </div>
  </div>


		<div class="container mt-4">
			<h2 class="mt-5">문의 관리</h2>
			
 <!-- 검색 폼 추가 -->
       

<div class="container" style="padding: 3% 0;">

	<form name="ask_search_frm">
		<select name="searchType">
			<option value="">검색대상</option>
			<option value="name">내용</option>
			<option value="userid">아이디</option>
			<option value="email">답변여부</option>
		</select> &nbsp;
		<input type="text" name="searchWord" placeholder="검색어를 입력하세요" />
		<%--
             form 태그내에서 데이터를 전송해야 할 input 태그가 만약에 1개 밖에 없을 경우에는
             input 태그내에 값을 넣고나서 그냥 엔터를 해버리면 submit 되어져 버린다.
             그래서 유효성 검사를 거친후 submit 을 하고 싶어도 input 태그가 만약에 1개 밖에 없을 경우라면 
             유효성검사가 있더라도 유효성검사를 거치지 않고 바로 submit 되어진다. 
             이것을 막으려면 input 태그가 2개 이상 존재하면 된다.  
             그런데 실제 화면에 보여질 input 태그는 1개 이어야 한다.
             이럴 경우 아래와 같이 해주면 된다. 
             또한 form 태그에 action 이 명기되지 않았으면 현재보이는 URL 경로로 submit 되어진다.   
        --%>
        <input type="text" style="display: none;" /> <%-- 조심할 것은 type="hidden" 이 아니다. --%>

		<button type="button" class="btn btn-secondary" onclick="goSearch()">검색</button>
	
		
	</form>

				<p>문의 내역</p>
				<hr class="custom-hr">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th><input type="checkbox" name="selectall"
								value="selectall" /></th>
							<th>번호</th>
							<th>문의유형</th>
							<th>내용</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>답변</th>
						</tr>
					</thead>
					<tbody>
	  <form name="askListForm">
	 	<c:forEach var="ask" items="${askList}" varStatus="status">
        <tr data-askno="${ask.askno}" onclick="openaskViewPage(this)">
        <td><input type="checkbox" name="askno" value="${ask.askno}" onclick="event.stopPropagation();" /></td>
        <td>${status.index + 1}</td> <!-- 번호: 순차적으로 1, 2, 3, 4, 5... 출력 -->
        <td>
        

        
            <c:choose>
                <c:when test="${ask.ask_category == 0}">
                    상품문의
                </c:when>
                <c:when test="${ask.ask_category == 1}">
                    배송문의
                </c:when>
                <c:when test="${ask.ask_category == 2}">
                    기타
               </c:when>
            </c:choose>
        </td> <!-- 문의 유형 -->
        <td>${ask.question}</td> <!-- 질문 -->
        <td>${ask.fk_userid}</td> <!-- 작성자 -->
        <td>${ask.registerday}</td> <!-- 작성일 -->
        <td>
            <!-- 답변 여부 조건 --> 
            <c:choose>
                <c:when test="${not empty ask.answer}">
                    O <!-- 답변 완료 -->
                </c:when>
                <c:otherwise>
                    X <!-- 미답변 -->
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</c:forEach>
</form>	

				     </tbody>
				</table>
 <hr class="custom3-hr">
			 <!-- 페이징 네비게이션 -->

           
            <button type="button" id="submit-btn" onclick="goSubmit()">문의 등록</button>
            <button type="button" id="delete-btn" onclick="goDelete()">문의 삭제</button>
        </div>
    </div>
    
    
</body>
</html>

<jsp:include page="../common/footer.jsp" />