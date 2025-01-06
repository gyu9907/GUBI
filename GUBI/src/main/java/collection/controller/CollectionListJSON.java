package collection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import collection.domain.CollectionVO;
import collection.model.*;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CollectionListJSON extends AbstractController {

	private CollectionDAO coldao = new CollectionDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String major_category = request.getParameter("major_category"); // "" or "SEATING" or "LIGHTING" or "TABLE"
		String start = request.getParameter("start");
		String len = request.getParameter("len");
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("major_category", major_category);
		paraMap.put("start", start);
		
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) - 1);
		paraMap.put("end", end);
		
		List<CollectionVO> collectionList = coldao.getCollectionList(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("collectionList", collectionList);
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/common/jsonview.jsp");
	}

}
