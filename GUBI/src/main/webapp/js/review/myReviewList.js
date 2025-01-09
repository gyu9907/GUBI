let start = 1;
let len = 10;
let reviewList = [];

$(document).ready(function() {
	// 각 리뷰의 별점을 표시할 span을 찾아서 처리
	/*    $('.star-rating .score').each(function() {
			const score = $(this).data('value'); // data-value에서 score 값을 가져옴
			const stars = starscore(score);  // 별점 계산
			$(this).text(stars);  // 텍스트로 별점을 삽입
		});*/

	displayReview(start)
	// === 스크롤 이벤트 발생시키기 시작 === //
	$(window).scroll(function() {
		const collection = $("select[name='collection']").val();
		if (collection == "") {
			if ($(window).scrollTop() + 1 >= $(document).height() - $(window).height()) {
				const totalCount = $("input#totalCount").text();
				const currentCount = $("input#currentCount").text();
				if (totalCount != currentCount) {
					start += len; // start 값에 +16 씩 누적하여 더해준다.
					displayReview(start);
				}
			}
		}


	});
	// === 스크롤 이벤트 발생시키기 끝 === //
});


function displayReview(start) {

	$.ajax({
		url: "/GUBI/review/myReviewDisplayJSON.gu",
		data: {
			"start": start,
			"len": len
		},
		dataType: "json",
		async: false,
		success: function(json) {

			let v_html = ``;
			console.log(json);

			if (start == "1" && json.length == 0) {

				v_html = `현재 상품 준비중 입니다...`;
				$("div#reviews-list").html(v_html);
			}

			else if (json.length > 0) {
				$.each(json, function(index, item) {
				   reviewList.push(item)
					
					v_html += `
					<div class="review-item">
						<div class="review-header">
							<span class="review-author">${item.title}</span>
							<span class="review-date">${item.registerday}</span>
						</div>
						<div class="star-rating">
			             	<span class="score">${starscore(item.score)}</span>
			            </div>
						<div class="review_img">
							<img id="imgPreview" src="/GUBI/data/images/${item.img}" />
						</div> 
						<p class="review-text">${item.content}</p>
						<div class="review-actions">
							<button onclick="location.href='/GUBI/product/productDetail.gu?productno=${item.productno}'">
								상품 보러가기
							</button>
							<button class="edit-btn" data-toggle="modal" data-target="#addReviewModal" onclick="editReview(${index})">수정</button>
							<button class="delete-btn" onclick="deleteReview('${item.reviewno}','${item.img}')">삭제</button>
						</div>
					</div>
					`
				}); // end of $.each(json, function(index, item){});-------

				const count = $("span#currentCount").text();

				if (start == 1) {
					$("div#reviews-list").html(v_html);
					$("span#currentCount").text(len);
				}
				else {
					$("div#reviews-list").append(v_html);
					$("span#currentCount").text(parseInt(count) + json.length);
				}


			} // end of else if(json.length > 0) {}--------------
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}



// 별점 계산 함수
function starscore(score) {
	let notemptystar = '★'.repeat(score); // 채워진 별
	let emptystar = '☆'.repeat(5 - score); // 빈 별

	return notemptystar + emptystar; // 채워진 별 + 빈 별
}
