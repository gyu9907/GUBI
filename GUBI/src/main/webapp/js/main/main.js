$(document).ready(function() {

	selectCollectionByCategory('SEATING')

	 const prevButton = $("button#previous");
	 const nextButton = $("button#next");
	 const productGrid = $("#best-prod-list");
	 const size = 3; // 한 번에 보여질 카드 수
	 const cardWidth = productGrid.children().first().outerWidth(); // 카드하나 width
	 // const totalCards = $productGrid.children().length;

	 let currentIndex = 0;

	 prevButton.on("click", function () {
	   currentIndex -= size;
	   if (currentIndex < 0) {
	     // currentIndex가 음수라면 두번째 슬라이드 페이지 시작점으로
	     currentIndex = size * 2;
	   }
	   slide();
	 });

	 nextButton.on("click", function () {
	   currentIndex += size;
	   if (currentIndex === size * 3) {
	     // currentIndex가 3번째 페이지라면 첫번째 슬라이드 페이지 시작점으로
	     currentIndex = 0; // 다시 처음으로 이동
	   }
	   slide();
	 });

	 function slide() {
	   console.log("currentIndex:", currentIndex);
	   productGrid.css("transform", `translateX(-${currentIndex * cardWidth}px)`);
	 }
	  

});



function selectCollectionByCategory(categoryName) {
	$.ajax({
		url: "/GUBI/collection/collectionListJSON.gu",
		data: {
			"major_category": categoryName,
			"start": 3,
			"len": 10
		},
		dataType: "json",
		success: function(json) {
			let items = json.collectionList;

			let v_html = '';
			
			console.log(items)
			
			$.each(items, function(index, item) {
				v_html += `<a  class="swiper-slide" href="/GUBI/collection/collectionDetail.gu?collectionno=${item.collectionno}">
				<div>
							<img src="/GUBI/data/images/${item.thumbnail_img}" alt="">
						 </div>
						   </a>`;
			})

			$("div#product-grid").html(v_html);
			
			new Swiper('.gallery-thumbs', {
			    slidesPerView: 3,
				spaceBetween: 80,
			    centeredSlides: true,
			    loop: true,
				slideToClickedSlide: true,
				slidesPerGroup: 1,           // 한 번에 1개의 슬라이드씩 이동
				 loopAdditionalSlides: 3,
			    navigation: {
			        nextEl: '#col_next',
			        prevEl: '#col_previous',
			    },
			});
		}
	});
}