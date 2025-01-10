<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String ctxPath = request.getContextPath();
%>


<script type="text/javascript">
const ctxPath = "${pageContext.request.contextPath}";
</script>

<title>배송지 수정</title>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">


<!-- Optional JavaScript -->
<script type="text/javascript"
	src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript"
	src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>



<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%= ctxPath%>/js/delivery/deliveryModifyPopup.js"></script>

<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/delivery/deliveryModify.css" />

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script> 

<style type="text/css">
body {
	min-width: 600px;
}
div#divRegisterFrm {
    text-align: center;
    margin-top: 0;
}
table#tblMemberRegister {
    width: 98%;
    margin: 20px auto;
    border-collapse: collapse;
}

button#zipcodeSearch {
	width: 120px;
    padding: 3px 5px;
	border: solid 1px #ccc;
	color: rgb(85, 85, 85);
	background-color: white;
	font-size: 10pt;
	border-radius: 5px;
	transition: all 0.3s;
}
button#zipcodeSearch:hover {
	border: solid 1px rgb(85, 85, 85);
}

input[type='text'] {
    width: calc(100% - 22px) !important;
    margin: 5px 0 !important;
}
textarea {
	width: calc(100% - 30px) !important;
}

input#postcode,
input#hp1,
input#hp2,
input#hp3 {
    width: 45px !important;
}
</style>

</head>
<body data-ctxPath="<%= request.getContextPath() %>">

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
									size="6" maxlength="5" value="${delivery.postcode}"> 
                       				<button type="button" id="zipcodeSearch">우편번호 찾기</button>
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
</body>
</html>
