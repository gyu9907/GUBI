$(document).ready(function() {
	
	// 포인트 input 태그
	$("input#usePoint").on("input", function(e) {
		
		$(e.target).val($(e.target).val().replace(/[^0-9]/g, ''));
		
		const memberPoint = Number($("div#memberPoint").text().replace(/[^0-9]/g, ''));
		let point = Number($(e.target).val());
		
		
		if(point > memberPoint) {
			$(e.target).val(memberPoint);
		}
		
		/* 포인트 차감 후 가격 계산 및 표시 */
		point = Number($(e.target).val());
		
		let amountTotal = Number($("#amountTotal").text().replace(/[^0-9]/g, ''));
		
		amountPaid = "₩ " + (amountTotal - point).toLocaleString("en");
		$("#amountPaid").text(amountPaid);
		
		let usePointResult = ((point != 0)?"- ":"") + point.toLocaleString("en") + "P";
		$("#usePointResult").text(usePointResult);
		
	});
	
	// 포인트 전액사용 버튼
	$("button#useAllPointBtn").on("click", function() {

		const memberPoint = Number($("div#memberPoint").text().replace(/[^0-9]/g, ''));
		
		$("input#usePoint").val(memberPoint);

		/* 포인트 차감 후 가격 계산 및 표시 */
		let amountTotal = Number($("#amountTotal").text().replace(/[^0-9]/g, ''));

		amountPaid = "₩ " + (amountTotal - memberPoint).toLocaleString("en");
		$("#amountPaid").text(amountPaid);
		
		let usePointResult = ((memberPoint != 0)?"- ":"") + memberPoint.toLocaleString("en") + "P";
		$("#usePointResult").text(usePointResult);
	});
	
	// 다음 버튼
	$("button#NextButton").on("click", function() {
		const width = 1000;
		const height = 600;

		const left = Math.ceil((window.screen.width - width) / 2); // 정수로 만듬
		const top = Math.ceil((window.screen.height - height) / 2); // 정수로 만듬

		let win = window.open("", "payment",
			`left=${left}, top=${top}, width=${width}, height=${height}`);
			
		const orderFrm = document.orderFrm;
		orderFrm.method = "post";
		orderFrm.action = ctxPath + "/order/orderPayment.gu";
		orderFrm.submit();
		
	});
});



function goPaymentSuccess(use_point) {
	
	$("input#usePoint").val(use_point);
	
	const data = $(document.orderFrm).serialize();
	
	$.ajax({
		url: ctxPath + "/order/orderRegister.gu",
		data: data,
		type: "post",
		dataType: "json",
		success: function(json) {
			alert(json.message);
			location.href = json.loc;
		},
		error: function(request, status, error){
		    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		}
	});
	
}