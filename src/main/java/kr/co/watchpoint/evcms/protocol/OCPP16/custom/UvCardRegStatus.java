package kr.co.watchpoint.evcms.protocol.OCPP16.custom;

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

public class UvCardRegStatus 
{
	private Logger logger = LoggerFactory.getLogger("UvCardRegStatus");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        UvCardRegStatus.Payload payload = gson.fromJson(payloadString, UvCardRegStatus.Payload.class);
	        
	        logger.info("================= UvCardRegStatus =================");
	        logger.info("status -> " + payload.data.status);
	        logger.info("memberId -> " + payload.data.memberId);
	        logger.info("====================================================");
			
//	        if (payload.data.status.compareToIgnoreCase("CardAuthMode") != 0)
//	        {
//	        	Map<String,Object> sqlParam = new HashMap<String,Object>();
//				sqlParam.put("memberId", payload.data.memberId);	    
//				sqlParam.put("machineId", machineId);
//				sqlParam.put("status", 3);
//				sqlParam.put("cardTag", "");
//				
//				evcmsService.updateCardTagRegistTransaction(sqlParam);
//	        }
	        
	        /****************************************************************************		
			{    
			     "vendorId":"UNIEV", "messageId":"uvCardRegStatus",
			      "data" : { "status" : "CardAuthMode", "memberId": "12303392930"   }
			}
			****************************************************************************/
			
			String customData = "{\"status\":\"Accepted\"}";
			
	        response = String.format("[3,\"%s\",%s]"
					,uniqueId, customData);
	        
        }catch(Exception e) {
        	logger.error(String.format("UvCardRegStatus.process() exception : %s", e.getMessage()));
        	
			String customData = "{\"status\":\"Blocked\"}";
			
	        response = String.format("[3,\"%s\",%s]"
					,uniqueId, customData);        	
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String vendorId;
    	public String messageId;
    	public Data data;
    }
    
    public class Data
    {
    	public String status;
    	public String memberId;
    }
}
