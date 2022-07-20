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

public class UvGetIdTagProfile 
{
	private Logger logger = LoggerFactory.getLogger("UvGetIdTagProfile");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        UvGetIdTagProfile.Payload payload = gson.fromJson(payloadString, UvGetIdTagProfile.Payload.class);
	        
	        logger.info("================= UvGetIdTagProfile =================");
	        logger.info("idTag -> " + payload.data.idTag);
	        logger.info("====================================================");
				
	        /****************************************************************************		
			{    
			     "vendorId":"UNIEV",    
			      "messageId":"uvGetIdTagProfile",
			      "data" : { "idTag" : "W00000000000000001"}
			}
			****************************************************************************/

	        int status = 1;
	        
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("cardTag", payload.data.idTag);
			
			List<Map<String,Object>> listCardTagData = evcmsService.getMemberProfileChargeTypeFromCardTag(sqlParam);
			if (listCardTagData == null || listCardTagData.size() < 1)
			{
				throw new Exception("get member profile fail");
			}
			
			String power = listCardTagData.get(0).get("profile_charge_type").toString();
			
			
			String customData = "{\"status\":\"Accepted\", \"data\":{\"power\":\"" + power + "\"}}";
			
	        response = String.format("[3,\"%s\",%s]"
					,uniqueId, customData);
	        
        }catch(Exception e) {
        	logger.error(String.format("UvGetIdTagProfile.process() exception : %s", e.getMessage()));
        	
			String customData = "{\"status\":\"Blocked\", \"data\":{\"power\":\"-1\"}}";
			
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
    	public String idTag;    	
    }
}
