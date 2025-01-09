<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />

<!-- Order CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/order/order.css">

<!-- 직접 만든 js -->
<script type="text/javascript">
document.title="Checkout";
const ctxPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/order/order.js"></script>

    <div class="Checkout_container container">
        <div class="CheckoutHeader_container py-4">
            <h4>Complete your order</h4>
        </div>
        
        <form name="orderFrm">
        <div class="CheckoutBody_container row">
            
            <div class="CheckoutOrder_container col-lg-6 px-4 py-4">
                <div class="h6 mb-2">1/2</div>
                <hr>
                <div class="h6 mb-4">USER DETAILS</div>
                <ul>
                    <li class="my-2">
                        <label>Name</label>
                        <div id="userName">${sessionScope.loginuser.name}</div>
                    </li>
                    <li class="my-2">
                        <label>Email</label>
                        <div id="userEmail">${sessionScope.loginuser.email}</div>
                    </li>
                    <li class="my-2">
                        <label>Phone number</label>
                        <c:set var="tel" value="${sessionScope.loginuser.tel}" />
                        
                        <div id="userTel">${fn:substring(tel, 0, 3)}-${fn:substring(tel, 3, 7)}-${fn:substring(tel, 7, 11)}</div>
                    </li>
                </ul>
                <div class="h6 my-4">DELIVERY DETAILS<button class="ml-3" type="button" id="selectDelivery">배송지 변경</button></div>
                
<%--                 <input type="hidden" name="deliveryno" value="${requestScope.deliveryVO.deliveryno}"/> --%>

				<%-- 임시 --%>
                <input type="hidden" name="deliveryno" value="${requestScope.deliveryVO.deliveryno}"/>
                
                <%-- 기본 배송지가 존재하면 숨기기 --%>
                <div id="deliveryEmpty" ${(not empty requestScope.deliveryVO) ? 'style="display: none;"':''}>기본 배송지가 없습니다. 배송지를 선택하세요.</div>
            	
            	<%-- 기본 배송지가 존재하지 않으면 숨기기 --%>
                <ul id="deliveryInfo" ${(empty requestScope.deliveryVO) ? 'style="display: none;"':''}>
                    <li class="my-3">
                        <label for="receiver">Name</label>
                        <input type="text" id="receiver" value="${requestScope.deliveryVO.receiver}" readonly/>
                        <!-- <span class="error">수령인 성명은 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label for="reciver_tel">Phone number</label>
                        <input type="text" id="receiver_tel" value="${fn:substring(requestScope.deliveryVO.receiver_tel, 0, 3)}-${fn:substring(requestScope.deliveryVO.receiver_tel, 3, 7)}-${fn:substring(requestScope.deliveryVO.receiver_tel, 7, 11)}" readonly/>
                        <!-- <span class="error">휴대전화는 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label for="postcode">Zip code / Postcode</label>
                        <input type="text" id="postcode" value="${requestScope.deliveryVO.postcode}" readonly/>
                        <!-- <span class="error">우편번호는 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label for="address">Address line 1</label>
                        <input type="text" id="address" value="${requestScope.deliveryVO.address}" readonly/>
                        <!-- <span class="error">주소는 필수입력 사항입니다.</span> -->
                    </li>
                    <li class="my-3">
                        <label for="detail_address">Address line 2</label>
                        <input type="text" id="detail_address" value="${requestScope.deliveryVO.detail_address}" readonly/>
                        <!-- <span class="error">상제주소는 필수입력 사항입니다.</span> -->
                    </li>
                	<li class="my-3">
                        <label for="memo">Additional notes for delivery</label>
                        <input type="text" id="memo" value="${requestScope.deliveryVO.memo}" readonly/>
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
	                            <div class="flex" style="font-size: 10pt; color: rgb(85, 85, 85); font-weight:300;"><span>Reward points</span><span><fmt:formatNumber value="${requestScope.total_point}" pattern="#,###" />P</span></div>
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
                <div class="flex" style="font-weight: 600;"><span>Amount total</span><span>₩&nbsp;<fmt:formatNumber value="${total_price}" pattern="#,###" /></span></div>
                <div class="flex" style="font-size: 11pt; color: rgb(85, 85, 85);"><span>Reward Points</span><span><fmt:formatNumber value="${total_point}" pattern="#,###" />P</span></div>
            </div>
        </div>
        </form>
        <div class="row">
            <div class="NextPrevButton_container col-lg-6 px-0">
<!--                 <button id="PrevButton" type="button" onclick="javascript:history.back()">Prev</button> -->
                <button id="NextButton" type="button">Next</button>
            </div>
        </div>

        <div class="CheckoutUSP_container row my-4">
            <div class="col-md-4 px-4">
                <hr>
                <div><img src=""/></div>
                <div class="my-3" style="font-weight: 500;">SHIPPING AND RETURN</div>
                <div>If you shop with us, you can return it for a refund within 30 days of receiving your product.</div>
            </div>
            <div class="col-md-4 px-4">
                <hr>
                <div><img src=""/></div>
                <div class="my-3" style="font-weight: 500;">FREE SHIPPING</div>
                <div>The shipping method for your purchase is determined by the type of item ordered. Smaller items ship with UPS; larger parcels and furniture ship with either Curbside delivery. Your basket will reflect the lowest applicable delivery charges for your order.</div>
            </div>
            <div class="col-md-4 px-4">
                <hr>
                <div><img src=""/></div>
                <div class="my-3" style="font-weight: 500;">WARRANTY</div>
                <div>2 year warranty on all products.</div>
            </div>
        </div>

    </div>

<jsp:include page="/WEB-INF/common/footer.jsp" />