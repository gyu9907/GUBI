<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />

<!-- Order CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/order/orderPaymentmethod.css">

<!-- 사용자 정의 js -->
<script type="text/javascript">
document.title="Checkout";
const ctxPath = "${pageContext.request.contextPath}";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/order/orderPaymentmethod.js"></script>

    <div class="Checkout_container container">
        <div class="CheckoutHeader_container py-4">
            <h4>Complete your order</h4>
        </div>
        
        <form name="orderFrm">
        <input type="hidden" name="deliveryno" value="${requestScope.deliveryno}"/>
        <div class="CheckoutBody_container row">
            
            <div class="CheckoutOrder_container col-lg-6 px-4 py-4">
                <div class="h6 mb-2">2/2</div>
                <hr>
                <div class="h6 my-4">포인트 사용</div>
                    <ul>
                        <li class="my-2">
                            <label>보유</label>
                            <div id="memberPoint"><fmt:formatNumber value="${sessionScope.loginuser.point}" pattern="#,###" />P</div>
                        </li>
                        <li class="my-3">
                            <label for="usePoint">사용</label>
                            <div style="position: relative;">
                                <input type="number" name="use_point" id="usePoint" maxlength="10"/>
                                <button id="useAllPointBtn" type="button" style="position: absolute; right: 0px;">전액 사용</button>
                            </div>
                        </li>
                    </ul>
            </div>
            <div class="CheckoutProduct_container offset-lg-1 col-lg-5 px-4 py-4">
                <div class="h6 mb-2">ORDER SUMMARY</div>
                <hr>
                <ul>
                	<%-- 장바구니를 거치지 않고 바로 주문하기로 넘어온 경우 --%>
                	<c:if test="${not empty requestScope.optionno}">
                		<input type="hidden" name="optionno" value="${requestScope.optionno}"/>
                		<input type="hidden" name="cnt" value="${requestScope.cnt}"/>
	                    <li class="my-2">
	                        <div class="ProductImg_container mr-1"><img src="${pageContext.request.contextPath}/data/images/${requestScope.o_img}"/></div>
	                        <div class="ProductInfo_container">
	                            <div class="ProductName mb-2">${requestScope.p_name}</div>
	                            <div class="ProductOption my-1">${requestScope.o_name}</div>
	                            <div class="flex"><span class="ProductCnt">Qty&nbsp;${requestScope.cnt}</span><span class="ProductPrice">₩&nbsp;<fmt:formatNumber value="${requestScope.price}" pattern="#,###" /></span></div>
	                        </div>
	                    </li>
                	</c:if>
                	
                	<%-- 장바구니를 거쳐 주문하기로 넘어온 경우 --%>
                	<c:if test="${empty requestScope.optionno}">
                	<c:forEach var="cartVO" items="${requestScope.cartList }">
	                    <li class="my-2">
	                    	<input type="hidden" name="cartno" value="${cartVO.cartno}" />
	                        <div class="ProductImg_container mr-1"><img src="${pageContext.request.contextPath}/data/images/${cartVO.optionVO.img}"/></div>
	                        <div class="ProductInfo_container">
	                            <div class="ProductName mb-2">${cartVO.productVO.name}</div>
	                            <div class="ProductOption my-1">${cartVO.optionVO.name}</div>
	                            <div class="flex"><span class="ProductCnt">Qty&nbsp;${cartVO.cnt}</span><span class="ProductPrice">₩&nbsp;<fmt:formatNumber value="${cartVO.productVO.price}" pattern="#,###" /></span></div>
	                            <div class="flex" style="font-size: 10pt; color: rgb(85, 85, 85); font-weight:300;"><span>Reward points</span><span><fmt:formatNumber value="${cartVO.productVO.price * cartVO.productVO.point_pct/100}" pattern="#,###" />P</span></div>
	                        </div>
	                    </li>
                	</c:forEach>
                	</c:if>
                </ul>
                <div class="flex mb-2" style="font-size: 11pt; color: rgb(85, 85, 85);">
                	<span>Delivery</span>
                	<c:if test="${total_delivery eq 0}">
                		<span>Free</span>
                	</c:if>
                	<c:if test="${total_delivery ne 0}">
                		<span>₩&nbsp;<fmt:formatNumber value="${total_delivery}" pattern="#,###" /></span>
                	</c:if>
                </div>
                <div class="flex" style="font-weight: 600;"><span>Amount total</span><span id="amountTotal">₩&nbsp;<fmt:formatNumber value="${total_price}" pattern="#,###" /></span></div>
                <div class="flex" style="font-size: 11pt; color: rgb(85, 85, 85);"><span>Reward Points</span><span><fmt:formatNumber value="${total_point}" pattern="#,###" />P</span></div>
                <hr>
                <div class="flex" style="font-size: 11pt; font-weight: 600;"><span>Use Points</span><span id="usePointResult">0P</span></div>
                <div class="flex" style="font-size: 11pt; font-weight: 600;"><span>Amount Paid</span><span id="amountPaid">₩&nbsp;<fmt:formatNumber value="${total_price}" pattern="#,###" /></span></div>
            </div>
        </div>
        </form>

        <div class="row">
            <div class="NextPrevButton_container col-lg-6 px-0">
                <button id="PrevButton" type="button">Prev</button>
                <button id="NextButton" type="button">Next</button>
            </div>
        </div>

        <div class="CheckoutUSP_container row my-4">
            <div class="col-md-4 px-4">
                <hr>
                <div><img src=""/></div>
                <div class="my-3" style="font-weight: 500;">SHIPPING AND RETURN</div>
                <div style="font-weight: 300;">If you shop with us, you can return it for a refund within 30 days of receiving your product.</div>
            </div>
            <div class="col-md-4 px-4">
                <hr>
                <div><img src=""/></div>
                <div class="my-3" style="font-weight: 500;">FREE SHIPPING</div>
                <div style="font-weight: 300;">The shipping method for your purchase is determined by the type of item ordered. Smaller items ship with UPS; larger parcels and furniture ship with either Curbside delivery. Your basket will reflect the lowest applicable delivery charges for your order.</div>
            </div>
            <div class="col-md-4 px-4">
                <hr>
                <div><img src=""/></div>
                <div class="my-3" style="font-weight: 500;">WARRANTY</div>
                <div style="font-weight: 300;">2 year warranty on all products.</div>
            </div>
        </div>

    </div>

<jsp:include page="/WEB-INF/common/footer.jsp" />