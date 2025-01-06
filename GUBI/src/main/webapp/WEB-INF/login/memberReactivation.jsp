<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
String ctx_Path = request.getContextPath();
//    /MyMVC
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>




<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login/memberReactivation.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login/memberReactivation.js"></script>


    <div class="container mt-5">

		<h4 style="font-weight: bold; margin-bottom: -4%;">마지막 접속 후 1년이 지나 휴면계정 처리됐습니다.</h4>
        <h4>휴먼계정 복구</h4>
        

		<%-- 로그인을 하고 휴면계정일 경우, 이메일인증 칠 수 있는 페이지로 넘어간 후
             바로 등록된 이메일로 인증메일이 전송되어, 바로 입력 후 메인 페이지로 넘어가는 것 생각해보자. --%>
        <form name="reactivationFrm">

            <div class="row justify-content-center">

                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">아이디&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="userid" id="userid" maxlength="16" class="requiredInfo underline disabled" 
                    	value="${requestScope.userid}" />
                </div>
                <div class="w-100"></div>


                <%-- 이메일로 인증 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이메일&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="email" name="email" id="email" maxlength="60" class="requiredInfo underline disabled"
                    	value="${requestScope.email}" />
                    <%-- 이메일 발송 --%>
                    <button type="button" id="emailSend" class="checkbutton"
                        style="position: absolute; right: 15px; display: inline-block;">이메일 인증</button>
                </div>
                <div class="w-100"></div>
                <div id="emailSendResult" class="error col-lg-5 col-md-7 hide_Result"></div>
                <div class="w-100"></div>

                <%-- 이메일 인증 넣는 칸 --%>
                <div class="col-lg-5 col-md-7 hide_emailAuth" style="margin: 3% 0 1% 0;">인증번호&nbsp;<span class="star">*</span></div>
                <div class="w-100 hide_emailAuth"></div>
                <div class="col-lg-5 col-md-7 hide_emailAuth" style="position: relative; margin: 0px;">
                    <input type="text" name="email_auth" id="email_auth" maxlength="60" class="requiredInfo underline hide_emailAuth"
                        placeholder="발송된 인증번호를 입력하세요." />
                    <%-- 인증번호 체크버튼 --%>
                    <button type="button" id="btn_email_auth" class="checkbutton hide_emailAuth"
                        style="position: absolute; right: 15px; display: inline-block;">인증번호 확인</button>
                </div>
                <div class="w-100 hide_emailAuth"></div>
                <div id="email_authResult" class="error col-lg-5 col-md-7 hide_emailAuth"></div> <%--  인증번호 일치한지 아닌지 보여주는 칸 --%> 
                <div class="w-100 hide_emailAuth"></div>

                <div style="margin-bottom: 7%;"></div>

                <%-- 휴먼 해제하기 버튼 --%>
                <div class="col-lg-5 col-md-7">
                    <input type="button" id="btnSubmit" class="btnstyle" value="휴면 해제하기" onclick="goReactivation()" />
                </div>
                <div class="w-100"></div>
                <%-- <div style="margin-bottom: 10%;"></div> --%>
                
                <input type="hidden" name="passwd" id="passwd" value="${requestScope.passwd}" />

            </div>

        </form>

    </div>




<jsp:include page="/WEB-INF/common/footer.jsp" />



