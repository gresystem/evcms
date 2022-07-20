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

public class GetDiagnosticsConf 
{
	private Logger logger = LoggerFactory.getLogger("GetDiagnosticsConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String requestMessage, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        GetDiagnosticsConf.Payload payload = gson.fromJson(payloadString, GetDiagnosticsConf.Payload.class);
	        
	        logger.info("================= GetDiagnosticsConf =================");
	        logger.info("machineId -> " + machineId);

	        String fileName = "";
	        if (payload.fileName == null)
	        {
	        	logger.info("fileName -> null");
	        }
	        else
	        {
	        	logger.info("fileName -> " + payload.fileName);
	        	fileName = payload.fileName;
	        }
	        
	        logger.info("====================================================");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineDiagnosticsFile(machineId, fileName);
	        
        }catch(Exception e) {
        	logger.error(String.format("GetDiagnosticsConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String fileName;
    }
}
