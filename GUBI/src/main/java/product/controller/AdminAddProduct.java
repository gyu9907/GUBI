package product.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import product.domain.OptionVO;
import product.domain.ProductImgVO;
import product.model.*;

public class AdminAddProduct extends AbstractController {
	
	ProductDAO pdao = new ProductDAO_imple();
	
	private String extractFileName(String partHeader) {
	      for(String cd : partHeader.split("\\;")) {
	         if(cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", ""); 
	            int index = fileName.lastIndexOf(File.separator); // File.separator 란? OS가 Windows 이라면 \ 이고, OS가 Mac, Linux, Unix 이라면 / 을 말하는 것이다.       
	            return fileName.substring(index + 1);
	         }
	      }
	      return null;
	}// end of private String extractFileName(String partHeader)-------------------
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		HttpSession session = request.getSession();
		
		if("GET".equals(method)) {
			
			List<CategoryVO> selectMajorCategory = pdao.selectMajorCategory();
			request.setAttribute("selectMajorCategory", selectMajorCategory);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/adminProduct/adminAddProduct.jsp");
		}
		else {
			// 채번 (상품번호)
			int productno = pdao.getProductno();
			
			// 카테고리번호찾기 
			String major_category = request.getParameter("major_category");
			String small_category = request.getParameter("small_category");
			//System.out.println(major_category + small_category );
			String categoryno = pdao.searchCategoryNo(major_category, small_category);
			//System.out.println("categoryno" + categoryno);
			
			// 상품
			String name = request.getParameter("name");
			String cnt = request.getParameter("cnt");
			String price = request.getParameter("price");
			String delivery_price = request.getParameter("delivery_price");
			String point_pct = request.getParameter("point_pct");
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			ServletContext svlCtx = session.getServletContext();
            String uploadFileDir = svlCtx.getRealPath("/data/images");
            
            String thumbnail_img = null; // 썸네일이미지 저장
            List<String> optionImgList = new ArrayList<>(); // 옵션이미지는 여러개
            List<String> productImgList = new ArrayList<>(); // 상품이미지 여러개
            
			Collection<Part> parts = request.getParts();
          
			for(Part part : parts) {

				if (part.getHeader("Content-Disposition").contains("filename=")) { // form 태그에서 전송되어온 것이 파일일 경우

					String fileName = extractFileName(part.getHeader("Content-Disposition"));

					if (part.getSize() > 0) {

						String newFilename = fileName.substring(0, fileName.lastIndexOf(".")); // 확장자를 뺀 파일명 알아오기
						newFilename += "_" + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
						newFilename += System.nanoTime();

						newFilename += fileName.substring(fileName.lastIndexOf(".")); // 확장자 붙이기

						// >>> 파일을 지정된 디스크 경로에 저장해준다. 이것이 바로 파일을 업로드 해주는 작업이다. <<<
						part.write(uploadFileDir + File.separator + newFilename);

						part.delete();
						
						System.out.println(part.getName() + " : " + newFilename);

						// 파일명 하나씩 저장 
						if ("thumbnail_img".equals(part.getName())) {
							thumbnail_img = newFilename;
						}

						else if ("optionimg".equals(part.getName())) {
							optionImgList.add(newFilename);
						}

						else if (part.getName().startsWith("img")) {
							productImgList.add(newFilename);
						}
						// 파일명 하나씩 저장 끝
						
					} // end of if(part.getSize() > 0)

				} // end of if(part.getHeader("Content-Disposition").contains("filename="))---------------------------

			} // end of for(Part part : parts)
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			String uploadHtmlName = productno+"_"+name;
			uploadHtmlName += "_" + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
			uploadHtmlName += System.nanoTime();
			uploadHtmlName += ".html";
			
			String detail_html = uploadHtmlName; // 상세설명 html 파일명

			/*** 상세설명 html 작성 시작 ***/			
			// 상세설명 가져오기
			String[] description_title_arr = new String[productImgList.size()];
			String[] description_detail_arr = new String[productImgList.size()];
			
			for(int i=0; i<productImgList.size(); i++) { // 상품 이미지 길이만큼 title 과 description 을 넣는다 
				description_title_arr[i] = request.getParameter("description_title"+(i+1));
				description_detail_arr[i] = request.getParameter("description_detail"+(i+1));
				
				if(description_title_arr[i] == null || description_detail_arr[i] == null) {
					break;
				}
				
				description_detail_arr[i] = description_detail_arr[i]
												.replaceAll("<", "&lt;")
												.replaceAll(">", "&gt;")
												.replaceAll("\r\n", "<br>"); // 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어코드) 작성하기
			}
			
			String html = "";
			
			String relativeImgPath = request.getContextPath() + "/data/images/";
			
			for(int i=0; i<productImgList.size(); i++) {
				String orderImg = (i%2!=0) ? "order-md-2" : "";
				String orderDiv = (i%2!=0) ? "order-md-1" : "";
				html += "<div class=\"d-flex align-items-center row my-5\">"
				      + "    <img class=\"img-fluid col-md-6 "+orderImg+"\" src=\""+relativeImgPath+productImgList.get(i)+"\"/>"
				      + "    <div class=\"col-md-6 px-md-5 "+orderDiv+"\">"
				      + "        <h4>"+description_title_arr[i]+"</h4>"
				      + "        <p></p>"
				      + "        <p style=\"text-align: justify;\">"+description_detail_arr[i]+"</p>"
				      + "    </div>"
				      + "</div>";
			}
			
			String uploadHtmlPath = svlCtx.getRealPath("/data/html");
			BufferedWriter bw = new BufferedWriter(new FileWriter(uploadHtmlPath+File.separator+uploadHtmlName));
			bw.write(html);
			bw.flush();
			bw.close();
			/*** 상세설명 html 작성 종료 ***/
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("categoryno", categoryno);
			paraMap.put("name", name);
			paraMap.put("cnt", cnt);
			paraMap.put("price", price);
			paraMap.put("delivery_price", delivery_price);
			paraMap.put("point_pct", point_pct);
			paraMap.put("productno", String.valueOf(productno));
			paraMap.put("thumbnail_img", thumbnail_img);
			paraMap.put("detail_html", detail_html);
			

			// 상품 등록하기 성공하면 1 이 나와야됨
			int addProduct = pdao.addProduct(paraMap);
			System.out.println("addProduct"+addProduct);
			
			int result = 0;
            if(addProduct==1) {
            	result = 1;
            }
			
			// 입력한 옵션이름
			String[] optionname = request.getParameterValues("optionname");
			// 입력한 옵션색상코드 
			String[] colorcode = request.getParameterValues("colorCode");
			
			// **** 옵션 등록하기 ****
			// 상품 입력이 성공되고 옵션미지가 0 이상일 떄 
			int addOption = 0;
			if(result == 1 && optionImgList.size() > 0) { // optionImgList 는 옵션이미지를 담은 List 이다. 
				
				result = 0;
				
				for(int i=0; i<optionImgList.size(); i++) {
					
					String optionName = optionname[i];
					String colorCode = colorcode[i];
					String optionImg = optionImgList.get(i);
					
					paraMap = new HashMap<>();
					paraMap.put("productno", String.valueOf(productno));
					paraMap.put("optionName", optionName);
					paraMap.put("colorCode", colorCode);
					paraMap.put("optionImg", optionImg);
					
					addOption = pdao.addOption(paraMap);
				
				}
				System.out.println("addOption" + addOption); // 성공하면 1 
				if(addOption == 1) {
					result = 1;
				}
			}

			// **** 이미지 등록하기 **** //
			// 상품 입력이 성공되고 상품이미지가 0 이상일 떄 
			int addproductImg = 0;
			
			if(addProduct == 1 && productImgList.size() > 0) {
				
				result = 0;
				
				for(int i=0; i<productImgList.size(); i++) {
					
					String productImg = productImgList.get(i);
					
					paraMap = new HashMap<>();
					paraMap.put("productno", String.valueOf(productno));
					paraMap.put("productImg", productImg);
				
					addproductImg = pdao.addImage(paraMap);
				}
				System.out.println("addproductImg" + addproductImg);
				if(addproductImg == 1) {
					result = 1;
				}
			}
			
			 JSONObject jsonObj = new JSONObject();  // {}
             jsonObj.put("result", result);
           
             String json = jsonObj.toString(); // 문자열로 변환 
             request.setAttribute("json", json);
           
             super.setRedirect(false);
             super.setViewPage("/WEB-INF/jsonview.jsp");

		} // end of else "POST"
		

	} 

}
