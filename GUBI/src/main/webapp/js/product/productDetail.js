const query = document.location.search;
const param = new URLSearchParams(query);

const param_productno = param.get('productno');


$(document).ready(function() {
	selectReviewList();

	$('span#read_more').on('click', function() {

		const content = $('p#product_desc');
		const read_more_btn = $('span#read_more');

		if (content.css('max-height') === '70px') {
			content.css('max-height', content.get(0).scrollHeight + 'px');
			read_more_btn.text('Read less');
		} else {
			content.css('max-height', '70px');
			read_more_btn.text('...Read More');
		}

	})

	/* 색상 버튼 클릭할 때 */
	$('button.color-button').on('click', function() {
		var imgSrc = $(this).data('img');
		var name = $(this).data('name');
		var optionno = $(this).data('optionno');

		/* 색상 버튼에 border 준다. */
		$('button.color-button').css('border', 'none');
		$(this).css('border', '1px solid #000');

		/* 이미지 영역에 이미지 세팅 */
		$('#product_image').attr('src', `/GUBI/data/images/${imgSrc}`);
		/* 옵션 뿌려지는 영역에 옵션명 세팅 */
		$('#prod_opt_name').text(name);

		/* 장바구니 및 구매 페이지로 이동할 때 사용하는 input 값에 optionno 세팅 */
		$('input[name="optionno"]').val(optionno);

	});

	/* count 수정할 때 이벤트 처리 */
	var totalCount = parseInt($("input[name='prodCount']").val()); // 상품의 전체 개수 (재고)
	$('input.product-cnt').on('change', function(e) {
		var value = parseInt(e.target.value)
		if (value > totalCount) {
			alert('재고보다 많은 수량입니다.')
			$('input.product-cnt').val(totalCount);  // 최대 재고 수량으로 설정
		}
		if (value < 1) {
			alert('수량은 1개 이상 선택해야 합니다.')
			$('input.product-cnt').val(1);  // 최소 수량 1으로 설정
		}
	});
})

function addCart() {
	const frm = document.addCartFrm;

	frm.method = "POST";
	frm.action = "/GUBI/product/cartAdd.gu";
	frm.submit();
}

function selectReviewList() {

	$.ajax({
		url: "/GUBI/product/reviewList.gu",
		data: { productno: param_productno },
		dataType: "json",
		success: function(json) {
			let html = '';

			/* 전역변수에 세팅*/
			reviewList = json;

			/* ⭐⭐⭐⭐☆  추후에 이미지 컬러박스 추가 예정 */
			$.each(json, function(index, item) {

				let score = starscore(item.score);

				console.log(item)

				html += `<div class="review-item">
							  <div class="review_title_wrapper">
								<span class="review_title">${item.title}</span>
								<span class="review_registerday">${item.registerday.substring(0, 10)}</span>
							  </div>
							  <p class="review-rating">${score}<span class="review_font_s" style="margin-left:3px;">${item.fk_userid}</span></p>
							  <p class="review_font_s" style="font-size: 11px;">${item.name}</p>`

			  /*<button data-toggle="modal" data-target="#addReviewModal" onclick="editReview('${index}')">
			  	수정
			  </button>
			  <button onclick="deleteReview('${item.reviewno}','${item.img}')">
			    삭제
			  </button>*/
				if (item.img != null) {
					html += `<div class="review_img">
								<img src="/GUBI/data/images/${item.img}" />
							 </div>
								<p class="review-comment">${item.content}</p>`;
				} else {
					html += `<p class="review-comment">${item.content}</p>`;
				}

				html += `</div>`;

			});

			$("div#reviewList").html(html);

		}
	});
}

function starscore(score) {
	let notemptystar = '★'.repeat(score);
	let emptystar = '☆'.repeat(5 - score);

	return notemptystar + emptystar;
}

// 주문 버튼 //
function goOrderPage() {
	
	var optionno = $("input[name='optionno']").val();
	var cnt = $("input[name='cnt']").val();
	

	if (loginuser != "null") {

		const frm = document.addCartFrm;

		frm.method = "POST";
		frm.action = "/GUBI/order/order.gu";
		frm.submit();


	} else {
		alert("주문을 하시려면 먼저 로그인 하세요!!");
		// 로그인중이 아니라면 로그인페이지로 이동
		location.href = '/GUBI/login/login.gu';
	}

} // end of function goOrderPage()-------------------------