<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String ctxPath = request.getContextPath();
%>
<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />

<script type="text/javascript">
const ctxPath = "${pageContext.request.contextPath}";
</script>


<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/delivery/deliveryModify.js"></script>

<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/delivery/deliveryModify.css" />

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script> 

</head>
<body data-ctxPath="<%= request.getContextPath() %>">

	<div id="content">
		<!-- 콘텐츠 영역 시작 -->
		<div class="sidebar2">
			<h2>마이페이지</h2>
			<hr>
			<div class="section">
				<h3>나의 쇼핑 정보</h3>
				<ul>
		            <li><a href="${pageContext.request.contextPath}/member/memberOrderList.gu?status=order">주문/배송</a></li>
		            <li><a href="${pageContext.request.contextPath}/member/memberOrderList.gu?status=refund">취소/반품/교환</a></li>
				</ul>
			</div>
			<div class="section">
				<h3>나의 활동 정보</h3>
				<ul>
		            <li><a href="${pageContext.request.contextPath}/member/memberEdit.gu">회원정보 수정</a></li>
		            <li><a href="${pageContext.request.contextPath}/member/memberDelete.gu">회원 탈퇴</a></li>
		            <li><a href="${pageContext.request.contextPath}/delivery/deliveryList.gu">배송지 관리</a></li>
		            <li><a href="${pageContext.request.contextPath}/review/myReviewList.gu">나의 리뷰</a></li>
		            <li><a href="#">1:1 문의</a></li>
				</ul>
			</div>
		</div>

		<!-- 배송지 수정 폼 -->
		<!-- deliveryno를 포함한 GET 방식의 URL -->
  <form name="registerFrm" method="POST" action="<%= ctxPath%>/delivery/deliveryModify.gu">

    <input type="hidden" name="deliveryno" id="deliveryno" value="${delivery.deliveryno}"> <!-- deliveryno를 hidden input으로 추가 -->
    <table id="tblMemberRegister">
						<thead>
							<tr>
								<th colspan="2">::: 배송지 수정 :::</th>
							</tr>
						</thead>
						<tbody>
							<!-- 배송지 수정 폼 -->
							   <tr>												     
							   <td colspan="2"><input type="checkbox" id="is_default" ${ delivery.is_default == 1 ? "checked" : "" }>  기본 배송지로 저장</td> 
							    </tr>						                                          
							<tr>
								<td>배송지명&nbsp;</td>
								<td><input type="text" name="delivery_name" id="delivery_name" maxlength="20" placeholder="배송지명을 입력하지 않으면 수령인 성명이 기본값입니다."
									value="${delivery.delivery_name}">
							</tr>

							<tr>
								<td>수령인&nbsp;<span class="star">*</span></td>
								<td><input type="text" name="receiver" id="receiver" maxlength="10" value="${delivery.receiver}" class="requiredInfo" /></td>
							</tr>

							<tr>
								<td>수령인 연락처&nbsp;<span class="star">*</span></td>
								<td><input type="text" name="hp1" id="hp1" size="6" maxlength="3" value="${fn:substring(delivery.receiver_tel, 0, 3)}" readonly>
									&nbsp;-&nbsp;<input type="text" name="hp2" id="hp2" size="6" maxlength="4"
									value="${fn:substring(delivery.receiver_tel, 3, 7)}">
									&nbsp;-&nbsp;<input type="text" name="hp3" id="hp3" size="6"
									maxlength="4"
									value="${fn:substring(delivery.receiver_tel, 7, 11)}">
									<span class="error">휴대폰 형식이 아닙니다.</span></td>
							</tr>
							<tr>
								<td>우편번호&nbsp;<span class="star">*</span></td>
								<td><input type="text" name="postcode" id="postcode"
									size="6" maxlength="5" value="${delivery.postcode}"> <img src="<%= ctxPath%>/images/b_zipcode.gif" id="zipcodeSearch" />
									<span class="error">우편번호 형식에 맞지 않습니다.</span></td>
							</tr>
							<tr>
								<td>주소&nbsp;<span class="star">*</span></td>
								<td><input type="text" name="address" id="address"
									size="40" maxlength="100" value="${delivery.address}">
									<br>
								<input type="text" name="detail_address" id="detail_address"
									size="40" maxlength="100" value="${delivery.detail_address}">
									<span class="error">주소를 입력하세요.</span></td>
							</tr>
							<tr>
								<td>배송시 요청사항</td>
								<td><textarea id="memo" name="memo" maxlength="100"
										placeholder="요청사항을 입력하세요."
										oninput="this.style.height='';this.style.height=this.scrollHeight+'px';">${delivery.memo}</textarea>
									<span class="error">요청사항을 입력하세요.</span></td>
							 </tr>
							 
							 
							 
					   <tr>
                        <td colspan="2">
                            <button type="button" id="submit-btn" onclick="goSubmit()">수정</button>
                            <button type="button" id="cancle-btn" onclick="goResist()">취소</button>
                        </td>
                    </tr> 
						</tbody>
					</table>
				</form>
			</div>


                            

	<!-- JavaScript -->
    <script type="text/javascript">
        function openSidebar() {
            document.getElementById("cartSidebar").classList.add("open");
            document.getElementById("content").classList.add("blur");
        }
        
        function closeSidebar() {
            document.getElementById("cartSidebar").classList.remove("open");
            document.getElementById("content").classList.remove("blur");
        }
    </script>
</body>
</html>
