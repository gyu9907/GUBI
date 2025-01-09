<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />
<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-myeongjo.css" rel="stylesheet">
<%-- 사용자 정의 JS --%>
<script type="text/javascript">
document.title = "주문 목록";
const ctxPath = "${pageContext.request.contextPath}";  // JSP에서 컨텍스트 경로를 가져와서 ctxPath에 할당
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/member/memberOrderList.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member/memberOrderList.css" />

	<div id="content">
		<!-- 콘텐츠 영역 시작 -->
		
<div class="sidebar2">
    <h2><a href="${pageContext.request.contextPath}/member/myPage.gu">마이페이지</a></h2>
    <hr>
    <div class="section">
        <h3>나의 쇼핑 정보</h3>
        <ul>
            <li><a href="${pageContext.request.contextPath}/member/memberOrderList.gu?status=order" class="${(requestScope.status ne 'refund') ? 'active' : ''}">주문/배송</a></li>
            <li><a href="${pageContext.request.contextPath}/member/memberOrderList.gu?status=refund" class="${(requestScope.status eq 'refund') ? 'active' : ''}">취소/반품/교환</a></li>
        </ul>
    </div>
    <div class="section">
        <h3>나의 활동 정보</h3>
        <ul>
            <li><a href="#">회원정보 및 탈퇴</a></li>
            <li><a href="${pageContext.request.contextPath}/delivery/deliverList.gu">배송지 목록</a></li>
            <li><a href="#">포인트</a></li>
            <li><a href="#">나의 리뷰</a></li>
            <li><a href="#">1:1 문의</a></li>
        </ul> 
    </div>
  </div>


		<div class="container mt-4">
			<h2>ORDER</h2>

			<c:if test="${not empty requestScope.orderList}">
				<h6>주문 상품 정보</h6>
				<hr>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>주문일자 (주문번호)</th>
							<th>상품정보</th>
							<th>수량</th>
							<th>주문금액</th>
							<th>적립 포인트</th>
							<th>주문 처리상태</th>
							<c:if test="${requestScope.status ne 'refund' }">
								<th>주문 변경</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="orderVO" items="${requestScope.orderList}">
							<tr class="start"><th colspan="7"></th></tr>
		                	<c:forEach var="odvo" items="${orderVO.orderDetailList}" varStatus="status">
							<tr>
								<c:if test="${status.index eq 0}">
									<td rowspan="${fn:length(orderVO.orderDetailList)}">${orderVO.orderday}&nbsp;(${orderVO.orderno})</td>
								</c:if>
								<td style="display:flex;">
			                        <a href="${pageContext.request.contextPath}/product/productDetail.gu?productno=${odvo.p_no}">
			                        	<div class="ProductImg_container mr-1"><img src="${pageContext.request.contextPath}/data/images/${odvo.op_img}"/></div>
			                        </a>
									<div class="ProductInfo_container">
			                        	<a href="${pageContext.request.contextPath}/product/productDetail.gu?productno=${odvo.p_no}">
			                        		<div class="ProductName mb-2">${odvo.p_name}</div>
			                        	</a>
			                            <div class="ProductOption my-1">${odvo.op_name}</div>
			                        </div>
			                    </td>
								<td>${odvo.cnt}</td>
								<td>₩&nbsp;<fmt:formatNumber value="${odvo.price}" pattern="#,###" /></td>
								<c:if test="${status.index eq 0}">
									<td rowspan="${fn:length(orderVO.orderDetailList)}"><fmt:formatNumber value="${orderVO.reward_point}" pattern="#,###" />P</td>
								</c:if>
								<c:if test="${status.index eq 0}">
								<td rowspan="${fn:length(orderVO.orderDetailList)}">
									<c:if test="${orderVO.status eq 1}">결제대기</c:if>
									<c:if test="${orderVO.status eq 2}">결제완료</c:if>
									<c:if test="${orderVO.status eq 3}">주문취소</c:if>
									<c:if test="${orderVO.status eq 4}">배송중</c:if>
									<c:if test="${orderVO.status eq 5}">배송완료</c:if>
									<c:if test="${orderVO.status eq 6}">구매확정완료</c:if>
									<c:if test="${orderVO.status eq 7}">환불접수</c:if>
									<c:if test="${orderVO.status eq 8}">환불완료</c:if>
								</td>
								</c:if>
								<c:if test="${orderVO.status eq 1 && status.index eq 0}"><%-- 결제대기 상태일 때 결제 버튼 표시 --%>
								<td rowspan="${fn:length(orderVO.orderDetailList)}">
									<button type="button" id="orderPayment" data-orderno="${orderVO.orderno}">결제</button>
								</td>
								</c:if>
								<c:if test="${orderVO.status eq 2 && status.index eq 0}"><%-- 주문완료 상태일 때 주문취소 버튼 표시 --%>
								<td rowspan="${fn:length(orderVO.orderDetailList)}">
									<button type="button" id="orderCancel" data-orderno="${orderVO.orderno}">주문취소</button>
								</td>
								</c:if>
								<%-- <c:if test="${orderVO.status eq 3}">주문취소</c:if> --%>
								<%-- <c:if test="${orderVO.status eq 4}">배송중</c:if> --%>
								<c:if test="${orderVO.status eq 5 && status.index eq 0}"><%-- 배송완료 상태일 때 구매확정, 환불신청 버튼을 표시 --%>
								<td rowspan="${fn:length(orderVO.orderDetailList)}">
									<button type="button" id="orderConfirm" class="black" data-orderno="${orderVO.orderno}">구매확정</button>
									<button type="button" id="orderRefund" data-orderno="${orderVO.orderno}">환불신청</button>
								</td>
								</c:if>
								<%-- <c:if test="${orderVO.status eq 7}">환불접수</c:if> --%>
								<%-- <c:if test="${orderVO.status eq 8}">환불완료</c:if> --%>
								<c:if test="${orderVO.status eq 6 && odvo.reviewno eq 0}">
								<td>
									<div class="button-wrapper">
							            <button type="button" class="reviewBtn black" data-toggle="modal" data-target="#addReviewModal" data-value="${odvo.fk_optionno}">
							               리뷰작성
							            </button>
							         </div>
								</td>	
								</c:if><%-- 구매확정 상태일 때 리뷰작성 버튼을 표시 --%>
								<c:if test="${orderVO.status eq 6 && odvo.reviewno ne 0}">
								<td>
									<button type="button" id=reviewCheck data-reviewno="${odvo.reviewno}">리뷰확인</button>
								</td>
								</c:if><%-- 구매확정 상태일 때 리뷰확인 버튼을 표시 --%>
							</tr>
						</c:forEach>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

		<div id="page" class="justify-content-center">
        	<ul class="pagination pagination-sm justify-content-center" style="margin:20px 0">
				${requestScope.pageBar}
       		</ul>
        </div>
		</div>
	</div>
	<!--블러 처리 콘텐츠 영역 끝--->
	
	<%-- 리뷰 작성 모달 --%>
	<jsp:include page="/WEB-INF/review/reviewRegister.jsp" />
	
<jsp:include page="/WEB-INF/common/footer.jsp" />