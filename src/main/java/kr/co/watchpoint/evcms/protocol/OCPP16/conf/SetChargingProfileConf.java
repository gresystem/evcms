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

public class SetChargingProfileConf 
{
	private Logger logger = LoggerFactory.getLogger("SetChargingProfileConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String connectorId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        SetChargingProfileConf.Payload payload = gson.fromJson(payloadString, SetChargingProfileConf.Payload.class);
	        
	        logger.info("================= SetChargingProfileConf =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("status -> " + payload.status);	        
	        logger.info("====================================================");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineSetChargingProfileLog(machineId, "recv", Integer.parseInt(connectorId), "");
	        
        }catch(Exception e) {
        	logger.error(String.format("SetChargingProfileConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String status;
    }
}
