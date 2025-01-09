<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctx_Path = request.getContextPath();
    //    /MyMVC
%>

<jsp:include page="/WEB-INF/admin/adminHeader.jsp" />
<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/order/adminOrderStatus.css">

<script type="text/javascript" src="<%= ctx_Path%>/js/admin/adminOrderStatus.js"></script>


<script type="text/javascript">
$(document).ready(function(){
	document.title="관리자 주문상태별목록";
	$(".nav-link.ORDER").addClass("active"); // 메뉴엑티브
});
</script>

		<div id="contents">
			<!-- 사이드 메뉴 -->
			 
			<div id="sidemenu" class="bg-light p-3">
				<i class="fa-solid fa-receipt fa-3x d-flex justify-content-center mt-4"></i>
				<h3>ORDER</h3>
				<div id="menulist">
					<ul>
						<li><a class="dropdown-item mb-3" href="#">Order</a></li>
						<ul>
							<li><a href="<%= ctx_Path%>/admin/order.gu">주문리스트 (전체)</a></li>
							
							<c:forEach var="status" items="${requestScope.statusCnt}">
								<c:if test="${status.status ==1 or status.status ==2 or status.status ==4 or status.status ==5 or status.status ==6}">
									<li class = "li">
									<c:choose>
										<c:when test='${status.status == 1}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=1" data-value="${status.status}">결제대기</a></c:when>
										<c:when test='${status.status == 2}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=2" data-value="${status.status}">주문완료</a></c:when>
										<c:when test='${status.status == 4}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=4" data-value="${status.status}">배송중</a></c:when>
										<c:when test='${status.status == 5}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=5" data-value="${status.status}">배송완료</a></c:when>
										<c:when test='${status.status == 6}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=6" data-value="${status.status}">구매확정</a></c:when>
									</c:choose>
									<button type="button" value="1" class="btn btn-info btn-sm">
										<span>${status.statusCnt}</span>
									</button>
									</li>
								</c:if>
							</c:forEach>
						</ul>
						
						<li><a class="dropdown-item mb-3" href="#">Cancle</a></li>
						<ul>
							<c:forEach var="status" items="${requestScope.statusCnt}">
								<c:if test="${status.status ==3 or status.status ==7 or status.status ==8}">
									<li>
									<c:choose>
										<c:when test='${status.status == 3}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=3" data-value="${status.status}">주문취소</a></c:when>
										<c:when test='${status.status == 7}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=7" data-value="${status.status}">환불접수</a></c:when>
										<c:when test='${status.status == 8}'><a href="<%= ctx_Path%>/admin/orderStatus.gu?status=8" data-value="${status.status}">환불완료</a></c:when>
									</c:choose>
									<button type="button" value="1" class="btn btn-success btn-sm">
									<span>${status.statusCnt}</span></button></li>
								</c:if>
							</c:forEach>
						</ul>
					</ul>
				</div>
			</div>

			<!-- 본문 -->
			<div id="article" class="m-3 p-3">
			<p>Order > 결제대기</p> 
			
				<form name="orderSearchFrm">
					<!-- 검색기능 -->
					<div id="membersearch">
						<h4 class="bold">주문완료</h4>
						<hr style="border: 1px solid black; opacity:20%">

				<!-- orderlist -->
				<div id="orderlist" class="p-3">
					<div id="top">
						<span>전체 : ${requestScope.statusOrderCnt}건 조회</span>&nbsp;&nbsp;
						<!-- <span>총 주문액 : 원</span>  -->
					</div>
					
					<div>
						<table class="table table-sm mt-3">
							<thead class="thead-light">
							  <tr>
								<th>no</th>
								<th>주문번호</th>
								<th>주문일시</th>
								<th>주문수량</th>
								<th>주문상태</th>
								<th>주문자</th>
								<th>주문자아이디</th>
								<th>연락처</th>
								<th>총주문액</th>
							  </tr>
							</thead>
							<c:if test="${not empty requestScope.statusList}">
								<c:forEach var="ordervo" items="${requestScope.statusList}" varStatus="status">
								<tbody>
								  <tr id="ordertr" data-id="${ordervo.orderno}" data-toggle="modal" data-target="#orderStatus${ordervo.orderno}">
								  <fmt:parseNumber var="currentShowPageNo" value="${requestScope.currentShowPageNo}"></fmt:parseNumber>
				    			  <fmt:parseNumber var="sizePerPage" value="${requestScope.sizePerPage}"></fmt:parseNumber>
								  	<td>${(requestScope.statusOrderCnt)-(currentShowPageNo-1)*sizePerPage-(status.index)}</td>
									<td id="orderno">${ordervo.orderno}</td>
									<td>${ordervo.orderday}</td>
									<td>${ordervo.total_cnt}</td>	
									<td>
									<c:choose>
				    					<c:when test='${ordervo.status == 1}'>결제대기</c:when>
				    					<c:when test='${ordervo.status == 2}'>주문완료</c:when>
				    					<c:when test='${ordervo.status == 3}'>주문취소</c:when>
				    					<c:when test='${ordervo.status == 4}'>배송중</c:when>
				    					<c:when test='${ordervo.status == 5}'>배송완료</c:when>
				    					<c:when test='${ordervo.status == 6}'>구매확정</c:when>
				    					<c:when test='${ordervo.status == 7}'>환불접수</c:when>
				    					<c:when test='${ordervo.status == 8}'>환불완료</c:when>
				    				</c:choose>
									</td>
									<td>${ordervo.name}</td>
									<td>${ordervo.fk_userid}</td>
									<td>${ordervo.tel}</td>
									<td><fmt:formatNumber value="${ordervo.total_price}" pattern="#,###"/></td>
								  </tr>
								</tbody>
								
								<!-- modal 만들기 -->
								<div class="modal fade" id="orderStatus${ordervo.orderno}" data-backdrop="static">
									<div class="modal-dialog modal-dialog-centered modal-md">
									  <div class="modal-content">
										<!-- Modal header -->
										<div class="modal-header">
										  <h5 class="modal-title">주문상태 변경하기</h5>
										  <button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<!-- Modal body -->
										<div class="modal-body">
											<div class="head"><span>Order no </span><span class="orno">${ordervo.orderno}</span></div>
											<div>
												<div class="title">${ordervo.name} 님의 주문상태</div>
												<div class="first mt-3">주문날짜</div><div class="second">${ordervo.orderday}</div>
												<div class="first">주문상태</div>
												<div class="second">
													<c:choose>
														<c:when test='${ordervo.status == 1}'>결제대기</c:when>
														<c:when test='${ordervo.status == 2}'>주문완료</c:when>
														<c:when test='${ordervo.status == 3}'>주문취소</c:when>
														<c:when test='${ordervo.status == 4}'>배송중</c:when>
														<c:when test='${ordervo.status == 5}'>배송완료</c:when>
														<c:when test='${ordervo.status == 6}'>구매확정</c:when>
														<c:when test='${ordervo.status == 7}'>환불접수</c:when>
														<c:when test='${ordervo.status == 8}'>환불완료</c:when>
													</c:choose>
												</div>
							    				
							    				
							    				<div class="first">주문상태 변경</div>
							    				<div id="radio" class="second">
							    					<c:if test="${ordervo.status == 1}"> <!-- 결제대기 -->
							    						<label><span class="mr-1">결제대기</span><input type="radio" value="1" name="orderStatus${ordervo.orderno}"  class="my mr-2"/></label>
								    					<label><span class="mr-1">주문완료</span><input type="radio" value="2" name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
														<label><span class="mr-1">주문취소</span><input type="radio" value="3" name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>										
														<label><span class="mr-1">구매확정</span><input type="radio" value="6" name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    					</c:if>
							    					<c:if test="${ordervo.status == 2}"> <!-- 주문완료 -->
							    						<label><span class="mr-1">주문완료</span><input type="radio" value="2" name="orderStatus${ordervo.orderno}"  class="my mr-2"/></label>
							    						<label><span class="mr-1">주문취소</span><input type="radio" value="3"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>												
														<label><span class="mr-1">배송중</span><input type="radio" value="4"   name="orderStatus${ordervo.orderno}"   class="mr-2"/></label>
														<label><span class="mr-1">구매확정</span><input type="radio" value="6"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
														<label><span class="mr-1">환불접수</span><input type="radio" value="7"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    					</c:if>
					    							<c:if test="${ordervo.status == 3}"> <!-- 주문취소 -->
							    						<label><span class="mr-1">주문취소</span><input type="radio" value="3"  name="orderStatus${ordervo.orderno}"  class="my mr-2"/></label>																	    							
														<label><span class="mr-1">환불접수</span><input type="radio" value="7" name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    					</c:if>									
							    					<c:if test="${ordervo.status == 4}"> <!-- 배송중 -->
							    						<label><span class="mr-1">배송중</span><input type="radio" value="4"   name="orderStatus${ordervo.orderno}"   class="my mr-2"/></label>
							    						<label><span class="mr-1">주문취소</span><input type="radio" value="3"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    						<label><span class="mr-1">배송완료</span><input type="radio" value="5"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
														<label><span class="mr-1">환불접수</span><input type="radio" value="7"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>							    					
													</c:if>		
							    					<c:if test="${ordervo.status == 5}"> <!-- 배송완료 -->
							    						<label><span class="mr-1">배송완료</span><input type="radio" value="5"  name="orderStatus${ordervo.orderno}"  class="my mr-2"/></label>							    					
							    						<label><span class="mr-1">구매확정</span><input type="radio" value="6"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
														<label><span class="mr-1">환불접수</span><input type="radio" value="7"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    					</c:if>
							    					<c:if test="${ordervo.status == 6}"> <!-- 구매확정 -->	
								    					<label><span class="mr-1">구매확정</span><input type="radio" value="6"  name="orderStatus${ordervo.orderno}"  class="my mr-2"/></label>						    					
														<label><span class="mr-1">환불접수</span><input type="radio" value="7" name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
														<label><span class="mr-1">환불완료</span><input type="radio" value="8" name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    					</c:if>
							    					<c:if test="${ordervo.status == 7}"> <!-- 환불접수 -->
														<label><span class="mr-1">환불접수</span><input type="radio" value="7" name="orderStatus${ordervo.orderno}"  class="my mr-2"/></label>
							    						<label><span class="mr-1">구매확정</span><input type="radio" value="6"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
														<label><span class="mr-1">환불완료</span><input type="radio" value="8"  name="orderStatus${ordervo.orderno}"  class="mr-2"/></label>
							    					</c:if>
							    					<c:if test="${ordervo.status == 8}"> <!-- 환불완료 -->
							    						변경 가능한 주문 상태가 없습니다
							    					</c:if>
													
												</div>	
											</div>	
										</div>
										<!-- Modal footer -->
										<div class="modal-footer">
										<button type="button" class="update btn btn-dark" data-id="${ordervo.orderno}" data-dismiss="modal" id="updataCategory">수정하기</button>
										  <button type="button" class="btn btn-light" data-dismiss="modal">Close</button>
										</div>
									  </div>
									</div>
								</div>

								</c:forEach>
							</c:if>
							<c:if test="${empty requestScope.statusList}">
								<tbody>
									<tr><td>주문 내역이 없습니다...</td></tr>
								</tbody>
							</c:if>
						</table>
					</div>
				</div>
				<c:if test="${not empty requestScope.statusList}">
					<div id="pageBar">
				       <nav>
				          <ul class="pagination">${requestScope.pageBar}</ul>
				       </nav>
		    		</div>
				</c:if>
				
	
			</div> <!-- end article -->
		</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />   