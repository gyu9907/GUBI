<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/common/bootstrap.jsp" />
<jsp:include page="/WEB-INF/common/header.jsp" />

<!-- Order CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/order/orderResult.css">

<script type="text/javascript">
document.title="Order completed";
</script>

	<div class="order-result-container container">
	    <div class="CheckoutHeader_container py-4">
	        <h4>Your order has been completed.</h4>
	    </div>
	        
	    <div class="CheckoutBody_container">
		    <div class="CheckoutProduct_container px-4 py-4">
                <div class="h6 mb-2">ORDER SUMMARY</div>
                <hr>
                <ul>
                	
                	<%-- 주문상세 목록 출력 --%>
                	<c:forEach var="odvo" items="${requestScope.orderVO.orderDetailList}">
	                    <li class="my-2">
	                        <a href="${pageContext.request.contextPath}/product/productDetail.gu?productno=${odvo.p_no}">
	                        	<div class="ProductImg_container mr-1"><img src="${pageContext.request.contextPath}/data/images/${odvo.op_img}"/></div>
	                        </a>
	                        <div class="ProductInfo_container">
	                        	<a href="${pageContext.request.contextPath}/product/productDetail.gu?productno=${odvo.p_no}">
	                        		<div class="ProductName mb-2">${odvo.p_name}</div>
	                        	</a>
	                            <div class="ProductOption my-1">${odvo.op_name}</div>
	                            <div class="flex"><span class="ProductCnt">Qty&nbsp;${odvo.cnt}</span><span class="ProductPrice">₩&nbsp;<fmt:formatNumber value="${odvo.price}" pattern="#,###" /></span></div>
	                        </div>
	                    </li>
                	</c:forEach>
                </ul>
                <div class="flex mb-2" style="font-size: 11pt; color: rgb(85, 85, 85);">
                	<span>Delivery</span>
                	<c:if test="${requestScope.orderVO.delivery_price eq 0}">
                		<span>Free</span>
                	</c:if>
                	<c:if test="${requestScope.orderVO.delivery_price ne 0}">
                		<span>₩&nbsp;<fmt:formatNumber value="${requestScope.orderVO.delivery_price}" pattern="#,###" /></span>
                	</c:if>
                </div>
                <div class="flex" style="font-weight: 600;"><span>Amount total</span><span>₩&nbsp;<fmt:formatNumber value="${requestScope.orderVO.total_price}" pattern="#,###" /></span></div>
                <div class="flex" style="font-size: 11pt; color: rgb(85, 85, 85);"><span>Reward Points</span><span><fmt:formatNumber value="${requestScope.orderVO.reward_point}" pattern="#,###" />P</span></div>
        	
                <div class="h6 mt-5">DELIVERY DETAILS</div>
                <hr>
				<ul id="deliveryInfo">
                    <li class="my-3">
                        <label>Name</label>
                        <div>${requestScope.deliveryVO.receiver}</div>
                        <!-- <span class="error">수령인 성명은 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label>Phone number</label>
                        <div>${requestScope.deliveryVO.receiver_tel}</div>
                        <!-- <span class="error">휴대전화는 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label>Zip code / Postcode</label>
                        <div>${requestScope.deliveryVO.postcode}</div>
                        <!-- <span class="error">우편번호는 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label>Address line 1</label>
                        <div>${requestScope.deliveryVO.address}</div>
                        <!-- <span class="error">주소는 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label>Address line 2</label>
                        <div>${requestScope.deliveryVO.detail_address}</div>
                        <!-- <span class="error">상제주소는 필수입력 사항입니다.</span> -->
                    </li>
                	<li class="my-3">
                        <label>Additional notes for delivery</label>
                        <div>${requestScope.deliveryVO.memo}</div>
            		</li>
            	</ul>
	        </div>
	    </div>
	    
	    <div class="button-container">
	    	<button class="button" id="white" onclick="javascript: location.href='${pageContext.request.contextPath}/product/productList.gu'">Order list</button>
	    	<button class="button" onclick="javascript: location.href='${pageContext.request.contextPath}/product/productList.gu'">Continue shopping</button>
	    </div>
	</div>

<jsp:include page="/WEB-INF/common/footer.jsp" />