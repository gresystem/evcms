package kr.co.watchpoint.evcms.protocol.OCPP16;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.utils.GsonDateTimeValueAdjust;
import kr.co.watchpoint.evcms.utils.OCPPDateTime;

public class Heartbeat 
{
	private Logger logger = LoggerFactory.getLogger("Heartbeat");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        logger.info("================= Heartbeat =================");
	        logger.info("machine id -> " + machineId);
	        logger.info("====================================================");
	        
        	OCPPDateTime dateTime = new OCPPDateTime();
        	
        	
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			
			evcmsService.updateMachineHeartbeatRecvLog(sqlParam);
			
	        response = String.format("[3,\"%s\",{\"currentTime\":\"%s\"}]"
	        						,uniqueId, dateTime.getCurrentDateTime()); 
	        		
        }catch(Exception e) {
        	//logger.error(String.format("process() json parsing exception : %s", e.getMessage()));
        	StringWriter sw = new StringWriter(); 
        	e.printStackTrace(new PrintWriter(sw)); 
        	String exceptionAsString = sw.toString(); 
        	logger.error(String.format("Heartbeat.process() exception : %s", exceptionAsString));
        }		
		
		return response;
	}

    public class Payload 
    {
    	public String idTag;
    }	
}
