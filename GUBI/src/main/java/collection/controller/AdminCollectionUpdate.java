package collection.controller;

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

import category.domain.CategoryVO;
import collection.domain.CollectionImgVO;
import collection.domain.CollectionVO;
import collection.model.CollectionDAO;
import collection.model.CollectionDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import product.domain.OptionVO;
import product.domain.ProductVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.check.Check;
import util.color.ColorUtil;
import util.color.KMeans;
import util.file.PartFileWriter;

public class AdminCollectionUpdate extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	private CollectionDAO coldao = new CollectionDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" or "POST"
	    HttpSession session = request.getSession();
		
		if(!"POST".equalsIgnoreCase(method)) { // GET 방식
			String collectionno = request.getParameter("collectionno");
			
			// 컬렉션 번호가 없으면
			if(Check.isNullOrBlank(collectionno)) {
				super.setRedirect(true);
				super.setViewPage(request.getContextPath() + "/admin/collection.gu");
				return;
			}
			
			// 컬렉션 번호가 숫자인지 확인
			try {
				Integer.parseInt(collectionno);
			} catch (NumberFormatException e) {
				super.setRedirect(true);
				super.setViewPage(request.getContextPath() + "/admin/collection.gu");
				return;
			}

			// 컬렉션 정보 조회
			CollectionVO collectionVO = coldao.getCollectionDetail(collectionno);
			
			// 해당하는 컬렉션이 없으면
			if(collectionVO == null) {
				super.setRedirect(true);
				super.setViewPage(request.getContextPath() + "/admin/collection.gu");
				return;
			}
			
			// 컬렉션에 포함된 상품 목록 조회
			List<ProductVO> productList = pdao.selectColProductList(collectionno);
			
			// === 색상 분류하기 === //
			List<OptionVO> optionVOList = pdao.selectAllOptions();
			List<int[]> colors = new ArrayList<>();
			
			for(int i=0; i<optionVOList.size(); i++) {
				colors.add(ColorUtil.hexToRGB(optionVOList.get(i).getColor()));
			}

			// 컬렉션에 포함된 이미지 조회
			List<CollectionImgVO> collecitonImgList = coldao.getCollectionImgs(collectionno);
			
	        // K-Means 클러스터링
	        int k = 8; // 클러스터 수
	        KMeans kMeans = new KMeans(k, 50);
	        kMeans.fit(colors);

	        int[][] centroids = kMeans.getCentroids();
	        List<String> colorList = new ArrayList<>();

	        for (int i = 0; i < centroids.length; i++) {
	            colorList.add(String.format("#%02x%02x%02x",
	                    centroids[i][0], centroids[i][1], centroids[i][2]));
	        }
			
			request.setAttribute("collectionVO", collectionVO);
			request.setAttribute("productList", productList);
			request.setAttribute("collecitonImgList", collecitonImgList);
	        request.setAttribute("colorList", colorList);
	        
			// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //
			List<CategoryVO> selectMajorCategory = pdao.selectMajorCategory();
			request.setAttribute("selectMajorCategory", selectMajorCategory);
			// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //
	        
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/collection/adminCollectionUpdate.jsp");
		}
		else { // POST 방식
			String collectionno = request.getParameter("collectionno");
			
			try {
				Integer.parseInt(collectionno);
			} catch(NumberFormatException e) {
				e.printStackTrace();
				
				super.setRedirect(true);
				super.setViewPage(request.getContextPath() + "/error.gu");
				return;
			}
			
			int result = 1; // 결과 저장 변수
			
			String thumbnail_img = "";  // 컬렉션 미리보기 이미지
			String fullscreen_img = ""; // 컬렉션 대표 이미지
			
			// 상세설명 이미지 개수 가져오기
			int detailCnt = Integer.parseInt(request.getParameter("detailCnt"));
			
			
			/*** 수정전 이미지와 새로운 이미지를 배열에 담기 시작 ***/
			
			String[] delete_img_no_arr = request.getParameter("delete_img_no_arr").split(",");
			String[] retain_img_name_arr = request.getParameter("retain_img_name_arr").split(",");
			
			int retainImgIndex = 0;
			
			
			// 상세설명 이미지 배열 생성
			String[] img_arr = new String[detailCnt];
			int idxDetail = 0; // 상세설명 이미지 인덱스

			// 이미지 업로드 경로 설정
            ServletContext svlCtx = session.getServletContext();
            String uploadImgPath = svlCtx.getRealPath("/data/images");
			
            // multipart에서 part 가져오기
			Collection<Part> parts = request.getParts();
			
			for(Part part : parts) {
				if (part.getHeader("Content-Disposition").contains("filename=")) { // form 태그에서 전송되어온 것이 파일일 경우
					
					String uploadImgName = PartFileWriter.writeFile(part, uploadImgPath); // 이미지 파일 저장 후 파일명 가져오기
					
//					if(uploadImgName==null) {
//						System.out.println("파일을 수정하지 않음 : " + part.getName());
//					}
					
					if("thumbnail_img".equals(part.getName())) {
						thumbnail_img = uploadImgName;
					}
					else if("fullscreen_img".equals(part.getName())) {
						fullscreen_img = uploadImgName;
					}
					else if(part.getName().startsWith("img")) {
						img_arr[idxDetail++] = uploadImgName;
					}
				}
			}
			
			
			/*** 수정전 이미지와 새로운 이미지를 배열에 담기 끝 ***/
			

			String name = request.getParameter("name");         // 컬렉션 이름
			String designer = request.getParameter("designer"); // 컬렉션 디자이너
			
			String uploadHtmlName = collectionno+"_"+name;
			uploadHtmlName += "_" + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
			uploadHtmlName += System.nanoTime();
			uploadHtmlName += ".html";
			
			String detail_html = uploadHtmlName; // 상세설명 html 파일명

			/*** 상세설명 html 작성 시작 ***/
			
			// 상세설명 가져오기
			String[] description_title_arr = new String[detailCnt];
			String[] description_detail_arr = new String[detailCnt];
			
			System.out.println("detailCnt : " + detailCnt);
			
			for(int i=0; i<detailCnt; i++) {
				description_title_arr[i] = request.getParameter("description_title"+(i+1));
				description_detail_arr[i] = request.getParameter("description_detail"+(i+1));
				
				if(description_title_arr[i] == null || description_detail_arr[i] == null) {
					break;
				}
				
				description_detail_arr[i] = description_detail_arr[i]
												.replaceAll("<", "&lt;")
												.replaceAll(">", "&gt;")
												.replaceAll("\r\n", "<br>"); // 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어코드) 작성하기
				
				System.out.println("description_title"+(i+1)+" : "+description_title_arr[i]);
				System.out.println("description_detail"+(i+1)+" : "+description_detail_arr[i]);
			}
			
			String html = "";
			
			String relativeImgPath = request.getContextPath() + "/data/images/";
			
			for(int i=0; i<detailCnt; i++) {
				String orderImg = (i%2!=0) ? "order-md-2" : "";
				String orderDiv = (i%2!=0) ? "order-md-1" : "";
				
				String img = (img_arr[i]!=null) ? img_arr[i] : retain_img_name_arr[retainImgIndex++];
				
				System.out.println(img);
				
				html += "<div class=\"d-flex align-items-center row my-5\">"
				      + "    <img class=\"img-fluid col-md-6 "+orderImg+"\" src=\""+relativeImgPath+img+"\"/>"
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
			
			CollectionVO colvo = new CollectionVO();
			colvo.setCollectionno(Integer.parseInt(collectionno));
			colvo.setName(name);
			colvo.setDesigner(designer);
			colvo.setThumbnail_img(thumbnail_img);
			colvo.setFullscreen_img(fullscreen_img);
			colvo.setDetail_html(detail_html);
			
			int n = 0;
			try {
				// 컬렉션 update
				n = coldao.updateCollection(colvo);
				
				/*** 컬렉션에 포함된 상품목록 update ***/
				String[] fk_productno_arr = request.getParameterValues("fk_productno");
				
				if(n == 1) {
					
					n = coldao.deleteColProduct(collectionno); // 우선 상품 목록 모두 삭제
					
					for(String fk_productno : fk_productno_arr) {
	
						Map<String, String> paraMap = new HashMap<>();
						
						paraMap.put("fk_collectionno", collectionno);
						paraMap.put("fk_productno", fk_productno);
						
						n = coldao.insertColProduct(paraMap);
						
						if(n != 1) {
							result = 0;
							System.out.println("컬렉션에 포함된 상품목록 update 중 오류 발생");
						}
					}
				}
				
				/*** 컬렉션 상세설명 이미지 delete ***/
				if(result == 1 ) {
					for(String imgno : delete_img_no_arr) {
						
						if(Check.isNullOrBlank(imgno)) {
							continue;
						}
						
						n = coldao.deleteCollectionImg(imgno);
						
						if(n != 1) {
							result = 0;
							System.out.println("컬렉션 상세설명 이미지 delete 중 오류 발생");
						}
					}
				}
				
				/*** 컬렉션 상세설명 이미지 insert ***/
				if(result == 1) {
					for(String img : img_arr) {
						
						if(Check.isNullOrBlank(img)) {
							continue;
						}
						
						Map<String, String> paraMap = new HashMap<>();
						paraMap.put("fk_collectionno", String.valueOf(collectionno));
						paraMap.put("img", img);
						
						n = coldao.insertCollectionImg(paraMap);
						
						if(n != 1) {
							result = 0;
							System.out.println("컬렉션 상세설명 이미지 insert 중 오류 발생");
						}
					}
				}
			
				if(result == 1) {
					session.setAttribute("collectionno", collectionno);
					super.setRedirect(true);
					super.setViewPage(request.getContextPath() + "/admin/collectionRegisterSuccess.gu");
				}
				else {
					// 에러 페이지로 이동
					super.setRedirect(true);
					super.setViewPage(request.getContextPath() + "/error.gu");
				}
			} catch(SQLException e) {
				e.printStackTrace();
				
				// 에러 페이지로 이동
				super.setRedirect(true);
				super.setViewPage(request.getContextPath() + "/error.gu");
			}
			
		}
	}

}
