$(document).ready(function(){

	let modalId;
	let modal;
	
	
	$("tr#ordertr").on("click", function(){
		
		const orderno = $(this).find("#orderno").text().trim();
		// console.log(orderno);
		const checkedstatus = $("input.my").prop("checked", true);
		const mystatus = $("input.my").val(); // 기존의 status
		
		modalId = "#OrderStatus"+orderno;
		modal = $(modalId); // 모달 id
		
		// 변수선언하기
		let currentStatus = mystatus;
		console.log("currentStatus1입니다"+currentStatus)

		
		// 라디오 버튼 체크
		$("div#radio").on("change", function(){
			const orderno = $(this).closest(".modal").find(".orno").text().trim(); 
			// console.log(orderno);
			currentStatus = $(`input:radio[name='orderStatus${orderno}']:checked`).val(); 
			// 새로운 status
			// console.log(status);

			console.log("currentStatus"+currentStatus);
		});
		
		
		// 수정하기 버튼 클릭
		$("button#updataCategory").on("click", function(){
			
			const orderno = $(this).data("id");
			// console.log("orderno" + orderno);
			
			// console.log("최종currentStatus"+currentStatus)
			
			$.ajax({
			  url:"orderStatus.gu",
			  type:"post",
		      data: {"orderno":orderno,
					 "currentStatus":currentStatus},
		      // async:false,
		      dataType:"json",
		      success:function(json) { 
			    console.log("~~~ 확인용: " + JSON.stringify(json));
				
			 	if(json.result == 1) {
	                 alert("수정이 완료되었습니다!");
					 location.reload();  // 페이지 새로 고침	 
				}
		      },
		      error: function(request, status, error){
		             alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		      }
	
			});
			
		})
			
		
	});
	
	
		
			
				
	
	
	
	
});