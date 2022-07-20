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

public class GetCompositeScheduleConf 
{
	private Logger logger = LoggerFactory.getLogger("GetCompositeScheduleConf");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, String compositeScheduleIdx, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[2]);
	        GetCompositeScheduleConf.Payload payload = gson.fromJson(payloadString, GetCompositeScheduleConf.Payload.class);
	        
	        logger.info("================= GetCompositeScheduleConf =================");
	        logger.info("machineId -> " + machineId);
	        logger.info("status -> " + payload.status);
	        logger.info("====================================================");
			
	        String chargingRateUnit = "";
	        
	        AccessDAO dao = new AccessDAO(evcmsService);	        
	        
	        if (payload.chargingSchedule != null)
	        {
	        	chargingRateUnit = payload.chargingSchedule.chargingRateUnit; 
	        }
	        
	        if (payload.chargingSchedule.chargingSchedulePeriod != null && payload.chargingSchedule.chargingSchedulePeriod.length > 0)
	        {
	        	for(int i=0; i<payload.chargingSchedule.chargingSchedulePeriod.length; i++)
	        	{
	        		logger.info("------------------------------------------------");
	        		int startPeriod = payload.chargingSchedule.chargingSchedulePeriod[i].startPeriod;
	        		int limit = payload.chargingSchedule.chargingSchedulePeriod[i].limit;
	        		
	        		logger.info("startPeriod -> " + String.valueOf(startPeriod));
	        		logger.info("limit -> " + String.valueOf(limit));
	        		
	        		logger.info("------------------------------------------------");
	        		
	        		dao.addMachineGetCompositeScheduleData(Long.parseLong(compositeScheduleIdx), chargingRateUnit, startPeriod, limit);
	        	}
	        }
	        
	        logger.info("====================================================");
	        
			dao.addMachineGetCompositeScheduleLog(machineId, payload.status, 0, 0);
	        
        }catch(Exception e) {
        	logger.error(String.format("GetCompositeScheduleConf.process() exception : %s", e.getMessage()));
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String status;
    	public CHARGING_SCHEDULE chargingSchedule;    	
    }
    
    public class CHARGING_SCHEDULE 
    {
    	public String chargingRateUnit;
    	public CHARGING_SCHEDULE_PERIOD[] chargingSchedulePeriod;
    }

    public class CHARGING_SCHEDULE_PERIOD 
    {
    	public int startPeriod;
    	public int limit;
    }
    
}
