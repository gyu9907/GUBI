$(document).ready(function() {
	
	// 다음 버튼 클릭시
	$("button#NextButton").on("click", function() {
		if(!isValid()) {
			return;
		}
		
		const orderFrm = document.orderFrm;
		
		orderFrm.action = ctxPath + "/order/orderPaymentmethod.gu"
		orderFrm.method = "post";
		orderFrm.submit();
	});
	
	// 배송지 변경 버튼 클릭시
	$("button#selectDelivery").on("click", function() {

		const width = 1000;
		const height = 800;

		const left = Math.ceil((window.screen.width - width) / 2); // 정수로 만듬
		const top = Math.ceil((window.screen.height - height) / 2); // 정수로 만듬
		
		const url = ctxPath + "/delivery/deliveryListPopup.gu";

		window.open(url, "delivery",
			`left=${left}, top=${top}, width=${width}, height=${height}`);
	});
});

function isValid() {
	const orderFrm = document.orderFrm;
	
	let deliveryno = $(orderFrm).find("input[name='deliveryno']").val();
	
	if(deliveryno == "") {
		alert("배송지를 선택해야 합니다.");
		return false;
	}
	
	return true;
}

function setDelivery(deliveryno) {
	$.ajax({
		url:ctxPath + "/delivery/getDeliveryJSON.gu",
		
	});
	
	$.ajax({
		url: ctxPath + "/delivery/getDeliveryJSON.gu",
		data: {"deliveryno":deliveryno},
		type: "post",
		dataType: "json",
		success: function(json) {
			if(json.deliveryno) {
				$("input[name='deliveryno']").val(json.deliveryno);
				$("input#receiver").val(json.receiver);
				$("input#receiver_tel").val(json.receiver_tel.substring(0,3)+"-"+json.receiver_tel.substring(3,7)+"-"+json.receiver_tel.substring(7,11));
				$("input#postcode").val(json.postcode);
				$("input#address").val(json.address);
				$("input#detail_address").val(json.detail_address);
				$("input#memo").val(json.memo);
				
				if($("#deliveryEmpty").css("display") != "none") {
					$("#deliveryEmpty").hide();
					$("#deliveryInfo").show();
				}
			}
			else {
				alert(json.message);
			}
		},
		error: function(request, status, error){
		    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		}
	});
}