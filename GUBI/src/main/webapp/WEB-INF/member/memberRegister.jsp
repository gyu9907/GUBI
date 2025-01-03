<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- === JSTL( Java Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
   String ctxPath = request.getContextPath();
   //      /MyMVC
%>

<%--  
.jsp 파일에서 사용되어지는 것들 
console.log('${pageContext.request.contextPath}');  // 컨텍스트패스   /MyMVC
console.log('${pageContext.request.requestURL}');   // 전체 URL     http://localhost:9090/MyMVC/WEB-INF/member/admin/memberList.jsp
console.log('${pageContext.request.scheme}');       // http        http
console.log('${pageContext.request.serverName}');   // localhost   localhost
console.log('${pageContext.request.serverPort}');   // 포트번호      9090
console.log('${pageContext.request.requestURI}');   // 요청 URI     /MyMVC/WEB-INF/member/admin/memberList.jsp 
console.log('${pageContext.request.servletPath}');  // 파일명       /WEB-INF/member/admin/memberList.jsp 
--%>





<jsp:include page="/WEB-INF/common/header1.jsp" />



<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member/memberRegister.css" />

<%-- 다음 우편번호 찾기 js 파일 --%>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/member/memberRegister.js"></script>



<div class="container mt-5">

        <h4>JOIN US</h4>

        <form name="registerFrm">

            <div class="row justify-content-center">

                <%-- 회원
                회원 아이디(PK)	userid(PK)	VARCHAR2(20)	NOT NULL	N/A
                비밀번호	passwd	VARCHAR2(200)	NOT NULL	SHA256
                성명	name	NVARCHAR2(10)	NOT NULL	N/A
                생년월일	birth	DATE	NOT NULL	N/A
                이메일	email	VARCHAR2(200)	NOT NULL	AES256
                전화번호	tel	VARCHAR2(200)	NOT NULL	AES256
                우편번호	postno	VARCHAR(5)	NOT NULL	N/A
                주소	address	NVARCHAR2(100)	NOT NULL	N/A
                상세주소	detail_address	NVARCHAR2(100)	NOT NULL	N/A --%> 

                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">아이디&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="userid" id="userid" maxlength="16" class="requiredInfo underline"
                        placeholder="영문소문자/숫자 4~16 자" />
                    <%-- 아이디중복체크 --%>
                    <button type="button" id="idcheck" class="checkbutton"
                        style="position: absolute; right: 15px; display: inline-block;">아이디 중복검사</button>
                </div>
                <div class="w-100"></div>
                <div id="idcheckResult" class="error col-lg-5 col-md-7 hide_Result"></div>
                <div class="w-100"></div> <%-- 이거 한줄씩 추가하자 --%>
                <div id="useriderror" class="error col-lg-5 col-md-7"></div>
                <div class="w-100"></div>

                <%-- 비밀번호 --%>
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

                <%-- 이름 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이름&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="text" name="name" id="name" maxlength="20" class="requiredInfo underline"
                        placeholder="이름을 입력하세요." />
                </div>
                <div class="w-100"></div>
                <div id="nameerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 생년월일 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">생년월일&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="text" name="birth" id="datepicker" maxlength="10" class="underline" />
                </div>
                <div class="w-100"></div>
                <div id="birtherror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 이메일 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">이메일&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="email" name="email" id="email" maxlength="60" class="requiredInfo underline"
                        placeholder="이메일을 입력하세요." />
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
                    <input type="text" name="email_auth" id="email_auth" maxlength="60" class="requiredInfo underline hide_emailAuth"
                        placeholder="발송된 인증번호를 입력하세요." />
                    <%-- 인증번호 체크버튼 --%>
                    <button type="button" id="btn_email_auth" class="checkbutton hide_emailAuth"
                        style="position: absolute; right: 15px; display: inline-block;">인증번호 확인</button>
                </div>
                <div class="w-100 hide_emailAuth"></div>
                <div id="email_authResult" class="error col-lg-5 col-md-7 hide_emailAuth"></div> <%-- 인증번호 일치한지 아닌지 보여주는 칸 --%> 
                <div class="w-100 hide_emailAuth"></div>

                <%-- 연락처 (전화번호 다채우면 다음 칸으로 focus 되도록 설정하자!) --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">연락처&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="text" name="hp1" id="hp1" size="6" maxlength="3" value="010" readonly
                        class="telunderline" />&nbsp;-&nbsp;
                    <input type="text" name="hp2" id="hp2" size="6" maxlength="4" class="telunderline requiredInfo" />&nbsp;-&nbsp;
                    <input type="text" name="hp3" id="hp3" size="6" maxlength="4" class="telunderline requiredInfo" />
                </div>
                <div class="w-100"></div>
                <div id="telerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 우편번호 --%>
                <div class="col-lg-5 col-md-7" style="margin: 3% 0 1% 0;">우편번호&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <%--  <img src="/기본 html 페이지들/images/b_zipcode.gif" id="zipcodeSearch" /> --%>
                <div class="col-lg-5 col-md-7" style="position: relative; margin: 0px;">
                    <input type="text" name="postcode" id="postcode" size="6" maxlength="5" class="postcodeunderline requiredInfo" />
                    <%-- 우편번호 찾기 --%>
                    <button type="button" id="zipcodeSearch" class="checkbutton"
                        style="position: absolute; right: 260px; display: inline-block;">우편번호찾기</button>
                </div>
                <div class="w-100"></div>
                <div id="postcodeerror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>

                <%-- 주소 --%>
                <div class="col-lg-5 col-md-7" style="margin: 0 0 1% 0;">주소&nbsp;<span class="star">*</span></div>
                <div class="w-100"></div>
                <div class="col-lg-5 col-md-7">
                    <input type="text" name="address" id="address" size="40" maxlength="200" placeholder="주소"
                        class="underline requiredInfo" /><br>
                    <input type="text" name="detail_address" id="detail_Address" size="40" maxlength="200"
                        placeholder="상세주소" class="underline requiredInfo" />
                    
                </div>
                <div class="w-100"></div>
                <div id="addresserror" class="col-lg-5 col-md-7 error"></div>
                <div class="w-100"></div>


                <div style="margin: 1% 0 5% 0;"></div>

                <%-- 약관동의 체크박스 --%>
                <div class="col-lg-5 col-md-7">
                    <label for="agree">이용약관에 동의합니다.</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
                </div>
                <div class="w-100"></div>

                <%-- 약관동의 칸 팝업 할지 고민해보자 --%>
                <div class="col-lg-10 col-md-7">
                    <iframe src="${pageContext.request.contextPath}/ifram_agree/agree.html" width="100%" height="150px"
                        style="border: solid 1px navy;"></iframe>
                </div>
                <div class="w-100"></div>

                <div style="margin: 5% 0 5% 0;"></div>

                <%-- 가입, 취소 버튼 --%>
                <div class="col-lg-5 col-md-7">
                    <input type="button" class="btnstyle" value="가입하기" onclick="goRegister()" />
                </div>
                <div class="w-100"></div>
                <div style="margin-bottom: 10%;"></div>

            </div>

        </form>

</div>




<jsp:include page="/WEB-INF/common/footer1.jsp" />


