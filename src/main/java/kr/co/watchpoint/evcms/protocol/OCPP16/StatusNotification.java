package kr.co.watchpoint.evcms.protocol.OCPP16;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.constants.EventStatus;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;

public class StatusNotification 
{
	private Logger logger = LoggerFactory.getLogger("StatusNotification");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = String.format("[3,\"%s\",{}]", uniqueId);
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        StatusNotification.Payload payload = gson.fromJson(payloadString, StatusNotification.Payload.class);
	        
	        logger.info("================= StatusNotification =================");
	        logger.info("connectorId -> " + payload.connectorId);
	        logger.info("errorCode -> " + payload.errorCode);
	        logger.info("status -> " + payload.status);
	        logger.info("====================================================");
			
			response = String.format("[3,\"%s\",{}]", uniqueId);
			
        }catch(Exception e) {
        	logger.error(String.format("StatusNotification.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public int connectorId;
    	public String errorCode;
    	public String status;
    }
}
