<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
String ctx_Path = request.getContextPath();
//    /MyMVC
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>




<jsp:include page="/WEB-INF/common/header1.jsp" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login/passwdUpdate.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login/passwdUpdate.js"></script>




<div class="container mt-5">

        <h4>비밀번호 수정</h4>

        <form name="passwdUpdateFrm">

            <div class="row justify-content-center">

                <%--  아이디 표시 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">아이디&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                
                <%-- 로그인 중 3개월 지난 회원이면 비밀번호 변경이 이렇게 된다. --%>
                <c:if test="${not empty (sessionScope.loginuser).userid}">
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="userid" id="userid" maxlength="16" class="requiredInfo underline input_background disabled" value="${(sessionScope.loginuser).userid}" />  
                </div>
                </c:if>
                
                <%-- 비밀번호 찾기 중 변경 순서에서 이렇게 된다. --%>
                <c:if test="${not empty requestScope.userid}">
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="userid" id="userid" maxlength="16" class="requiredInfo underline input_background disabled" value="${requestScope.userid}" />  
                </div>
                </c:if>
                <div class="w-100"></div>
                
                <%-- 새 비밀번호 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">비밀번호&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="password" name="passwd" id="passwd" maxlength="15" class="requiredInfo underline"
                        placeholder="영문자/숫자/특수기호 조합하여 8~16글자" />
                </div>
                <div class="w-100"></div>
                <div id="passwderror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 새 비밀번호 확인 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">비밀번호 확인&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="password" name="passwdcheck" id="passwdcheck" maxlength="15" class="requiredInfo underline" 
                        placeholder="비밀번호 확인" />
                </div>
                <div class="w-100"></div>
                <div id="passwdcheckerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>


                <%-- 이메일로 인증 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이메일&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                
                <%-- 로그인 중 3개월 지난 회원이면 비밀번호 변경이 이렇게 된다. --%>
                <c:if test="${not empty (sessionScope.loginuser).email}">
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="email" name="email" id="email" maxlength="60" class="requiredInfo underline disabled" value="${(sessionScope.loginuser).email}" />
                    <%-- 이메일 발송 --%>
                    <button type="button" id="emailSend" class="checkbutton"
                        style="position: absolute; right: 15px; display: inline-block;">이메일 인증</button>
                </div>
                </c:if>
                
                <%-- 비밀번호 찾기 중 변경 순서에서 이렇게 된다. --%>
                <c:if test="${not empty requestScope.email}">
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="email" name="email" id="email" maxlength="60" class="requiredInfo underline disabled" value="${(sessionScope.loginuser).email}" />
                    <%-- 이메일 발송 --%>
                    <button type="button" id="emailSend" class="checkbutton"
                        style="position: absolute; right: 15px; display: inline-block;">이메일 인증</button>
                </div>
                </c:if>
                <div class="w-100"></div>
                <div id="emailSendResult" class="error col-lg-5 col-md-7 hide_Result"></div>
                <div class="w-100"></div>

                <%--  이메일 인증 넣는 칸 --%>
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


                <%--  비밀번호 변경하기 버튼 --%>
                <div class="col-lg-5 col-md-7">
                    <input type="button" class="btnstyle" value="비밀번호 변경하기" onclick="goPasswdUpdate()" />
                </div>
                <div class="w-100"></div>
                <div style="margin-bottom: 10%;"></div>
                

            </div>

        </form>

    </div>






<jsp:include page="/WEB-INF/common/footer1.jsp" />






