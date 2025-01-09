let file = ""; // 첨부되어진 파일 정보를 담아둘 변수 

var is_edit = false;

$(document).ready(function() {
	$("span.error").hide();

	// 모달 창에서 입력된 값 초기화 시키기 //
	$("button#modal_cancel").on("click", reset);

	// 버튼 클릭 시 optionno 값을 hidden input에 설정
	$('.reviewBtn').on('click', function(event) {

		var button = $(event.target); // 클릭된 버튼
		var optionno = button.data('value'); // data-value에서 optionno 값을 가져옴

		var modal = $('#addReviewModal');

		// hidden 필드에 optionno 값을 설정
		modal.find('#optionno').val(optionno);
	});

	// ==>> 리뷰이미지 파일선택을 선택하면 화면에 이미지를 미리 보여주기 시작 <<== //
	$(document).on("change", "input#img_file", function(e) {

		const input_file = $(e.target).get(0);

		//console.log(input_file.files[0].name); // 알고자하는 파일 이름

		// 자바스크립트에서 file 객체의 실제 데이터(내용물)에 접근하기 위해 FileReader 객체를 생성하여 사용한다.
		const fileReader = new FileReader();
		fileReader.readAsDataURL(input_file.files[0]);
		// FileReader.readAsDataURL() --> 파일을 읽고, result속성에 파일을 나타내는 URL을 저장 시켜준다.

		fileReader.onload = function() { // FileReader.onload --> 파일 읽기 완료 성공시에만 작동하도록 하는 것임.
			//console.log(fileReader.result);
			/*
				data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAeAB4AAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAg 
				이러한 형태로 출력되며, img.src 의 값으로 넣어서 사용한다.
				*/
			document.getElementById("imgPreview").src = fileReader.result;
			document.getElementById("imgPreview").style = "width:150px; min-width:150px; height:150px;";

		};

		//////////////////////////////////////////////////////////////////
	});
	// ==>> 리뷰이미지 파일선택을 선택하면 화면에 이미지를 미리 보여주기 끝 <<== //
	

	/* 별점 */
	const stars = document.querySelectorAll(".star");
	const ratingValueDisplay = document.getElementById("score");
	
	stars.forEach((star) => {
	  star.addEventListener("click", () => {
	    const rating = star.getAttribute("data-value");
	    ratingValueDisplay.value = rating;
	    fillStars(rating)
	  });
	});

	// 리뷰등록 및 수정하기
	$("button[name='addReview']").click(function() {
		let is_infoData_OK = true;

		//유효성검사
		$(".infoData").each(function(index, elmt) {
			const val = $(elmt).val().trim();
			if (val == "") {
				$(elmt).next().show();

				return false;
			}
		});

		// form 태그 전송하기
		if (is_infoData_OK) {
			// alert("확인용 : form 태그 전송하기");

			/* 
				FormData 객체는 ajax 로 폼 전송을 가능하게 해주는 자바스크립트 객체이다.
				즉, FormData란 HTML5 의 <form> 태그를 대신 할 수 있는 자바스크립트 객체로서,
				자바스크립트 단에서 ajax 를 사용하여 폼 데이터를 다루는 객체라고 보면 된다. 
				FormData 객체가 필요하는 경우는 ajax로 파일을 업로드할 때 필요하다.
			  */

			/*
				=== FormData 의 사용방법 2가지 ===
				<form id="myform">
				   <input type="text" id="title"   name="title" />
				   <input type="file" id="imgFile" name="imgFile" />
				</form>
				 
				첫번째 방법, 폼에 작성된 전체 데이터 보내기   
				var formData = new FormData($("form#myform").get(0));  // 폼에 작성된 모든것       
				또는
				var formData = new FormData($("form#myform")[0]);  // 폼에 작성된 모든것
				// jQuery선택자.get(0) 은 jQuery 선택자인 jQuery Object 를 DOM(Document Object Model) element 로 바꿔주는 것이다. 
				// DOM element 로 바꿔주어야 순수한 javascript 문법과 명령어를 사용할 수 있게 된다. 
		 
				또는
				var formData = new FormData(document.getElementById('myform'));  // 폼에 작성된 모든것
		  
				두번째 방법, 폼에 작성된 것 중 필요한 것만 선택하여 데이터 보내기 
				var formData = new FormData();
				// formData.append("key", value값);  // "key" 가 name 값이 되어진다.
				formData.append("title", $("input#title").val());
				formData.append("imgFile", $("input#imgFile")[0].files[0]);
			*/

			var formData = new FormData($("form[name='addReviewFrm']").get(0));
			// $("form[name='prodInputFrm']").get(0) 폼 에 작성된 모든 데이터 보내기
			console.log(formData)
			formData.forEach((v) => console.log(v))

			console.log('file::', file)

			// if(file_arr.length > 0) { // 추가이미지파일을 추가했을 경우
			if (file) {
				// 첨부한 파일의 총합의 크기가 50MB 이상 이라면 전송을 하지 못하게 막는다.
				const file_size = file.size;
				///////////////////////////////////////////
				if (file_size >= 50 * 1024 * 1024) { //첨부한 파일의 총합의 크기가 50MB 이상 이라면(50*1024*1024 = 10MB)
					alert("첨부한 추가이미지 파일의 총합의 크기가 50MB 이상이라서 제품등록을 할 수 없습니다.!!");
					return; // 종료
				} else { // 첨부한 파일의 총합의 크기가 50MB 미만 이라면, formData 속에 첨부파일 넣어주기
					formData.append("img", file)
				}

			} // end of if(file_arr.length > 0) {}--------------
			////////////////////////////////////////////////////////////

			let url = "/GUBI/review/reviewAdd.gu";

			if (is_edit) {
				url = "/GUBI/review/reviewEdit.gu";

				// file 수정 안했으면 formData에서 제거
				if (file.name == '') {
					formData.delete("img")
				}
			}

			$.ajax({
				url: url,
				type: "post",
				data: formData,
				processData: false,  // 파일 전송시 설정 
				contentType: false,  // 파일 전송시 설정
				dataType: "json",
				success: function(json) {
					console.log("~~~ 확인용 : " + JSON.stringify(json));
					if (json.result == 1) {
						location.href = location.href;
					}
					is_file_edited = false;
				},
				error: function(request, status, error) {
					alert(`리뷰를 ${is_edit ? '수정' : '작성'}하시려면 먼저 로그인 해주세요!`);
					location.href = "/GUBI/login/login.gu";
					is_file_edited = false;
				}
			});

			/*
				processData 관련하여, 일반적으로 서버에 전달되는 데이터는 query string(쿼리 스트링)이라는 형태로 전달된다. 
				ex) http://localhost:9090/board/list.action?searchType=subject&searchWord=안녕
					? 다음에 나오는 searchType=subject&searchWord=안녕 이라는 것이 query string(쿼리 스트링) 이다. 
	  
				data 파라미터로 전달된 데이터를 jQuery에서는 내부적으로 query string 으로 만든다. 
				하지만 파일 전송의 경우 내부적으로 query string 으로 만드는 작업을 하지 않아야 한다.
				이와 같이 내부적으로 query string 으로 만드는 작업을 하지 않도록 설정하는 것이 processData: false 이다.
			*/

			/*
				contentType 은 default 값이 "application/x-www-form-urlencoded; charset=UTF-8" 인데, 
				"multipart/form-data" 로 전송이 되도록 하기 위해서는 false 로 해야 한다. 
				만약에 false 대신에 "multipart/form-data" 를 넣어보면 제대로 작동하지 않는다.
			*/

		} // end of if(is_infoData_OK) {}------



	}); // end of 리뷰등록하기 -------------

	// 취소하기
	$("input[type='reset']").click(function() {
		$("span.error").hide();
		file = ""; // 첨부되어진 파일 정보를 담아 둘 변수 초기화
		$("img#previewImg").hide();
	});


})

// === 리뷰삭제하기 === //
function deleteReview(reviewno, img) {


	if (confirm(`리뷰를 삭제하시겠습니까?`)) {

		$.ajax({
			url: "/GUBI/review/reviewDelete.gu",
			type: "post",
			data: {
				"reviewno": reviewno,
				"img": img
			},
			dataType: "json",
			success: function(json) {

				if (json.n == 1) {
					alert("리뷰가 삭제되었습니다.");
					location.href = location.href; // 삭제가 완료된 경우 해당페이지 새로고침
				}
			},
			error: function(request, status, error) {
				alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
			}
		});

	} else {
		alert(`리뷰삭제를 취소하셨습니다.`);
	}

}// end of function goDel(cartno)---------------------------

// === 리뷰수정하기 버튼 누를 때 실행 === //
function editReview(index) {
	// 해당 button의 data- 인 것들
	const targetReview = reviewList[index];
	console.log(reviewList, targetReview);
	if (!targetReview) {
	    console.error("Invalid review target.");
	    return;
	}
	is_edit = true;

	
	// 수정 타겟 리뷰 데이터 세팅
	document.querySelector("input[name='reviewno']").value = targetReview.reviewno;
	document.querySelector("input[name='optionno']").value = targetReview.optionno;
	document.querySelector("input[name='title']").value = targetReview.title;
	document.querySelector("input[name='score']").value = targetReview.score;
	fillStars(targetReview.score);
	document.querySelector("textarea[name='content']").value = targetReview.content;
	// document.querySelector("input[name='img']").value = targetReview.img;


	// title 영역
	console.log(document.getElementsByClassName('modal-header'))
	document.getElementById('modal-title').textContent = `Review Edit For ${targetReview.optionname}`;
	
	if (targetReview.img) {
		const imageTag = document.getElementById("imgPreview");
		imageTag.src = `/GUBI/data/images/${targetReview.img}`;
		imageTag.style = "width:150px; min-width:150px; height:150px;";
	}

}

// 별 채우기
function fillStars(count) {
	const stars = document.querySelectorAll(".star");
	stars.forEach((s) => s.classList.remove("filled"));
	
    for (let i = 0; i < count; i++) {
      stars[i].classList.add("filled");
    }
}

// 리뷰 폼 모두 리셋
function reset() {
	document.querySelector("form[name='addReviewFrm']").reset();
}