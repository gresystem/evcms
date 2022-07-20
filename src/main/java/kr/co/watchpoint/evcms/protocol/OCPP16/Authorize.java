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

public class Authorize 
{
	private Logger logger = LoggerFactory.getLogger("Authorize");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        Authorize.Payload payload = gson.fromJson(payloadString, Authorize.Payload.class);
	        
	        logger.info("================= Authorize =================");
	        logger.info("idTag -> " + payload.idTag);
	        logger.info("====================================================");
	        		
	        
			AccessDAO dao = new AccessDAO(evcmsService);
			long memberIdx = (long)dao.getMemberIdxFromCardTag(payload.idTag)[0];	  
			
			aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.AUTHORIZE);
	        
	        logger.info("================= Authorize =================");
	        logger.info("idTag -> " + payload.idTag);
	        logger.info("====================================================");
   
			
			String status = "Accepted";
			if (memberIdx == -1)
			{
				status = "Blocked";   // "Accepted", "Blocked", "Expired", "ConcurrentTx"
			}

		    response = String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}}]"
		        						, uniqueId, status);			
	        		
        }catch(Exception e) {
        	logger.error(String.format("Authorize.process() exception : %s", e.getMessage()));
        	
		    response = String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}}]"
					, uniqueId, "Blocked");	        	
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String idTag;
    }
}
