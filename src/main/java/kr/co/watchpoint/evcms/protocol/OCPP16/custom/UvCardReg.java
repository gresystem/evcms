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
import kr.co.watchpoint.evcms.protocol.OCPP16.StopTransaction;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;

public class UvCardReg 
{
	private Logger logger = LoggerFactory.getLogger("UvCardReg");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        UvCardReg.Payload payload = gson.fromJson(payloadString, UvCardReg.Payload.class);
	        
	        logger.info("================= UvCardReg =================");
	        logger.info("memberId -> " + payload.data.memberId);
	        logger.info("token -> " + payload.data.token);
	        logger.info("====================================================");
				
	        /****************************************************************************		
			{    
			     "vendorId":"UNIEV",    
			      "messageId":"uvCardReg",
			      "data" : { "memberId" : "kkko", "token" : "00112030020303"   }
			}
			****************************************************************************/

	        int status = 1;
	        
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("cardTag", payload.data.token);
			
			List<Map<String,Object>> listCardTagData = evcmsService.isCardTagExist(sqlParam);
			if (listCardTagData == null || listCardTagData.size() < 1)
			{
				status = 1;
			}
			else
			{
				status = 2;
			}
			
			sqlParam.clear();
			sqlParam.put("memberId", payload.data.memberId);	    
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			sqlParam.put("cardTag", payload.data.token);
			
			evcmsService.updateCardTagRegistTransaction(sqlParam);
			
			
			String customData = "{\"status\":\"Accepted\", \"data\":{\"reason\":\"Success\"}}";
			
	        response = String.format("[3,\"%s\",%s]"
					,uniqueId, customData);
	        
        }catch(Exception e) {
        	logger.error(String.format("UvCardReg.process() exception : %s", e.getMessage()));
        	
			String customData = "{\"status\":\"Blocked\", \"data\":{\"reason\":\"Fail\"}}";
			
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
    	public String token;    	
    	public String memberId;    	
    }
}
