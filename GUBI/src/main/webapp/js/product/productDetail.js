const query = document.location.search;
const param = new URLSearchParams(query);

const param_productno = param.get('productno');

$(document).ready(function(){
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
	
	$('button.color-button').on('click', function() {
	    var imgSrc = $(this).data('img');
		var name = $(this).data('name');
		var optionno = $(this).data('optionno');
		
		$('button.color-button').css('border', 'none');
		$(this).css('border', '1px solid #000');

	    $('#product_image').attr('src', `/GUBI/data/images/${imgSrc}`);
		$('#prod_opt_name').text(name);  
		
		$('input[name="optionno"]').val(optionno);  
		
	});
})

function addCart(){
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
		success: function(json){
			//console.log(json);
			
			let html = '';
			
			$.each(json, function(index, item){
			
				html += `<div class="review-item">
						  <p class="review-author">${item.title}</p>
						  <p class="review-rating">${item.score}</p>
						  <p class="review-comment">${item.content}</p>
						</div>`;
				
					
			});
			
			$("div#reviewList").html(html);
			
			
			
			
		}
	});
	
}