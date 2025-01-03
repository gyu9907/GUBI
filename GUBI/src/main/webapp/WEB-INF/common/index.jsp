<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/main/main.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/js/product/productListJSON.js"></script>


<div class="common_container">
	<div class="main_container">
		<jsp:include page="header.jsp" />
			<div class="main_wrapper">
				<div class="banner_wrapper">
					<div class="banner_video_wrapper">
						<video autoplay="" class="banner_vedio" loop="" muted=""
							playsinline="" preload="none" role="presentation"
							src="images/main/gubimain.mp4"></video>
						<div class="text_box">
							<p class="headline1">BAGDAD PORTABLE LAMP</p>
							<p class="headline2">By Mathieu Matégot</p>
							<a href="#"> <span>explore</span>
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
	
					<!-- 제품 목록 -->
					<div class="container">
	
						<div class="product-grid">
							<!-- 제품 카드 1 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Copacabana Lounge Chair</p>
										<div class="price">USD 1,999</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 2 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>IOI Coffee Table</p>
										<div class="price">USD 1,799</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 3 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>GUBI X PIERRE FREY</p>
										<div class="price">USD 1,999</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 4 -->
							<a href="#">
								<div class="product-card">
									<div class="new-tag">New</div>
									<div class="product-image"
										style="background-image: url('https://media.gubi.com/73c82377c09a3b32010440746a08d8be/640.webp');"></div>
									<div class="product-info">
										<p>Daumiller Armchair</p>
										<div class="price">USD 899</div>
									</div>
								</div>
							</a>
						</div>
					</div>
	
				</div>
				<div class="main_contents_wrapper newitem">
					<p>SPRING 2025 LAUNCHES</p>
	
					<div class="product-grid">
						<!-- 제품 카드 1 -->
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
						<!-- 제품 카드 1 -->
						<a href="<%= ctxPath%>/product/productList.gu?major_category=LIGHTING">
							<div class="product-card">
								<div class="product-image" style="background-image: url('https://media.gubi.com/6cebba8c-22f8-4540-8eed-3fdba678b4df-640.webp');"></div>
								<div class="product-image__inner" style="background-image: url('https://media.gubi.com/6cebba8c-22f8-4540-8eed-3fdba678b4df-640.webp');">
									<p>LIGHTING</p>
								</div>
							</div>
						</a>
	
						<!-- 제품 카드 1 -->
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
	
						<!-- 제품 카드 1 -->
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
						<li>SEATING</li>
						<li>LIGHTING</li>
						<li>TABLES</li>
					</ul>
					<div class="container swiper">
						<div class="product-grid swiper-wrapper" id="product-grid">
							<!-- 제품 카드 1 -->
							<a href="#">
								<div class="product-card">
									<div class="product-image">
										<img src="images/main/collection.png">
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 2 -->
							<a href="#">
								<div class="product-card">
									<div class="product-image active">
										<img src="images/main/collection2.png">
									</div>
								</div>
							</a>
	
							<!-- 제품 카드 3 -->
							<a href="#">
								<div class="product-card">
									<div class="product-image">
										<img src="images/main/collection3.png">
									</div>
								</div>
							</a>
							<!-- 제품 카드 3 -->
							<a href="#">
								<div class="product-card">
									<div class="product-image">
										<img src="images/main/collection.png">
									</div>
								</div>
							</a>
							<!-- 제품 카드 3 -->
							<a href="#">
								<div class="product-card">
									<div class="product-image">
										<img src="images/main/collection2.png">
									</div>
								</div>
							</a>
							<!-- 제품 카드 3 -->
							<a href="#">
								<div class="product-card">
									<div class="product-image">
										<img src="images/main/collection3.png">
									</div>
								</div>
							</a>
						</div>
						<div class="arrow_wrapper">
							<button type="button" class="arrow" id="previous">
								<img src="images/main/main_arrow_left4.svg">
							</button>
							<button type="button" class="arrow" id="next">
								<img src="images/main/main_arrow_left4.svg">
							</button>
	
						</div>
					</div>
				</div>
	
			</div>
		</div>
	</div>
<jsp:include page="footer.jsp" />
