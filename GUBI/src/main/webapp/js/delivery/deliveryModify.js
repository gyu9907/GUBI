function openSidebar() {
        document.getElementById("cartSidebar").classList.add("open");
        document.getElementById("content").classList.add("blur");  // 내용에 블러 추가
       // document.getElementById("header").classList.add("blur"); // 헤더 블러 추가
    }

    function closeSidebar() {
        document.getElementById("cartSidebar").classList.remove("open");
        document.getElementById("content").classList.remove("blur");  // 블러 제거
      //  document.getElementById("header").classList.add("blur"); //헤더 블러 제거
    }
    
    
$(document).ready(function(){

	
	    $("span.error").hide();
	 // $("input#delivery_name").focus();

	 // $("input#name").bind("blur", function(e){ alert("name 에 있던 포커스를 잃어버렸습니다."); }); 
	 // 또는
	 // $("input#name").blur(function(e){ alert("name 에 있던 포커스를 잃어버렸습니다."); }); 

 
	 
	 // 배송지명 입력란은 페이지 시작 시 비활성화
	    $("#delivery_name").prop("disabled", false);

		$("input#receiver").blur((e) => { 
		    const receiver = $(e.target).val().trim();

		    if (receiver == "") {
				
		        // 수령인 성명이 입력되지 않았을 경우 처리
		        $("table#tblMemberRegister :input").prop("disabled", true);
		        $(e.target).prop("disabled", false);
		    //  $(e.target).val("").focus();
		        $(e.target).parent().find("span.error").show();  // 수령인 에러 메시지 보이게 처리
	
				
		    } else {
		        // 수령인 성명이 입력되었을 경우
		        const deliveryNameField = $("#delivery_name");

		        if (deliveryNameField.val().trim() === "") { 
		            // 배송지명이 비어있을 때 기본값으로 수령인 성명을 입력
		            deliveryNameField.val(receiver);
		            $(deliveryNameField).parent().find("span.error").show();
		        } else {
		            // 배송지명이 이미 입력되었을 경우 에러 메시지 숨기기
		            $(deliveryNameField).parent().find("span.error").hide();
		        }

		        // 공백이 아닌 경우 다른 처리
		        $("table#tblMemberRegister :input").prop("disabled", false);
		        $(e.target).parent().find("span.error").hide();
		    }
		});

        $("input#hp2").blur((e) => { 

        // const regExp_hp2 = /^[1-9][0-9]{3}$/; 
        // 또는
           const regExp_hp2 = new RegExp(/^[1-9][0-9]{3}$/);
           // 연락처 국번( 숫자 4자리인데 첫번째 숫자는 1-9 이고 나머지는 0-9) 정규표현식 객체 생성
    
            const bool = regExp_hp2.test($(e.target).val());
            
            if(!bool) {
                // 연락처 국번이 정규표현식에 위배된 경우 
    
                $("table#tblMemberRegister :input").prop("disabled", true);
                $(e.target).prop("disabled", false);
              // $(e.target).val("").focus();
    
              // $(e.target).next().show();
              // 또는
                $(e.target).parent().find("span.error").show();
            
            }
            else {
                // 연락처 국번이 정규표현식에 맞는 경우
                $("table#tblMemberRegister :input").prop("disabled", false); 
    
                //  $(e.target).next().hide();
                //  또는
                $(e.target).parent().find("span.error").hide();
            }
    
        });// 아이디가 hp2 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.        


        $("input#hp3").blur((e) => { 

        // const regExp_hp3 = /^[0-9]{4}$/; 
        // 또는
        // const regExp_hp3 = /^\d{4}$/;
        // 또는
        // const regExp_hp3 = new RegExp(/^[0-9]{4}$/);
        // 또는
           const regExp_hp3 = new RegExp(/^\d{4}$/);
            // 연락처 국번( 숫자 4자리인데 첫번째 숫자는 1-9 이고 나머지는 0-9) 정규표현식 객체 생성
    
           const bool = regExp_hp3.test($(e.target).val());
            
            if(!bool) {
                // 마지막 전화번호 4자리가 정규표현식에 위배된 경우 
    
                $("table#tblMemberRegister :input").prop("disabled", true);
                $(e.target).prop("disabled", false);
             //   $(e.target).val("").focus();
    
            //  $(e.target).next().show();
            //  또는
                $(e.target).parent().find("span.error").show();
            
            }
            else {
                // 마지막 전화번호 4자리가 정규표현식에 맞는 경우
                $("table#tblMemberRegister :input").prop("disabled", false); 
    
                //  $(e.target).next().hide();
                //  또는
                $(e.target).parent().find("span.error").hide();
            }
    
        });// 아이디가 hp3 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 


        $("input#postcode").blur((e) => { 

            const regExp_postcode = /^\d{5}$/;
            // 숫자 5자리만 들어오도록 검사해주는 정규표현식 객체 생성
            
            const bool = regExp_postcode.test($(e.target).val());
            
            if(!bool) {
                // 우편번호가 정규표현식에 위배된 경우 

                $("table#tblMemberRegister :input").prop("disabled", true);
                $(e.target).prop("disabled", false);
            //    $(e.target).val("").focus();

            //  $(e.target).next().show();
            //  또는
                $(e.target).parent().find("span.error").show();
            
            }
            else {
                // 우편번호가 정규표현식에 맞는 경우
                $("table#tblMemberRegister :input").prop("disabled", false); 

                //  $(e.target).next().hide();
                //  또는
                $(e.target).parent().find("span.error").hide();
            }

        });// 아이디가 postcode 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.


        /////////////////////////////////////////////////////////////////

        /*	
            >>>> .prop() 와 .attr() 의 차이 <<<<	         
                 .prop() ==> form 태그내에 사용되어지는 엘리먼트의 disabled, selected, checked 의 속성값 확인 또는 변경하는 경우에 사용함. 
                 .attr() ==> 그 나머지 엘리먼트의 속성값 확인 또는 변경하는 경우에 사용함.
		*/

        // 우편번호를 읽기전용(readonly) 로 만들기
        $("input#postcode").attr("readonly", true);

        // 주소를 읽기전용(readonly) 로 만들기
        $("input#address").attr("readonly", true);

        // 참고항목을 읽기전용(readonly) 로 만들기
        // $("input#extraAddress").attr("readonly", true); // 참고항목은 세미 플젝에서 사용안함!!

        // ==== "우편번호찾기"를 클릭했을 때 이벤트 처리하기 ==== //
   $("img#zipcodeSearch").click(function(){
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을 때 실행할 코드를 작성하는 부분.

            let addr = ''; // 주소 변수
            let extraAddr = ''; // 참고항목 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일 때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            }

            // 최종적으로 addr에 extraAddr을 합쳐서 address 필드에 넣는다.
            document.getElementById("address").value = addr + (extraAddr ? extraAddr : '');

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;

            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detail_address").focus();
        }
    }).open();

            
         // 주소를 비활성화 로 만들기
        //  $("input#address").attr("disabled", true);
        
         // 참고항목을 비활성화 로 만들기
        //  $("input#extraAddress").attr("disabled", true);

         // 주소를 쓰기가능 으로 만들기
		//  $("input#address").removeAttr("readonly");
        
         // 참고항목을 쓰기가능 으로 만들기
        //  $("input#extraAddress").removeAttr("readonly");
        
         // 주소를 활성화 시키기
	    //	$("input#address").removeAttr("disabled");
        
         // 참고항목을 활성화 시키기
        //  $("input#extraAddress").removeAttr("disabled");    

        });// end of $("img#zipcodeSearch").click(function(){})--------



});// $(document).ready(function(){}-------------------------


// Function Declaration
// 수정하기 버튼 클릭시 호출되는 함수
function goSubmit() {

    // *** 필수입력사항에 모두 입력이 되었는지 검사하기 시작 *** //
    let b_requiredInfo = true;

    const requiredInfo_list = document.querySelectorAll("input.requiredInfo"); 
    for (let i = 0; i < requiredInfo_list.length; i++) {
        const val = requiredInfo_list[i].value.trim();
        if (val == "") {
            alert("* 표시된 필수입력사항은 모두 입력하셔야 합니다.");
            b_requiredInfo = false;
            break;
        }
    }
    if (!b_requiredInfo) {
        return;
    }   
    // *** 필수입력사항에 모두 입력되었는지 검사 끝 *** //
	
    // *** 우편번호 및 주소 입력 여부 검사 시작 *** //
    const arr_addressInfo = [
        $("#postcode").val(),
        $("#address").val(),
        $("#detail_address").val()
    ];

    for (let i = 0; i < arr_addressInfo.length; i++) {
        if (arr_addressInfo[i].trim() == "") {
            alert("우편번호 및 주소를 입력하셔야 합니다.");
            return; 
        }
    }
    // *** 우편번호 및 주소 입력 여부 검사 끝 *** //

    // 배송지 수정 요청
    $.ajax({
        url: ctxPath + '/delivery/deliveryModify.gu', // 서버 요청 URL
        type: 'POST',
        data: {
            deliveryno: $("#deliveryno").val(),			
            delivery_name: $("#delivery_name").val(),
            receiver: $("#receiver").val(),
            hp1: $("#hp1").val(),
            hp2: $("#hp2").val(),
            hp3: $("#hp3").val(),
            postcode: $("#postcode").val(),
            address: $("#address").val(),
            detail_address: $("#detail_address").val(),
            memo: $("#memo").val(),
            is_default: $("#is_default").prop('checked') // 체크박스 상태
        },
        success: function(response) {
            if (confirm("수정이 완료되었습니다. 배송지 목록으로 이동하시겠습니까?")) {
                window.location.href = ctxPath + '/delivery/deliveryList.gu';
            } 
        },
        error: function(xhr, status, error) {
            alert("전송 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const isDefaultCheckbox = document.getElementById("is_default"); // 기본 배송지 체크박스
    
    if (isDefaultCheckbox) {
        isDefaultCheckbox.addEventListener("change", function () {
            if (this.checked) {
                // 기본 배송지 변경 여부 확인
                fetch("/checkDefaultDelivery", {
                    method: "GET",
                })
                .then((response) => response.json())
                .then((data) => {
                    if (data.hasDefault) {
                        if (!confirm("기본 배송지가 이미 있습니다. 기본 배송지를 바꾸시겠습니까?")) {
                            this.checked = false; // 취소 시 체크박스 상태 초기화
                        }
                    }
                })
                .catch((error) => console.error("기본 배송지 확인 요청 중 오류 발생:", error));
            }
        });
    }
});

	


	// 취소 함수: 배송지 등록 취소 확인 후, 마이페이지로 돌아가는 처리
	function goResist() {
		const isCancelled = confirm("정말 배송지 수정을 취소하고 마이페이지로 돌아가겠습니까?");

		if (isCancelled) { // 취소 확인 후
			// 폼 초기화
			$("form[name='registerFrm']")[0].reset();
			$("span.error").hide();  // 에러 메시지 숨기기
			$("#delivery_name").prop("disabled", false);  // 폼 요소 활성화
			$("#is_default").prop("checked", false);  // 기본값 체크 해제

			// 배송지 목록으로 이동          
			window.location.href = ctxPath+'/delivery/deliveryList.gu';
		}
	}
