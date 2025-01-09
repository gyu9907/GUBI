<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/common/header.jsp" />	

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/css/bootstrap.min.css" >
<!-- Bootstrap js -->
<script src="${pageContext.request.contextPath}/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" type="text/javascript"></script>

<!-- collection CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/collection/collection.css">

<!-- Bentham 폰트 -->
<!-- <link rel="preconnect" href="https://fonts.googleapis.com"> -->
<!-- <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin> -->
<!-- <link href="https://fonts.googleapis.com/css2?family=Bentham&display=swap" rel="stylesheet"> -->

<!-- HelveticaNeueLTStd-Roman 폰트 -->
<link href="https://db.onlinewebfonts.com/c/66e796dac9aff5a6967ebdd5e021db01?family=HelveticaNeueLTStd-Roman" rel="stylesheet">

<script>
document.title = "Collections";

let start = 1;
let len = 8;
let total = ${requestScope.totalCollectionCnt};
let requestLock = false;

$(document).ready(function() {
	getCollection();
	
	$(window).scroll(function() {
		// 스크롤이 전체 페이지 크기만큼 내려가면
		if( $(window).scrollTop() + $(window).height() + 300 >= $(document).height() ) {
			getCollection();
		}
	});
});

function getCollection() {
	
	if(requestLock) {
		return;
	}
	
	requestLock = true; // 스크롤 이벤트가 여러 번 발생 하기 때문에 ajax를 쓰는동안 락을 걸어야 한다.
	
	// 만약 시작숫자와 총 개수가 일치한다면
	if(start >= total) {
		if(start == 1) { // 목록이 하나도 없다면
			let html = '<div class="col-12" align="center">검색된 컬렉션이 없습니다.</div>';

			$("div.CollectionBody_container").html(html);
		}
		return; // 종료
	}
	
	// 컬렉션 목록 불러오기 ajax
	$.ajax({
		url: "collectionListJSON.gu",
		data: {
			"major_category":"${requestScope.selected_category}",
			"start":start,
			"len":len
		},
		dataType: "json",
		success: function(json) {
			const collectionList = json.collectionList;
			
			if(collectionList.length == 0) {
				let html = '<div class="col-12">등록된 컬렉션이 없습니다.</div>';

				$("div.CollectionBody_container").html(html);
			}
			else {
				let html = '';
				for(let i=0; i<collectionList.length; i++) {
					html += '<div class="CollectionCard_container col-lg-3 col-6 mb-3">'
						  + '  <a href="collectionDetail.gu?collectionno='+collectionList[i].collectionno+'">'
		                  + '    <div class="CollectionImg_container">'
		                  + '      <img src="${pageContext.request.contextPath}/data/images/'+collectionList[i].thumbnail_img+'"/>';
		            if(collectionList[i].fullscreen_img.endsWith('.mp4')) {
		            	html += '  <video autoplay loop playsinline muted class="hoverImg" src="${pageContext.request.contextPath}/data/images/'+collectionList[i].fullscreen_img+'"></video>';
		            }
		            else {
		            	html += '  <img class="hoverImg" src="${pageContext.request.contextPath}/data/images/'+collectionList[i].fullscreen_img+'"/>';
		            }
		            html += '    </div>'
		                  + '    <div class="CollectionName my-2">'+collectionList[i].name+'</div>'
		                  + '  </a>'
		            	  + '</div>';
				}
				
				$("div.CollectionBody_container").append(html);
				start += collectionList.length;
				$(".CollectionImg_container").hover(function(){
					$(this).find(".hoverImg").fadeIn();
				}, function(){
					$(this).find(".hoverImg").fadeOut();
				});
			}

			requestLock = false;
			
		},
	    error : function(request, status, error) { // 결과 에러 콜백함수
	        console.log(error);
			requestLock = false;
	    }
	});
}
</script>
    <div class="Collection_container container-fluid py-5">

        <div class="CollectionHeader_container">
            <div class="CollectionTitle">COLLECTIONS</div>
        </div>

        <div class="CollectionNav_container">
        	<%-- 카테고리 목록 표시 --%>
            <ul>
           		<c:choose>
           			<c:when test="${empty requestScope.selected_category}">
               			<li><a href="${pageContext.request.contextPath}/collection/collection.gu" class="selected">ALL</a></li>
           			</c:when>
           			<c:otherwise>
               			<li><a href="${pageContext.request.contextPath}/collection/collection.gu">ALL</a></li>
           			</c:otherwise>
           		</c:choose>
            	<c:forEach var="category" items="${requestScope.selectMajorCategory}">
	           		<c:choose>
	           			<c:when test="${category.major_category eq requestScope.selected_category}">
	                		<li><a href="${pageContext.request.contextPath}/collection/collection.gu?selected_category=${category.major_category}" class="selected">${category.major_category}</a></li>
	           			</c:when>
           				<c:otherwise>
	                		<li><a href="${pageContext.request.contextPath}/collection/collection.gu?selected_category=${category.major_category}">${category.major_category}</a></li>
           				</c:otherwise>
	           		</c:choose>
            	</c:forEach>
            </ul>
        </div>

        <div class="CollectionBody_container row">
        	<%-- 컬렉션 목록을 읽어오는 공간 --%>
        	<%--
            <div class="CollectionCard_container col-lg-3 col-6">
                <a href="/collection_detail.html">
                    <div class="CollectionImg_container"><img src="/images/collections/1/1_Pietra Side Table_main.webp"/></div>
                    <div class="CollectionName my-2">Pietra Side Table</div>
                </a>
            </div>
            --%>
        </div>
    </div>
  <jsp:include page="../common/footer.jsp" />