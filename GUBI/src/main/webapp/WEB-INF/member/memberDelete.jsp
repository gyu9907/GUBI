<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 


<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />


<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member/memberDelete.css" />

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/member/memberDelete.js"></script>

<div class="container mt-5" style="border: solid 0px red;">

	<div class="row">
		
		<%-- <div class="col-lg-12 col-md-12" style="margin: 3% 0 1% 0; font-size: 30pt; font-weight: bold;">
			회원 탈퇴
		</div> --%>	
		<div class="w-100"></div>
		
		<div class="col-lg-12 col-md-12" style="margin: 3% 0 1% 0; font-size: 30pt; font-weight: bold; text-align: center;">
			지금까지 GUBI를 이용해주셔서 정말 감사드립니다.
		</div>
	
		<div class="w-100" style="margin-bottom: 50px"></div>
		
	</div>
	
	<div class="row" style="border: solid 1px gray; margin: 0 10px">
		<div class="col-lg-12 col-md-12">
			<p style="margin-top: 20px; font-size: 15pt; font-weight: bold;">
				1. 회원탈퇴 전, 유의사항을 확인해 주시기 바랍니다.
			</p>
			<ol>
				<li class="contents"><span class="spanNum">1</span>회원탈퇴 시 회원전용 웹 서비스 이용이 불가합니다.</li>
				<li class="contents"><span class="spanNum">2</span>거래정보가 있는 경우, 전자상거래 등에서의 소비자 보호에 관한 법률에 따라 계약 또는 청약철회에 관한 기록, 대금결제 및 재화 등의 공급에 관한 기록은 5년동안 보존됩니다.</li>
				<li class="contents"><span class="spanNum">3</span>보유하셨던 포인트는 탈퇴와 함께 삭제되며 환불되지 않습니다.</li>
				<li class="contents"><span class="spanNum">4</span>회원탈퇴 후 GUBI 서비스에 입력하신 상품문의 및 후기, 댓글은 삭제되지 않으며, 회원정보 삭제로 인해 작성자 본인을 확인할 수 없어 편집 및 삭제처리가 원천적으로 불가능 합니다.</li>
				<li class="contents"><span class="spanNum">5</span>상품문의 및 후기, 댓글 삭제를 원하시는 경우에는 먼저 해당 게시물을 삭제하신 후 탈퇴를 신청하시기 바랍니다.</li>
				<li class="contents"><span class="spanNum">6</span>이미 결제가 완료된 건은 탈퇴로 취소되지 않습니다.</li>
			</ol>
		</div>
		<%-- 약관동의 체크박스 --%>
		<div class="col-lg-12 col-md-12 box" style="background-color: #f2f2f2">
			<input type="checkbox" id="agreeCheck" />&nbsp;&nbsp;<label for="agreeCheck">상기 GUBI 회원탈퇴 시 처리사항 안내를 확인하였음에 동의합니다.</label> 
		</div>
	</div>
			
	<div class="row" style="border: solid 1px #ccc; margin: 25px 10px;">
		<div class="col-lg-12 col-md-12" style="margin-bottom: 15px">
			보안을 위해 회원님의 이름과 계정 이메일 및 비밀번호를 확인합니다.
		</div>
	
		<%-- 데이터 전송용 form --%>
		<form name="memberDeleteFrm">
	
			<div class="col-lg-12 col-md-12">
				<input type="hidden" id="userid" name="userid" value="${(sessionScope.loginuser).userid}" /> <%-- 아이디 저장용 input --%>
				<label class="labelMr" for="name">이름 : <input type="text" id="name" readonly class="underline disabled" value="${(sessionScope.loginuser).name}" /></label>
				<label class="labelMr" for="email">이메일 : <input type="email" id="email" readonly class="underline disabled" value="${(sessionScope.loginuser).email}" /></label>
				<label class="labelMr" for="passwd">비밀번호 : <input type="password" id="passwd" class="underline" /></label>
				<input type="button" id="btnSubmit" class="btnstyle" value="본인확인" onclick="isExistMember()" />
				<span id="isExist" style="margin-left: 5px"></span>
			</div>
			
		</form>
			
	</div>
	
	<div class="row" style="margin: 0 10px 100px 10px">
		
		<div id="btnOutDelete" class="col-lg-12 col-md-12" >
			<%-- <input type="button" id="btnOut" class="btnstyle" value="취소 후 복귀" onclick="goOut()" /> --%>
			<input type="button" id="btnDelete" class="btnstyle" value="회원탈퇴 하러가기" onclick="gomemberDelete()" />
		</div>
		
	</div>
		
		
		
		
		
		
</div>
	
	
	
	
<jsp:include page="/WEB-INF/common/footer.jsp" />
		
	
	
	
	
	
	
	
	



</div>