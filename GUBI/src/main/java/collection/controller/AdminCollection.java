package collection.controller;

import java.util.ArrayList;
import java.util.List;

import category.domain.CategoryVO;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import product.domain.OptionVO;
import product.model.ProductDAO;
import product.model.ProductDAO_imple;
import util.color.ColorUtil;
import util.color.KMeans;

public class AdminCollection extends AbstractController {

	private ProductDAO pdao = new ProductDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// === 색상 분류하기 === //
		List<OptionVO> optionVOList = pdao.selectAllOptions();
		List<int[]> colors = new ArrayList<>();
		
		for(int i=0; i<optionVOList.size(); i++) {
			colors.add(ColorUtil.hexToRGB(optionVOList.get(i).getColor()));
		}
		
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
        
        request.setAttribute("colorList", colorList);

		// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //
		List<CategoryVO> selectMajorCategory = pdao.selectMajorCategory();
		request.setAttribute("selectMajorCategory", selectMajorCategory);
		// ----------------- 카테고리 대분류 소분류 가져오기 ---------------- //
        
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/collection/adminCollection.jsp");
	}

}
