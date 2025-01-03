<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
String ctx_Path = request.getContextPath();
//    /MyMVC
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>




<jsp:include page="/WEB-INF/common/header1.jsp" /> 


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login/login.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login/login.js"></script>

<script type="text/javascript">

$(document).ready(function () {
	//로그인을 하지 않은 상태일 때 로컬스토리지에 저장된 key 값 saveid 인 userid 를
	// input 태그 userid 에 넣어주기
	if (${empty sessionScope.loginuser}) {
		
		const loginUserid = localStorage.getItem("saveid");
		
		if (loginUserid != null) {
			$("input#loginUserid").val(loginUserid);
			$("input:checkbox[id='saveid']").prop("checked", true);
		}
		
	}    
	
});//end of ready

</script>






<div class="container mt-5" style="border: solid 0px red;">

        <h4>LOGIN / JOIN US</h4>

        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-7 col_mt" style="margin-bottom: -1%;">
                <ul class="nav nav-tabs nav-fill">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="#userlogin">회원으로 로그인</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#adminlogin">관리자로 로그인 </a>
                    </li>
                </ul>
            </div>

        </div>

		<%-- 회원 로그인 --%>
        <div class="tab-content py-3">
            <div class="tab-pane container active" id="userlogin">

                <form name="userloginFrm" action="${pageContext.request.contextPath}/login/login.gu" method="post">

                    <div class="row justify-content-center">
        
                        <div class="col-lg-5 col-md-7 col_mt" style="margin-bottom: -1%;">아이디</div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt">
                            <input class="underline" type="text" name="userid" id="loginUserid" size="20" autocomplete="off"
                                placeholder="아이디" />
                        </div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt" style="margin-bottom: -1%;">비밀번호</div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt">
                            <input class="underline" type="password" name="passwd" id="loginUserpasswd" size="20"
                                autocomplete="off" placeholder="비밀번호" />
                        </div>
                        <div class="w-100"></div>
        
        
                        <div class="col-lg-5 col-md-7 col_mt" style="display: flex; width: 100%;">
                            <div>
                                <input type="checkbox" id="saveid" /><label style="margin-left: 5px; cursor: pointer;"
                                    for="saveid">아이디 저장하기</label>
                            </div>
                            <div style=" display: inline-block; margin-left: auto;">
                                <a style="cursor: pointer; text-align: right; display: inline-block; color: black;"
                                    href="${pageContext.request.contextPath}/login/idFind.gu">아이디찾기</a>
                                /
                                <a style="cursor: pointer; text-align: right; display: inline-block; color: black;"
                                    href="${pageContext.request.contextPath}/login/passwdFind.gu">비밀번호찾기</a>
                            </div>
                        </div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt">
                            <button type="button" id="btnuserSubmit" class="btnstyle" onclick="gouserLogin()">로그인</button>
                        </div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt">혹시 회원이 아니신가요?</div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt"><button type="button" id="btnRegister" class="btnstyle"
                                style="border: solid 0px; background-color: lightslategray;"
                                onclick="goRegister()">회원가입</button>
                        </div>
                    </div>
        
                </form>  

            </div>

            <%-- 관리자 로그인 --%>
            <div class="tab-pane container" id="adminlogin">

                <form name="adminloginFrm" action="${pageContext.request.contextPath}/admin/adminlogin.gu" method="post">

                    <div class="row justify-content-center">
        
                        <div class="col-lg-5 col-md-7 col_mt" style="margin-bottom: -1%;">아이디</div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt">
                            <input class="underline" type="text" name="adminid" id="loginAdminid" size="20" autocomplete="off"
                                placeholder="아이디" />
                        </div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt" style="margin-bottom: -1%;">비밀번호</div>
                        <div class="w-100"></div>
                        <div class="col-lg-5 col-md-7 col_mt">
                            <input class="underline" type="password" name="adminpasswd" id="loginAdminpasswd" size="20"
                                autocomplete="off" placeholder="비밀번호" />
                        </div>
        
                        <div class="w-100" style="margin-bottom: 4.9%;"></div>
                        <div class="col-lg-5 col-md-7 col_mt">
                            <button type="button" id="btnadminSubmit" class="btnstyle" onclick="goadminLogin()">로그인</button>
                        </div>
                        <div class="w-100" style="margin-bottom: 120px"></div>
                    </div>
        
                </form>  

            </div>




        </div>

    </div>





<jsp:include page="/WEB-INF/common/footer1.jsp" /> 








