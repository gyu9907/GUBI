<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String ctxPath = request.getContextPath();
    // /MyMVC
%>
<!-- jQuery 라이브러리 추가 -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 


<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="/bootstrap-4.6.2-dist/css/bootstrap.min.css" >

<!-- Cart CSS -->
<link rel="stylesheet" type="text/css" href="/css/cart/cart.css">
<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="Cart-container container pt-5">
        <div class="CartBody-container row">
            
            <div class="Cart-container col-lg-6 px-4 py-4">
                <div class="h5 mb-4">Items in basket</div>
                
                <!-- 전체 선택 체크박스 -->
                <input type="checkbox" id="check-all" style="margin-left:20px; margin-right:10px;"/>
                <label for="check-all">Select all products</label>

                <form name="cartFrm">
                <ul>
                    <!-- for문으로 장바구니에 담긴 상품 여러 개 출력 -->
                    <c:forEach var="item" items="${cartList}">
                        <li class="my-2 Product-container">
                            <input type="checkbox" name="cartno" value="${item.cartno}"/><!-- 장바구니 일련번호를 담아서 주문("컨텍스트패스/order/order.gu")에 POST로 전송 -->
                            <div class="ProductImg-container mx-3"><img src="<%= ctxPath %>/data/images/${item. thumbnail_img}" alt="상품 이미지"/></div><!-- 이미지 경로는 "컨텍스트패스/data/images/옵션이미지파일명(DB에서 읽어오기)" -->
                            <div class="ProductInfo-container">
                                <div class="flex mb-2">
                                    <span class="ProductName">${item.name}</span><!-- 상품명 -->
                                    <button type="button" class="delete-product-btn">×</button><!-- 삭제버튼 -->
                                </div>
                                <div class="ProductOption my-1">${item.name}</div><!-- 상품옵션명 -->
                                <div class="inStock">${item.stockStatus}</div><!-- 판매중이면 In Stock, 품절이면 Sold Out -->
                                <div class="Point" align="end">${item.point_pct}P</div><!-- 적립 포인트 -->
                                <div class="flex">
                                    <div class="ProductCnt-container">
                                        <button type="button" class="ProductMinusBtn"><i class="fa-solid fa-minus"></i></button><!-- 개수 줄이기버튼(ajax로 실시간 변경) -->
                                        <span class="ProductCnt">${item.cnt}</span><!-- 장바구니에 담은 개수 -->
                                        <button type="button" class="ProductPlusBtn"><i class="fa-solid fa-plus"></i></button><!-- 개수 늘리기버튼(ajax로 실시간 변경) -->
                                    </div>
                                    <span class="ProductPrice">₩<span class="product-price">${item.price}</span></span><!-- 가격을 상품가격 * 장바구니담긴개수로 출력한다! 원화로 해야함!-->

                                </div>
                            </div>
                        </li>
                    </c:forEach>

                </ul>
                </form>
            </div>
            <div class="OrderDetails-container offset-lg-1 col-lg-5 px-4 py-4">
                <div class="h5 mb-4">Order details</div>
                <div class="flex mb-2" style="font-size: 11pt; color: rgb(85, 85, 85);"><span>Delivery</span><span>Free</span></div><!-- 배송비, 선택한 상품 중 가장 작은 배송비로 결정 -->
                <div class="flex" style="font-weight: 600;"><span>Order total</span><span>₩&nbsp;3,597.00</span></div><!-- 선택한 상품의 총 가격 -->
                <div class="flex" style="font-size: 11pt; color: rgb(85, 85, 85);"><span>Reward Points</span><span>230,000P</span></div><!-- 총 적립 포인트 -->
                
                <hr>

                <button id="goCheckoutBtn" type="button" >Secure Checkout</button><!-- 폼 전송 버튼 -->
                <button id="goProductsBtn" type="button" onclick="javascript:location.href='상품화면링크'">Continue shopping</button><!-- 상품목록 페이지로 돌아가기 -->
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
