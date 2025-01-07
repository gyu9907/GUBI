let start = 1;

$(document).ready(function(){

	$("input[name='freeShipping']").on('click', freeshippingOnclick);
	$("select[name='sortby']").on('change', onChageSort);
	
	let len = 16;
	getTotalCount();
	displayProduct(start);
	
	// === 스크롤 이벤트 발생시키기 시작 === //
    $(window).scroll(function() {

        //console.log( "$(window).scrollTop() + 1  => " + ( $(window).scrollTop() + 1  ) );
        //console.log( "$(document).height() - $(window).height() => " + ( $(document).height() - $(window).height() ) );

		const collection = $("select[name='collection']").val();
		
		if(collection == "") {
			if( $(window).scrollTop() + 1 >= $(document).height() - $(window).height() ) { 
				const totalCount = $("span#totalProductCount").text();
				const countProduct = $("span#countProduct").text();
	        		if(totalCount != countProduct) {  
	                start += len; // start 값에 +16 씩 누적하여 더해준다.
	                displayProduct(start);
	            }
        	}
		}
		
		
    });
    // === 스크롤 이벤트 발생시키기 끝 === //
	
	// 콜렉션별 조회
	$("select[name='collection']").on('change', onChageCollection);

}); // end of $(document).ready(function(){}---------


// 상품목록조회
let len = 16;

// 일주일 전 날짜 계산
const oneWeekAgo = new Date();
oneWeekAgo.setDate(new Date().getDate() - 21);

function displayProduct(start) {
	
	const freeshipping = $("input[name='freeShipping']").is(":checked");

	const sortby = $("select[name='sortby']").val();

	const collection = $("select[name='collection']").val();
	
	$.ajax({
		url: "productDisplayJSON.gu",
		data : {"majorCname" : param_major_category, 
				"smallCname" : param_small_category,
				"freeshipping" :freeshipping,
				"sortby" : sortby,
				"collection" : collection,
				"start" : start,
				"len" : len
			},
		dataType : "json",
		async: false,
		success : function(json) {
			
           let v_html = ``;
			console.log(json);

           if(start == "1" && json.length == 0) { 
 
               v_html = `현재 상품 준비중 입니다...`;
               $("div#product-grid").html(v_html);
           }
           
           else if(json.length > 0 ) {
			
               $.each(json, function(index, item){

					const registerdayDate = new Date(item.registerday);
					
					const is_new = registerdayDate > oneWeekAgo;
	                   
	                v_html += `<a href="/GUBI/product/productDetail.gu?productno=${item.productno}">
								  <div class="product-card">`;
					if (is_new) {
						v_html += `<div class="new-tag">New</div>`;
					}
					
					v_html += `<div class="product-image">
								  <img src="/GUBI/data/images/${item.thumbnail_img}" />
								</div>
							    <div class="product-info">
							      <p>${item.name}</p>
							      <div class="price">&#8361;${item.price.toLocaleString('ko-KR')}</div>
							    </div>
							  </div>
							</a>`;
               }); // end of $.each(json, function(index, item){});-------
			   
				const countProd = $("span#countProduct").text();
				
			    if (start == 1) {
					$("div#product-grid").html(v_html);
					$("span#countProduct").text(len);
			    } 
			    else {
					$("div#product-grid").append(v_html);
					$("span#countProduct").text(parseInt(countProd) + json.length);
			    }
			   

           } // end of else if(json.length > 0) {}--------------
		} ,
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
	});	
}



function getTotalCount() {
	
	const freeshipping = $("input[name='freeShipping']").is(":checked");
	
	$.ajax({
		url: "productListTotalCount.gu",
		data : {"majorCname" : param_major_category, 
				"smallCname" : param_small_category,
				"freeshipping" :freeshipping
			},
		dataType : "json",
		success : function(json) {

			console.log(json);
			   
		   $("span#totalProductCount").text(json.totalProductCount);

		} ,
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
	});	
}

function freeshippingOnclick() {
	start = 1;
	getTotalCount();
	displayProduct(start);
}

function onChageSort() {
	start = 1;
	getTotalCount();
	displayProduct(start);
}

function onChageCollection() {
	start = 1;
	// getTotalCount();
	displayProduct(start);
}






