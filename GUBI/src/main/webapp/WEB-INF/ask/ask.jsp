<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ask/ask.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ask/ask.js"></script>
    
    
<div class="container mt-5">

        <form name="QnAFrm">

			<input type="hidden" id="productno" name="productno" value="${requestScope.productno}" />

            <div class="row top_box" style="margin: 0 10px">
                <div class="col-lg-12 col-md-12">
                    <!-- <br><br> -->
                    <div class="spans">
                        <label class="label_dd" id="userid_div">아이디</label>
                        <input type="text" id="userid" name="userid" class="disabled" readonly value="${sessionScope.loginuser.userid}" />
                    </div>
                </div>
                <div class="w-100" style="margin-top: 30px;"></div>


                <div class="col-lg-12 col-md-12">
                    <input type="checkbox" id="is_hide" name="is_hide" />&nbsp;&nbsp;<label
                        for="is_hide">비밀글</label>
                </div>

                <div class="col-lg-12 col-md-12 hidden_passwd">
                    <label class="spans label_dd">비밀번호</label><input type="password" id="QnA_passwd"
                        name="QnA_passwd" maxlength="4" placeholder="숫자 4자리로 입력해주십시오." /> <span id="passwderror"></span>
                </div>

                <div class="col-lg-12 col-md-12">
                    <label class="spans label_dd">문의내용</label>
                    <select name="QnA_type" id="QnA_type">
                        <option value="0" selected>상품문의</option>
                        <option value="1">배송문의</option>
                        <option value="2">기타문의</option>
                    </select>
                </div>

                <div class="col-lg-12 col-md-12" style="margin-top: 30px;">
                    <div style="margin-bottom: 10px;">
                        <textarea id="question" name="question" maxlength="100" placeholder="질문은 최소 5글자 이상 입력하세요."></textarea>
                    </div>
                </div>
           </div>

        </form>

        <div class="col-lg-12 col-md-12">
            <div id="button_div">
                <input type="button" id="btnBack" class="btnstyle" value="취소" onclick="goBack()" />
                <input type="button" id="btnSumit" class="btnstyle" value="등록" onclick="goQnA()" />
            </div>
        </div>

        <div>
            <span id="dkdk">상품 QnA 작성 시 유의사항</span>
            <ul id="ulul">
                <li class="contents"> 교환, 반품, 취소는 1:1문의를 통해 접수 부탁드립니다.</li>
                <li class="contents"> 상품 및 상품 구매 과정과 관련 없는 비방, 욕설, 명예훼손성 게시글 및 상품과 관련 없는 광고글 등 부적절한 게시글 등록 시 글쓰기 제한 및 게시글
                    삭제 조치 될 수 있습니다.</li>
                <li class="contents"> 전화번호, 이메일 등 개인 정보가 포함된 글 작성이 필요한 경우 판매자만 볼 수 있도록 비밀글로 문의해 주시기 바랍니다.</li>
            </ul>
        </div>


</div>





