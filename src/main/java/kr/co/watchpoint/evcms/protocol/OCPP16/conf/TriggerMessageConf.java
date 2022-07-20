package kr.co.watchpoint.evcms.protocol.OCPP16.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.protocol.OCPP16.BootNotification;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;

public class TriggerMessageConf 
{
	private Logger logger = LoggerFactory.getLogger("TriggerMessageConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String requestMessage, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        TriggerMessageConf.Payload payload = gson.fromJson(payloadString, TriggerMessageConf.Payload.class);
	        
	        logger.info("================= TriggerMessageConf =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("requestMessage -> " + requestMessage);
	        logger.info("status -> " + payload.status);	        
	        logger.info("====================================================");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineTriggerMessageLog(machineId, requestMessage, payload.status);
	        
        }catch(Exception e) {
        	logger.error(String.format("TriggerMessageConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String status;
    }
}
