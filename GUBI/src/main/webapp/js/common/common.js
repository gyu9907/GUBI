window.onload = function() {
	
	headerMenuList();
	
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
				$.each(json, function(index,item) {
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

