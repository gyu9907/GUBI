$(document).ready(function(){
	
	/************************* 유효성 검사하기 **************************/

	/* 카테고리 대분류 검사하기 */
	$("select#major_category").blur((e) => { 

	   const major_category = $("select#major_category").val();	
		
       if(major_category == null) {
			$("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
	   }
		else {
			$("table#table :input").prop("disabled", false); 
			$(e.target).parent().find("span.error").hide();
		}	

	});// 아이디가 major_category 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
		
	/* 카테고리 소분류 검사하기 */
	$("select#small_category").blur((e) => { 

		const small_category = $("select#small_category").val();	
		
       if(small_category == null) {
			$("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
	   }
		else {
			$("table#table :input").prop("disabled", false); 
			$(e.target).parent().find("span.error").hide();
		}	

	});// 아이디가 small_category 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	

	/* 상품명 검사하기 */
	$("input#name").blur((e) => { 

        const name = $(e.target).val().trim();
        if(name == "") {
            // 입력하지 않거나 공백만 입력했을 경우 
            $("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
        
        }
        else {
            // 공백이 아닌 글자를 입력했을 경우
            $("table#table :input").prop("disabled", false); 

            $(e.target).parent().find("span.error").hide();
        }

	});// 아이디가 name 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	
	/* 상품설명 검사하기 */
	$("textarea#description").blur((e) => { 

        const description = $(e.target).val().trim();
        if(description == "") {
            // 입력하지 않거나 공백만 입력했을 경우 

            $("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
        
        }
        else {
            // 공백이 아닌 글자를 입력했을 경우
            $("table#table :input").prop("disabled", false); 

            $(e.target).parent().find("span.error").hide();
        }

	});// 아이디 description 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	$("input#cnt").val(1);
	/* 수량 검사하기 */
	$("input#cnt").blur((e) => { 

	    const cnt = $(e.target).val().trim();
	    if(cnt == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	/* 가격 검사하기 */
	$("input#price").blur((e) => { 

	    const price = $(e.target).val().trim();
	    if(price == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 

	/* 배송비 검사하기 */
	$("input#delivery_price").blur((e) => { 

	    const delivery_price = $(e.target).val().trim();
	    if(delivery_price == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 

	
	/* 포인트 검사하기 */
	$("input#point_pct").blur((e) => { 

	    const point_pct = $(e.target).val().trim();
	    if(point_pct == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 

	
	/* 옵션파일 검사하기 */
	$("input#optionimg").blur((e) => { 

	    const optionimg = $(e.target).val().trim();
	    if(optionimg == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 optionimg 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	
	/* 옵션명 검사하기 */
	$("input#optionname").blur((e) => { 

	    const optionname = $(e.target).val().trim();
	    if(optionname == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 optionname 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	/* 옵션코드값 검사하기 */
	$("input.code").blur((e) => { 

	    const code = $(e.target).val().trim();
	    if(code == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 code 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.
	
	
	/* 이미지1 파일 검사하기 */
	/************************* 유효성 검사하기 끝 **************************/
	
	// "제품수량" 에 스피너 달아주기
    $("input#cnt").spinner({
       
       spin:function(event,ui){
             if(ui.value > 100) {
                $(this).spinner("value", 100);
                return false;
             }
             else if(ui.value < 1) {
                $(this).spinner("value", 1);
                return false;
             }
     }
       
    });// end of $("input#spinnerPqty").spinner({})-----------------------------------------------------

	
	$(document).on("change","select#major_category", function(e){
		selectSmallCategory();
	});// end of $(document).on("change","select#major_category", function(e)
	

	// 초반은 경고문 숨기기
	$("span.error").hide();

	//  선택한 색상의 컬러코드 
	$(`div#color`).on("click", function(e) {
		  const color = $(e.target).attr("value");
		  // console.log(color);
		  $("input:text[id='colorCode1']").val(color);
	});
	// 컬러피커로
	$("input#color").on("input", function(e) {
		const selectcolor = $(e.target).val();
		$("input:text[id='colorCode1']").val(selectcolor);
	});

	// 상세설명에서 옵션 추가 버튼을 눌렀을 때
	$("button.addoption").on("click", function() {
		
		// alert("옵션추가클릭");

		const toastEl = document.querySelector('.toast#addOptionToast');
		const toast = new bootstrap.Toast(toastEl);
		toast.show();

		const index = $(".option > thead").children().length + 1;

		if(index > 10) {
		   alert("옵션은 최대 6개까지 등록 가능합니다.");
		   return;
		}

		let html = ``;

		html = `<tr>
					<th class="th"><span>옵션 등록</span></th>
					<td id="option">
					<input type="file" class="infoData optionimg mr-2" name="optionimg" placeholder="옵션이미지" />
					<input type="text" name="optionname" class="infoData mr-2"  placeholder="옵션명" /><br>
					<div id="colorSelect" class="mt-2">
						<input type="text" name="colorCode" id="colorCode${index}"  class="code infoData mr-2"   placeholder="컬러코드(선택 시 자동입력)" />
						<div id="circlecolor"><div id="color${index}" class="color1" value="#674636"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color2" value="#ECDFCC"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color3" value="#FCDE70"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color4" value="#F7B5CA"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color5" value="#9ABF80"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color6" value="#1F509A"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color7" value="#C4D7FF"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color8" value="#F5F7F8"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color9" value="#DDDDDD"></div></div>
						<div id="circlecolor"><div id="color${index}" class="color10" value="#31363F"></div></div>
					</div>
					</td>
				</tr>`;
								
		$("thead.thead").append(html);

		$(`div#color${index}`).on("click", function(e) {
			  const color = $(e.target).attr("value");
			  // console.log(color);
			  $(`input:text[id='colorCode${index}']`).val(color);
		});
	});

    // 상세설명에서 이미지 추가 버튼을 눌렀을 때
    $("button.AddImgBtn").on("click", function() {
	
        const index = $(".ImageTable > thead").children().length + 1;

        if(index > 6) {
            alert("이미지는 최대 6개까지 등록 가능합니다.");
            return;
    	}

    	const html = `
			<tr id="image${index}_tr">
				<th class="th">
					<input id="image${index}" class="ImgCheckbox" type="checkbox" />
					<label for="image${index}">이미지 ${index}</label>
                </th>
				<td>
					<label for="img${index}"><img class="preview" /></label>
					<div>
						<label>이미지 *</label> <span class="error">이미지는 필수입력 사항입니다.</span>
						<input id="img${index}" name="img${index}" type="file" class="img_file required" accept=".bmp, .jpg, .jpeg, .png, .webp"/>

						<label>제목 *</label> <span class="error">제목은 필수입력 사항입니다.</span>
						<input class="description_title required" name="description_title${index}" type="text" maxlength="100"/>

						<label>설명 *</label> <span class="error">설명은 필수입력 사항입니다.</span>
						<textarea class="description_detail required" name="description_detail${index}" rows="6" cols="70" maxlength="1000"></textarea>
					</div>
				</td>
			</tr>`;
        $(".ImageTable > thead").append(html);
		
		$("span.error").hide();

        // '이미지가 추가되었습니다.' 토스트를 표시
        const toastEl = document.querySelector('.toast#addImgToast');
        const toast = new bootstrap.Toast(toastEl);
        toast.show();

    });

    // 상세설명에서 이미지 삭제 버튼을 눌렀을 때
    $("button.RemoveImgBtn").on("click", function(e) {
        if($("input.ImgCheckbox:checked").length == 0) {
            alert('선택된 이미지가 없습니다.');
            return;
        }
        if(confirm('선택한 이미지를 삭제하시겠습니까?')){
            // 선택한 이미지 삭제
            $("input.ImgCheckbox:checked").each(function (index, elmt) { 
                let tr_id = $(elmt).attr("id")+"_tr";

                $(`tr#${tr_id}`).remove();
            });

            // 이미지 번호 다시 매기기
            $("input.ImgCheckbox").each(function (index, elmt) { 
                let tr_id = $(elmt).attr("id")+"_tr";
                const imageCnt = index + 2; // .ImgCheckbox의 index는 0부터 시작하지만 이미지 번호는 2부터 시작해야 함

                $(`tr#${tr_id} > td > label`).attr("for", `img${imageCnt}`);
				$(`tr#${tr_id} > td input[type='file']`).attr("id", `img${imageCnt}`);
                $(`tr#${tr_id} > td input[type='file']`).attr("name", `img${imageCnt}`);
				$(`tr#${tr_id} > td input.description_title`).attr("name", `description_title${imageCnt}`);
				$(`tr#${tr_id} > td textarea.description_detail`).attr("name", `description_detail${imageCnt}`);

                const html = `
                                        <input id="image${imageCnt}" class="ImgCheckbox" type="checkbox" />
                                        <label for="image${imageCnt}">이미지 ${imageCnt}</label>`;
                $(`tr#${tr_id} > th`).html(html);

                $(`tr#${tr_id}`).attr("id", `image${imageCnt}_tr`);
            });
        }
    });
	
	
	// 썸네일 이미지
	$("input.thumbnail_img").on("change", function(e){
		// alert("썸네일이미지");
		const thumbnail_img = $(e.target).get(0);
		
		// 썸네일이미지 
		if(thumbnail_img) {
			console.log("썸네일 파일 정보:", thumbnail_img); // 파일 정보 출력
			const fileReader =  new FileReader();
					
			fileReader.readAsDataURL(thumbnail_img.files[0]); 
			
			// 파일 읽기 성공! 
			fileReader.onload = function(){ 
				console.log(fileReader.result);
				document.getElementById("previewImg").src = fileReader.result
			};
		} else {
			alert("썸네일 사진이 선택되지 않음");
		}
	});
	
	// 옵션사진 추가하기
	$(document).on("change", "input.optionimg", function(e) {
		const input = $(e.target);
		const files = input[0].files; // 선택한 파일배열
		
		if(files.length == 0) {
			alert("파일이 선택되지 않았습니다.");
			return;
		}
		
		for(let i=0; i < files.length; i++) {
			const file = files[i];
			const fileReader =  new FileReader();
			
			fileReader.onload = function(){ 
				  console.log("fileReader.result",fileReader.result);
			}
			fileReader.readAsDataURL(file);
		}

	  });
	   
	  // 추가이미지 (최소 2개)
	  // 이미지 미리보기
	  	$(document).on("change", "input.img_file", function(e) {
	  		const inputFileEl = $(e.target).get(0);
	  	    const previewEl = $(inputFileEl).parent().parent().find(".preview"); // 미리보기 element

	  	    if (inputFileEl.files && inputFileEl.files[0]) { // 파일을 업로드한 경우
	  	        
	  	        const fileType = inputFileEl.files[0].type; // "image/jpeg", "image/png", ...
	  	        const reg = /image\/(jpeg|png|webp)$/; // 확장자가 이미지인지 확인하기 위한 regex
	  	        
	  	        if(!reg.test(fileType)){ // 확장자가 이미지가 아닌 경우
	  	            alert('이미지 파일만 업로드 가능합니다.\n .jpeg .png, .webp');
	  	            inputFileEl.value = ""; // input 비우기
	  	            return;
	  	        }

	  	        const limitSize = 5 * 1024 * 1024; // 5mb 크기 제한을 위한 변수

	  	        const uploadSize = inputFileEl.files[0].size;

	  	        if (limitSize < uploadSize) { // 이미지 크기가 5mb 이상인 경우
	  	            alert('5MB 미만 이미지만 업로드가 가능합니다.');
	  	            inputFileEl.value = ""; // input 비우기
	  	            return;
	  	        }

	  			// 이미지 파일을 로드해서 미리보기에 표시
	  	        const fileReader = new FileReader();
	  			
	  			fileReader.readAsDataURL(inputFileEl.files[0]);
	  	        fileReader.onload = function() { 
	  				$(previewEl).attr("src", fileReader.result);
	  	        };
	  	    } else { // 파일을 업로드하지 않은 경우
	  	        $(previewEl).attr("src", ""); // 미리보기 이미지 삭제
	  	    }
	  	});

	  // 상품등록하기 시작 //////////////////////////////////////////////////////////////////////////
	  $("button.ragibtn").on("click", function() {

			let is_infoData_Ok = true;
			
			$(".infoData").each(function(index, elmt) {
				const val = $(elmt).val();
				if(val == "") {
					$(elmt).next(".error").show();
					is_infoData_Ok = false;
					return false;
				}
			});
			
			if(is_infoData_Ok) {
				const formData = new FormData(document.forms["productAddsFrm"]);
				
				// console.log("FormData 내용 확인:");
			    //     for (const [key, value] of formData.entries()) {
		        //     console.log(key, value); // FormData 키-값 출력
			    // }

				$.ajax({
				   url : "addProduct.gu",
	               type : "post",
	               data : formData,
	               processData:false,  // 파일 전송시 설정 
	               contentType:false,  // 파일 전송시 설정
	               dataType:"json",
	               success:function(json){
	                   console.log("~~~ 확인용 : " + JSON.stringify(json));
	                   
	                   if(json.result == 1) {
		                    alert("상품등록성공입니다");
							location.reload();  // 페이지 새로 고침	 
	                   }
	                  
	              },
	              error: function(request, status, error){
	              		alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	           	  }
				});

			}
	  });
	  ////////////////////////////////////////////////////////////////////////////////////

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
	   				//console.log(item.small_category);
	      	 		html += `<option value='${item.small_category}'>${item.small_category}</option>`;
	      	 	});// end of $.each(json, function(index, item)
	      	 			
	   			$("select#small_category").html(html);
	        },                   
	        error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
	    });// end of $.ajax
}