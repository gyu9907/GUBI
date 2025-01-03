$(document).ready(function(){

	$(document).on("change","select#major_category", function(e){	
		selectSmallCategory();
	});// end of $(document).on("change","select#major_category", function(e)
	
	$('[data-toggle="tooltip"]').tooltip();

	$("tr#tr").on("click", function(){

		const productNo = $(this).data("id"); // tr의 dataid 
		
		$.ajax({
			url:"detailProduct.gu",
	        data:{"productno": productNo}, 
	        dataType:"json",  
			success:function(response){
				//console.log("Received JSON:", option);    // 전체 데이터 확인

				const modalbody = $("div.modal-body");
	        	modalbody.empty(); // 기존 내용을 비우기

				let html = ``;
				response.product.forEach((item, index) => {

					html += `<div class="left">
							<div>
								<span>Product ${item.productno}</span>
								<span class="regiday">${item.registerday}</span>
							</div>
							<div><img src="/GUBI/data/images/${item.thumbnail_img}"></div>
							</div>
							<div class="right">
								<div class="category">${item.major_category} > ${item.small_category}</div>
								<div class="productname">${item.productname}</div>
								<div class="option">option</div> 
								<div class="optionlist" id="optionlist">`
				
				response.option.forEach( (option) => {
					if(option.fk_productno == item.productno) {
						let optionhtml = `<div class="optionimg" data-toggle="tooltip" data-placement="bottom" title="${option.optionname}" style="background-color:${option.color};"></div>`
						html += optionhtml;
					}
				});

				html +=  `</div>
						 <div class='info mt-4'>등록여부 : ${item.is_delete == 0 ? '등록' : '삭제'} <span>(재고 ${item.cnt}개)</span></div>
						 <div class='info mt-2'>구매수 : </div>
						 <div class='info mt-2'>후기수 : ${response.reviewcnt == 0 ? '후기없음' : response.reviewcnt+'개'}</div>
						 <div class="delivery_price">배송비 ${item.delivery_price.toLocaleString()} 원</div>
						 <div class="product_price">상품가격 ${item.price.toLocaleString()} 원</div>
						 <div class="price">${(item.price+item.delivery_price).toLocaleString()}원</div>	
						 </div>`
				})

				modalbody.append(html);	
				$('[data-toggle="tooltip"]').tooltip(); 
			},                   
	        error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});
	});
	
	
	
});// end of $(document).ready(function(){

// 카테고리 가져오기 
function selectSmallCategory() {
	
	$.ajax({
	        url:"selectSmallCategory.gu",
	        data:{"major_category":$("select#major_category").val()}, 
	        dataType:"json",  
			async:false, // 동기 처리
	        success:function(json){
	        	// console.log(json);
	       		let html = `<option value="" disabled selected>소분류</option>`;
	       	
	   			$.each(json, function(index, item){
	   				console.log(item.small_category);
	      	 		html += `<option value='${item.small_category}'>${item.small_category}</option>`;
	      	 	});// end of $.each(json, function(index, item)
	      	 			
	   			$("select#small_category").html(html);
	        },                   
	        error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
	    });// end of $.ajax
}


function productSearch() {
	const frm = document.productFrm;
				frm.action = "product.gu";
				frm.submit();
}