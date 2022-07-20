package kr.co.watchpoint.evcms.protocol.OCPP16;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;

public class DiagnosticsStatusNotification 
{
	private Logger logger = LoggerFactory.getLogger("DiagnosticsStatusNotification");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = String.format("[3,\"%s\",{}]", uniqueId);
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        DiagnosticsStatusNotification.Payload payload = gson.fromJson(payloadString, DiagnosticsStatusNotification.Payload.class);
	        
	        logger.info("================= DiagnosticsStatusNotification =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("status -> " + payload.status);
	        logger.info("====================================================");
	        
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineDiagnosticsStatusLog(machineId, payload.status);	   
		
	        		
        }catch(Exception e) {
        	logger.error(String.format("DiagnosticsStatusNotification.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String status;
    }
}
