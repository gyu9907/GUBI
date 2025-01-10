

const contextPath = sessionStorage.getItem("contextpath");



$(document).ready(function () {

    // 헤더
    document.querySelector('#hamburger').addEventListener('click', function () {
        const display = document.querySelector('div.grid_box:nth-child(2) ul').style.display;

        if (display == 'block') {
            document.querySelector('div.grid_box:nth-child(2) ul').style.display = "";
        }
        else {
            document.querySelector('div.grid_box:nth-child(2) ul').style.display = "block";
        }
    });

    window.addEventListener('resize', function () {
        if (window.innerWidth > 768) {
            document.querySelector('div.grid_box:nth-child(2) ul').style.display = "";
        }
    });



    $("span.span_button").click(function (e) {

        // 
        $("span.span_button").css({ "font-weight": "normal" });
        $(e.target).css({ "font-weight": "bold" });

        // 클릭했을 때 소트해야한다.

    });




    // 비밀번호 체크박스를 체크하면 비밀번호 칸이 나옴
    // 체크박스 벨류도 정해줄 수 있다.
    // 체크박스 선택 안하면 벨류 안간다.
    $("input#is_hide").click(function (e) {

        const checked = $(e.target).prop("checked");

        $("div.hidden_passwd").slideToggle();
        // $("div.hidden_passwd").slideDown();

        if (checked) { // 체크박스 value 줄 수 있다!
            // $("div.hidden_passwd").slideUp();
            $(e.target).val("1");
            console.log($(e.target).val());

        } else {
            // $("div.hidden_passwd").slideDown();
            // $(e.target).val("0");
            // console.log($(e.target).val());
            // 비밀번호 비워줘야함
            // null 로 전송
            $("input#QnA_passwd").val("");
        }

    });//end of $("input#secretQnA").click(function (e) { }...


    // 나오자마자 실행
    sortAsk("0");



    $("span.span_button").click(function (e) {

        let span_index = $("span.span_button").index(this);
        console.log("span_index => ",span_index);

        sortAsk(span_index);

    });//end of $("span.span_button").click(function (e) {}...




    $("span#span_QnA").on("click", function() {

		const width = 1100;
		const height = 800;

		const left = Math.ceil((window.screen.width - width) / 2); // 정수로 만듬
		const top = Math.ceil((window.screen.height - height) / 2); // 정수로 만듬
		
		const url = `${contextPath}/ask/ask.gu?productno=${$("input#productno").val()}`;

		window.open(url, "ask",
			`left=${left}, top=${top}, width=${width}, height=${height}`);
	});







});//end of ready...




// 취소하기 메소드
function goBack() {

    // 모든 값들 초기화하기

    // 체크박스 값 0 으로,초기화, 체크박스 해제     
    // $("input#is_hide").val("0");
    $("input#is_hide").prop("checked", false);

    // 비밀번호 초기화, 숨김
    $("input#QnA_passwd").val("");
    $("div.hidden_passwd").hide();

    // select 태그 처음 위치로 돌아가기
    $("select#QnA_type option:eq(0)").prop("selected", true);//처음 위치로 돌아가기 

    // textarea 태그 내용 초기화
    $("textarea#question").val("");

    self.close();

}//end of function goBack(params) {}...




// submit 메소드
function goQnA() {

    // 내용이 5글자 이하면
    const textarea_val = $("textarea#question").val().trim();
    console.log("textarea_val => ", textarea_val);

    if (textarea_val.length < 5) {
        alert("문의는 5글자 이상 입력하십시오.");
        return;
    }

    // passwd 처리
    const passwd = $("input#QnA_passwd").val().trim();

    if (passwd == "") {

        $("input#QnA_passwd").val("");
        $("span#passwderror").html("비밀번호는 필수입력 사항입니다.");

    } else {
        const regExp_passwd = new RegExp(/^[0-9]{4}$/);
        // 숫자/문자/특수문자 포함 형태의 8~16자리 이내의 암호 정규표현식 객체 생성

        const bool = regExp_passwd.test(passwd);

        if (!bool) { // 암호가 정규표현식에 위배된 경우 
            $("span#passwderror").html("");
            $("input#QnA_passwd").val("").focus();
            $("span#passwderror").html("비밀번호는 숫자4자리로 입력해주십시오.");
        }
        else {
            // 암호가 정규표현식에 맞는 경우
            $("span#passwderror").html("");
        }
    }



    const queryString = $("form[name='QnAFrm']").serialize();

    // console.log(`${contextPath}/ask/ask.gu`);


    // 등록 처리는 ajax
    $.ajax({
        type: "post",
        url: `${contextPath}/ask/ask.gu`,
        data: queryString,
        dataType: "json",
        success: function (json) {

            if (json.n == "1") {

                alert(json.message); // 띄어주기
                self.close();// 팝업 닫기

            }
        },
        error: function (request, status, error) {
            alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
        }
    });

}//end of function goQnA() {}...




// 처음 나오는 문의 소트, 선택 시 소트
function sortAsk(index) {
    
    $.ajax({
        type: "get",
        url: `${contextPath}/ask/sortAsk.gu`,
        data: {
            "QnA_type": index,
            "productno": $("input#productno").val() // 원래는 productDetail.jsp 의 productno 를 받아와야함
            // 유저아이디는 java 에서 세션 꺼내오자.
        },
        dataType: "json",
        async: true,
        success: function (json) {

            let v_html = ``;
            // console.log("json.true => ",json.true);
            // console.log("json.false => ",json.false);
            console.log("json => ",json);
            console.log("json.length => ",json.length); // 안떠
            
            let QnA_type = "";

            if (json.length > 0) {
                
                let cnt = 1;
                $.each(json, function (index, item) {

                    if (item.ask_category == "0") {
                        QnA_type = "상품문의";
                    }
                    if (item.ask_category == "1") {
                        QnA_type = "배송문의";
                    }
                    if (item.ask_category == "2") {
                        QnA_type = "기타문의";
                    }

                    if (item.is_hide == "1") { // 비공개 문의
                        
                        v_html += `
                            <div class="containerddd">
                                <div class="text-section">
                                    <span class="span2">${QnA_type}</span>
                                    <strong class="span2">비공개 문의입니다.</strong>
                                </div>
                                <span class="id">${item.fk_userid}</span>
                                <span class="date">${item.registerday}</span>
                            </div>`;

                    } else { // 공개글 문의 답변

                    v_html += 
                        `<p>
                            <button class="btn containersss" type="button" data-toggle="collapse" data-target="#demo${cnt}" style="width: 100%;"> 
                                <div class="text-section">
                                    <span class="span1">${QnA_type}</span>
                                    <strong>${item.subject}</strong>
                                </div>
                                <span class="id">${item.fk_userid}</span>
                                <span class="date">${item.registerday}</span>
                            </button>
                        </p>

                        <div class="collapse mb-1" id="demo${cnt}">
                            <div class="card card-body" style="display: flex; margin-bottom: 20px; background-color:#f7f7f7;">
                                <div class="containeraaa">
                                    <div class="text-section">
                                        <div>문의내용 :</div>
                                        <strong class="div_width">${item.question}</strong>
                                        <br>
                                        <div>답변 :</div>
                                        <strong class="div_width">${item.answer}</strong>
                                    </div>
                                    <span class="id">관리자</span>
                                    <span class="date">${item.answerday}</span>
                                </div> 
                            </div>
                        </div>`;
                        
                        cnt++;
                    }
                });
                $("div#sortAsklist").html(v_html); 

            } else {
                v_html = `조건에 해당하는 문의 사항이 없습니다.`;
                $("div#sortAsklist").html(v_html).addClass('no_list');
            }
        },
        error: function (request, status, error) {
            alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
        }

    });


}//end of function sortAsk(params) {}...

























