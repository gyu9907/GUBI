
const query = document.location.search;
const param = new URLSearchParams(query);

const param_major_category = param.get('major_category');
const param_small_category = param.get('small_category');
const param_freeshipping = param.get('freeshipping');


$(document).ready(function(){
	
	if (param_major_category) {
		$("p#category_title").html(param_major_category); 
	}
	if (param_small_category) {
		$("p#category_title").html(param_small_category); 
	}

    if (param_small_category == null) {
		getCategoryListAjax();
	}
}); // end of $(document).ready(function(){}------------

function getCategoryListAjax() {
	$.ajax({
        url : "/GUBI/category/categoryListJSON.gu",
		data: {
			major_category : param_major_category,
			small_category : param_small_category
		},
        dataType: "json",
        success : function(json) {
            let v_html = ``;
			
            if(json.length > 0) {
                $.each(json, function(index, item){
					let href = `/GUBI/product/productList.gu`;
					
					if (!param_major_category && !param_small_category) {
						href += `?major_category=${item.major_category}`;
					}
					else {
						href += `?major_category=${param_major_category}&small_category=${item.small_category}`
					}
					
					const categoryName = param_major_category ? item.small_category : item.major_category;
					
					v_html += `<li>
								  <a href="${href}">
								    <img src="/GUBI/data/images/${item.category_img}" class="category-icon" />
								    <p>${categoryName}</p>
								  </a>
							   </li>`;
                            
                });
                $("ul#category-list").html(v_html); 
            }
        }, 
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
}