
$(document).ready(function(){

	let modalId;
	let modal;
	
	$("tr#reviewtr").on("click", function(){
		
		alert("클릭");
		
		const reviewno = $(this).find(".reviewno").text().trim();
		// console.log("reviewno"+reviewno);
		
		modalId = "#detailReview"+reviewno;
		modal = $(modalId); // 모달 id

	});

	
});