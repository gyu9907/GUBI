<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />

<script type="text/javascript">
const ctxPath = "${pageContext.request.contextPath}";

$(document).ready(function() {
	if ($(".sold-out").children().length > 0) {
		$(".sold-out-title").show();
	}
});
</script>
<!-- Cart CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cart/cart.css">
<!-- Cart JS -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/cart/cart.js"></script>

    <div class="Cart-container container pt-5">
        <div class="CartBody-container row">
            
            <div class="Cart-container col-lg-6 px-4 py-4">
                <div class="h5 mb-4">Items in basket</div>
                
                <!-- 전체 선택 체크박스 -->
                <input type="checkbox" id="check-all" style="margin-left:20px; margin-right:10px;"/>
                <label for="check-all">Select all items</label>

                <form name="cartFrm">
                <ul>
                    <%-- 장바구니 상품 목록 출력 --%>
                    <c:forEach var="cartVO" items="${requestScope.cartList}">
                    
                    	<c:if test="${cartVO.productVO.cnt > 0}">
                        <li class="my-2 Product-container">
                            <input type="checkbox" name="cartno" id="cartno${cartVO.cartno}" value="${cartVO.cartno}" data-delivery_price="${cartVO.productVO.delivery_price}"/><!-- 장바구니 일련번호를 담아서 주문("컨텍스트패스/order/order.gu")에 POST로 전송 -->
                            <div class="ProductImg-container mx-3"><img src="${pageContext.request.contextPath}/data/images/${cartVO.optionVO.img}" alt="상품 이미지"/></div><!-- 이미지 경로는 "컨텍스트패스/data/images/옵션이미지파일명(DB에서 읽어오기)" -->
                            <div class="ProductInfo-container">
                                <div class="flex mb-2">
                                    <span class="ProductName">${cartVO.productVO.name}</span><!-- 상품명 -->
                                    <button type="button" class="delete-product-btn" data-cartno='${cartVO.cartno}'>×</button><!-- 삭제버튼 -->
                                </div>
                                <div class="ProductOption my-1">${cartVO.optionVO.name}</div><!-- 상품옵션명 -->
                                <div class="inStock">In Stock</div><!-- 판매중이면 In Stock, 품절이면 Sold Out -->
                                <div class="Point" align="end"><fmt:formatNumber value="${cartVO.productVO.price * cartVO.productVO.point_pct/100}" pattern="#,###" />P</div><!-- 적립 포인트 -->
                                <div class="flex">
                                    <div class="ProductCnt-container">
                                        <button type="button" class="ProductMinusBtn" data-cartno='${cartVO.cartno}' data-p_cnt='${cartVO.productVO.cnt}' ${(cartVO.cnt == 1)?'disabled':''}><i class="fa-solid fa-minus"></i></button><!-- 개수 줄이기버튼(ajax로 실시간 변경) -->
                                        <span class="ProductCnt">${cartVO.cnt}</span><!-- 장바구니에 담은 개수 -->
                                        <button type="button" class="ProductPlusBtn" data-cartno='${cartVO.cartno}' data-p_cnt='${cartVO.productVO.cnt}' ${(cartVO.cnt == cartVO.productVO.cnt)?'disabled':''}><i class="fa-solid fa-plus"></i></button><!-- 개수 늘리기버튼(ajax로 실시간 변경) -->
                                    </div>
                                    <span class="ProductPrice">₩&nbsp;<fmt:formatNumber value="${cartVO.productVO.price}" pattern="#,###" /></span><!-- 가격을 상품가격 * 장바구니담긴개수로 출력한다! 원화로 해야함!-->
                                </div>
                            </div>
                        </li>
                    	</c:if>
                    </c:forEach>
                    	
                </ul>
                
                <div class="h5 my-4 sold-out-title" style="display: none;">Sold out items</div>
                
                <ul class="sold-out">
                    <%-- 장바구니 상품 목록 출력(재고 없음) --%>
                    <c:forEach var="cartVO" items="${requestScope.cartList}">
                    	<c:if test="${cartVO.productVO.cnt <= 0}">
                        <li class="my-2 Product-container" style="color: #aaa !important;">
                            <div class="ProductImg-container mx-3"><img src="${pageContext.request.contextPath}/data/images/${cartVO.optionVO.img}" alt="상품 이미지"/></div><!-- 이미지 경로는 "컨텍스트패스/data/images/옵션이미지파일명(DB에서 읽어오기)" -->
                            <div class="ProductInfo-container">
                                <div class="flex mb-2">
                                    <span class="ProductName">${cartVO.productVO.name}</span><!-- 상품명 -->
                                    <button type="button" class="delete-product-btn" data-cartno='${cartVO.cartno}'>×</button><!-- 삭제버튼 -->
                                </div>
                                <div class="ProductOption my-1" style="color: #aaa !important;">${cartVO.optionVO.name}</div><!-- 상품옵션명 -->
                                <div class="inStock">Sold Out</div><!-- 판매중이면 In Stock, 품절이면 Sold Out -->
                                <div class="Point" align="end" style="color: #aaa !important;"><fmt:formatNumber value="${cartVO.productVO.price * cartVO.productVO.point_pct/100}" pattern="#,###" />P</div><!-- 적립 포인트 -->
                                <div  align="end">
                                    <span class="ProductPrice">₩&nbsp;<fmt:formatNumber value="${cartVO.productVO.price}" pattern="#,###" /></span><!-- 가격을 상품가격 * 장바구니담긴개수로 출력한다! 원화로 해야함!-->
                                </div>
                            </div>
                        </li>
                    	</c:if>
                    	
                    </c:forEach>

                </ul>
                </form>
            </div>
            <div class="OrderDetails-container offset-lg-1 col-lg-5 px-4 py-4">
                <div class="h5 mb-4">Order details</div>
                <div class="flex mb-2" style="font-size: 11pt; color: rgb(85, 85, 85);">
                	<span>Delivery</span>
                	<c:if test="${total_delivery eq 0}">
                		<span>Free</span>
                	</c:if>
                	<c:if test="${total_delivery ne 0}">
                		<span id="totalDelivery">₩&nbsp;<fmt:formatNumber value="${total_delivery}" pattern="#,###" /></span>
                	</c:if>
                </div>
                <div class="flex" style="font-weight: 600;"><span>Amount total</span><span id="totalPrice">₩&nbsp;<fmt:formatNumber value="${total_price}" pattern="#,###" /></span></div>
                <div class="flex" style="font-size: 11pt; color: rgb(85, 85, 85);"><span>Reward Points</span><span id="totalPoint"><fmt:formatNumber value="${total_point}" pattern="#,###" />P</span></div>
                
                <hr>

                <button id="goCheckoutBtn" type="button" >Secure Checkout</button><!-- 폼 전송 버튼 -->
                <button id="goProductsBtn" type="button" onclick="javascript:location.href='${pageContext.request.contextPath}/product/productList.gu'">Continue shopping</button><!-- 상품목록 페이지로 돌아가기 -->
            </div>
        </div>

        <div class="CheckoutUSP-container row my-4">
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
</body>
</html>
