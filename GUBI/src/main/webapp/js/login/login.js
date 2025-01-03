/**
 * 
 */
const contextPath = sessionStorage.getItem("contextpath");

$(document).ready(function () {

    $(`input#loginUserpasswd`).keydown((e) => {

        if (e.keyCode == 13) { // 입력란에 엔터를 했을 경우

            gouserLogin();
        }

    });//end of $(`input#loginPwd`).keydown((e) => { }...



    // 
    $(`input#loginAdminpasswd`).keydown((e) => {

        if (e.keyCode == 13) { // 입력란에 엔터를 했을 경우

            goadminLogin();
        }

    });//end of $(`input#loginPwd`).keydown((e) => { }...

    


});//end of ready...



// 로그인 처리 함수
function gouserLogin() {

    // alert("로그인처리합니다.");


    // 유효성 검사
    if ($(`input#loginUserid`).val().trim() == "") {
        alert("아이디를 입력하세요!");
        $(`input#loginUserid`).val("");
        return; // 종료
    }


    if ($(`input#loginUserpasswd`).val().trim() == "") {
        alert("비밀번호를 입력하세요!");
        $(`input#loginUserpasswd`).val("");
        return; // 종료
    }


    // === 로그인을 하지 않은 상태일 때 
    //     로컬스토리지(localStorage)에 저장된 key가 'saveid' 인 userid 값을 불러와서 
    //     input 태그 userid 에 넣어주기 ===

    if ($(`input:checkbox[id="saveid"]`).prop("checked")) {
        // alert("아이디저장 체크를 하셨습니다.");
        localStorage.setItem("saveid", $(`input#loginUserid`).val());

    } else {
        // alert("아이디저장 체크를 해제하셨습니다.");
        localStorage.removeItem("saveid");
    }

    const frm = document.userloginFrm;


    frm.submit();


    // 휴면은 백엔드에서 갈라지는 것으로 합의
    
}//end of function goLogin() { }...





function goadminLogin() {

    // 유효성 검사
    if ($(`input#loginAdminid`).val().trim() == "") {
        alert("아이디를 입력하세요!");
        $(`input#loginAdminid`).val("");
        return; // 종료
    }


    if ($(`input#loginAdminpasswd`).val().trim() == "") {
        alert("비밀번호를 입력하세요!");
        $(`input#loginAdminpasswd`).val("");
        return; // 종료
    }


    const frm = document.adminloginFrm;


    frm.submit();
    
}//end of function goLogin() { }...




// // 로그아웃 버튼 메소드
// function goLogOut() {

//     // 로그아웃을 처리해주는 페이지로 이동
//     location.href = `${contextPath}/login/logout.gu`;

// }//end of function goLogOut() { }...





// 회원가입으로 이동하는 버튼 메소드
function goRegister() { // 세션에서 얻어오는 것이 좋을 듯

    // 로그아웃을 처리해주는 페이지로 이동
    location.href = `${contextPath}/member/memberRegister.gu`;

}//end of function goLogOut() { }...



// 무지성 세션 발싸용 크롬 세션에 패스 저장, header 에 넣자.

// // HTML, JSP에서 EL을 사용하여 context path를 session에 저장
// <script type="text/javascript" charset="utf-8">
// sessionStorage.setItem("contextpath", "${pageContext.request.contextPath}");
// </script>

// // script에서 저장된 context path를 호출
// const contextPath = sessionStorage.getItem("contextpath");













/*   !!!!!! 필수로 기억해야 합니다. !!!!!!
    
        >>>> 로컬 스토리지(localStorage)와 세션 스토리지(sessionStorage) <<<< 
        로컬 스토리지와 세션 스토리지는 HTML5에서 추가된 저장소이다.
        간단한 키와 값을 저장할 수 있다. 키-밸류 스토리지의 형태이다.
        
        ※ 로컬 스토리지와 세션 스토리지의 차이점은 데이터의 영구성이다. 
        로컬 스토리지의 데이터는 사용자가 지우지 않는 이상 계속 브라우저에 남아 있게 된다. 
        만료 기간을 설정할 수 없다.
        하지만 세션 스토리지의 데이터는 윈도우나 브라우저 탭을 닫을 경우 자동적으로 제거된다.
        지속적으로 필요한 데이터(자동 로그인 등)는 로컬 스토리지에 저장하고, 
        잠깐 동안 필요한 정보(일회성 로그인 정보라든가)는 세션 스토리지에 저장하도록 한다. 
        그러나 비밀번호같은 중요한 정보는 절대로 저장하면 안된다.
        왜냐하면 클라이언트 컴퓨터 브라우저에 저장하는 것이기 때문에 타인에 의해 도용당할 수 있기 때문이다.

        로컬 스토리지랑 세션 스토리지가 나오기 이전에도 브라우저에 저장소 역할을 하는 게 있었다.
        바로 쿠키인데 쿠키는 만료 기한이 있는 키-값 저장소이다.

        쿠키는 4kb 용량 제한이 있고, 매번 서버 요청마다 서버로 쿠키가 같이 전송된다.
        만약 4kb 용량 제한을 거의 다 채운 쿠키가 있다면, 요청을 할 때마다 기본 4kb의 데이터를 사용한다. 
        4kb 중에는 서버에 필요하지 않은 데이터들도 있을 수 있다. 
        그러므로 데이터 낭비가 발생할 수 있게 된다. 
        바로 그런 데이터들을 이제 로컬 스토리지와 세션 스토리지에 저장할 수 있다. 
        이 두 저장소의 데이터는 서버로 자동 전송되지 않는다.

        >> 로컬 스토리지(localStorage) <<
        로컬 스토리지는 window.localStorage에 위치한다. 
        키 밸류 저장소이기 때문에 키와 밸류를 순서대로 저장하면 된다. 
        값으로는 문자열, boolean, 숫자, null, undefined 등을 저장할 수 있지만, 
        모두 문자열로 변환된다. 키도 문자열로 변환된다.

        localStorage.setItem('name', '이순신');
        localStorage.setItem('birth', 1994);

        localStorage.getItem('name');        // 이순신
        localStorage.getItem('birth');       // 1994 (문자열)

        localStorage.removeItem('birth');    // birth 삭제
        localStorage.getItem('birth');       // null (삭제됨)

        localStorage.clear();                // 전체 삭제

        localStorage.setItem(키, 값)으로 로컬스토리지에 저장함.
        localStorage.getItem(키)로 조회함. 
        localStorage.removeItem(키)하면 해당 키가 지워지고, 
        localStorage.clear()하면 스토리지 전체가 비워진다.

        localStorage.setItem('object', { userid : 'leess', name : '이순신' });
        localStorage.getItem('object');   // [object Object]
        객체는 제대로 저장되지 않고 toString 메소드가 호출된 형태로 저장된다. 
        [object 생성자]형으로 저장되는 것이다. 
        객체를 저장하려면 두 가지 방법이 있다. 
        그냥 키-값 형식으로 풀어서 여러 개를 저장할 수도 있다. 
        한 번에 한 객체를 통째로 저장하려면 JSON.stringify를 해야된다. 
        객체 형식 그대로 문자열로 변환하는 것이다. 받을 때는 JSON.parse하면 된다.

        localStorage.setItem('object', JSON.stringify({ userid : 'leess', name : '이순신' }));
        JSON.parse(localStorage.getItem('object')); // { userid : 'leess', name : '이순신' }
     
        이와같이 데이터를 지우기 전까지는 계속 저장되어 있기 때문에 
        사용자의 설정(보안에 민감하지 않은)이나 데이터들을 넣어두면 된다.  

        >> 세션 스토리지(sessionStorage) <<
        세션 스토리지는 window.sessionStorage에 위치한다. 
        clear, getItem, setItem, removeItem, key 등 
        모든 메소드가 로컬 스토리지(localStorage)와 같다. 
        단지 로컬스토리지와는 다르게 데이터가 영구적으로 보관되지는 않을 뿐이다. 
            
        >> 로컬 스토리지(localStorage)와 세션 스토리지(sessionStorage) 에 저장된 데이터를 보는 방법 << 
        크롬인 경우 F12(개발자도구) Application 탭에 가면 Storage - LocalStorage 와 SessionStorage 가 보여진다.
        거기에 들어가서 보면 Key 와 Value 값이 보여진다.
    */










