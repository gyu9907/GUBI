<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 


<jsp:include page="/WEB-INF/common/header1.jsp" />


<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member/memberEdit.css" />

<%-- 다음 우편번호 찾기 js 파일 --%>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/member/memberEdit.js"></script>

<div class="container mt-5">

        <h4>Edit My Information</h4>

        <form name="memberEditFrm">

            <div class="row justify-content-center">

                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">아이디&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="userid" id="userid" maxlength="16" class="underline disabled" readonly value="${(sessionScope.loginuser).userid}" />
                </div>
                <div class="w-100"></div>

                <%-- 비밀번호 바꾸기 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">비밀번호&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="password" name="passwd" id="passwd" maxlength="15" class="requiredInfo underline" 
                    	placeholder="영문자/숫자/특수기호 조합하여 8~16글자" />
                </div>
                <div class="w-100"></div>
                <div id="passwderror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>
                <%-- 비밀번호 확인 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">비밀번호 확인&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="password" name="passwdcheck" id="passwdcheck" maxlength="15"
                        class="requiredInfo underline" placeholder="비밀번호 확인" />
                </div>
                <div class="w-100"></div>
                <div id="passwdcheckerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 이름 바꾸기 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이름&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="text" name="name" id="name" maxlength="20" class="requiredInfo underline" value="${(sessionScope.loginuser).name}" />
                </div>
                <div class="w-100"></div>
                <div id="nameerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 이메일 바꾸기 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이메일&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="email" name="email" id="email" maxlength="60" class="requiredInfo underline disabled" value="${(sessionScope.loginuser).email}" />
                    
                    <%-- 이메일 바꾸려면 눌러서 input 풀어주는 버튼 --%>
                    <button type="button" id="btn_email_change" class="checkbutton_change"
                        style="position: absolute; right: 15px; display: inline-block;">이메일 변경</button>
                    
                    <%-- 이메일 중복체크 및 이메일 인증번호 발송 버튼 --%>
                    <button type="button" id="emailcheck" class="checkbutton"
                        style="position: absolute; right: 15px; display: inline-block;">인증번호 발송</button>
                </div>
                <div class="w-100"></div>
                <div id="emailCheckResult" class="error col-lg-5 col-md-7 hide_Result"></div>
                <div class="w-100"></div>
                <div id="emailerror" class="error col-lg-5 col-md-7"></div>
                <div class="w-100"></div>
            
                <%-- 이메일 인증 넣는 칸 --%>
                <div class="col-lg-5 col-md-7 hide_emailAuth" style="margin: 3% 0 1% 0;">인증번호&nbsp;<span class="star">*</span></div>
                <div class="w-100 hide_emailAuth"></div>
                <div class="col-lg-5 col-md-7 hide_emailAuth" style="position: relative; margin: 0px;">
                    <input type="text" name="email_auth" id="email_auth" maxlength="60" class="underline hide_emailAuth"
                        placeholder="발송된 인증번호를 입력하세요." />
                    <%-- 인증번호 체크버튼 --%>
                    <button type="button" id="btn_email_auth" class="checkbutton hide_emailAuth"
                        style="position: absolute; right: 15px; display: inline-block;">인증번호 확인</button>
                </div>
                <div class="w-100 hide_emailAuth"></div>
                <div id="email_authResult" class="error col-lg-5 col-md-7 hide_emailAuth"></div> <%-- 인증번호 일치한지 아닌지 보여주는 칸 --%> 
                <div class="w-100 hide_emailAuth"></div>


                <%-- 연락처 바꾸기 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">연락처&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
				<c:if test="${not empty sessionScope.loginuser and not empty sessionScope.loginuser.tel}">
					<%-- 'c:set' 태그에서 value 속성만 사용 --%>
					<c:set var="telString" value="${sessionScope.loginuser.tel}" />

					<input type="text" name="hp1" id="hp1" size="6" maxlength="3"
						value="010" readonly class="telunderline" />&nbsp;-&nbsp;
					<input type="text" name="hp2" id="hp2" size="6" maxlength="4" class="telunderline requiredInfo"
						value="${fn:substring(telString, 3, 7)}" />&nbsp;-&nbsp;
					<input type="text" name="hp3" id="hp3" size="6" maxlength="4" class="telunderline requiredInfo"
						value="${fn:substring(telString, 7, 11)}" />
				</c:if>
				</div>
				
                <div class="w-100"></div>
                <div id="telerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 우편번호 바꾸기 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">우편번호&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <%--  <img src="/기본 html 페이지들/images/b_zipcode.gif" id="zipcodeSearch" /> --%>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="postcode" id="postcode" size="6" maxlength="5" class="postcodeunderline requiredInfo"
                    	value="${(sessionScope.loginuser).postcode}" />
                    <%-- 우편번호 찾기 --%>
                    <button type="button" id="zipcodeSearch" class="checkbutton"
                        style="position: absolute; right: 260px; display: inline-block;">우편번호찾기</button>
                </div>
                <div class="w-100"></div>
                <div id="postcodeerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 주소 바꾸기 --%>
                <div class="col-lg-5 col-md-7" style="margin: 0 0 1% 0;">주소&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="text" name="address" id="address" size="40" maxlength="200" value="${(sessionScope.loginuser).address}"
                        class="underline requiredInfo" /><br>
                    <input type="text" name="detail_address" id="detail_Address" size="40" maxlength="200"
                     	value="${(sessionScope.loginuser).detail_address}" class="underline requiredInfo" />
                </div>
                <div class="w-100"></div>
                <div id="addresserror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>


                <div style="margin: 5% 0 5% 0;"></div>

                <%-- 바꾸기 버튼 --%>
                <div class="col-lg-5 col-md-7">
                    <input type="button" class="btnstyle" value="회원정보 바꾸기" onclick="gomemberEdit()" />
                </div>
                <div class="w-100"></div>
                <div style="margin-bottom: 10%;"></div>

            </div>

        </form>

</div>




<jsp:include page="/WEB-INF/common/footer1.jsp" />






