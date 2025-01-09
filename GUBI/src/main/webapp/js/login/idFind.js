

let is_email_auth = false; // 인증번호를 인증 받았는지 여부

let is_email_ok = false; // 이메일 입력 블러 중 버튼 바로 누를 시 걸러주는 역할


const contextPath = sessionStorage.getItem("contextpath");

$(document).ready(function () {


	// name 블러 처리
	$("input#name").blur((e) => {

		const name = $(e.target).val().trim();
		if (name == "") {
			// 입력하지 않거나 공백만 입력했을 경우 

			$(e.target).val("");
			// $(e.target).focus();

			$("div#nameerror").html("성명은 필수입력 사항입니다.");

		}
		else {
			// 공백이 아닌 글자를 입력했을 경우
			const regExp_name = new RegExp(/^[가-힣]{2,10}|[a-zA-Z]{2,10}[a-zA-Z]{2,10}$/);
			// 한글, 또는 영어 

			const bool = regExp_name.test(name);

			if (!bool) { // 성명이 정규표현식에 위배된 경우 
				$("div#nameerror").html("");
				$(e.target).val("").focus();
				$("div#nameerror").html("성명은 한글, 영어로 최대 20글자까지 가능합니다.");
			}
			else {
				// 성명이 정규표현식에 맞는 경우
				$("div#nameerror").html("");
			}
		}

	});// 아이디가 name 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 



    // 이메일 메소드
    const func_email = (e) => {

        const email = $(e.target).val().trim();

        if (email == "") {

            $(e.target).val("");
            // $(e.target).focus();

            $("div#emailerror").html("이메일은 필수입력 사항입니다.");

        } else {
            const regExp_email = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i);
            // 이메일 정규표현식 객체 생성

            const bool = regExp_email.test(email);

            if (!bool) {
                // 이메일이 정규표현식에 위배된 경우 
                $("div#emailerror").html("");
                $(e.target).val("").focus();
                $("div#emailerror").html("이메일 형식이 아닙니다.");
            }
            else { // 이메일이 정규표현식에 맞는 경우

                $("div#emailerror").html("");
                is_email_ok = true;

            }
        }

    };// 아이디가 email 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.


    $("input#email").blur((e) => { func_email(e) });








    // 인증번호 확인 버튼 클릭했을 시 이메일 발송 이벤트 처리하기 
    $(`button#emailSend`).click(function (e) {

        is_email_send = false; // 또 클릭하면 자기 자신의 전송도 일단 거짓으로 만들자
        is_email_auth = false; // 이메일 인증까지 받아놓고 또 이메일 보내기 누르면 기존 인증번호를 없애야한다.
        $(`div#email_authResult`).html("");

        /*$(`.hide_emailAuth`).show();*/ // 인증번호 칸 나타내기 실험

        if (is_email_ok == false) {   // 여기까지 함, 인증번호 칸 숨기고 아이디 이메일 레디 밑에 함수로 선언해서 정리하자!!!
            // func_userid(e);
            return;

        } else {

            $.ajax({
                url: "emailSend.gu", //             
                data: { "email": $(`input#email`).val() }, // // data 속성은 http://localhost:9090/MyMVC/member/emailDuplicateCheck.up 로 전송해야할 데이터를 말한다.
                type: "post",  // 빼면 get 방식
                async: true,       // async:true 가 비동기 방식을 말한다. async 을 생략하면 기본값이 비동기 방식인 async:true 이다.
                // async:false 가 동기 방식이다. 지도를 할때는 반드시 동기방식인 async:false 을 사용해야만 지도가 올바르게 나온다.
                dataType: "json", // Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다. 
                // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
                // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.

                success: function (json) { //    

                    if (!json["sendMailSuccess"]) {
                        // 인증번호 메일 발송 실패
                        $(`div#emailSendResult`).html(`인증번호를 발송하지 못했습니다!`).css({ "color": "red" });
                    } else {
                        // 인증번호 발송
                        $(`div#emailSendResult`).html(`인증번호를 발송했습니다!`).css({ "color": "blue" });
                        is_email_send = true; // 인증번호 발송성공!
                        $(`.hide_emailAuth`).slideDown(); // 인증번호 칸 나타내기
                    }
                }
                ,
                error: function (request, status, error) { // 코딩이 잘못되어지면 어디가 잘못되어졌는지 보여준다!
                    alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
                }
            });
        }//end of if else...

    });//end of $(`button#btn_email_auth`).click(function (e) { }...




    // 인증번호 확인 버튼 클릭했을 시 이벤트 처리하기 
    $(`button#btn_email_auth`).click(function (e) {

        if (is_email_send == false) { // 발송이 실패하면
            alert("인증번호 발송 실패하였습니다. 나중에 다시 시도해주십시오.");

        } else { // true 여야 데이터 베이스에 들어감
            $.ajax({
                url: `${contextPath}/member/emailAuth.gu`, //              /member/ 까진 똑같아서 제외함
                data: { "email_auth_text": $(`input#email_auth`).val() }, // // data 속성은 http://localhost:9090/MyMVC/member/emailDuplicateCheck.up 로 전송해야할 데이터를 말한다.
                type: "post",  // 빼면 get 방식
                async: true,       // async:true 가 비동기 방식을 말한다. async 을 생략하면 기본값이 비동기 방식인 async:true 이다.
                // async:false 가 동기 방식이다. 지도를 할때는 반드시 동기방식인 async:false 을 사용해야만 지도가 올바르게 나온다.
                dataType: "json", // Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다. 

                success: function (json) { //    

                    if (!json["isExists"]) {
                        // 인증번호가 일치하지 않는다!
                        $(`div#email_authResult`).html(`인증번호가 일치하지 않습니다!`).css({ "color": "red" }).show();
                        $(`input#email_auth`).val("");
                    } else {
                        // 인증번호 일치!
                        $(`div#email_authResult`).html(`인증번호가 일치합니다!`).css({ "color": "blue" }).show();
                        is_email_auth = true; // 인증번호 일치!
                    }
                }
                ,
                error: function (request, status, error) { // 코딩이 잘못되어지면 어디가 잘못되어졌는지 보여준다!
                    alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
                }
            });

        }//end of if else..

    });//end of $(`button#btn_email_auth`).click(function (e) { }...


    // 이메일 값이 변경되면 가입하기 버튼 클릭시 아이디중복확인을 클릭했는지 여부 알아오기 용도 초기화 시키기
    $(`input#email`).change(function (e) {
        is_email_send = false;        // 이메일 전송 됐는지
        is_email_auth = false;        // 이메일 인증 일치 여부
        is_email_ok = false;          // 이메일 입력 중 바로 버튼을 눌렀을 경우 걸러주는 역할
        // 이메일도 중복검사를 받아놓고 다시 바꾸면 중복확인을 초기화 시켜줘야 한다!!!
        $(`div#emailSendResult`).html("").hide();
        $(`div#email_authResult`).html("").hide();

    });//end of $(`span#email`).click(function (e) { }...


});// end $(document).ready(function(){})----------------------





// 비밀번호 변경하기 버튼
function goidFind() {

    if (!is_email_send) {
        alert("이메일 발송 하셔야 합니다.");
        return;
    }

    if (!is_email_auth) {
        alert("인증번호 인증 후 아이디 찾기가 가능합니다.");
        return;
    }

    // 아이디 찾기 전송
    const frm = document.idFindFrm;

    // 발송 서브밋
    frm.submit();

}//end of function goReactivation() { }...





















