window.onload = function() {
	
	headerMenuList();
	getHeaderCollection('SEATING');
	

	var listItems = document.querySelectorAll("ul#major_categry_list > li");

	  listItems.forEach(function(item) {
	    // 마우스를 올렸을 때 이벤트 처리
	    item.addEventListener("mouseenter", function(e) {
	      console.log("Hovered over: ", e.target);  // 실제로 마우스를 올린 요소 확인
	      var aTagText = item.querySelector("a").textContent; // a 태그의 텍스트 가져오기
	      getHeaderCollection(aTagText); // 함수 호출
	    });
	  });

	
	const header_product_menu = document.querySelector('li#product_menu>a');
	const header_dropdown = document.querySelector('div#header_dropdown_menu');
	const headerWrapper = header_product_menu.closest('.header_wrapper'); 
	
	if (header_product_menu && headerWrapper) {
		const scrollTop = document.documentElement.scrollTop || document.body.scrollTop; // scrollTop 체크
		headerWrapper.classList.add('bg_main');
		
		header_product_menu.addEventListener('mouseenter', function() {
			headerWrapper.classList.remove('bg_main');
			header_dropdown.style.top = '0px';
			header_dropdown.style.maxHeight = '100vh';
		});
		
		header_dropdown.addEventListener('mouseleave', function() {
			headerWrapper.classList.add('bg_main');
			header_dropdown.style.top = '-780px';
			header_dropdown.style.maxHeight = '0';
			if (scrollTop === 0) {
				headerWrapper.classList.add('bg_main');
			}
		});
		
		// 스크롤 이벤트 추가
		document.addEventListener('scroll', function() {
			const scrollTop = document.documentElement.scrollTop || document.body.scrollTop; // scrollTop 체크

			
			if (scrollTop === 0) {
				headerWrapper.classList.add('bg_main');
			} else {
				headerWrapper.classList.remove('bg_main');
			}
		});
	}
	

}


function headerMenuList() {
	
	$.ajax({
		url: "/GUBI/category/headerCategoryListJSON.gu",
		dataType: "json",
		success: function(json) {
			
			let s_html = "";
			let l_html = "";
			let t_html = "";
			
			if(json.length > 0) {
				$.each(json, function(index, item) {
					if (item.major_category == "SEATING") {
						s_html += `<li><a href='/GUBI/product/productList.gu?major_category=${item.major_category}&small_category=${item.small_category}'>${item.small_category}</a></li>`;		
					}
					if (item.major_category == "LIGHTING") {
						l_html += `<li><a href='/GUBI/product/productList.gu?major_category=${item.major_category}&small_category=${item.small_category}'>${item.small_category}</a></li>`;		
					}
					if (item.major_category == "TABLES") {
						t_html += `<li><a href='/GUBI/product/productList.gu?major_category=${item.major_category}&small_category=${item.small_category}'>${item.small_category}</a></li>`;		
					}
				})
				
				$("ul#seating_list").html(s_html);
				$("ul#lighting_list").html(l_html);
				$("ul#tables_list").html(t_html);
			}
		}
	})
}

function getHeaderCollection(major) {
	
	$.ajax({
		url: "/GUBI/collection/collectionListJSON.gu",
		data: {
			"major_category": major,
			"start": 32,
			"len": 2
		},
		dataType: "json",
		success: function(json) {
			
			let html = "";
			
			console.log('sdfsdf:', json)
			
			if(json.collectionList.length > 0) {
				$.each(json.collectionList, function(index, item) {
					html += `<a href="/GUBI/collection/collectionDetail.gu?collectionno=${item.collectionno}">
			                    <div class="product-card">
			                        <div class="product-image"> 
			                            <img src="/GUBI/data/images/${item.thumbnail_img}">
			                        </div>
			                        <div class="product-info">
			                            <p>${item.name}</p>
			                        </div>
			                    </div>
			                </a>`;
				})
				
				$("#collection-section").html(html);
			}
		}
	})
}




