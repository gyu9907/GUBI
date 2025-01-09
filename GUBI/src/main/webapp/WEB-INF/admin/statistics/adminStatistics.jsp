<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../adminHeader.jsp" />

<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/collection/adminCollection.css">

<style type="text/css">

select {
	font-size: 10pt;
}

th:not(.bar), td:not(.bar) {
	width: 120px;
}
th, td{
	vertical-align: middle !important;
	text-align: center;
}
tr:last-child {
	background-color: rgb(248, 249, 250);
}

.bar-background {
	background-color: rgb(241, 241, 241);
	display:flex;
	align-items: center;
}

div.bar {
	height: 10px;
	border: solid 1px rgb(96, 137, 243);
	background-color: rgb(178, 205, 247);
}

</style>

<script type="text/javascript">
document.title = "${requestScope.title}";
$(document).ready(function() {
	$(".nav-link.STATISTICS").addClass("active");
	
	if($("select[name='majorCategory']")) {
		$("select[name='majorCategory']").val('${requestScope.majorCategory}');
		$("select[name='orderStatus']").val('${requestScope.orderStatus}');
	}
	
	$("select").on("change", () => {
		if(document.statisticsFrm) {
			document.statisticsFrm.submit();
		}
	});
	
	$("#menulist a").each((index, elmt) => {
		if($(elmt).text() == "${requestScope.title}") {
			$(elmt).css({"color" : "black"});
		}
	});
	
	$("td.percentage").each((index, elmt) => {
		
		const width = Number($(elmt).text());
		
		$("div.bar").eq(index).css({"width" : $(elmt).text() + "%"});
	});
});
</script>
		

	<div id="contents">
		<!-- 사이드 메뉴 -->
		<div id="sidemenu" class="bg-light p-3">
			<i class="fa-solid fa-chart-simple fa-3x d-flex justify-content-center mt-4"></i>
			<h3>Statistics</h3>
			<div id="menulist">
				<ul>
					<li><a class="dropdown-item mb-3" href="#">접속자통계</a>
						<ul>
							<li><a href="statistics.gu?date=day">일별 접속통계</a></li>
							<li><a href="statistics.gu?date=month">월별 접속통계</a></li>
							<li><a href="statistics.gu?date=year">연도별 접속통계</a></li>
						</ul>
					</li>
					
					<li><a class="dropdown-item mb-3" href="#">주문통계</a>
						<ul>
							<li><a href="statistics.gu?type=order&date=day">일별 주문통계</a></li>
							<li><a href="statistics.gu?type=order&date=month">월별 주문통계</a></li>
							<li><a href="statistics.gu?type=order&date=year">연도별 주문통계</a></li>
							<li><a href="statistics.gu?type=order&majorCategory=SEATING">카테고리별 주문통계</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>

		<!-- 본문 -->
		<div id="article" class="m-3 p-3">

			<p>STATISTICS > ${requestScope.title}</p>
			
			<c:if test="${fn:endsWith( requestScope.title, '주문통계')}">
				<form name="statisticsFrm">
					<input type="hidden" name="type" value="order"/>
				<c:if test="${requestScope.title eq '카테고리별 주문통계'}">
					<select name="majorCategory">
						<option value="SEATING">SEATING</option>
						<option value="LIGHTING">LIGHTING</option>
						<option value="TABLES">TABLES</option>
					</select>
				</c:if>
					<select name="orderStatus">
						<option value="1">결제대기</option>
						<option value="2">주문완료</option>
						<option value="3">주문취소</option>
						<option value="4">배송중</option>
						<option value="5">배송완료</option>
						<option value="6">구매확정</option>
						<option value="7">환불접수</option>
						<option value="8">환불완료</option>
					</select>
				
				<c:if test="${requestScope.title ne '카테고리별 주문통계'}">
					<input type="hidden" name="date" value="${requestScope.date}"/>
				</c:if>
				</form>
			</c:if>

			
			<table id="graph" class="table table-sm table-hover mt-2">
				<thead class="thead-light">
				<tr>
					<c:if test="${requestScope.title ne '카테고리별 주문통계'}">
						<th>날짜</th>
					</c:if>
					<c:if test="${requestScope.title eq '카테고리별 주문통계'}">
						<th>카테고리</th>
					</c:if>
					<th class="bar">그래프</th>
					<c:if test="${fn:endsWith(requestScope.title, '접속통계')}">
						<th>접속자수</th>
					</c:if>
					<c:if test="${fn:endsWith(requestScope.title, '주문통계')}">
						<th>상품수</th>
					</c:if>
					<th>비율%</th>
				</tr>
				</thead>
				<tbody>
				<c:if test="${empty requestScope.statisticsList }">
					<tr><td colspan="4">조회된 내역이 없습니다.</td></tr>
				</c:if>
				<c:forEach var="svo" items="${requestScope.statisticsList}">
				<tr class="graph-item">
					<c:if test="${svo.name eq '합계'}">
						<td class="name" colspan="2">${svo.name}</td>
					</c:if>
					<c:if test="${svo.name ne '합계'}">
						<td class="name">${svo.name}</td>
						<td class="bar"><div class="bar-background"><div class="bar"></div></div></td>
					</c:if>
					<td class="value">${svo.value}</td>
					<td class="percentage">${svo.percentage}</td>
				</tr>
				</c:forEach>
				</tbody>
			</table>

		</div> <!-- end article -->
	</div>
<jsp:include page="/WEB-INF/admin/adminFooter.jsp" />  