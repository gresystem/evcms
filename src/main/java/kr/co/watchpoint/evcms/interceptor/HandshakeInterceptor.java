package kr.co.watchpoint.evcms.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
  
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor
{
	@Autowired
	private EVCMSService evcmsService;
	
	@Autowired
	private Environment environment;
	
	private Logger logger = LoggerFactory.getLogger("HandshakeInterceptor");

	
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,ServerHttpResponse response, WebSocketHandler wsHandler,
         Map<String, Object> attributes) throws Exception 
    {    
    	String requestUri = request.getURI().toString();
    	
    	// ws://evcms.watchpoint.co.kr/webServices/ocpp/CP001
		logger.info("Request URI -> " + requestUri);
		
		if (EVCMSConfig.domain.length() < 1)
		{
			String domain = environment.getProperty("spring.jmx.default-domain");
			EVCMSConfig.domain = "ws://" + domain + "/webServices/ocpp/";
		}

		if (EVCMSConfig.ocppMessageMode.length() < 1)
		{
			EVCMSConfig.ocppMessageMode = environment.getProperty("ocpp16.message.mode");
		}
		
		if(requestUri.length() <= EVCMSConfig.domain.length())
		{
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return false;			
		}
		
		int idx = requestUri.lastIndexOf('/');
		if (idx == -1)
		{
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return false;
		}
		
		String machineId = requestUri.substring(idx + 1);
		logger.info("machineId -> " + machineId);
		
		/*
		 * AccessDAO dao = new AccessDAO(evcmsService); if
		 * (dao.isMachineExist(machineId) == false) {
		 * logger.info("invalid machineId -> " + machineId);
		 * response.setStatusCode(HttpStatus.NOT_FOUND); return false; }
		 */
		
/*		
		HttpHeaders headers = request.getHeaders();
		if (headers != null)
		{
			headers.forEach((key, values) -> 
			{
			    for (String value : values) 
			    {
			    	logger.info("request header key -> " + key + ", value -> " + value); 
			    }
			});
		}    	
*/
		/*    	
        // 위의 파라미터 중, attributes 에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다
        System.out.println("Before Handshake");
          
          
        ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
        System.out.println("URI:"+request.getURI());
  
        HttpServletRequest req =  ssreq.getServletRequest();
 
        String userId = req.getParameter("userid");
        System.out.println("param, id:"+userId);
        attributes.put("userId", userId);
  
        // HttpSession 에 저장된 이용자의 아이디를 추출하는 경우
        String id = (String)req.getSession().getAttribute("id");
        attributes.put("userId", id);
        System.out.println("HttpSession에 저장된 id:"+id);
*/         
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
  
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) 
    {
/*    	
    	logger.info("afterHandshake called");
    	
    	
		HttpHeaders headers = response.getHeaders();
		if (headers != null)
		{
			headers.forEach((key, values) -> 
			{
			    for (String value : values) 
			    {
			    	logger.info("response header key -> " + key + ", value -> " + value); 
			    }
			});
		}          
*/		
        super.afterHandshake(request, response, wsHandler, ex);
    }

}
