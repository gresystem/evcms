package kr.co.watchpoint.evcms.protocol.OCPP16;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.protocol.OCPP16.MeterValue.SAMPLED_VALUE;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.utils.GsonDateTimeValueAdjust;
import kr.co.watchpoint.evcms.utils.OCPPDateTime;

public class MeterValue 
{
	private Logger logger = LoggerFactory.getLogger("MeterValue");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        MeterValue.Payload payload = gson.fromJson(payloadString, MeterValue.Payload.class);
	        
	        
	        logger.info("================= MeterValue =================");
	        logger.info("connectorId -> " + String.valueOf(payload.connectorId));
	        logger.info("transactionId -> " + payload.transactionId);
	        
	        aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.METER);
	        
	        if (payload.transactionId == null || payload.transactionId.trim().length() < 1)
	        {
	        	response = String.format("[3,\"%s\",{}]", uniqueId);
	        	return response;
	        }
	        
	        int connectorId = payload.connectorId;
	        
	        String idTag = aprotechSession.getIdTag(connectorId);
	        if (idTag.length() < 1)
	        {
	        	logger.error("session idTag value not found");
	        	return response;
	        }
	        
	        if (payload.meterValue != null && payload.meterValue.length > 0)
	        {
	        	OCPPDateTime dateTime = new OCPPDateTime();
	        	
	        	for(int i=0; i<payload.meterValue.length; i++)
	        	{
	        		logger.info("------------------------------------------------");
	        		if(payload.meterValue[i].sampledValue == null) 
	        		{
	        			continue;
	        		}
	        		
	        		for(int j=0; j<payload.meterValue[i].sampledValue.length; j++)
	        		{
		        		SAMPLED_VALUE sampledValue = (SAMPLED_VALUE)payload.meterValue[i].sampledValue[j];
		        		
		    	        //timeStamp = payload.meterValue[i].timestamp.replace("T", " ").replace("Z", "");
		    	        //LocalDateTime parsedDate = LocalDateTime.parse(timeStamp, dateFormatter);
		    	        //timeStamp = parsedDate.format(dateFormatter);
		    	        
		    	        String timeStamp = dateTime.convertLocalDateTimeStringFromUtc(payload.meterValue[i].timestamp);		    	        
		        		
		        		String context = sampledValue.context;
		        		String format = sampledValue.format;
		        		String unit = sampledValue.unit;
		        		String value = sampledValue.value;
	
		        		logger.info("sampledValue.context -> " + context);
		        		logger.info("sampledValue.format -> " + format);
		        		logger.info("sampledValue.unit -> " + unit);
		        		logger.info("sampledValue.value -> " + value);
		        		logger.info("------------------------------------------------");
		        		
			        	float transactionPower = Float.parseFloat(value) - aprotechSession.getLastPower(connectorId);
			        	
		    			AccessDAO dao = new AccessDAO(evcmsService);
		    			dao.updateTransaction(machineId, idTag, Float.parseFloat(value), transactionPower, timeStamp);
		    			dao.updateServerTransactionInfo(machineId, connectorId, Math.round((Float.parseFloat(payload.transactionId))), idTag, Float.parseFloat(value), timeStamp);
		    			
		    			aprotechSession.updateLastPower(connectorId, Float.parseFloat(value));
	        		}
	        	}
	        }
	        
			response = String.format("[3,\"%s\",{}]", uniqueId);
	    	
        }catch(Exception e) {
        	//logger.error(String.format("process() json parsing exception : %s", e.getMessage()));
        	StringWriter sw = new StringWriter(); 
        	e.printStackTrace(new PrintWriter(sw)); 
        	String exceptionAsString = sw.toString(); 
        	logger.error(String.format("MeterValue.process() exception : %s", exceptionAsString));        	
        }		
		
		return response;
	}
	
/*	
	  "meterValue":[
			{"sampledValue":
				[
				 {"context":"Sample_Periodic","format":"Raw","unit":"Wh","value":"2"}
				]
				,"timestamp":"2021-3-29T15+21+35.000Z"
			}
		]
*/			
			
    public class Payload 
    {
    	public int connectorId;
    	public METER_VALUE[] meterValue;    	
    	public String transactionId;
    }
    
    public class METER_VALUE 
    {
    	public SAMPLED_VALUE[] sampledValue;
    	public String timestamp;
    }
    
    public class SAMPLED_VALUE 
    {
    	public String context;
    	public String format;
    	public String unit;
    	public String value;  
    	
    }
    
    public class SAMPLED_VALUE_DATA 
    {
    	public String context;
    	public String format;
    	public String unit;
    	public String value;
    }    
}
