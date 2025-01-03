<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>


<jsp:include page="/WEB-INF/common/header1.jsp" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login/idFind.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login/passwdFind.js"></script>


<div class="container mt-5" style="border: solid 0px red;">

        <h4>비밀번호 찾기</h4>


        <form name="passwdFindFrm" action="${pageContext.request.contextPath}/login/passwdFind.gu" method="post">

            <div class="row justify-content-center">

                <%-- 아이디 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">아이디&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="userid" id="userid" maxlength="16" class="requiredInfo underline"
                        placeholder="영문소문자/숫자 4~16 자" />
                </div>
                <div class="w-100"></div>
                <div id="useriderror" class="error col-lg-5 col-md-7"></div>
                <div class="w-100"></div>
                

                <%-- 이메일로 인증하기 위해 넘겨주는 역할 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이메일&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="email" name="email" id="email" maxlength="60" class="requiredInfo underline"
                        placeholder="이메일" />
                    <%-- 이메일 발송 --%>
                    <%-- <button type="button" id="emailSend" class="checkbutton"
                        style="position: absolute; right: 15px; display: inline-block;">이메일 인증</button> --%>
                </div>
                <div class="w-100"></div>
                <%-- <div id="emailSendResult" class="error col-lg-5 col-md-7 hide_Result"></div> --%>
                <div id="emailerror" class="error col-lg-5 col-md-7"></div>
                <div class="w-100"></div>

                <%-- 이메일 인증 넣는 칸 --%>
                <%-- <div class="col-lg-5 col-md-7 hide_emailAuth" style="margin: 3% 0 1% 0;">인증번호&nbsp;<span
                        class="star">*</span></div>
                <div class="w-100 hide_emailAuth"></div>
                <div class="col-lg-5 col-md-7 hide_emailAuth" style="position: relative; margin: 0px;">
                    <input type="text" name="email_auth" id="email_auth" maxlength="60"
                        class="requiredInfo underline hide_emailAuth" placeholder="발송된 인증번호를 입력하세요." />  --%>
                    <%-- 인증번호 체크버튼 --%>
                <%--    <button type="button" id="btn_email_auth" class="checkbutton hide_emailAuth"
                        style="position: absolute; right: 15px; display: inline-block;">인증번호 확인</button>
                </div>
                <div class="w-100 hide_emailAuth"></div>
                <div id="email_authResult" class="error col-lg-5 col-md-7 hide_emailAuth"></div> --%>
                <%-- 인증번호 일치한지 아닌지 보여주는 칸 --%>
                <%--<div class="w-100 hide_emailAuth"></div> --%>

                <div style="margin-bottom: 7%;"></div> 


                <%--  비밀번호 찾기 버튼 --%>
                <div class="col-lg-5 col-md-7">
                    <input type="button" class="btnstyle" value="비밀번호 찾기" onclick="gopasswdFind()" />
                </div>
                <div class="w-100"></div>
                <div style="margin-bottom: 10%;"></div>


            </div>

        </form>

</div>





<jsp:include page="/WEB-INF/common/footer1.jsp" />


