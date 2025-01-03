$(document).ready(function(){
	
	let modalId;
	let modal;
	
	// 행을 클릭했을 때 함수 
	$("table.table tr#updateCategoryTr").on("click", function() {
		
		const categoryno = $(this).find("#categoryno").text().trim();
		const majorCategory = $(this).find("#majorCategory").text().trim();
		const smallCategory = $(this).find("#smallCategory").text().replace("/","").trim();
		const categoryimg = $(this).find("#categoryimg").attr("src");
		
		const filename = categoryimg.substring(categoryimg.lastIndexOf("/") + 1);

		modalId = "#detailOrder"+categoryno;
		modal = $(modalId); // 모달 id
		
		// 카테고리설정
		modal.find("#major_category").val(majorCategory);
		selectSmallCategory(modal, smallCategory);
		
		// 이미지설정
		modal.find("#viewcategoryimg").attr("src",categoryimg);
		modal.find("#categoryimg").val(filename);
		
		// 기본값 저장해두기
		modal.data("initialData", {
			categoryno,
			majorCategory,
			smallCategory,
			filename,
		});

	});// end of $("table.table tr#updateCategoryTr").on("click", function()
	
	// 대분류값 변경되면 소분류도 변경 
	$("select#major_category").on("change",function (){	
		selectSmallCategory(modal, ""); // 소분류 초기화 및 업데이트
	});

	// 이미지 변경 되었을 때 함수
	$("input#newcategoryimg").on("change", function(e) {
		
		const input = this;
				
		if(input.files && input.files[0]) { // 파일이 선택 되었는지 
			
			// input.files 은 file 의 리스트 
			const filename = input.files[0].name; // 0 번째 요소 file 의 name 값 (파일명)

			$("input:text[name='categoryimg']").val(filename);
					
			const reader = new FileReader();
					
			reader.onload = function(e) {
				$("img#viewcategoryimg").attr("src", e.target.result);
			};
			reader.readAsDataURL(input.files[0]);
		}			
	});// end of $("input#newcategoryimg").on("change", function(e)


	// 수정하기 버튼 클릭했을 경우 
	$("button#updataCategory").on("click",function (){	

		// 모달의 지금 값 (값을 변경하면 바뀜)
		const majorCategory = modal.find("#major_category").val();
	 	const smallCategory = modal.find("#small_category").val();
		const categoryimg = modal.find("#viewcategoryimg").attr("src");
		const filename = categoryimg.substring(categoryimg.lastIndexOf("/") + 1);

	  	const initialData = modal.data("initialData"); // 기본값들

		const formData = new FormData();
		
		formData.append("categoryno", initialData.categoryno);

		// 카테고리 대분류
		if(majorCategory == initialData.majorCategory) {
			formData.append("majorCategory", initialData.majorCategory);
		}
		else {
			formData.append("majorCategory", majorCategory);
		}

		// 카테고리 소분류
		if(smallCategory == initialData.smallCategory) {
			formData.append("smallCategory", initialData.smallCategory);	
		}
		else {
			formData.append("smallCategory", smallCategory);
		}
		
		// 이미지 변경 여부 처리
	    const newCategoryImg = modal.find("input#newcategoryimg")[0].files[0]; // 새로운 이미지 파일
		
	    if(newCategoryImg) { // 새로운 파일이 있다면 덮어씀
	        formData.append("categoryimg", newCategoryImg);
	    } else if (filename == initialData.filename) { // 기존파일과 새로운 파일 같다면 씀
	        formData.append("categoryimg", filename);
	    } else { // 새파일 x 기존파일 xx 
			alert("카테고리 사진을 선택하세요");
			return;
		}
		
		
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
		      url:"categoryUpdate.gu",
			  type:"post",
		      data: formData,
			  processData: false,  // 데이터를 기본적으로 처리하지 않음
		      contentType: false,  // 자동으로 Content-Type을 설정하지 않음
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
		
	});

});// end of ready


// 카테고리 가져오기 
function selectSmallCategory(modal, smallCategory) {
	
	const majorCategory = modal.find("select#major_category").val();
		
	$.ajax({
        url:"selectSmallCategory.gu",
        data:{"major_category":majorCategory},
        dataType:"json",  
		async:false, // 동기 처리
        success:function(json){
       		let html = `<option value="" disabled>소분류</option>`;
       	
   			$.each(json, function(index, item){
      	 		html += `<option value='${item.small_category}'>${item.small_category}</option>`;
      	 	});
      	 			
   			modal.find("select#small_category").html(html);
			
			if(smallCategory) {
				modal.find("#small_category").val(smallCategory);
			}
			
        },                   
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });// end of $.ajax
}

/*function updateCategory(formData) { // 수정하기 클릭했을 때 실행되는 함수
	


	const frm = document.updateCategory;
	      frm.action = "categoryUpdate.gu";
	      frm.method = "post"; 
	      frm.submit();
}

*/