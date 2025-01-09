<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>

<html>
<head>

<title>::: 마이 페이지 :::</title> 
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/member/myPage.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 


<%-- 직접 만든 JS --%>
  <script type="text/javascript" src="<%= ctxPath%>/js/member/myPage.js"></script>

</head>
<body>


  
  <div class="sidebar2">
    <h2>마이페이지</h2>
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
            <li><a href="#">배송지 관리</a></li>
            <li><a href="#">마일리지</a></li>
            <li><a href="#">나의 리뷰</a></li>
            <li><a href="#">1:1 문의</a></li>
        </ul> 
    </div>
  </div>


<div class="main-content">
    <div class="blackContainer">
      <div>
      
        <div class="name">마이페이지</div>
            <p>${loginuser.name}님 반갑습니다.</p>
          <hr class="custom-hr">
      </div>    
        
    </div>
    <div class="summaryContainer">
      <div class="item">
          <div class="number">welcome</div>
         <div>${loginuser.name}님 반갑습니다.</div>  <%-- userid 컬럼명이 들어가야 한다. --%>
        </div>
        <div class="item">
          <div class="number">${loginuser.point}P</div>
          <div>총 포인트</div>
                                      
        </div>
        <div class="item">
          <div class="number">${loginuser.point}P</div>
          <div>사용가능 포인트</div>
        </div>
    </div>  
<div class="shippingStatusContainer">
    <div class="title">
        주문/배송조회
    </div>
    <div class="status">
        <c:forEach var="entry" items="${orderStatusMap}">
            <div class="item">
                <div>
                    <div class="black number">${entry.key}</div> <!-- 상태 코드 표시 -->
                    <div class="text">
                        <!-- 주문 상태 텍스트 표시 -->
                        <c:choose>
                            <c:when test="${entry.key == '1'}">입금대기</c:when>
                            <c:when test="${entry.key == '2'}">주문완료</c:when>
                            <c:when test="${entry.key == '4'}">배송중</c:when>
                            <c:when test="${entry.key == '5'}">배송완료</c:when>                        
                        </c:choose>
                    </div>
                </div>
                <div class="count">(${entry.value})</div> <!-- 상태 개수 표시 -->
                <div class="icon"> > </div>
            </div>
        </c:forEach>
    </div>   
</div>
    <!-- 4. 최근 주문 내역 -->
    <div class="recent-orders">
      <h3>최근 주문내역</h3>
      <table>
          <thead>
              <tr>
                  <th>상품명</th>
                  <th>가격</th>
                  <th>옵션</th>
                  <th>주문 현황</th>
                  <th>수량</th>
                  <th>리뷰 작성</th>
              </tr>
          </thead>
          <tbody>
              <tr>
                  <td>구매 상품명을 표시하는 곳 입니다.</td>
                  <td>가격을 표시하는 곳 입니다.</td>
                  <td>옵션을 표시하는 곳 ex) 옵션1</td>
                  <td>주문 여부를 표시하는 곳 입니다.</td>
                  <td>수량을 표시하는 곳 입니다.</td>
                  <td><a href="리뷰작성페이지링크" class="review-button">리뷰 작성</a></td>
              </tr>
              <tr>
                  <td>구매 상품명을 표시하는 곳 입니다.</td>
                  <td>가격을 표시하는 곳 입니다.</td>
                  <td>옵션을 표시하는 곳 ex) 옵션2</td>
                  <td>주문 여부를 표시하는 곳 입니다.</td>
                  <td>수량을 표시하는 곳 입니다.</td>
                  <td><a href="리뷰작성페이지링크" class="review-button">리뷰 작성</a></td>
              </tr>
          </tbody>
      </table>
  </div>
</div>
  

</body>
</html>