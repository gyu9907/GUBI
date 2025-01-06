let start = 1;
let len = 16;
let total = 2;
let requestLock = false;

let inputTimeout;

// 일주일 전 날짜 계산
const oneWeekAgo = new Date();
oneWeekAgo.setDate(new Date().getDate() - 21);

$(document).ready(function() {
	
	$("input[name='searchWord']").focus();
	
	newSearch();
	
	// 카테고리 변경시
	$(document).on("change","select#major_category", function(e){
		selectSmallCategory();
	});
	
	$(window).scroll(function() {
		// 스크롤이 전체 페이지 크기만큼 내려가면
		if( $(window).scrollTop() + $(window).height() + 300 >= $(document).height() ) {
			productSearch();
		}
	});
	
	// 검색어가 바뀔 때 0.5초 후 검색
	$("input").on("input", function() {
		
		start = 1;
		
		if (inputTimeout) {
            clearTimeout(inputTimeout);
        }

		inputTimeout = setTimeout(function(){
			productSearch();
		}, 500);
	});
	
	// 엔터를 눌렀을 때 바로 검색
	$("input").on("keydown", function(e) {
		if(e.keyCode == 13) {
			newSearch();
		}
	});

	// 대분류를 제외한 select 태그 바꿨을 때 바로 검색
	$("select:not(#major_category)").on("change", function(e) {
		newSearch();
	});
	
	// 색상 선택
	$("input:checkbox[name='color']").on("change", (e) => {
		if($(e.target).prop("checked")){
			$(e.target).parent().css({"border-color": "black"});
		}
		else {
			$(e.target).parent().css({"border-color": "#eee"});
		}
		newSearch();
	});
});

// 새로운 조건으로 1페이지부터 검색
function newSearch() {
	start = 1;

	if (inputTimeout) {
	    clearTimeout(inputTimeout);
	}
	productSearch();
}

// 상품 검색
function productSearch() {
	
	if(total <= start) {
		return;
	}
	
	if(requestLock) {
		return;
	}
	requestLock = true;
	
	$("input[name='start']").val(start);
	$("input[name='len']").val(len);
	
	const data = $(document.searchFrm).serialize();
	
	const productGrid = $(".productGrid_wrapper div.product-grid");
	
	$.ajax({
		url: ctxPath+"/product/productSearchScrollJSON.gu",
		data: data,
		dataType: "json",
		success: function(json) {
			const productList = json.productList;

			if(start==1) {
				$(productGrid).html("");
			}
			
			if(productList.length == 0) {
				let html = '<div class="resultEmpty">검색 결과가 없습니다.</div>';

				$(productGrid).html(html);
			}
			else {
				let html = '';
				for(let i=0; i<productList.length; i++) {

					const registerdayDate = new Date(productList[i].registerday);
					const is_new = registerdayDate > oneWeekAgo;
					
					let newTag = ``;
					
					if (is_new) {
						newTag += `<div class="new-tag">New</div>`;
					}
					
					html += `
					<a href="${ctxPath}/product/productDetail.gu?productno=${productList[i].productno}">
						<div class="product-card">
							${newTag}
							<div class="product-image">
								<img src="${ctxPath}/data/images/${productList[i].thumbnail_img}">
							</div>
							<div class="product-info">
								<p>${productList[i].name}</p>
								<div class="price">₩&nbsp;${productList[i].price.toLocaleString('en')}</div>
							</div>
						</div>
					</a>
					`;
				}
				
				$(productGrid).append(html);
				start += productList.length;
				
				total = json.totalProductCnt;
			}

			requestLock = false;
		},
	    error : function(request, status, error) { // 결과 에러 콜백함수
	        console.log(error);
			requestLock = false;
	    }
	});
	
}


// 카테고리 가져오기 from 이혜연
function selectSmallCategory() {
	
	$.ajax({
        url: ctxPath+"/admin/selectSmallCategory.gu",
        data:{"major_category":$("select#major_category").val()}, 
        dataType:"json",
        success:function(json){
        	// console.log(json);
       		let html = `<option value="" selected>Small Category</option>`;
       	
   			$.each(json, function(index, item){
   				console.log(item.small_category);
      	 		html += `<option value='${item.small_category}'>${item.small_category}</option>`;
      	 	});// end of $.each(json, function(index, item)
      	 			
   			$("select#small_category").html(html);
			
			newSearch(); // 카테고리를 불러온 후 검색
        },                   
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });// end of $.ajax
}