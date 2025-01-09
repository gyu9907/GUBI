function selectAll() {
    // 모든 checkbox를 체크/언체크하는 함수
    var isChecked = document.getElementById('check-all').checked;
    var checkboxes = document.querySelectorAll("input[name='cartno']");
    
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = isChecked;
    });
}

// AJAX 요청으로 장바구니 개수 업데이트
function updateCartCount(cartno, newCount) {
    $.ajax({
        url: ctxPath+'/cart/cartUpdate.gu', // 서버 URL 음 이건 ctxPath를 쓰는게 맞다! 
        type: 'POST',
        data: {
            "cartno": cartno,
            "cnt": newCount
        },
		dataType: "json",
        success: function(json) {
            if (json.success) { // 응답 성공
				
				window.location.reload();
				
            } else {
                alert(json.message);
            }
        },
        error: function() {
            alert("서버와 연결할 수 없습니다.");
        }
    });
}

// AJAX 요청으로 장바구니 삭제
function deleteCart(cartno) {
    $.ajax({
        url: ctxPath+'/cart/cartDelete.gu', // 서버 URL 음 이건 ctxPath를 쓰는게 맞다! 
        type: 'POST',
        data: {
            "cartno": cartno,
        },
		dataType: "json",
        success: function(json) {
            if (json.success) { // 응답 성공
				
				window.location.reload();
				
            } else {
                alert(json.message);
            }
        },
        error: function() {
            alert("서버와 연결할 수 없습니다.");
        }
    });
}

// 총 금액, 배송비, 포인트를 계산하는 함수
function calcTotal() {
	// 결과 값 변경
	let totalPrice = 0;
	let totalPoint = 0;
	let totalDelivery = 10000000000;

	$("input[name='cartno']:checked").each((index, elmt) => {
		totalPrice += Number($(elmt).parent().find(".ProductPrice").text().replace(/[^0-9]/g, ''));
		totalPoint += Number($(elmt).parent().find(".Point").text().replace(/[^0-9]/g, ''));
		totalDelivery = (totalDelivery < Number($(elmt).data("delivery_price")))? totalDelivery:Number($(elmt).data("delivery_price"));
	});

	if(totalDelivery == 10000000000) {
		totalDelivery = 0;
	}

	$("#totalPrice").text("₩ " + totalPrice.toLocaleString('en'));
	$("#totalPoint").text(totalPoint.toLocaleString('en') + "P");
	$("#totalDelivery").text("₩ " + totalDelivery.toLocaleString('en'));
}

$(document).ready(function() {

	$("input#check-all").prop("checked", true);
	selectAll();

	$("input#check-all").on("click", function() {
		selectAll();
		calcTotal(); // 총 금액, 배송비, 포인트를 계산
	});

	// 개수 줄이기 버튼 클릭
    $(".ProductMinusBtn").click(function() {
        var $productContainer = $(this).closest('.Product-container');
        var $productCnt = $productContainer.find('.ProductCnt');
        var currentCnt = parseInt($productCnt.text()); // 현재 개수
        var cartno = $(this).data('cartno'); // 상품 ID

        if (currentCnt > 1) {
            // 개수 하나 줄임
            var newCnt = currentCnt - 1;
//            $productCnt.text(newCnt); // 화면에 개수 업데이트
            $(this).prop('disabled', newCnt === 1); // 개수가 1이면 - 버튼 비활성화

            // AJAX로 서버에 새로운 개수 전송
            updateCartCount(cartno, newCnt);
        }
    });

    // 개수 늘리기 버튼 클릭
    $(".ProductPlusBtn").click(function() {
        var $productContainer = $(this).closest('.Product-container');
        var $productCnt = $productContainer.find('.ProductCnt');
        var currentCnt = parseInt($productCnt.text()); // 현재 개수
        var cartno = $(this).data('cartno'); // 상품 ID
        var p_cnt = parseInt($(this).data('p_cnt')); // 재고 수량

        if (currentCnt < p_cnt) {
            // 개수 하나 늘림
            var newCnt = currentCnt + 1;
//            $productCnt.text(newCnt); // 화면에 개수 업데이트
            $(this).prop('disabled', newCnt === p_cnt); // 재고가 최대 개수면 + 버튼 비활성화한다! 중요함 

            // AJAX로 서버에 새로운 개수 전송
            updateCartCount(cartno, newCnt);
        } else {
            alert("상품 재고가 주문 수 보다 적습니다");
        }
    });
	
	// 장바구니 삭제 버튼
	$(".delete-product-btn").on("click", function() {
		let cartno = $(this).data('cartno');
		if(confirm('상품을 장바구니에서 삭제하시겠습니까?')) {
			deleteCart(cartno);
		}
	});
	
	$("input[name='cartno']").on("change", function() {
		// 전체 체크박스 체크/해제
		if($("input[name='cartno']").length == $("input[name='cartno']:checked").length) {
			$('#check-all').prop("checked", true);
		}
		else {
			$('#check-all').prop("checked", false);
		}
		
		calcTotal(); // 총 금액, 배송비, 포인트를 계산
	});
	
	$("button#goCheckoutBtn").on("click", function() {
		if($("input[name='cartno']:checked").length == 0) {
			alert("상픔을 1개 이상 선택하세요.");
			return;
		}
		
		const cartFrm = document.cartFrm;
		
		cartFrm.action = ctxPath + "/order/order.gu";
		cartFrm.method = "post";
		cartFrm.submit();
	});

    // 장바구니에 담긴 상품의 가격과 개수를 가져와 계산
//    $(".ProductCnt").each(function() {
//        var count = $(this).text(); // 장바구니에 담긴 개수
//        var price = $(this).closest('.Product-container').find('.product-price').text(); // 상품 가격
//
//        // 가격 계산 (상품 가격 * 개수)
//        var totalPrice = parseInt(price.replace(/[^0-9]/g, '')) * parseInt(count); // 원화 기호 제거 후 계산
//
//        // 계산된 가격을 해당 부분에 출력
//        $(this).closest('.Product-container').find('.ProductPrice').text('₩' + totalPrice.toLocaleString()); // 가격에 콤마 추가
//    });
});

