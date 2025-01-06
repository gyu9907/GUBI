let currentSearch = 0; // 0: 컬렉션 정보로 검색, 1: 상품 정보로 검색

$(document).ready(function() {

	// 카테고리 변경시
	$(document).on("change","select#major_category", function(){
		selectSmallCategory();
	});

	searchCollection(ctxPath, 1); // 컬렉션 목록 표시
	
	// 컬렉션 전체 선택
	$("input:checkbox.check-all-item").on("change", (e) => {
		const table = $(e.target).parent().parent().parent().parent();
		
		table.find("input:checkbox.item-checkbox").prop("checked", e.target.checked);
	});
	
	// 컬렉션 삭제 버튼 눌렀을 때
	$("button#deleteCollection").on("click", (e) => {
		const checkedEl = $("input:checkbox[name='collectionno']:checked");
		if(checkedEl.length == 0) {
			alert("선택된 컬렉션이 없습니다.");
			return;
		}
		
		if(!confirm("선택한 컬렉션을 정말로 삭제하시겠습니까?")) {
			return;
		}

		const collectionListFrm = document.collectionListFrm;

		const data = $(collectionListFrm).serialize();

		$.ajax({
			url : ctxPath+'/admin/collectionDelete.gu',
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(json) {

			    if(json.isDeleted) {
					// 다시 검색하여 컬렉션 목록 표시
					if(currentSearch == 0) {
						searchCollection(ctxPath, $(searchCollectionFrm.currentShowPageNo).val());
					}
					else {
						searchByProduct(ctxPath, $(searchByProductFrm.currentShowPageNo).val());
					}
					alert("선택한 컬렉션이 삭제되었습니다.");
				}
				else {
					alert("컬렉션 삭제를 실패했습니다.");
				}
			},
			error: function(request, status, error){
			    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	});
	
	// 컬렉션검색 엔터 키 입력시
	$("form[name='searchCollectionFrm'] input[name='searchWord']").on("keydown", (e)=> {
		if(e.keyCode == 13) {
			searchCollection(ctxPath, 1);
			return false;
		}
	});

	// 포함상품검색 엔터 키 입력시
	$("form[name='searchByProductFrm'] input[name='searchWord']").on("keydown", (e)=> {
		if(e.keyCode == 13) {
			searchByProduct(ctxPath, 1);
		}
	});
	
	// 포함상품검색 색상 선택
	$("input:checkbox[name='color']").on("change", (e) => {
		if($(e.target).prop("checked")){
			$(e.target).parent().css({"border-color": "black"});
		}
		else {
			$(e.target).parent().css({"border-color": "#eee"});
		}
	});
	
}); // end of $(document).ready(function() {})------------------

// 컬렉션 검색 ajax
function searchCollection(ctxPath, page) {
    
	document.searchByProductFrm.reset(); // 포함상품 검색 폼을 초기화
	
    const searchCollectionFrm = document.searchCollectionFrm;

    $(searchCollectionFrm.currentShowPageNo).val(page);

    const data = $(searchCollectionFrm).serialize();

    $.ajax({
		url : ctxPath+'/admin/collectionListJSON.gu',
        data : data,
        dataType : 'json',
        success : function(json) {

            const collectionList = json.collectionList;
			const searchResult = $("tbody#searchResult");

            let html = ``;

            if(collectionList.length == 0) {
                html = `<tr><th colspan="10" style="text-align: center;">검색된 컬렉션이 없습니다.</th></tr>`;
            }
            else {
                for(let i=0; i<collectionList.length; i++) {
                    html += `<tr style="color:${collectionList[i].is_delete==0?"black":"#ccc"};">
                                        <td><input type="checkbox" value="${collectionList[i].collectionno}" name="collectionno" class="item-checkbox"/></td>
                                        <td id="no">${json.totalCollectionCnt - (json.currentShowPageNo - 1) * json.sizePerPage - i}</td>
                                        <td class="image"><img src="${ctxPath}/data/images/${collectionList[i].thumbnail_img}" class="img-fluid"/></td>
                                        <td>${collectionList[i].collectionno}</td>
                                        <td><a href="${ctxPath}/collection/collectionDetail.gu?collectionno=${collectionList[i].collectionno}">${collectionList[i].name}</a></td>
                                        <td>${collectionList[i].registerday}</td>
                                        <td>
											${collectionList[i].productListCnt}
										</td>
										<td><button type="button" class="update btn btn-light border"><span>수정하기</span></button></td>
                                    </tr>`;
                }
            }

            searchResult.html(html);
			
			$("span#totalCnt").text("전체 : "+json.totalCollectionCnt+"건 조회");

			// 테이블이 비어있으면 비어있다는 메시지 표시
			if(html.length > 0) {
				searchResult.find(".table-empty").hide();
			}
			else {
				searchResult.find(".table-empty").show();
			}

            html = json.pageBar;
            $("ul.pagination").html(html);

			// 전체 선택 해제
			$("input:checkbox.check-all-item").prop("checked", false);
			
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
			
			// 수정하기 버튼 클릭시 이동
			$("form[name='collectionListFrm'] button.update").on("click", function() {
				const trEl = $(this).parent().parent();
				
				const collectionno = $(trEl).find("input:checkbox[name='collectionno']").val();
				
				location.href=ctxPath + "/admin/collectionUpdate.gu?collectionno="+collectionno;
			});
			
			currentSearch = 0; // 0: 컬렉션 정보로 검색, 1: 상품 정보로 검색
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
}

// 상품으로 컬렉션 검색 ajax
function searchByProduct(ctxPath, page) {

	document.searchCollectionFrm.reset(); // 컬렉션 검색 폼을 초기화
	
    const searchByProductFrm = document.searchByProductFrm;

    $(searchByProductFrm.currentShowPageNo).val(page);

    const data = $(searchByProductFrm).serialize();

    $.ajax({
		url : ctxPath+'/admin/collectionListByProductJSON.gu',
        data : data,
        dataType : 'json',
        success : function(json) {

            const collectionList = json.collectionList;
			const searchResult = $("tbody#searchResult");

            let html = ``;

            if(collectionList.length == 0) {
                html = `<tr><th colspan="10" style="text-align: center;">검색된 컬렉션이 없습니다.</th></tr>`;
            }
            else {
                for(let i=0; i<collectionList.length; i++) {
                    html += `<tr style="color:${collectionList[i].is_delete==0?"black":"#ccc"};">
                                        <td><input type="checkbox" value="${collectionList[i].collectionno}" name="collectionno" class="item-checkbox"/></td>
                                        <td id="no">${json.totalCollectionCnt - (json.currentShowPageNo - 1) * json.sizePerPage - i}</td>
                                        <td class="image"><img src="${ctxPath}/data/images/${collectionList[i].thumbnail_img}" class="img-fluid"/></td>
                                        <td>${collectionList[i].collectionno}</td>
                                        <td>${collectionList[i].name}</td>
                                        <td>${collectionList[i].registerday}</td>
                                        <td>
											${collectionList[i].productListCnt}
										</td>
										<td><button type="button" class="update btn btn-light border"><span>수정하기</span></button></td>
                                    </tr>`;
                }
            }

            searchResult.html(html);
			
			$("span#totalCnt").text("전체 : "+json.totalCollectionCnt+"건 조회");

			// 테이블이 비어있으면 비어있다는 메시지 표시
			if(html.length > 0) {
				searchResult.find(".table-empty").hide();
			}
			else {
				searchResult.find(".table-empty").show();
			}

            html = json.pageBar;
            $("ul.pagination").html(html);

			// 전체 선택 해제
			$("input:checkbox.check-all-item").prop("checked", false);
			
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

			// 수정하기 버튼 클릭시 이동
			$("form[name='collectionListFrm'] button.update").on("click", function() {
				const trEl = $(this).parent().parent();
				
				const collectionno = $(trEl).find("input:checkbox[name='collectionno']").val();
				
				location.href=ctxPath + "/admin/collectionUpdate.gu?collectionno="+collectionno;
			});
			
			currentSearch = 1; // 0: 컬렉션 정보로 검색, 1: 상품 정보로 검색
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