package kr.co.watchpoint.evcms.protocol.OCPP16.conf;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.protocol.OCPP16.BootNotification;
import kr.co.watchpoint.evcms.protocol.OCPP16.MeterValue.METER_VALUE;
import kr.co.watchpoint.evcms.protocol.OCPP16.MeterValue.SAMPLED_VALUE;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;

public class SendLocalListConf 
{
	private Logger logger = LoggerFactory.getLogger("SendLocalListConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String dumy, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        SendLocalListConf.Payload payload = gson.fromJson(payloadString, SendLocalListConf.Payload.class);
	        
	        logger.info("================= SendLocalListConf =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("status -> " + payload.status);	
	        logger.info("====================================================");
        
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineSendLocalListLog(machineId, payload.status, 0, "", "");
	        
        }catch(Exception e) {
        	logger.error(String.format("SendLocalListConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String status;
    }

}
