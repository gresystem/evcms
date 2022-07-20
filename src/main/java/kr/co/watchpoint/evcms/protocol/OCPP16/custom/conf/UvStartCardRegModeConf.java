package kr.co.watchpoint.evcms.protocol.OCPP16.custom.conf;

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

public class UvStartCardRegModeConf 
{
	private Logger logger = LoggerFactory.getLogger("UvStartCardRegModeConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String memberId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        UvStartCardRegModeConf.Payload payload = gson.fromJson(payloadString, UvStartCardRegModeConf.Payload.class);
	        
	        logger.info("================= UvStartCardRegModeConf =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("memberId -> " + memberId);
	        logger.info("status -> " + payload.status);	        
	        logger.info("====================================================");
			
	        if (payload.status.compareToIgnoreCase("Rejected") == 0)
	        {
	        	Map<String,Object> sqlParam = new HashMap<String,Object>();
				sqlParam.put("memberId", memberId);	    
				sqlParam.put("machineId", machineId);
				sqlParam.put("status", 3);
				sqlParam.put("cardTag", "");
				
				evcmsService.updateCardTagRegistTransaction(sqlParam);
	        }
	        
        }catch(Exception e) {
        	logger.error(String.format("UvStartCardRegModeConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String status;
    }
}
