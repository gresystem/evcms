package kr.co.watchpoint.evcms.interceptor;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggerInterceptor implements HandlerInterceptor {
	
	private Logger logger = LoggerFactory.getLogger("MainController");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		logger.info("Request URI -> " + request.getRequestURI());
		
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null)
		{
			while (headerNames.hasMoreElements()) 
			{
				logger.info("Header Name -> " + headerNames.nextElement());
			}
		}



		return HandlerInterceptor.super.preHandle(request, response, handler);

	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
