<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>KG 이니시스 결제</title>

<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script type="text/javascript" src="https://service.iamport.kr/js/iamport.payment-1.1.2.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/order/iamportApiKey.js"></script>

<script type="text/javascript">

$(document).ready(function() {
	// 참고 링크 http://www.iamport.kr/getstarted
    var IMP = window.IMP;     // 생략가능
    IMP.init(iamportApiKey);  // 아임포트에 가입시 부여받은 "가맹점 식별코드". "iamportApiKey.js"에서 가져옴
	
   // 결제요청하기
   IMP.request_pay({
       pg : 'html5_inicis', // 결제방식 PG사 구분
       pay_method : 'card',	// 결제 수단
       merchant_uid : 'merchant_' + new Date().getTime(), // 가맹점에서 생성/관리하는 고유 주문번호
       name : '${requestScope.order_name}',   // 코인충전 또는 order 테이블에 들어갈 주문명 혹은 주문 번호. (선택항목)원활한 결제정보 확인을 위해 입력 권장(PG사 마다 차이가 있지만) 16자 이내로 작성하기를 권장
       amount : ${requestScope.amount_paid},  // '${coinmoney}'  결제 금액 number 타입. 필수항목. 
       buyer_email : '${sessionScope.loginuser.email}',  // 구매자 email
       buyer_name : '${sessionScope.loginuser.name}',	   // 구매자 이름 
       buyer_tel : '${sessionScope.loginuser.tel}',   // 구매자 전화번호 (필수항목)
       buyer_addr : '',  
       buyer_postcode : '',
       m_redirect_url : ''  // 휴대폰 사용시 결제 완료 후 action : 컨트롤러로 보내서 자체 db에 입력시킬것!
   }, function(rsp) {
       /*
		   if ( rsp.success ) {
			   var msg = '결제가 완료되었습니다.';
			   msg += '고유ID : ' + rsp.imp_uid;
			   msg += '상점 거래ID : ' + rsp.merchant_uid;
			   msg += '결제 금액 : ' + rsp.paid_amount;
			   msg += '카드 승인번호 : ' + rsp.apply_num;
		   } else {
			   var msg = '결제에 실패하였습니다.';
			   msg += '에러내용 : ' + rsp.error_msg;
		   }
		   alert(msg);
	   */

		if ( rsp.success ) {
			window.opener.goPaymentSuccess("${requestScope.use_point}");
			
		    self.close(); // 팝업창 닫기
			
        } else {
            alert("결제에 실패하였습니다. 다시 결제를 진행해주세요.");
		    self.close(); // 팝업창 닫기
       }

   }); // end of IMP.request_pay()----------------------------
   

	window.opener.goPaymentSuccess("${requestScope.use_point}");

}); // end of $(document).ready()-----------------------------

</script>
</head>	

<body>
</body>
</html>
