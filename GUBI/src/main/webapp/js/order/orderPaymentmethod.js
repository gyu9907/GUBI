let win;

$(document).ready(function() {
	
	$("input#usePoint").on("focus", function(){
		if(win && !win.closed){
			$("input#usePoint").attr("readonly", true);
			return;
		}
		else {
			$("input#usePoint").attr("readonly", false);
			return;
		}
	});
	
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
		
		if(win && !win.closed){
			alert("결제가 진행중이므로 수정이 불가합니다.");
			return;
		}

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

		if(win && !win.closed){
			alert("이미 결제가 진행중입니다.");
			return;
		}
		
		const width = 1000;
		const height = 600;

		const left = Math.ceil((window.screen.width - width) / 2); // 정수로 만듬
		const top = Math.ceil((window.screen.height - height) / 2); // 정수로 만듬

		const title = "payment";
		
		win = window.open("", title,
			`left=${left}, top=${top}, width=${width}, height=${height}`);
			
		const orderFrm = document.orderFrm;
		orderFrm.method = "post";
		orderFrm.action = ctxPath + "/order/orderPayment.gu";
		orderFrm.target = title;
		orderFrm.submit();

	});
	
	$("button#PrevButton").on("click", function() {
		
		if(win && !win.closed){
			alert("결제가 진행중이므로 이동이 불가합니다.");
			return;
		}
		
		history.back();
	});
});

function goPaymentSuccess(userid, use_point) {
	
	console.log(userid, use_point);

	const formData = new FormData(document.orderFrm);
	
	formData.append("userid", userid);
	formData.set("use_point", use_point);
	
	formData
	
	$.ajax({
		url: ctxPath + "/order/orderRegister.gu",
		enctype: 'multipart/form-data',
		data: formData,
		processData: false,
		contentType: false,
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