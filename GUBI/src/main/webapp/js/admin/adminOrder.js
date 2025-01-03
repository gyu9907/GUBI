$(document).ready(function(){
	
	$("table.table tr#ordertr").on("click", function(){
		const orderId = $(this).data("id"); // tr의 dataid 
		const modalId = `#detailOrder${orderId}`; // 모달 id
		
		$.ajax({
	        url:"detailOrder.gu",
	        data:{"orderno": orderId}, 
	        dataType:"json",  
	        success:function(json){
	        	
	        	const modalbody = $("div.modal-body");
	        	modalbody.empty(); // 기존 내용을 비우기
						
				let html = ``;
				json.forEach((item, index) => {

					if(index === 0) {
						
						let status = '';
						
						if(item.status == 1) {
							status = '결제대기';
						}
						else if(item.status == 2) {
							status = '주문완료';
						}
						else if(item.status == 3) {
							status = '주문취소';
						}
						else if(item.status == 4) {
							status = '배송중';
						}
						else if(item.status == 5) {
							status = '배송완료';
						}
						else if(item.status == 6) {
							status = '주문완료';
						}
						else if(item.status == 7) {
							status = '환불접수';
						}
						else if(item.status == 7) {
							status = '환불완료';
						}

						html = ` <div>
									<span style="font-size:14pt; font-weight:bold; ">${item.username}(${item.userid})</span>
									<span style="font-size:10pt;"> 님의 주문상세</span>
								    <hr style='border: 1px solid black; opacity:20%'> 
								    <div id='orderhead'>
										<div>no ${item.orderno} (총 ${item.totalcnt}건)</div>
										<div>주문일자 ${item.orderday}&nbsp;&nbsp;
										( ${status} )
								 	</div>
								 </div> `
						 modalbody.append(html);
					}
											 
	 				html = ` <div id='orderbody'>
	 							<div class='productimage'>
	 								<img src='/GUBI/data/images/${item.thumbnail_img}'/>
	 							</div>
	 							<div class="productoption">
	 								<div>${item.productname}</div>
	 								<div>${item.optionno}${item.optionname}</div>
	 								<div><span style='font-size: 10pt;'>주문수량</span>  ${item.cnt}</div>
	 							</div>	
	 							<div class='productprice'>
	 								<div>${item.price.toLocaleString()} 원 x ${item.cnt.toLocaleString()}</div>	
									<div>배송비 ${item.delivery_price.toLocaleString()}원</div>
	 								<div>${(item.price * item.cnt).toLocaleString()}원</div>
	 							</div>														
 							 </div> `
							
	 				modalbody.append(html);
					
					if(json.length == index+1) {
						
						html  = `<div id="totalprice">
									<div class='mt-1'>최종주문금액 </div>
									<div>${item.totalprice.toLocaleString()}원</div>
								 </div>`
						modalbody.append(html);
					}

				});
	        	// $(modalId).modal("show");
	        },                   
	        error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
	    });// end of $.ajax
		
	});

	
});
