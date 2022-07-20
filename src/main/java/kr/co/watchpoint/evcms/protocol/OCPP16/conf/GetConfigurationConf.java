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

public class GetConfigurationConf 
{
	private Logger logger = LoggerFactory.getLogger("GetConfigurationConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String dumy, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        GetConfigurationConf.Payload payload = gson.fromJson(payloadString, GetConfigurationConf.Payload.class);
	        
	        logger.info("================= GetConfigurationConf =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("====================================================");
			
	        String configurationKey = "";
	        String unknownKey = "";
	        
	        if (payload.configurationKey != null && payload.configurationKey.length > 0)
	        {
	        	for(int i=0; i<payload.configurationKey.length; i++)
	        	{
	        		logger.info("------------------------------------------------");
	        		String key = payload.configurationKey[i].key;
	        		String readonly = payload.configurationKey[i].readonly;
	        		String value = payload.configurationKey[i].value;
	        		
	        		logger.info("configurationKey.key -> " + key);
	        		logger.info("configurationKey.readonly -> " + readonly);
	        		logger.info("configurationKey.value -> " + value);
	        		
	        		logger.info("------------------------------------------------");
	        		
	        		if (configurationKey.length() > 0)
	        		{
	        			configurationKey += ",";
	        		}
	        		
	        		configurationKey += String.format("{\"key\":\"%s\", \"readonly\":\"%s\", \"value\":\"%s\"}", key, readonly, value);
	        	}
	        }
	        
	        if (payload.unknownKey != null && payload.unknownKey.length > 0)
	        {
	        	for(int i=0; i<payload.unknownKey.length; i++)
	        	{
	        		logger.info("------------------------------------------------");
	        		String key = payload.unknownKey[i];
	        		
	        		logger.info("unknownKey.key -> " + key);
	        		logger.info("------------------------------------------------");
	        		
	        		if (unknownKey.length() > 0)
	        		{
	        			unknownKey += ",";
	        		}
	        		
	        		unknownKey += String.format("{\"key\":\"%s\"}", key);
	        	}
	        }
	        
	        logger.info("====================================================");
	        
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineGetConfigurationLog(machineId, "recv", configurationKey, unknownKey);
	        
        }catch(Exception e) {
        	logger.error(String.format("GetConfigurationConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
	/*	
		{"configurationKey":
			[
			  {"key":"keyName1","readonly":"True","value":"2"}
			 ,{"key":"keyName2","readonly":"True","value":"2"}
			 ,{"key":"keyName3","readonly":"True","value":"2"}
			 ,{"key":"keyName4","readonly":"True","value":"2"}
			]
		},
		{"unknownKey":
			["keyName1","keyName2","keyName3","keyName4","keyName5"]
		}		
	 */	
	
    public class Payload 
    {
    	public CONFIGURATION_KEY[] configurationKey;    	
    	public String[] unknownKey;
    }
    
    public class CONFIGURATION_KEY 
    {
    	public String key;
    	public String readonly;
    	public String value;
    }

}
