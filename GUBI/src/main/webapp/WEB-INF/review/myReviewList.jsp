<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String ctxPath = request.getContextPath();
//	   /GUBI
%>

<link rel="stylesheet" href="<%=ctxPath%>/css/review/myReviewList.css">
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/member/myPage.css" />

<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<jsp:include page="../common/bootstrap.jsp" />
<jsp:include page="../common/header.jsp" />

<!-- 리뷰 작성 모달 -->
<jsp:include page="../review/reviewRegister.jsp" />
<script type="text/javascript" src="<%= ctxPath%>/js/review/myReviewList.js"></script>
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
            <li><a href="/GUBI/review/myReviewList.gu">나의 리뷰</a></li>
            <li><a href="#">1:1 문의</a></li>
        </ul> 
    </div>
  </div>
  
<div class="reviews-container">
	<h1 class="title">My Review List</h1>
	<div id="reviews-list" class="reviews-list">
		<input type="hidden" id="totalCount" value="${requestScope.totalCount}">
		<input type="hidden" id="currentCount" value="">
		<%-- <c:forEach var="review" items="${requestScope.reviewList}">
			<div class="review-item">
				<div class="review-header">
					<span class="review-author">${review.title}</span>
					<span class="review-date">${review.registerday}</span>
				</div>
				<div class="star-rating">
	             	<span class="score" data-value="${review.score}"></span>
	            </div>
				<div class="review_img">
					<img id="imgPreview" src="/GUBI/data/images/${review.img}" />
				</div> 
				<p class="review-text">${review.content}</p>
				<div class="review-actions">
					<button
						onclick="location.href='<%=ctxPath%>/product/productDetail.gu?productno=${review.optionvo.fk_productno}'">상품
						보러가기</button>
					<button class="edit-btn" data-toggle="modal"
						data-reviewno="${review.reviewno}"
						data-optionno="${review.optionvo.optionno}"
						data-optionname="${review.optionvo.name}" data-img="${review.img}"
						data-title="${review.title}" data-score="${review.score}"
						data-content="${review.content}" data-target="#addReviewModal"
						onclick="editReview(this)">수정</button>
					<button class="delete-btn">삭제</button>
				</div>
			</div>
		</c:forEach> --%>

	</div>
</div>