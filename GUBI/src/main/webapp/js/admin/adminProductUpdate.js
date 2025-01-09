$(document).ready(function(){
	
	// selectSmallCategory();
			
	const major = $("input.major").val();
	const small = $("input.small").val();
	const pname = $("input.pname").val();
	const description = $("input.description").val();
	const cnt = $("input.cnt").val();
	const price = $("input.price").val();
	const point_pct = $("input.point_pct").val();
	const delivery_price = $("input.delivery_price").val();

	// input 값 넣기 
	$("select#major_category").val(major);
	$("select#small_category").val(small);
	$("input#name").val(pname);
	$("textarea#description").val(description);
	$("input#cnt").val(cnt);
	$("input#price").val(price);
	$("input#delivery_price").val(delivery_price);
	$("input#point_pct").val(point_pct);
	
	
	$(document).on("change","select#major_category", function(){
			selectSmallCategory();
	});// end of $(document).on("change","select#major_category", function(e)
		
	
	/************************* 유효성 검사하기 **************************/

	/* 카테고리 대분류 검사하기 */
	$("select#major_category").blur((e) => { 

	   const major_category = $("select#major_category").val();
		
       if(major_category == null) {
			$("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
	   }
		else {
			$("table#table :input").prop("disabled", false);
			$(e.target).parent().find("span.error").hide();
		}	

	});// 아이디가 major_category 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
		
	/* 카테고리 소분류 검사하기 */
	$("select#small_category").blur((e) => {

		const small_category = $("select#small_category").val();	
		
       if(small_category == null) {
			$("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
	   }
		else {
			$("table#table :input").prop("disabled", false); 
			$(e.target).parent().find("span.error").hide();
		}	

	});// 아이디가 small_category 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	/* 상품명 검사하기 */
	$("input#name").blur((e) => { 

        const name = $(e.target).val().trim();
        if(name == "") {
            // 입력하지 않거나 공백만 입력했을 경우 
            $("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
        
        }
        else {
            // 공백이 아닌 글자를 입력했을 경우
            $("table#table :input").prop("disabled", false); 

            $(e.target).parent().find("span.error").hide();
        }

	});// 아이디가 name 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	
	/* 상품설명 검사하기 */
	$("textarea#description").blur((e) => { 

        const description = $(e.target).val().trim();
        if(description == "") {
            // 입력하지 않거나 공백만 입력했을 경우 

            $("table#table :input").prop("disabled", true);
            $(e.target).prop("disabled", false);
            $(e.target).val("").focus();

            $(e.target).parent().find("span.error").show();
        
        }
        else {
            // 공백이 아닌 글자를 입력했을 경우
            $("table#table :input").prop("disabled", false); 

            $(e.target).parent().find("span.error").hide();
        }

	});// 아이디 description 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	$("input#cnt").val(0);
	/* 수량 검사하기 */
	$("input#cnt").blur((e) => { 

	    const cnt = $(e.target).val().trim();
	    if(cnt == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 
	
	/* 가격 검사하기 */
	$("input#price").blur((e) => { 

	    const price = $(e.target).val().trim();
	    if(price == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 

	/* 배송비 검사하기 */
	$("input#delivery_price").blur((e) => { 

	    const delivery_price = $(e.target).val().trim();
	    if(delivery_price == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 

	
	/* 포인트 검사하기 */
	$("input#point_pct").blur((e) => { 

	    const point_pct = $(e.target).val().trim();
	    if(point_pct == "") {
	        // 입력하지 않거나 공백만 입력했을 경우 

	        $("table#table :input").prop("disabled", true);
	        $(e.target).prop("disabled", false);
	        $(e.target).val("").focus();

	        $(e.target).parent().find("span.error").show();
	    }
	    else {
	        // 공백이 아닌 글자를 입력했을 경우
	        $("table#table :input").prop("disabled", false); 

	        $(e.target).parent().find("span.error").hide();
	    }

	});// 아이디 cnt 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다. 

	/************************* 유효성 검사하기 끝 **************************/
	
	// "제품수량" 에 스피너 달아주기
    $("input#cnt").spinner({
       
       spin:function(event,ui){
             if(ui.value > 100) {
                $(this).spinner("value", 100);
                return false;
             }
             else if(ui.value < 1) {
                $(this).spinner("value", 1);
                return false;
             }
     }
       
    });// end of $("input#spinnerPqty").spinner({})-----------------------------------------------------

	
	// 초반은 경고문 숨기기
	$("span.error").hide();
	
	
	
	//상품수정하기 시작	//////////////////////////////////////////////////////////////////////////
	$("button.updatebtn").on("click", function() {
		
		const queryString = $("form[name='productUpdateFrm']").serialize();
		// console.log(queryString);

		let is_infoData_Ok = true;
					
		$(".infoData").each(function(index, elmt) {
			const val = $(elmt).val();
			if(val == "") {
				$(elmt).next(".error").show();
				is_infoData_Ok = false;
				return false;
			}
		});
		
		if(is_infoData_Ok) {
			$.ajax({
			   url : "productUpdate.gu",
               type : "post",
               data : queryString,
               dataType:"json",
               success:function(json){
                   console.log("~~~ 확인용 : " + JSON.stringify(json));
                   // ~~~ 확인용 : {"result":1}
                   
                  if(json.result == 1) {
                    	alert("상품 수정이 완료되었습니다.")
						window.location.href = contextPath + "/admin/product.gu"
                  }
                  
              },
              error: function(request, status, error){
              		alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
           	  }
			});
		}
	
			/*const newmajor = $("input.major").val();
			const newsmall = $("input.small").val();
			const newpname = $("input.pname").val();
			const newdescription = $("input.description").val();
			const newcnt = $("input.cnt").val();
			const newprice = $("input.price").val();
			const newpoint_pct = $("input.point_pct").val();
			const newdelivery_price = $("input.delivery_price").val();*/
			
	  });
	  // 수정하기 끝 /////////////////////////////////////////////////////////////////////////

});// end of $(document).ready(function(){



// 카테고리 가져오기 
function selectSmallCategory() {
	
	const majorCategory = $("select#major_category").val();
	// alert(majorCategory);
	
	$.ajax({
	        url:"selectSmallCategory.gu",
	        data:{"major_category":majorCategory}, 
	        dataType:"json",  
	        success:function(json){
	        	// console.log(json);
	       		let html = `<option value="" disabled selected>소분류</option>`;
	       	
	   			$.each(json, function(index, item){
	   				//console.log(item.small_category);
	      	 		html += `<option value='${item.small_category}'>${item.small_category}</option>`;
	      	 	});// end of $.each(json, function(index, item)
	      	 			
	   			$("select#small_category").html(html);
	        },                   
	        error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
	});// end of $.ajax
}