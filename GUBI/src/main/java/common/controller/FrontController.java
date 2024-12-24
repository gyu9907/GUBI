package common.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.config.AppConfig;

/*
@MultipartConfig(location = "C:\\NCS\\workspace_jsp\\MyMVC\\images_temp_upload",
                 fileSizeThreshold = 1024,  // 이 크기 값(1024 byte)을 넘지 않으면 업로드된 데이터를 메모리상에 가지고 있지만, 이값을 넘는 경우 위의 location 로 지정된 경로에 임시파일로 저장된다.  
                                            // 메모리상에 저장된 파일 데이터는 언젠가 제거된다. 하지만 크기가 큰 파일을 메모리상에 올리게 되면 서버에 부하를 줄 수 있으므로 적당한 크기를 지정해주고, 그 이상크기의 파일은 임시파일로 저장하는것이 좋다.    
                                            // 만약에 기재 하지 않으면 기본값은 0 이다. 0 을 쓰면 무조건 임시디렉토리에 저장된다.
                 maxFileSize = 20971520,    // 업로드 되어질 파일들을 합친 최대 크기. 단위는 byte 임. 20*1024*1024 즉, 20MB. 기본은 -1L 즉, 제한이 없음.   
                 maxRequestSize = 31457280  // multipart/form-data 상태인 폼태그에 요청되어지는 모든 전송데이터 및 모든 파일들을 합친 크기. 단위는 byte 임. 30*1024*1024 즉, 30MB. 기본은 -1L 즉, 제한이 없음.
                )                                               
*/
@MultipartConfig ( // 위의 location 을 기입하지 않으면 Windows 는 자동적으로 C:\Windows\Temp 디렉토리를 사용하도록 되어있다.
      maxFileSize = 52428800,
      maxRequestSize = 52428800)
@WebServlet(
		description = "사용자가 웹에서 *.gu 를 했을 경우 이 서블릿이 응답을 해주도록 한다.",
		urlPatterns = {"*.gu"},
		initParams = {
				@WebInitParam(name = "propertyConfig", value = AppConfig.PropertieDir, description = "*.gu 에 대한 클래스의 매핑파일") 
	    })
public class FrontController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> cmdMap = new HashMap<>();
	
	public void init(ServletConfig config) throws ServletException {
		FileInputStream fls = null;
		
		String props = config.getInitParameter("propertyConfig");
		
		try {
			fls = new FileInputStream(props);
			Properties pr = new Properties();
			
			pr.load(fls);
			Enumeration<Object> en = pr.keys();
			
			while(en.hasMoreElements()) {
				
				String key = (String) en.nextElement();
//				System.out.println("확인용 key => " + key);
//				System.out.println("확인용 value => " + pr.getProperty(key) + "\n");
				
				String className = pr.getProperty(key);
				
				if(className != null) {
				
					className = className.trim();
					
					Class<?> cls = Class.forName(className);
					
				 	Constructor<?> constrt = cls.getDeclaredConstructor();
					
					Object obj = constrt.newInstance();
					
					cmdMap.put(key, obj);
					
				}// end of if(className != null) {}-----------------
				
				
			}// end of while(en.hasMoreElements())---------------
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(">> 문자열로 명명되어진 클래스가 존재하지 않습니다. <<");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}// end of public void init(ServletConfig config) throws ServletException {}----------
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String uri = request.getRequestURI().toString();
		
		String key = uri.substring(request.getContextPath().length());
		
		AbstractController action = (AbstractController) cmdMap.get(key);
		
		if(action == null) {
			System.out.println(">>> " + key + " 은 URI 패턴에 매핑된 클래스는 없습니다.");
		}
		else {
			try {
				action.execute(request, response);
				
				boolean bool = action.isRedirect();
				String viewPage = action.getViewPage();
				
				if(!bool) {
					if(viewPage != null) {
						RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
						dispatcher.forward(request, response);
					}
				}
				else {
					if(viewPage != null) {
						response.sendRedirect(viewPage);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
