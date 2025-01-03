
let is_id_ok = false; // 아이디 입력 블러 중 버튼 바로 누를 시 걸러주는 역할

let is_email_ok = false; // 이메일 입력 블러 중 버튼 바로 누를 시 걸러주는 역할

let b_idcheck_click = false; // 아이디 중복확인 버튼 클릭했는지 여부

// let b_email_click = false; // 이메일 중복확인 버튼 클릭했는지 여부

let b_email_auth_click = false; // 인증버튼 클릭했는지 여부

let is_email_auth = false; // 인증번호를 인증 받았는지 여부


$(document).ready(function () {

    // $("div.error").hide();
    $("input#userid").focus();

    // $("input#name").bind("blur", function(e){ alert("name 에 있던 포커스를 잃어버렸습니다."); }); 
    // 또는
    // $("input#name").blur(function(e){ alert("name 에 있던 포커스를 잃어버렸습니다."); }); 


    // userid 블러 처리
    const func_userid = (e) => {

        const userid = $(e.target).val().trim();

        if (userid == "") { // 입력하지 않거나 공백만 입력했을 경우 

            $(e.target).val("");
            // $(e.target).focus();
            $("div#idcheckResult").html("").hide(); // 아이디 중복체크
            $("div#useriderror").html("아이디는 필수입력 사항입니다.");

        }
        else { // 공백이 아닌 글자를 입력했을 경우
            const regExp_userid = /^[a-z][A-Za-z0-9]{4,19}$/;

            // 생성된 정규표현식 객체속에 데이터를 넣어서 검사하기 
            const bool = regExp_userid.test(userid); // 리턴타입 boolean

            if (!bool) {
				$("div#idcheckResult").html("").hide(); // 아이디 중복체크
                $("div#useriderror").html("");
                $(e.target).val("");
                $(e.target).focus();
                $("div#useriderror").html("아이디는 영문소문자/숫자 4~16 조합으로 입력하세요.");
                console.log("bool => ", bool);

            } else {
                $("div#useriderror").html("");
                is_id_ok = true;
            }
        }

    }// 아이디가 userid 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.

    $("input#userid").blur((e) => { func_userid(e) });

    // passwd 블러 처리
    $("input#passwd").blur((e) => {

        const passwd = $(e.target).val().trim();

        if (passwd == "") {

            $(e.target).val("");
            // $(e.target).focus();

            $("div#passwderror").html("비밀번호는 필수입력 사항입니다.");

        } else {
            const regExp_passwd = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
            // 숫자/문자/특수문자 포함 형태의 8~16자리 이내의 암호 정규표현식 객체 생성

            const bool = regExp_passwd.test(passwd);

            if (!bool) { // 암호가 정규표현식에 위배된 경우 
                $("div#passwderror").html("");
                $(e.target).val("").focus();
                $("div#passwderror").html("비밀번호는 숫자/문자/특수문자 포함하여 8~16자리로 입력하세요.");
            }
            else {
                // 암호가 정규표현식에 맞는 경우
                $("div#passwderror").html("");
            }
        }

    });// 아이디가 passwd 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.


    // 비밀번호 확인
    $("input#passwdcheck").blur((e) => {

        if ($("input#passwd").val() != $(e.target).val()) {
            // 암호와 암호확인값이 틀린 경우 

            // $("input#passwd").val("")
            if ($(e.target).val() != "") { // 틀렸는데 비어있지 않으면 비우고 포커스 하고 오류출력
                $(e.target).val("").focus();
            }
            // 그냥 비어있으면 출력
            $(`div#passwdcheckerror`).html("비밀번호가 일치하지 않습니다.");

        }
        else {
            // 암호와 암호확인값이 같은 경우
            $(`div#passwdcheckerror`).html("");
        }

    });// 아이디가 pwdcheck 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.


    // name 블러 처리
    $("input#name").blur((e) => {

        const name = $(e.target).val().trim();
        if (name == "") {
            // 입력하지 않거나 공백만 입력했을 경우 

            $(e.target).val("");
            // $(e.target).focus();

            $("div#nameerror").html("이름은 필수입력 사항입니다.");

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



    // 생년월일 
    // === jQuery UI 의 datepicker === //
    $("input#datepicker").datepicker({
        dateFormat: 'yy-mm-dd'     //Input Display Format 변경
        , showOtherMonths: true    //빈 공간에 현재월의 앞뒤월의 날짜를 표시
        , showMonthAfterYear: true //년도 먼저 나오고, 뒤에 월 표시
        , changeYear: true         //콤보박스에서 년 선택 가능
        , changeMonth: true        //콤보박스에서 월 선택 가능                
        //  ,showOn: "both"        //button:버튼을 표시하고,버튼을 눌러야만 달력 표시됨. both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시됨.  
        //  ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
        //  ,buttonImageOnly: true   //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
        //  ,buttonText: "선택"       //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
        , yearSuffix: "년"         //달력의 년도 부분 뒤에 붙는 텍스트
        , monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'] //달력의 월 부분 텍스트
        , monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 Tooltip 텍스트
        , dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'] //달력의 요일 부분 텍스트
        , dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'] //달력의 요일 부분 Tooltip 텍스트
        , minDate: "-100Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
        , maxDate: "today" //최대 선택일자(+1D:하루후, +1M:한달후, +1Y:일년후)   
        , yearRange: "1900:today"
    });

    // 초기값을 오늘 날짜로 설정
    // $('input#datepicker').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후) 

    // === 전체 datepicker 옵션 일괄 설정하기 ===  
    //     한번의 설정으로 $("input#fromDate"), $('input#toDate')의 옵션을 모두 설정할 수 있다.
    // $(function () {
    //     //모든 datepicker에 대한 공통 옵션 설정
    //     $.datepicker.setDefaults({
    //         dateFormat: 'yy-mm-dd' //Input Display Format 변경
    //         , showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
    //         , showMonthAfterYear: true //년도 먼저 나오고, 뒤에 월 표시
    //         , changeYear: true //콤보박스에서 년 선택 가능
    //         , changeMonth: true //콤보박스에서 월 선택 가능        
    //         // ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시됨. both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시됨.  
    //         // ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
    //         // ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
    //         // ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
    //         , yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
    //         , monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'] //달력의 월 부분 텍스트
    //         , monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 Tooltip 텍스트
    //         , dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'] //달력의 요일 부분 텍스트
    //         , dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'] //달력의 요일 부분 Tooltip 텍스트
    //         , minDate: "-100Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
    //         // ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                    
    //     });

    //     // input을 datepicker로 선언
    //     $("input#fromDate").datepicker();
    //     $("input#toDate").datepicker();

    //     // From의 초기값을 오늘 날짜로 설정
    //     $('input#fromDate').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)

    //     // To의 초기값을 3일후로 설정
    //     $('input#toDate').datepicker('setDate', '+3D'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
    // });

    ////////////////////////////////////////////////////////////////////

	$("input#datepicker").attr("readonly", true); 
	// 생년월일에 키보드로 입력하는 경우 

	$("input#datepicker").bind("change", e => {
	    if ($(e.target).val() != "") {
	        $(e.target).next().hide();
	    }
	}); // 생년월일에 마우스로 값을 변경하는 경우







    // 이메일 메소드
    const func_email = (e) => {

        const email = $(e.target).val().trim();

        if (email == "") {

            $(e.target).val("");
            // $(e.target).focus();
            $(`div#emailCheckResult`).html("").hide(); // 이메일 체크 유무
            $("div#emailerror").html("이메일은 필수입력 사항입니다.");

        } else {
            const regExp_email = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i);
            // 이메일 정규표현식 객체 생성

            const bool = regExp_email.test(email);

            if (!bool) {
                // 이메일이 정규표현식에 위배된 경우 
                $(`div#emailCheckResult`).html("").hide(); // 이메일 체크 유무
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







    $("input#hp2").blur((e) => { /////////////////////////// 여기까지 봤다!!!

        // const regExp_hp2 = /^[1-9][0-9]{3}$/; 
        // 또는
        const regExp_hp2 = new RegExp(/^[1-9][0-9]{3}$/);
        // 연락처 국번( 숫자 4자리인데 첫번째 숫자는 1-9 이고 나머지는 0-9) 정규표현식 객체 생성

        const bool = regExp_hp2.test($(e.target).val());

        if (!bool) {
            // 연락처 국번이 정규표현식에 위배된 경우 
            $(e.target).val("");
            // $(e.target).focus();
            $(`telerror`).html("휴대폰 형식이 아닙니다.");
        }
        else {
            // 연락처 국번이 정규표현식에 맞는 경우
            $(`telerror`).html("");
        }

    });// 아이디가 hp2 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.        


    $("input#hp3").blur((e) => {

        const regExp_hp3 = new RegExp(/^\d{4}$/);
        // 연락처 국번( 숫자 4자리인데 첫번째 숫자는 1-9 이고 나머지는 0-9) 정규표현식 객체 생성

        const bool = regExp_hp3.test($(e.target).val());

        if (!bool) {
            // 마지막 전화번호 4자리가 정규표현식에 위배된 경우 
            $(e.target).val("");
            // $(e.target).focus();
            $(`telerror`).html("휴대폰 형식이 아닙니다.");
        }
        else {
            // 마지막 전화번호 4자리가 정규표현식에 맞는 경우
            $(`telerror`).html("");
        }

    });// 아이디가 hp3 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 



    // 우편번호
    $("input#postcode").blur((e) => {

        const postcode = /^\d{5}$/;
        // 숫자 5자리만 들어오도록 검사해주는 정규표현식 객체 생성

        const bool = postcode.test($(e.target).val());

        if (!bool) {
            // 우편번호가 정규표현식에 위배된 경우
            $(e.target).val("");
            // $(e.target).focus();
            $(`postcodeerror`).html("우편번호 형식에 맞지 않습니다.");
        }
        else {
            // 우편번호가 정규표현식에 맞는 경우
            $(`postcodeerror`).html("");

        }

    });// 아이디가 postcode 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다.


    // /////////////////////////////////////////////////////////////////

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
    // $("input#extra_Address").attr("readonly", true);

    var width = 500; //팝업의 너비
    var height = 500; //팝업의 높이

    // ==== "우편번호찾기"를 클릭했을 때 이벤트 처리하기 ==== //
    $("button#zipcodeSearch").click(function () {
        new daum.Postcode({
            width: width, //생성자에 크기 값을 명시적으로 지정해야 합니다.
            height: height,

            oncomplete: function (data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                let addr = ''; // 주소 변수
                let extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if (data.userSelectedType === 'R') {
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
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
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    // document.getElementById("extra_Address").value = extraAddr;

                } else {
                    // document.getElementById("extra_Address").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("address").value = addr + extraAddr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("detail_Address").focus();
            }
        }).open({
            left: -(window.screen.width / 2) - (width / 2), // 집에서는 － 넣어야 가운데로 온다
            top: (window.screen.height / 2) - (height / 2)
        });
        // open({q: '검색어', left: '팝업위치 x값', top: '팝업위치 y값', popupTitle: '팝업창의 타이틀', popupKey: '팝업창 구분값', autoClose: '자동 닫힘 유무'})

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




    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////
















    // 아이디 중복확인 클릭했을 시 이벤트 처리하기
    $(`button#idcheck`).click(function (e) {

        if (is_id_ok == false) {   // 여기까지 함, 인증번호 칸 숨기고 아이디 이메일 레디 밑에 함수로 선언해서 정리하자!!!
            // func_userid(e);
            return;
        }
        // === 두번째 방법 === //
        $.ajax({
            url: "idDuplicateCheck.gu", //              /member/ 까진 똑같아서 제외함
            data: { "userid": $(`input#userid`).val() }, // data 속성은 http://localhost:9090/MyMVC/member/idDuplicateCheck.up 로 전송해야할 데이터를 말한다.
            type: "post",  // 빼면 get 방식
            async: true,       // async:true 가 비동기 방식을 말한다. async 을 생략하면 기본값이 비동기 방식인 async:true 이다.
            // async:false 가 동기 방식이다. 지도를 할때는 반드시 동기방식인 async:false 을 사용해야만 지도가 올바르게 나온다.
            dataType: "json", // Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다. 
            // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
            // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.

            success: function (json) { //    
                console.log("json => ", json);
                console.log("typeof json => ", typeof json);

                if (json["isExists"]) {
                    // 입력한 userid 가 이미 사용중인 것이다!
                    $(`div#idcheckResult`).html(`${$(`input#userid`).val()}은 이미 사용중 이므로 다른 아이디를 입력하세요`).css({ "color": "red" }).show();
                    $(`input#userid`).val("");
                } else {
                    // 사용 가능한 아이디다!
                    $(`div#idcheckResult`).html(`${$(`input#userid`).val()}은 사용할 수 있습니다!`).css({ "color": "blue" }).show();
                    b_idcheck_click = true;
                }
            }
            ,
            error: function (request, status, error) { // 코딩이 잘못되어지면 어디가 잘못되어졌는지 보여준다!
                alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
            }
        });


    });//end of $(`img#idcheck`).click(function (e) {  }....



    // 아이디 값이 변경되면 가입하기 버튼 클릭시 아이디중복확인을 클릭했는지 여부 알아오기 용도 초기화 시키기
    $(`input#userid`).change(function (e) {
        b_idcheck_click = false;
        is_id_ok = false;
        $(`div#idcheckResult`).html("").hide();
        // 아이디 중복검사를 받아놓고 다시 아이디를 바꾸면 중복확인을 초기화 시켜줘야 한다!!!
    });//end of $(`input#userid`).change(function (e) { }...





    // 이메일 중복확인 클릭했을 시 이벤트 처리하기 
    $(`button#emailcheck`).click(function (e) {
        
		b_email_auth_click = false; // 또 클릭하면 자기 자신의 전송도 일단 거짓으로 만들자
		is_email_auth = false; // 이메일 인증까지 받아놓고 또 이메일 보내기 누르면 기존 인증번호를 없애야한다.
		$(`div#email_authResult`).html("");

        if (is_email_ok == false) {   // 여기까지 함, 인증번호 칸 숨기고 아이디 이메일 레디 밑에 함수로 선언해서 정리하자!!!
            // func_userid(e);
            return;
        } else {
            // 데이터 베이스 검사 후 중복이 안되면 true 값을 반환, 인증메일 발송
            $.ajax({
                url: "emailCheck_Send.gu", //              /member/ 까진 똑같아서 제외함
                data: { "email": $(`input#email`).val() }, // // data 속성은 http://localhost:9090/MyMVC/member/emailDuplicateCheck.up 로 전송해야할 데이터를 말한다.
                type: "post",  // 빼면 get 방식
                async: true,       // async:true 가 비동기 방식을 말한다. async 을 생략하면 기본값이 비동기 방식인 async:true 이다.
                // async:false 가 동기 방식이다. 지도를 할때는 반드시 동기방식인 async:false 을 사용해야만 지도가 올바르게 나온다.
                dataType: "json", // Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다. 
                // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
                // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.

                success: function (json) { //    
                    console.log("json => ", json);
                    console.log("typeof json => ", typeof json);

                    if (json["isExists"]) {
                        // 입력한 email 가 이미 사용중인 것이다!
                        $(`div#emailCheckResult`).html(`${$(`input#email`).val()}은 이미 사용중 이므로 다른 이메일을 입력하세요`).css({ "color": "red" }).show();
                        $(`input#email`).val("");
                    } else {
                        // 사용 가능한 이메일   인증번호 발송을 백엔드에서 구현하라
                        $(`div#emailCheckResult`).html(`${$(`input#email`).val()}로 인증번호 발송하였습니다!`).css({ "color": "blue" }).show();
                        b_email_auth_click = true;
                        $(`.hide_emailAuth`).show(); // 인증번호 칸 나타내기
                    }
                }
                ,
                error: function (request, status, error) { // 코딩이 잘못되어지면 어디가 잘못되어졌는지 보여준다!
                    alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
                }
            });
        }//end of if else...

    });//$(`span#email`).click(function (e) { }...




    // 인증번호 확인 버튼 클릭했을 시 이벤트 처리하기 
    $(`button#btn_email_auth`).click(function (e) {

        if (b_email_auth_click == false) {
            alert("이메일이 변경되었습니다. 인증번호를 다시 발급해주세요.");

        } else { // true 여야 데이터 베이스에 들어감
            $.ajax({
                url: "emailAuth.gu", //              /member/ 까진 똑같아서 제외함
                data: { "email_auth_text": $(`input#email_auth`).val() }, // // data 속성은 http://localhost:9090/MyMVC/member/emailDuplicateCheck.up 로 전송해야할 데이터를 말한다.
                type: "post",  // 빼면 get 방식
                async: true,       // async:true 가 비동기 방식을 말한다. async 을 생략하면 기본값이 비동기 방식인 async:true 이다.
                // async:false 가 동기 방식이다. 지도를 할때는 반드시 동기방식인 async:false 을 사용해야만 지도가 올바르게 나온다.
                dataType: "json", // Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다. 
                // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
                // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.

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
        b_email_auth_click = false;   // 이메일 인증 버튼 체크
        is_email_auth = false;        // 이메일 인증 했는지 체크
        is_email_ok = false;          // 이메일 입력 중 바로 버튼을 눌렀을 경우 걸러주는 역할
        // 이메일도 중복검사를 받아놓고 다시 바꾸면 중복확인을 초기화 시켜줘야 한다!!!
        $(`div#emailCheckResult`).html("").hide();
        $(`div#email_authResult`).html("").hide();

    });//end of $(`span#email`).click(function (e) { }...



});// end $(document).ready(function(){})----------------------





// Function Declaration
// "가입하기" 버튼 클릭시 호출되는 함수
function goRegister() {

    // === 필수 입력사항 모두 입력했는지 검사하기 시작 === //
    let b_requiredInfo = true;

    $("input.requiredInfo").each(function (index, elmt) {
        const data = $(elmt).val().trim();
        if (data == "") {
            alert("*표시된 필수 입력사항은 모두 입력하셔야 합니다.");
            b_requiredInfo = false;
            return false; // break; 라는 뜻
        }
    });


    if (!b_requiredInfo) {
        return; // goRegister() 함수 종료
    }


    // // *** 우편번호 및 주소에 값을 입력했는지 검사하기 시작 *** //
    const arr_addressInfo = []; // 배열로 저장하여 일일이 안써도 된다!
    arr_addressInfo.push($("input#postcode").val());
    arr_addressInfo.push($("input#address").val());
    arr_addressInfo.push($("input#detail_Address").val());

    let b_addressInfo = true;


    for (let i = 0; i < arr_addressInfo.length; i++) {
        if (arr_addressInfo[i].trim() == "") {
            alert("우편번호 및 주소를 입력하셔야 합니다.");
            b_addressInfo = false;
            break;
        }
    }//end of for...


    if (!b_addressInfo) {
        return; //함수종료
    }


   // *** 우편번호 및 주소에 값을 입력했는지 검사하기 끝 *** //


    // 생년월일 입력했는지 검사하기
    const birth = $(`input#datepicker`).val().trim();

    if (birth == "") {
        alert("생년월일을 입력하셔야 합니다.");
        return;
    }


    // 약관에 동의 했는지 검사
    const agree_checked_length = $(`input#agree:checked`).length; // 1 나와야함

    if (agree_checked_length == 0) {
        alert("이용약관에 동의하셔야 회원가입할 수 있습니다.");
        return;
    }



    // 아이디 중복확인 클릭했는지 여부 확인
    if (!b_idcheck_click) {
        alert("아이디 중복확인을 하셔야 합니다!");
        return;
    }


    // 이메일 중복확인 클릭했는지 여부 확인
    if (!b_email_auth_click) {
        alert("이메일 중복확인을 하셔야 합니다!");
        return;
    }



    // 발송 서브밋
    const frm = document.registerFrm;
    frm.action = "memberRegister.gu";
    frm.method = "post";
    frm.submit();



    // alert("회원가입 하러 갑니다.");

}// end of function goRegister() {}-------------------------





















