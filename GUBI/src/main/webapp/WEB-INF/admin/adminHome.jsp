<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctx_Path = request.getContextPath();
    //    /MyMVC
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 메인페이지</title>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> 

<!-- Font Awesome 6 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<!-- jquery js-->
<script type="text/javascript" src="<%= ctx_Path%>/js/jquery-3.7.1.min.js"></script>
<!-- Bootstrap js-->
<script type="text/javascript" src="<%= ctx_Path%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="<%= ctx_Path%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" type="text/css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" href="<%= ctx_Path%>/css/admin/adminHome.css">

</head>
<body>
<div class="">

        <!-- 관리자메뉴-->
        <div id="header">   
            <a href="<%= ctx_Path%>/admin/admin.gu">
	 			<img id="logo" src="<%= ctx_Path%>/image/logo.png" onclick="<%= ctx_Path%>/admin/admin.gu"/>
	  		</a>
        </div><!-- header-->
       

        <div id="article" class="bg-light">
            <div id="adminmenu" class="bg-white">
                    <!-- Links 추가하기 -->
                    <ul class="navbar-nav">
                        <li class="dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/admin.gu">HOME</a>
                        </li>
                        <li class="dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/member.gu">MEMBER</a>
                        </li>
                        <li class="dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/order.gu">ORDER</a>
                        </li>
                        <li class="dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/category.gu">CATEGORY</a>
                        </li>
                        <li class="dropdown-item">
                        <a class="nav-link" href="<%= ctx_Path%>/admin/product.gu">PRODUCT</a>
                        </li>
                        <li class="dropdown-item">
                        <a class="nav-link">STATUS</a>
                        </li>
                    </ul>
            </div> <!-- adminmenu-->
    
            <div id="contents">

                <div id="todayissue" class="border rounded-sm">
                    <span class="today">Today</span>

                    <hr style="border: solid 1px black; opacity:100%">

                    <div id="card" class="row justify-content-around mt-4">
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Visitor</h5>
                            <i class="icon fa-solid fa-house fa-4x mb-3 mt-1" style="color:#493628"></i>
                            <h3 class="card-text mt-4">0</h3>
                            </div>
                        </div>
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Register</h5>
                            <i class="icon fa-solid fa-user fa-4x mb-3 mt-1" style="color:#493628"></i>
                            <h3 class="card-text mt-4">0</h3>
                            </div>
                        </div>
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Purchase</h5>
                            <i class="icon fa-solid fa-cart-shopping fa-4x mb-3 mt-1" style="color:#493628"></i>
                            <h3 class="card-text mt-4">0</h3>
                            </div>
                        </div>
                        <div class="row justify-content-around" style="width: 18rem;">
                            <div class="card-body">
                            <h5 class="card-title mb-4">Sales</h5> 
                            <i class="fa-solid fa-sack-dollar fa-4x mb-3 mt-1" style="color:#493628"></i>
                            <h3 class="card-text mt-4">0</h3>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="week">
                    <div id="money" class="row justify-content-between mb-3">
                        <div class="col"><h6>Weekly Visitor</h6><h4>40</h4></div>
                        <div class="col"><h6>Weekly Register</h6><h4>30</h4></div>
                        <div class="col"><h6>Weekly Purchase</h6><h4>30</h4></div>
                        <div class="col"><h6>Weekly Sales</h6><h4>500,000</h4></div>
                    </div>
                </div>
                <div id="month">
                    <div id="money" class="row justify-content-between mb-3">
                        <div class="col"><h6>Monthly Visitor</h6><h4>500</h4></div>
                        <div class="col"><h6>Monthly Register</h6><h4>78</h4></div>
                        <div class="col"><h6>Monthly Purchase</h6><h4>90</h4></div>
                        <div class="col"><h6>Monthly Sales</h6><h4>1,400,000</h4></div>
                    </div>
                </div>
                

            <div id="info">
                <div id="tables" ><!-- 최근주문, 최근가입-->
                    <!-- 최근주문내역-->
                    <div id="allorder" class="border rounded-sm">
                        <div id="ordertitle">
                            <div class="title mr-2">Recent Orders</div>
                            <button type="button" class="datebtn btn"><span>today</span></button>
                            <button type="button" class="datebtn btn"><span>yesterday</span></button>
                            <button type="button" class="datebtn btn"><span>week</span></button>
                            <button type="button" class="viewbtn btn"><span>+ more</span></button>
                        </div>
                        <div>
                            <table class="table table-sm mt-2">
                                <thead>
                                    <tr>
                                    <th>주문번호</th>
                                    <th>주문자명</th>
                                    <th>전화번호</th>
                                    <th>총주문액</th>
                                    <th>주문일시</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>10001</td>
                                        <td>이진호</td>
                                        <td>010-1234-5678</td>
                                        <td>160,000</td>
                                        <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>10001</td>
                                        <td>한민정</td>
                                        <td>010-1234-5678</td>
                                        <td>160,000</td>
                                        <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                    <td>10001</td>
                                    <td>이혜연</td>
                                    <td>010-1234-5678</td>
                                    <td>160,000</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="page" class="justify-content-center">
                            <ul class="pagination pagination-sm justify-content-center" style="margin:20px 0">
                                <li class="page-item disabled"><a class="page-link" href="#">&laquo;</a></li>
                                <li class="page-item"><a class="page-link" href="#">1</a></li>
                                <li class="page-item"><a class="page-link" href="#">2</a></li>
                                <li class="page-item"><a class="page-link" href="#">3</a></li>
                                <li class="page-item"><a class="page-link" href="#">&raquo;</a>
                              </ul>
                        </div>
                    </div><!-- allorder -->
                    
                    <!-- 최근회원가입-->
                    <div id="registerlist" class="border rounded-sm">
                        <div id="registertitle">
                            <div class="title mr-2">Recent Register</div>
                            <button type="button" class="datebtn btn"><span>today</span></button>
                            <button type="button" class="datebtn btn"><span>yesterday</span></button>
                            <button type="button" class="datebtn btn"><span>week</span></button>  
                            <button type="button" class="viewbtn btn"><span>+ more</span></button>
                        </div>
                        <div>
                            <table class="table table-sm mt-2">
                                <thead>
                                    <tr>
                                    <th>회원번호</th>
                                    <th>이름</th>
                                    <th>아이디</th>
                                    <th>이메일</th>
                                    <th>접속횟수</th>
                                    <th>가입일시</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                    <td>1001</td>
                                    <td>이혜연</td>
                                    <td>leehy</td>
                                    <td>leehy@naver.com</td>
                                    <td>3회</td>
                                    <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                        <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                         <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                         <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                        <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                         <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                         <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</td>
                                         <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                                <tbody>
                                    <tr>
                                        <td>1001</td>
                                        <td>이혜연</td>
                                        <td>leehy</td>
                                        <td>leehy@naver.com</td>
                                        <td>3회</th>
                                         <td>2024-12-10</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="page" class="justify-content-center">
                            <ul class="pagination pagination-sm justify-content-center" style="margin:20px 0">
                                <li class="page-item disabled bg-dark"><a class="page-link" href="#">&laquo;</a></li>
                                <li class="page-item bg-dark"><a class="page-link" href="#">1</a></li>
                                <li class="page-item bg-dark"><a class="page-link" href="#">2</a></li>
                                <li class="page-item bg-dark"><a class="page-link" href="#">3</a></li>
                                <li class="page-item"><a class="page-link" href="#">&raquo;</a>
                              </ul>
                        </div>
                    </div> <!--registerlist -->
                </div>
                    
                <!-- 최근리뷰-->
                <div id="review" class="border rounded-sm">
                    <div id="reviewtitle">
                        <div class="title mr-2">Recent Reviews</div>
                        <button type="button" class="datebtn btn"><span>today</span></button>
                        <button type="button" class="datebtn btn"><span>yesterday</span></button>
                        <button type="button" class="datebtn btn"><span>week</span></button>          
                        <button type="button" class="viewbtn btn"><span>+ more</span></button>
                    </div>
                    <div>
                        <table class="table table-hover mt-2 overflow-auto">
                            <thead>
                                <tr>
                                <th>상품번호</th>
                                <th>상품명</th>
                                <th>작성자아이디</th>
                                <th>작성내용</th>
                                <th>작성일시</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                <td>2001</td>
                                <td>멋진의자</td>
                                <td>leehy</td>
                                <td>멋져요 편합니다...</td>
                                <td>2024-12-10</td>
                                </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>

                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>

                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                   <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                    <td>2001</td>
                                    <td>멋진의자</td>
                                    <td>leehy</td>
                                    <td>멋져요 편합니다...</td>
                                    <td>2024-12-10</td>
                                    </tr>
                            </tbody>
                        </table>
                    </div>
                </div> <!-- review -->
            </div>
            
        </div><!-- contents-->
        </div><!-- article-->
        
    </div><!-- container-fluid -->
</body>
</html>