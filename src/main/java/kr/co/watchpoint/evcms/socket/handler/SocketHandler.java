package kr.co.watchpoint.evcms.socket.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.message.handler.MessageHandler;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.struct.ReqMessageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SocketHandler extends TextWebSocketHandler implements SubProtocolCapable 
{
	@Autowired
	private EVCMSService evcmsService;
	
	@Value("${spring.jmx.default-domain}")
	private String emcmsDomain;
	
	private Logger logger = LoggerFactory.getLogger("SocketHandler");
	
	public static HashMap<String, AprotechWebSocketSession> sessionMap = new HashMap<>();
	public static HashMap<String, ReqMessageInfo> reqMap = new HashMap<>();
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) 
	{
		String msg = message.getPayload();
		logger.info("recv -> " + message.getPayload());
		
		String machineId = getMachineId(session.getUri().toString());
		AprotechWebSocketSession aprotectSession = sessionMap.get(machineId); 
		String response = MessageHandler.process(aprotectSession, evcmsService, machineId, msg);
		
		try
		{
			if (response.length() > 0)
			{
				logger.info("send -> " + response);
				session.sendMessage(new TextMessage(response));
			}
			
		} catch(Exception e) {
			logger.error(e.getMessage());
			//e.printStackTrace();
		}	
		
		
/*		
		// 전체 메시지 발송
		String msg = message.getPayload();
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage(new TextMessage(msg));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
*/			
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception 
	{
		//소켓 연결
		super.afterConnectionEstablished(session);
		
		String requestUri = session.getUri().toString();
		String machineId = getMachineId(requestUri);
		
		AprotechWebSocketSession aprotechSession = sessionMap.get(machineId);
		if (aprotechSession == null)
		{
			aprotechSession = new AprotechWebSocketSession(session);
			sessionMap.put(machineId, aprotechSession);
			logger.info("connected -> " + session.getRemoteAddress() + "(" + requestUri + ")");
			
	        AccessDAO dao = new AccessDAO(evcmsService);
	        
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			List<Map<String,Object>> listTransactionData = evcmsService.selectServerTransactionInfo(sqlParam);
			
			for (int i=0; i<listTransactionData.size(); i++)
			{
				String serverMachineId = listTransactionData.get(i).get("machine_id").toString();
				logger.info(String.format("serverMachineId = %s", serverMachineId));
				
				if (machineId.compareToIgnoreCase(serverMachineId) == 0)
				{
					logger.info("===================================================================");
					logger.info(String.format("afterConnectionEstablished machineId = %s", machineId));

					int serverConnectorId = Integer.parseInt(listTransactionData.get(i).get("connector_id").toString());
					int serverTransactionId = Integer.parseInt(listTransactionData.get(i).get("transaction_id").toString());
					String serverIdTag = listTransactionData.get(i).get("id_tag").toString();
					int serverLastPower = Math.round(Float.parseFloat(listTransactionData.get(i).get("last_power").toString()));
					
					logger.info(String.format("serverConnectorId = %d", serverConnectorId));
					logger.info(String.format("serverTransactionId = %d", serverTransactionId));
					logger.info(String.format("serverIdTag = %s", serverIdTag));
					logger.info(String.format("serverLastPower = %d", serverLastPower));
					
					aprotechSession.updateTransactionId(serverTransactionId, serverConnectorId);
					aprotechSession.updateIdTag(serverConnectorId, serverIdTag);
					aprotechSession.updateLastPower(serverConnectorId, serverLastPower);
					aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.METER);

					logger.info(String.format("dropServerTransactionInfo %s, %d", machineId, serverConnectorId));
			        dao.dropServerTransactionInfo(machineId, serverConnectorId);
			        logger.info("===================================================================");
			        
			        break;
				}
			}
		}
		else
		{
			aprotechSession.updateWebSocketSession(session);
			aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.CONNECT);
			
			logger.info("reconnected -> " + session.getRemoteAddress() + "(" + requestUri + ")");
		}
		
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception 
	{
		String requestUri = session.getUri().toString();
		String machineId = getMachineId(requestUri);
		
		AccessDAO dao = new AccessDAO(evcmsService);
		dao.machineDisConnected(machineId);
		
		logger.info("closed -> " + session.getRemoteAddress());
		
		//소켓 종료
		//sessionMap.remove(machineId);
		AprotechWebSocketSession aprotechSession = sessionMap.get(machineId);
		aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.DISCONNECT);
		
		super.afterConnectionClosed(session, status);
	}
	
	@Override
	public List<String> getSubProtocols()
	{
		return Collections.singletonList("ocpp1.6");
	}
	
	private String getMachineId(String uri)
	{
		String machineId = "";
		
		if(uri.length() > EVCMSConfig.domain.length())
		{
			int idx = uri.lastIndexOf('/');
			if (idx != -1)
			{
				machineId = uri.substring(idx + 1);
			}
		}		
		
		return machineId;
	}
}