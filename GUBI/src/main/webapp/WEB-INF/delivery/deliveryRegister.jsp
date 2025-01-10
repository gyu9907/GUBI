<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
    //     /MyMVC
%>

<jsp:include page="/WEB-INF/common/header.jsp" />
<jsp:include page="/WEB-INF/common/bootstrap.jsp" />

<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/delivery/deliveryRegister.css" />

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script> 

<script type="text/javascript">
const ctxPath = "${pageContext.request.contextPath}";
</script>

<script type="text/javascript" src="<%= ctxPath%>/js/delivery/deliveryRegister.js"></script> 
 
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
            <li><a href="${pageContext.request.contextPath}/ask/askList.gu">1:1 문의</a></li>
        </ul> 
    </div>
  </div>

<div class="row" id="divRegisterFrm">
   <div class="col-md-12">
      <form name="registerFrm">
          <table id="tblMemberRegister">
             <thead> 
             
                <tr>
                   <th colspan="2">::: 배송지 관리 &nbsp; <span style="font-size: 10pt; font-style: italic;">(<span class="star">*</span>표시는 필수입력사항)</span> :::</th>
                </tr>
             </thead>
             
             <tbody>
              
                 <!-- 기본 배송지 설정 체크박스 -->
                        <tr>
                            <td colspan="2">
                                <input type="checkbox" name="is_default" id="is_default" /> 기본 배송지로 저장
                            </td>
                        </tr>               
                 <tr>
                    <td>배송지명&nbsp;
                    <td>
                       <input type="text" name="delivery_name" id="delivery_name" maxlength="20" placeholder="배송지명을 입력하지 않으면 수령인 성명이 기본값입니다." />
                    </td>
                </tr>
                
                <tr>
                    <td>수령인&nbsp;<span class="star">*</span></td>
                    <td>
                       <input type="text" name=receiver id="receiver" maxlength="10" class="requiredInfo" />
                       <span class="error">수령인은 필수입력 사항입니다.</span>
                    </td>
                </tr>
                
                <tr>
                    <td>수령인 연락처&nbsp; <span class="star">*</span></td> <%--receiver_tel --%>
                    <td>
                       <input type="text" name="hp1" id="hp1" size="6" maxlength="3" value="010" readonly />&nbsp;-&nbsp; 
                       <input type="text" name="hp2" id="hp2" size="6" maxlength="4" />&nbsp;-&nbsp;
                       <input type="text" name="hp3" id="hp3" size="6" maxlength="4" />    
                       <span class="error">휴대폰 형식이 아닙니다.</span>
                    </td>
                </tr>
                
                <tr>
                    <td>우편번호&nbsp; <span class="star">*</span></td>
                    <td>
                       <input type="text" name="postcode" id="postcode" size="6" maxlength="5" />&nbsp;&nbsp;
                       <%-- 우편번호 찾기 --%>
                       <img src="<%= ctxPath%>/images/b_zipcode.gif" id="zipcodeSearch" />
                       <span class="error">우편번호 형식에 맞지 않습니다.</span>
                    </td>
                </tr>
                
                <tr>
                    <td>주소&nbsp; <span class="star">*</span></td>
                    <td>
                       <input type="text" name="address" id="address" size="40" maxlength="100" placeholder="주소" /><br>
                       <input type="text" name="detail_address" id="detail_address" size="40" maxlength="100" placeholder="상세주소" />&nbsp;           
                       <span class="error">주소를 입력하세요.</span>
                    </td>
                </tr>
                    
                <tr>
                    <td>배송시 요청사항</td>
                    <td>                                 
                       <textarea id="memo" name="memo" maxlength="100" placeholder="요청사항을 입력하세요.(최대 100글자)" oninput="this.style.height = ''; this.style.height = this.scrollHeight + 'px'"></textarea>
                    </td>
                </tr>
                
                
                <!-- 버튼 -->
                    <tr>
                        <td colspan="2">
                            <button type="button" id="submit-btn" onclick="goSubmit()">등록</button>
                            <button type="button" id="cancle-btn" onclick="goResist()">취소</button>
                            <!-- 수정 버튼 (숨겨져 있다가 수정할 때만 보이도록) -->
                           <button type="button" id="update-btn" style="display:none;" onclick="updateAddress()">수정</button>
                        </td>
                    </tr>        
                </tbody>
            </table>
        </form>
    </div>
</div>
