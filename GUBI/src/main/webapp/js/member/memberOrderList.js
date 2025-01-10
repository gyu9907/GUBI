$(document).ready(function() {
	// 주문취소 버튼을 눌렀을 때
	$("button#orderCancel").on("click", function() {
		if(confirm("주문을 취소하시겠습니까?")) {
			updateOrder($(this).data("orderno"), 3); // 주문취소 request
		}
	});
	
	// 구매확정 버튼을 눌렀을 때
	$("button#orderConfirm").on("click", function() {
		if(confirm("구매확정을 하시겠습니까?")) {
			updateOrder($(this).data("orderno"), 6); // 구매확정 request
		}
	});

	// 환불신청 버튼을 눌렀을 때
	$("button#orderRefund").on("click", function() {
		if(confirm("환불신청을 하시겠습니까?")) {
			updateOrder($(this).data("orderno"), 7); // 환불신청 request
		}
	});

	// 리뷰확인 버튼을 눌렀을 때
	$("button#reviewCheck").on("click", function() {
		const productno = $(this).data("productno");
		
		location.href=ctxPath + '/product/productDetail.gu?productno=' + productno +"#reivewDiv";
	});
	
});

function updateOrder(orderno, status) {
	
	$.ajax({
        url: ctxPath + '/order/orderStatusUpdate.gu', 
        type: 'POST',
        data: {
            "orderno": orderno,
			"status": status
        },
		dataType: "json",
        success: function(json) {
			alert(json.message);
			window.location.reload();
        },
        error: function() {
            alert("서버와 연결할 수 없습니다.");
        }
    });
}