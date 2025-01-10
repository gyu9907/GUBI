package admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import admin.domain.StatisticsVO;
import admin.model.AdminDAO;
import admin.model.AdminDAO_imple;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.check.Check;

public class AdminStatistics extends AbstractController {

	AdminDAO adao = new AdminDAO_imple();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!super.checkAdmin(request) ) { // 관리자가 아닌 경우
			// 로그인을 안한 경우 또는 일반사용자로 로그인 한 경우
			String message = "관리자만 접근이 가능합니다.";
			String loc = request.getContextPath() + "/index.gu";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			// super.setRedirect(false);
			super.setViewPage("/WEB-INF/common/msg.jsp");
			return;
		}
		
		String date = request.getParameter("date");
		String type = request.getParameter("type");
		
		if(date == null || !("day".equals(date) || "month".equals(date) || "year".equals(date))) {
			date = "day";
		}
		
		List<StatisticsVO> statisticsList = null;
		
		String title = "";
		
		switch (date) {
		case "day":
			title = "일별 ";
			break;

		case "month":
			title = "월별 ";
			break;

		case "year":
			title = "연도별 ";
			break;
		}
		
		if("order".equals(type)) { // 주문 통계
			
			String orderStatus = request.getParameter("orderStatus");
			String majorCategory = request.getParameter("majorCategory");
			
			try {
				int status = Integer.parseInt(orderStatus);
				
				if(status < 1 || status > 8) { // orderStatus가 범위 밖이면
					orderStatus = "1";
				}
			}catch (NumberFormatException e) {
				e.printStackTrace();
				orderStatus = "1";
			}

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("orderStatus", orderStatus);
			
			if(Check.isNullOrBlank(majorCategory)) { // 만약 majorCategory가 없는 경우 날짜별 검색
				title += "주문통계";
				
				paraMap.put("date", date);
				statisticsList = adao.getOrderStatByDate(paraMap);
				
			}
			else { // 만약 majorCategory가 있는 경우 카테고리별 검색
				title = "카테고리별 주문통계";
				if(!("SEATING".equals(majorCategory) || "LIGHTING".equals(majorCategory) || "TABLES".equals(majorCategory))) {
					majorCategory = "SEATING";
				}
				
				paraMap.put("majorCategory", majorCategory);
				statisticsList = adao.getOrderStatByCategory(paraMap);
				
				request.setAttribute("majorCategory", majorCategory);
			}
			
			request.setAttribute("orderStatus", orderStatus);
		}
		else { // 접속자수 통계 (기본값)
			title += "접속통계";
			statisticsList = adao.getLoginStat(date);
		}
		
		request.setAttribute("title", title);
		request.setAttribute("date", date);
		request.setAttribute("statisticsList", statisticsList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/statistics/adminStatistics.jsp");

	}

}
