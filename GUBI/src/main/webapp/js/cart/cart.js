function selectAll() {
    // 모든 checkbox를 체크/언체크하는 함수
    var isChecked = document.getElementById('check-all').checked;
    var checkboxes = document.querySelectorAll("input[name='cartno']");
    
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = isChecked;
    });
}


$(document).ready(function() {


// 개수 줄이기 버튼 클릭
    $(".ProductMinusBtn").click(function() {
        var $productContainer = $(this).closest('.Product-container');
        var $productCnt = $productContainer.find('.ProductCnt');
        var currentCnt = parseInt($productCnt.text()); // 현재 개수
        var productId = $(this).data('product-id'); // 상품 ID

        if (currentCnt > 1) {
            // 개수 하나 줄임
            var newCnt = currentCnt - 1;
            $productCnt.text(newCnt); // 화면에 개수 업데이트
            $(this).prop('disabled', newCnt === 1); // 개수가 1이면 - 버튼 비활성화

            // AJAX로 서버에 새로운 개수 전송
            updateCartCount(productId, newCnt);
        }
    });

    // 개수 늘리기 버튼 클릭
    $(".ProductPlusBtn").click(function() {
        var $productContainer = $(this).closest('.Product-container');
        var $productCnt = $productContainer.find('.ProductCnt');
        var currentCnt = parseInt($productCnt.text()); // 현재 개수
        var productId = $(this).data('product-id'); // 상품 ID
        var stock = parseInt($(this).data('stock')); // 재고 수량

        if (currentCnt < stock) {
            // 개수 하나 늘림
            var newCnt = currentCnt + 1;
            $productCnt.text(newCnt); // 화면에 개수 업데이트
            $(this).prop('disabled', newCnt === stock); // 재고가 최대 개수면 + 버튼 비활성화한다! 중요함 

            // AJAX로 서버에 새로운 개수 전송
            updateCartCount(productId, newCnt);
        } else {
            alert("상품 재고가 주문 수 보다 적습니다");
        }
    });

    // AJAX 요청으로 장바구니 개수 업데이트
    function updateCartCount(productId, newCount) {
        $.ajax({
            url: ctxPath+'/cart/cart.gu', // 서버 URL 음 이건 ctxPath를 쓰는게 맞다! 
            type: 'POST',
            data: {
                productId: productId,
                newCount: newCount
            },
            success: function(response) {
                if (response.success) {
                    // 만약에 응답이 성공되어진다면?  
                    updatePrice(); // 가격을 업데이트한다! 
                } else {
                    alert("장바구니 업데이트 중 오류가 발생했습니다.");
                }
            },
            error: function() {
                alert("서버와 연결할 수 없습니다.");
            }
        });
    }

    // 가격 업데이트 함수 (기존과 동일)
    function updatePrice() {
        $(".Product-container").each(function() {
            var $productContainer = $(this);
            var price = parseInt($productContainer.find('.product-price').text().replace(/[^0-9]/g, '')); // 가격
            var count = parseInt($productContainer.find('.ProductCnt').text()); // 장바구니 개수
            var totalPrice = price * count; // 총 가격 계산
            $productContainer.find('.ProductPrice').text('₩' + totalPrice.toLocaleString()); // 가격에 원화 표시
        });
    }


    // 장바구니에 담긴 상품의 가격과 개수를 가져와 계산
    $(".ProductCnt").each(function() {
        var count = $(this).text(); // 장바구니에 담긴 개수
        var price = $(this).closest('.Product-container').find('.product-price').text(); // 상품 가격

        // 가격 계산 (상품 가격 * 개수)
        var totalPrice = parseInt(price.replace(/[^0-9]/g, '')) * parseInt(count); // 원화 기호 제거 후 계산

        // 계산된 가격을 해당 부분에 출력
        $(this).closest('.Product-container').find('.ProductPrice').text('₩' + totalPrice.toLocaleString()); // 가격에 콤마 추가
    });
});

