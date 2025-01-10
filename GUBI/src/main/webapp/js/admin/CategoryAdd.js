$(document).ready(function(){

	// 이미지 파일 선택함 
	$("input.category_img").on("change", function(e){
		
		const input_file = $(e.target).get(0);
		//console.log(input_file);
		// <input type="file" class="category_img">
		
		const fileReader =  new FileReader();
		
		fileReader.readAsDataURL(input_file.files[0]);
		
		fileReader.onload = function(e){ 
		   $("img#viewcategoryimg").attr("src", e.target.result);
        };
		reader.readAsDataURL(input.files[0]);
	}); 
	
	$("button#cateAdd").on("click", function(){
		
		const bigCategory = $("select[name='major_category']").val();
		const smallCategory = $("input#small_category").val().trim();
		
		// 이미지
		const categoryimg = $("input.category_img")[0].files[0];
		
		const formData = new FormData();

		if(bigCategory==null) {
		   alert("카테고리 대분류를 선택하셔야 합니다.");
		   return; // goRegister() 함수를 종료한다.
		}
		  
		if(smallCategory == null || smallCategory=="") {
			 alert("카테고리 소분류를 입력해야 합니다.");
		   return; // goRegister() 함수를 종료한다.
		}
		
		if(!categoryimg) {
			alert("이미지를 선택하세요")
			return;
		}
		
		formData.append("categoryimg", categoryimg);
		
		formData.append("major_category", bigCategory);
		formData.append("small_category", smallCategory);
		
		// FormData 내용을 확인하기
	    formData.forEach((value, key) => {
	       if (value instanceof File) {
	           // 파일일 경우 파일의 이름과 크기를 출력
	           console.log(`${key}: ${value.name}, ${value.size} bytes`);
	       } else {
	           // 파일이 아닌 값은 그대로 출력
	           console.log(`${key}: ${value}`);
	       }
	    });

		$.ajax({
		      url:"categoryAdd.gu",
			  type:"post",
		      data: formData,
			  processData: false,  // 데이터를 기본적으로 처리하지 않음
		      contentType: false,  // 자동으로 Content-Type을 설정하지 않음
		      // async:false,
		      dataType:"json",
		      success:function(json) { 
			    
				if(json.result == 1) {
                 	alert("카테고리가 등록되었습니다!");	
					location.reload();  // 페이지 새로 고침	 
				}
		      },
		      error: function(request, status, error){
		             alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		      }
		});
		
/*
		const frm = document.categoryFrm;
		            frm.action = "categoryAdd.gu";
		            frm.method = "post";
		            frm.submit(); */
	});
	
}); // end of ready

// 카테고리 삭제
function deleteCategory() {
	
	if($("input:checkbox[name='deleteCategoryCheck']:checked").length == 0) {
		alert("삭제할 카테고리를 선택하세요");
		return;
	}
	
	$("input:checkbox[name='deleteCategoryCheck']:checked").each((index, elmt) => {
		$(document.deleteCategoryFrm).append("<input type='hidden' name='categoryno' value='"+ $(elmt).val() +"'/>")
	});
	
	const frm = document.deleteCategoryFrm
    frm.action = "categoryDelete.gu";
    frm.method = "post";
    frm.submit(); 
	
}