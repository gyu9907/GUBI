

let isExist = false; // 본인확인 완료면

const contextPath = sessionStorage.getItem("contextpath");


$(document).ready(function () {


    $("input#agreeCheck").click(function (e) { 
        console.log("checkbox => ", $(e.target).prop("checked"));
        
    });


    const checkbox = $("input#agreeCheck").prop('checked');





    // console.log("checkbox => ", checkbox);
    

    






    // 본인확인 버튼 클릭 후 비밀번호 변경 시 인증 초기화 용도
    $(`input#passwd`).change(function (e) {
        isExist = false;
        $(`span#isExist`).html("").hide();

    });//end of $(`span#email`).click(function (e) { }...







});//end of ready...




// 회원 확인 메소드
function isExistMember() {

    $.ajax({
        url: "isExistMember.gu", //              
        data: {
            "userid": $(`input#userid`).val(),
            "passwd": $(`input#passwd`).val()
        },
        type: "post",  // 빼면 get 방식
        async: true,       // async:true 가 비동기 방식을 말한다. async 을 생략하면 기본값이 비동기 방식인 async:true 이다.
        // async:false 가 동기 방식이다. 지도를 할때는 반드시 동기방식인 async:false 을 사용해야만 지도가 올바르게 나온다.
        dataType: "json", // Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다. 
        // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
        // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.

        success: function (json) { //   
            if (json["isExists"]) {
                // 본인확인완료
                $(`span#isExist`).html(`본인확인 완료`).css({ "color": "blue" }).show();
                isExist = true;
            } else {
                // 본인이 아님
                $(`span#isExist`).html(`본인이 아닙니다.`).css({ "color": "red" }).show();
            }
        }
        ,
        error: function (request, status, error) { // 코딩이 잘못되어지면 어디가 잘못되어졌는지 보여준다!
            alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
        }
    });

}//end of function isExistMember(params) {}...








function gomemberDelete() {

    if ($("input#agreeCheck").prop("checked") == false) {
        alert("처리사항 안내 확인 동의를 해주십시오.");
        return;
    }

    if (!isExist) {
        alert("본인인증을 해주십시오.");
        return;
    }

    // 
    if (confirm("정말로 회원을 탈퇴하시겠습니까?")) {

        // 발송 서브밋
        const frm = document.memberDeleteFrm;
        frm.action = "memberDelete.gu";
        frm.method = "post";
        frm.submit();

    } else {
        location.href = `${contextPath}/index.gu`;
    }

}//end of function gomemberDelete() {}...
















