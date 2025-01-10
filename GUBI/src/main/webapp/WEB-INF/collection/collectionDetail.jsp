<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/common/header.jsp" />	


<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/css/bootstrap.min.css" >

<!-- collection detail CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/collection/collectionDetail.css">

<!-- Bentham 폰트 -->
<!-- <link rel="preconnect" href="https://fonts.googleapis.com"> -->
<!-- <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin> -->
<!-- <link href="https://fonts.googleapis.com/css2?family=Bentham&display=swap" rel="stylesheet"> -->

<!-- HelveticaNeueLTStd-Roman 폰트 -->
<link href="https://db.onlinewebfonts.com/c/66e796dac9aff5a6967ebdd5e021db01?family=HelveticaNeueLTStd-Roman" rel="stylesheet">

<style type="text/css">
.header_wrapper.bg_main {
  background-color: transparent;
  box-shadow: none;
}
.header_wrapper.bg_main .header_menu a {
  color: #fff;
}
.header_wrapper.bg_main .logo_wrapper img {
  filter: invert(100%) sepia(100%) saturate(0%) hue-rotate(288deg) brightness(102%) contrast(102%);
}
.header_wrapper.bg_main .header_icon a {
  color: #fff;
}
</style>

<script type="text/javascript">
document.title = "${requestScope.collectionVO.name}";
$(document).ready(function() {

	$(".ProductImg_container").hover(function(){
		$(this).find(".hoverImg").fadeIn();
	}, function(){
		$(this).find(".hoverImg").fadeOut();
	});

	const header_product_menu = document.querySelector('li#product_menu>a');
	const header_dropdown = document.querySelector('div#header_dropdown_menu');
	const headerWrapper = $('#header_dropdown_menu').get(0); 
	
	header_product_menu.addEventListener('mouseenter', function() {
		headerWrapper.classList.remove('bg_main');
		header_dropdown.style.top = '0px';
		header_dropdown.style.maxHeight = '100vh';
	});
	
	header_dropdown.addEventListener('mouseleave', function() {
		headerWrapper.classList.add('bg_main');
		header_dropdown.style.top = '-780px';
		header_dropdown.style.maxHeight = '0';
		if (scrollTop === 0) {
			headerWrapper.classList.add('bg_main');
		}
	});
	// 스크롤 이벤트 추가
	document.addEventListener('scroll', function() {
		const scrollTop = document.documentElement.scrollTop || document.body.scrollTop; // scrollTop 체크

		
		if (scrollTop === 0) {
			headerWrapper.classList.add('bg_main');
		} else {
			headerWrapper.classList.remove('bg_main');
		}
	});
});
</script>
    <div class="ColDetailMovie_container">
        <div class="ColDetailVideo_container">
        	<c:choose>
        		<c:when test="${fn:endsWith(requestScope.collectionVO.fullscreen_img, '.mp4')}">
            		<video autoplay loop playsinline muted src="${pageContext.request.contextPath}/data/images/${requestScope.collectionVO.fullscreen_img}"></video>
            	</c:when>
        		<c:otherwise>
        			<img src="${pageContext.request.contextPath}/data/images/${requestScope.collectionVO.fullscreen_img}"/>
        		</c:otherwise>
        	</c:choose>
        </div>
        <div class="ColDetailTitle">
        	<p class="title">${requestScope.collectionVO.name}</p>
        	<p class="sub-title">BY ${requestScope.collectionVO.designer}</p>
        </div>
        <i class="fa-solid fa-angle-down"></i>
    </div>

    <div class="ColDetail_container container">

        <div class="ColDetailProduct_container">
			<%-- 상품 상세정보 공간 --%>
        </div>

        <div class="ColDetailDescripton_container my-5">
        	<%-- detail_html 내용 공간 --%>
        	<c:import url="/data/html/${requestScope.collectionVO.detail_html}" charEncoding="UTF-8"/>
        </div>

        <div class="ColDetailProductList_container">
            <div class="ColDetailProductListTitle">EXPLORE MORE ${requestScope.collectionVO.name}</div>

            <div class="ColDetailProductList row" style="display:flex; margin:0 auto;">
            	<%-- 상품 목록 표시 --%>
            	<%-- 상품이 3개 보다 적으면 옵션을 모두 표시 --%>
            	<c:if test="${fn:length(requestScope.productList) < 3}">
           		<c:forEach var="productVO" items="${requestScope.productList}">
           			<c:forEach var="optionVO" items="${productVO.optionList}">
	            		<div class="ProductCard_container col-lg-3 col-6">
		                    <a href="${pageContext.request.contextPath}/product/productDetail.gu?productno=${productVO.productno}">
		                        <div class="ProductImg_container">
		                            <img class="option" src="${pageContext.request.contextPath}/data/images/${optionVO.img}"/>
		                        </div>
		                        <div class="ProductName my-2">
		                        	<p>${productVO.name}</p>
		                        	<p class="price">₩&nbsp;<fmt:formatNumber value="${productVO.price}"  pattern="#,###"/></p>
		                        </div>
		                    </a>
		                </div>
           			</c:forEach>
           		</c:forEach>
            	</c:if>
            	
            	<%-- 상품이 3개 이상이면 상품만 표시 --%>
            	<c:if test="${fn:length(requestScope.productList) >= 3}">
            	<c:forEach var="productVO" items="${requestScope.productList}">
            		<div class="ProductCard_container col-lg-3 col-6">
	                    <a href="${pageContext.request.contextPath}/product/productDetail.gu?productno=${productVO.productno}">
	                        <div class="ProductImg_container">
	                            <img src="${pageContext.request.contextPath}/data/images/${productVO.thumbnail_img}"/>
	                            <img class="hoverImg" src="${pageContext.request.contextPath}/data/images/${productVO.productImgList[0].img}"/>
	                        </div>
	                        <div class="ProductName my-2">
	                        	<p>${productVO.name}</p>
	                        	<p class="price">₩&nbsp;<fmt:formatNumber value="${productVO.price}"  pattern="#,###"/></p>
	                        </div>
	                    </a>
	                </div>
            	</c:forEach>
            	</c:if>
            </div>
        </div>
    </div>
<jsp:include page="../common/footer.jsp" />