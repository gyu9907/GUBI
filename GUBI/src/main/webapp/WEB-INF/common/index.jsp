<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/main/main.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.css"/>
<script src="https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.js"></script>
<div class="common_container">
	<div class="main_container">
		<jsp:include page="header.jsp" />
			<script type="text/javascript" src="${pageContext.request.contextPath}/js/main/main.js"></script>
			<div class="main_wrapper">
				<div class="banner_wrapper">
					<div class="banner_video_wrapper">
						<video autoplay="" class="banner_vedio" loop="" muted=""
							playsinline="" preload="none" role="presentation"
							src="images/main/gubimain.mp4"></video>
						<div class="text_box">
							<p class="headline1">BAGDAD PORTABLE LAMP</p>
							<p class="headline2">By Mathieu Matégot</p>
							<a href="<%= ctxPath%>/product/productDetail.gu?productno=109"> <span>explore</span>
							</a>
						</div>
					</div>
				</div>
				<div class="main_contents_wrapper">
					<p class="headline3">GUBI is the global design house where
						timeless modern icons and the creative talents of today meet and
						mingle – a century of design brilliance in one daring, definitive,
						and ever-evolving collection.</p>
					<div class="arrow_wrapper">
						<button type="button" class="arrow" id="previous">
							<img src="images/main/main_arrow_left4.svg">
						</button>
						<button type="button" class="arrow" id="next">
							<img src="images/main/main_arrow_left4.svg">
						</button>
					</div>
					<div class="pridGridContainer">
	
						<div id="best-prod-list" class="product-grid">
						
							 <c:forEach var="bestprod" items="${requestScope.bestProductlist}">
								<a href="<%= ctxPath%>/product/productDetail.gu?productno=${bestprod.productno}">
									<div class="product-card">
										<div class="product-image">
										  <img src="/GUBI/data/images/${bestprod.thumbnail_img}" />
										</div>
									    <div class="product-info">
									      <p>${bestprod.name}</p>
									     <div class="price">&#8361;<fmt:formatNumber value="${bestprod.price}" pattern="#,###"/></div>
									    </div>
									  </div>
									</a>
							
							</c:forEach>
						</div>
					</div>
	
				</div>
				<div class="main_contents_wrapper newitem">
					<p>SPRING 2025 LAUNCHES</p>
					<div class="product-grid">
						<c:forEach var="newprod" items="${requestScope.productList}">
						
							<a href="<%= ctxPath%>/product/productDetail.gu?productno=${newprod.productno}">
								<div class="product-card">
									<div class="product-image">
										<img src="<%= ctxPath%>/data/images/${newprod.thumbnail_img}">
									</div>
									<div class="product-info">
										<p>${newprod.name}</p>
										<div class="subText">${newprod.description}</div>
									</div>
								</div>
							</a>
	
						</c:forEach>
	
					</div>
	
				</div>
				<div class="main_contents_wrapper category">
					<div class="product-grid">
						<a href="<%= ctxPath%>/product/productList.gu?major_category=LIGHTING">
							<div class="product-card">
								<div class="product-image" style="background-image: url('https://media.gubi.com/6cebba8c-22f8-4540-8eed-3fdba678b4df-640.webp');"></div>
								<div class="product-image__inner" style="background-image: url('https://media.gubi.com/6cebba8c-22f8-4540-8eed-3fdba678b4df-640.webp');">
									<p>LIGHTING</p>
								</div>
							</div>
						</a>
						<a href="<%= ctxPath%>/product/productList.gu?major_category=SEATING">
							<div class="product-card">
	
								<div class="product-image"
									style="background-image: url('https://media.gubi.com/3e62968f-7b52-44aa-b09b-98fdd9d8b859-828.webp');">
								</div>
								<div class="product-image__inner"
									style="background-image: url('https://media.gubi.com/3e62968f-7b52-44aa-b09b-98fdd9d8b859-828.webp');">
									<p>SEATING</p>
	
								</div>
							</div>
						</a>
						<a href="<%= ctxPath%>/product/productList.gu?major_category=TABLES">
							<div class="product-card">
								<div class="product-image"
									style="background-image: url('https://media.gubi.com/55eaefd1-c127-4eea-a444-5b8c2cd1c381-640.webp');"></div>
								<div class="product-image__inner"
									style="background-image: url('https://media.gubi.com/55eaefd1-c127-4eea-a444-5b8c2cd1c381-640.webp');">
									<p>TABLES</p>
	
								</div>
							</div>
						</a>
					</div>
				</div>
				<div class="main_contents_wrapper collection">
					<p class="title">COLLECTION</p>
					<ul class="category_tab_list">
						<li onclick="selectCollectionByCategory('SEATING')">SEATING</li>
						<li onclick="selectCollectionByCategory('LIGHTING')">LIGHTING</li>
						<li onclick="selectCollectionByCategory('TABLES')">TABLES</li>
					</ul>
					<div class="gallery">
			              <div class="swiper-container gallery-thumbs" >
			                 <div class="swiper-wrapper"  id="product-grid"></div>
							<button type="button" class="arrow" id="col_previous">
								<img src="images/main/main_arrow_left4.svg">
							</button>
							<button type="button" class="arrow" id="col_next">
								<img src="images/main/main_arrow_left4.svg">
							</button>
			              </div>
			          </div>
			          
					<div class="container swiper swiper-container">
              			
						
					</div>
				</div>
	
			</div>
		</div>
	</div>
<jsp:include page="footer.jsp" />
