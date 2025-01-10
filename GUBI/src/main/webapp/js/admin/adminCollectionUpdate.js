$(document).ready(function () {

	// 카테고리 변경시
	$(document).on("change","select#major_category", function(){
		selectSmallCategory();
	});
	
	// 상세설명 이미지 개수
	let detailCnt = $(".ImageTable tr").length;
	$("input[name='detailCnt']").val(detailCnt);
	
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

		$("input[name='detailCnt']").val(++detailCnt); // 상세설명 이미지 개수

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
				
				// 만약 기존 등록된 이미지를 삭제하는 경우
				let imgno = $(`tr#${tr_id}`).find("input.retain_img_no").val();
				if(imgno != null) {
					
					let delete_img = $("input.delete_img_no_arr").val(); // 삭제 이미지 input의 값을 가져온다.
					
					if(delete_img != "") { // 만약 삭제 이미지 input이 비어있지 않다면
						$("input.delete_img_no_arr").val(delete_img+","+imgno); // 1, 2, 3 같은 형식
					}
					else {
						$("input.delete_img_no_arr").val(imgno);
					}
				}

                $(`tr#${tr_id}`).remove();
				
				$("input[name='detailCnt']").val(--detailCnt); // 상세설명 이미지 개수
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
	
	// 컬렉션 input 및 textarea blur 이벤트 발생 시 값이 비어있는지 검사하도록 만들기
	setBlurError(document.registerFrm);

    // 컬렉션 등록 버튼 클릭했을 때
    $("button.CollectionRegisterButton").on("click", function(){

		const registerFrm = document.registerFrm;
		
        // 값이 올바르게 입력되었는지 확인
        if(!isValid(registerFrm)) {
            return; // 값이 올바르지 않은 경우 종료
        }
		
		$("input.retain_img_name").each((index, elmt) => {
			if(index == 0) {
				$("input[name='retain_img_name_arr']").val($(elmt).val());
			}
			else {
				$("input[name='retain_img_name_arr']").val($("input[name='retain_img_name_arr']").val()+","+$(elmt).val());
			}
		});

		registerFrm.enctype="multipart/form-data";
		registerFrm.method = "post";
		registerFrm.submit();

    });
	
	
	// === 상품 검색 관련 시작 === //
	
	// 색상 선택
	$("input:checkbox[name='color']").on("change", (e) => {
		if($(e.target).prop("checked")){
			$(e.target).parent().css({"border-color": "black"});
		}
		else {
			$(e.target).parent().css({"border-color": "#eee"});
		}
	});
	
	// 상품 전체 선택
	$("input:checkbox.check-all-item").on("change", (e) => {
		const table = $(e.target).parent().parent().parent().parent();
		
		table.find("input:checkbox.item-checkbox").prop("checked", e.target.checked);
	});
	
	// 상품 선택 후 추가 버튼 클릭시
	$("button.add-product-btn").on("click", (e) => {
		let html = ``;
		
		const checkbox = $("input:checkbox.item-checkbox:checked");
		const collectionProductsTbody = $("tbody#collectionProductsTbody");
		
		// 테이블이 비어있으면 비어있다는 메시지 표시
		if(checkbox.length > 0) {
			collectionProductsTbody.find(".table-empty").hide();
		}
		else {
			collectionProductsTbody.find(".table-empty").show();
		}
		
		// 체크한 상품들을 추가
		checkbox.each((index, elmt) => {
			if(collectionProductsTbody.find("input[value='"+$(elmt).val()+"']").length == 0){ // 이미 상품이 추가되어 있지 않다면
				const row = $(elmt).parent().parent();
				row.find("td#no").text(index+1);
				html += row.prop("outerHTML"); // 상품 추가
			}
		});
		
		collectionProductsTbody.append(html);
		
		// 추가 후 닫기 버튼을 클릭한게 아니고 추가 버튼을 클릭한 경우
		if($(e.target).attr('id')!="closeModal") {
			// '선택한 상품이 추가되었습니다.' 토스트를 표시
			const toastEl = document.querySelector('.toast#addProductToast');
			toastEl.style.display = "";
			const toast = new bootstrap.Toast(toastEl);
			toast.show();
		}
	});

	// === 상품 검색 관련 끝 === //
	
	// 상품 삭제
	$("button#removeProductBtn").on("click", () => {

		const collectionProductsTbody = $("tbody#collectionProductsTbody");
		const checkboxChecked = collectionProductsTbody.find("input:checkbox.item-checkbox:checked");
		
		if(checkboxChecked.length == 0) {
			alert("선택된 상품이 없습니다.");
		}
		
		// 체크한 상품들을 삭제
		checkboxChecked.each((index, elmt) => {
			const row = $(elmt).parent().parent();
			row.remove();
		});

		const checkbox = collectionProductsTbody.find("input:checkbox.item-checkbox");

		// 테이블이 비어있으면 비어있다는 메시지 표시
		if(checkbox.length == 0) {
			collectionProductsTbody.find(".table-empty").show();
		}
		
		// 번호 다시 매기기
		checkbox.each((index, elmt) => {
			const row = $(elmt).parent().parent();
			row.find("td#no").text(index+1);
		});

		// '선택한 상품이 삭제되었습니다.' 토스트를 표시
		const toastEl = document.querySelector('.toast#deleteProductToast');
		toastEl.style.display = "";
		const toast = new bootstrap.Toast(toastEl);
		toast.show();
		
		$("input:checkbox.check-all-item").prop("checked", false);
		
	});
	
	// 이미지 미리보기
	$(document).on("change", "input.img_file", function(e) {
		const inputFileEl = $(e.target).get(0);
	    const previewEl = $(inputFileEl).parent().parent().find(".preview"); // 미리보기 element
		
		// 만약 기존 등록된 이미지를 변경하는 경우
		let imgno = $(e.target).next("input.retain_img_no").val();

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

			// 기존 등록된 이미지를 삭제
			if(imgno != "" && imgno != undefined) {
				$(e.target).next("input.retain_img_no").val("");
				let delete_img = $("input.delete_img_no_arr").val(); // 삭제 이미지 input의 값을 가져온다.
				
				if(delete_img != "") { // 만약 삭제 이미지 input이 비어있지 않다면
					$("input.delete_img_no_arr").val(delete_img+","+imgno); // 1, 2, 3 같은 형식
				}
				else {
					$("input.delete_img_no_arr").val(imgno);
				}
			}
			
			// 이미지 파일을 로드해서 미리보기에 표시
	        const fileReader = new FileReader();
			
			fileReader.readAsDataURL(inputFileEl.files[0]);
	        fileReader.onload = function() { 
				$(previewEl).attr("src", fileReader.result);
	        };
	    } else { // 파일을 업로드하지 않은 경우
	        $(previewEl).attr("src", ""); // 미리보기 이미지 삭제

			// 만약 기존 이미지를 삭제한 경우라면 이미지를 반드시 넣도록 만든다.
			if($(e.target).next("input.retain_img_no") != undefined) {
				$(e.target).addClass("required");
			}
	    }
	});

	// 이미지 또는 비디오 미리보기
	$(document).on("change", "input[name='fullscreen_img']", function(e) {
		const inputFileEl = $(e.target).get(0);
	    const previewEl = $(inputFileEl).parent().parent().find(".preview"); // 미리보기 element
		
	    if (inputFileEl.files && inputFileEl.files[0]) { // 파일을 업로드한 경우
	        
	        const fileType = inputFileEl.files[0].type; // "image/jpeg", "image/png", ...
	        let reg = /image\/(jpeg|png|webp)$/; // 확장자가 이미지인지 확인하기 위한 regex
			
			let isVideo = false;
	        
	        if(!reg.test(fileType)){ // 확장자가 이미지가 아닌 경우
				reg = /video\/(mp4)$/;
				
				if(!reg.test(fileType)) {
					alert('이미지 또는 mp4 파일만 업로드 가능합니다.\n .jpeg, .png, .webp, mp4');
					inputFileEl.value = ""; // input 비우기
					return;
				}
				
				isVideo = true;
	        }

			const limitSize = 30 * 1024 * 1024; // 30mb 크기 제한을 위한 변수

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
				if(!isVideo) {
					$(previewEl).attr("src", fileReader.result);
					$(previewEl).show();
					$("video.preview").hide();
				}
				else {
					$(previewEl).hide();
					$("video.preview").attr("src", fileReader.result);
					$("video.preview").show();
				}
	        };
	    } else { // 파일을 업로드하지 않은 경우
		        $(previewEl).attr("src", ""); // 미리보기 이미지 삭제
		    }
	});

	$("input[name='searchWord']").on("keydown", function(e) {
		if(e.keyCode == 13) {
			searchProduct(ctxPath, 1);
		}
	});
	
});// end of $(document).ready(function () {})---------------------------

// 컬렉션 등록하기 전, 값이 올바르게 입력되었는지 확인하는 함수
function isValid(formEl) {
    let bool = true;
    $(formEl).find(".required").each((index, elmt) => {
        if($(elmt).val().trim() == "") {
            $(elmt).next(".error").show();
            $(elmt).prev(".error").show();
			
            if(bool) {
                $(elmt).focus();
            }
            bool = false;
        }
		else {
		    $(elmt).next(".error").hide();
		    $(elmt).prev(".error").hide();
		}
    });
	
	// 상품 목록이 하나도 없으면
	if($("tbody#collectionProductsTbody .table-empty").css("display") != "none") {
		alert("컬렉션은 1개 이상 상품을 포함해야 합니다.");
		bool = false;
	}
	
    return bool;
}

function setBlurError(formEl) {
	$(formEl).find(":not(input[type='file']).required").on("blur", (e) => {
	    if($(e.target).val() == "") {
			$(e.target).next(".error").show();
			$(e.target).prev(".error").show();
	    }
		else {
			$(e.target).next(".error").hide();
			$(e.target).prev(".error").hide();
		}
	});
}

// 상품검색 ajax
function searchProduct(ctxPath, page) {
    
    const productSearchFrm = document.productSearchFrm;

    $(productSearchFrm.currentShowPageNo).val(page);

    const data = $(productSearchFrm).serialize();

    $.ajax({
		url : ctxPath+'/product/adminProductSearchJSON.gu',
        data : data,
        dataType : 'json',
        success : function(json) {

            const productList = json.productList;
			const searchProductResult = $("tbody#searchProductResult");

            let html = ``;

            if(productList.length == 0) {
                html = `<tr><th colspan="10" style="text-align: center;">검색된 상품 목록이 없습니다.</th></tr>`;
            }
            else {
                for(let i=0; i<productList.length; i++) {
                    html += `<tr style="color:${productList[i].is_delete==0?"black":"#ccc"};">
                                        <td><input type="checkbox" value="${productList[i].productno}" class="item-checkbox"/></td>
                                        <td id="no">${json.totalProductCnt - (json.currentShowPageNo - 1) * json.sizePerPage - i}</td>
                                        <td class="image"><img src="${ctxPath}/data/images/${productList[i].thumbnail_img}"/></td>
                                        <td>${productList[i].productno}<input type="hidden" value="${productList[i].productno}" name="fk_productno"/></td>
                                        <td>${productList[i].categoryVO.major_category}</td>
                                        <td>${productList[i].name}</td>
                                        <td>${productList[i].registerday}</td>
                                        <td style="color:${productList[i].cnt==0?"red":"black"};">
											${productList[i].cnt}
										</td>
                                        <td style="color:${productList[i].cnt==0?"red":"black"};">
											${productList[i].is_delete==0?'':'단종'}${productList[i].cnt==0?"품절":"판매중"}
										</td>
                                        <td>${productList[i].price.toLocaleString("en")}원</td>
                                    </tr>`;
                }
            }

            searchProductResult.html(html);
			
			$("span#totalCnt").text("전체 : "+json.totalProductCnt+"건 조회");

			// 테이블이 비어있으면 비어있다는 메시지 표시
			if(html.length > 0) {
				searchProductResult.find(".table-empty").hide();
			}
			else {
				searchProductResult.find(".table-empty").show();
			}

            html = json.pageBar;
            $(".AddProductModal ul.pagination").html(html);

			// 상품 전체 선택 해제
			$(".AddProductModal input:checkbox.check-all-item").prop("checked", false);
			
			// 체크박스 체크시 전체 선택 검사
			$("input:checkbox.item-checkbox").on("change", (e)=> {
				if(e.target.checked) {
					for(let chkbox of document.querySelectorAll("input[type='checkbox'].item-checkbox")) {
						if(!chkbox.checked) {
							return;
						}
					}
					$("input:checkbox.check-all-item").prop("checked", true);
				}
				else {
					$("input:checkbox.check-all-item").prop("checked", false);
				}
			});
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
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
        },                   
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });// end of $.ajax
}