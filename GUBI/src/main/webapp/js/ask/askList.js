$(document).ready(function() {
	 // 전체 선택 체크박스를 클릭할 때 실행되는 함수
    function selectAll(selectAllCheckbox) {
        const checkboxes = $("input[name='askno']");  // 개별 체크박스들 선택
        checkboxes.prop('checked', selectAllCheckbox.checked);  // checked 속성 변경
    }

    // 전체 선택 체크박스를 클릭하면 개별 체크박스들을 동기화
    $("input[name='selectall']").click(function() {
        selectAll(this);
    });

    // 개별 체크박스를 클릭할 때 전체 선택 체크박스 상태 업데이트
    $("input[name='askno']").change(function() {
        const totalCheckboxes = $("input[name='askno']").length;  // 전체 체크박스 개수
        const checkedCheckboxes = $("input[name='askno']:checked").length;  // 체크된 체크박스의 개수
 
        // 모든 개별 체크박스가 체크된 경우, 전체 선택 체크박스 체크
        $("input[name='selectall']").prop('checked', totalCheckboxes === checkedCheckboxes);
    });

    // 삭제 버튼 클릭 이벤트
    $("#deleteButton").click(goDelete);
    // 등록 버튼 클릭 이벤트
    $("#submitButton").click(goSubmit);


		
}); // end of $(document).ready(function() {)}---------------------------------------------------------
		
		
		
	function goSearch() {
    const form = document.ask_search_frm;
    const searchType = form.searchType.value;
    const searchWord = form.searchWord.value.trim();

    if (!searchType || !searchWord) {
        alert('검색 조건과 검색어를 입력해주세요.');
        return;
        
    }

    const query = `?searchType=${searchType}&searchWord=${encodeURIComponent(searchWord)}`;
    location.href = ctxPath + '/ask/askList.gu' + query;		
}// end of fucntion goSearch() {}----------------------


// 문의 삭제 함수
function goDelete() {
   const queryString = $("form[name='askListForm']").serialize();
   console.log(queryString)
    if (queryString == '') {
        alert("삭제할 문의를 선택하세요.");
        return;
    }

    if (confirm("정말 문의를 삭제하시겠습니까?")) {
        $.ajax({
            url: ctxPath + '/ask/askDelete.gu', 
            type: "POST",           
            data: queryString,
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    alert("문의가 성공적으로 삭제되었습니다.");
                    location.reload();  // 페이지 새로고침
                } else {
                    alert("삭제 실패: " + data.message);
                }
            },
            error: function (xhr, status, error) {
                console.error("오류 발생:", error);
                alert("삭제 중 오류가 발생했습니다.");
            }
        });
    }
}



// 배송지 수정 페이지로 이동하는 함수
function openaskViewPage(row) {
    var askno = row.getAttribute('data-askno');
    var ctxPath = document.body.getAttribute('data-ctxPath'); // JSP에서 전달된 ctxPath 값 가져오기

    // 수정 페이지로 이동
    window.location.href = ctxPath + "/ask/askModify.gu?askno=" + askno;
}