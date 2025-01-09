$(document).ready(function() {
    // 전체 선택 체크박스를 클릭할 때 실행되는 함수
    function selectAll(selectAllCheckbox) {
        const checkboxes = $("input[name='select']");  // 개별 체크박스들 선택
        checkboxes.prop('checked', selectAllCheckbox.checked);  // checked 속성 변경
    }

    // 전체 선택 체크박스를 클릭하면 개별 체크박스들을 동기화
    $("input[name='selectall']").click(function() {
        selectAll(this);
    });

    // 개별 체크박스를 클릭할 때 전체 선택 체크박스 상태 업데이트
    $("input[name='select']").change(function() {
        const totalCheckboxes = $("input[name='select']").length;  // 전체 체크박스 개수
        const checkedCheckboxes = $("input[name='select']:checked").length;  // 체크된 체크박스의 개수

        // 모든 개별 체크박스가 체크된 경우, 전체 선택 체크박스 체크
        $("input[name='selectall']").prop('checked', totalCheckboxes === checkedCheckboxes);
    });

    // 삭제 버튼 클릭 이벤트
    $("#delete-btn").click(goDelete);
    // 등록 버튼 클릭 이벤트
    $("#submit-btn").click(goRegister);
	
	$("tr").on("click", function(e) {
		if($(e.target).is("input[name='deliveryno']")) {
			
		}
		else if($(e.target).is("button#modify-btn")) {
			opendeliveryModifyPage(this);
		}
		else {
			const deliveryno = $(this).data("deliveryno");
			opener.location.href = `javascript:setDelivery("${deliveryno}")`;

			self.close(); // 팝업창을 닫는다.
		}
	})
});


// 배송지 삭제 함수
function goDelete() {
   const queryString = $("form[name='deliveryDeleteForm']").serialize();
   console.log(queryString)
    if (queryString == '') {
        alert("삭제할 배송지를 선택하세요.");
        return;
    }

    if (confirm("정말 배송지를 삭제하시겠습니까?")) {
        $.ajax({
            url: ctxPath + '/delivery/deliveryDelete.gu', 
            type: "POST",           
            data: queryString,
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    alert("배송지가 성공적으로 삭제되었습니다.");
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



// 배송지 등록 함수
function goRegister() {
   
    // 등록 페이지로 이동
    window.location.href = ctxPath + "/delivery/deliveryRegisterPopup.gu";

}

// 배송지 수정 페이지로 이동하는 함수
function opendeliveryModifyPage(row) {
    var deliveryno = row.getAttribute('data-deliveryno');
    var ctxPath = document.body.getAttribute('data-ctxPath'); // JSP에서 전달된 ctxPath 값 가져오기

    // 수정 페이지로 이동
    window.location.href = ctxPath + "/delivery/deliveryModifyPopup.gu?deliveryno=" + deliveryno;
}
