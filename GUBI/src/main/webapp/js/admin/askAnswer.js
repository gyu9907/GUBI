$(document).ready(function(){
	
   $("button.answerbtn").on("click", function(){
	
		const textarea = $("textarea#adminAnswer").val(); // 답변내용
		const askno = $("input:hidden[name='askno']").val();
		// console.log("askno",askno);
		
		if(textarea == null || textarea.trim() == "") {
			alert("답변을 작성하지 않으셨습니다.");
			return;
		}
		else {
			$.ajax({
			      url:"askAnswer.gu",
				  type:"post",
			      data: {"askno":askno ,
						 "answer": textarea},
			      // async:false,
			      dataType:"json",
			      success:function(json) { 
					
					if(json.answerAdd == 1) {
		             	alert("문의 답변이 등록되었습니다!");	
						location.reload();  // 페이지 새로 고침	 
					}
			      },
			      error: function(request, status, error){
			             alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			      }
			});
				
		}
		
		
   }); // end of $("button.answerbtn").on("click", function()
	
	 

});
