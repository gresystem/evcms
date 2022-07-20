package kr.co.watchpoint.evcms.protocol.OCPP16;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.constants.EventStatus;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.utils.GsonDateTimeValueAdjust;
import kr.co.watchpoint.evcms.utils.OCPPDateTime;

public class StopTransaction 
{
	private Logger logger = LoggerFactory.getLogger("StopTransaction");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
        	//  [2,"1154","StopTransaction",{"idTag":"1234","meterStop":4055,"reason":"Local","timestamp":"2021-05-10T02:20:24.000Z",
        	//	"transactionData":[{"sampledValue":[{"context":"Sample_Clock","format":"Raw","unit":"Wh","value":"2865"}],"timestamp":"2021-05-10T02:00:00.000Z"}],"transactionId":0}]
        	
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        StopTransaction.Payload payload = gson.fromJson(payloadString, StopTransaction.Payload.class);
        
//	        String timeStamp = payload.timestamp.replace("T", " ").replace("Z", "");
//	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	        
//	        LocalDateTime parsedDate = LocalDateTime.parse(timeStamp, dateFormatter);
//	        
//	        timeStamp = parsedDate.format(dateFormatter);	        
	        
	        OCPPDateTime dateTime = new OCPPDateTime();
	        String timeStamp = dateTime.convertLocalDateTimeStringFromUtc(payload.timestamp);	 
	        
	        
	        logger.info("================= StopTransaction =================");
	        logger.info("idTag -> " + payload.idTag);
	        logger.info("meterStop -> " + String.valueOf(payload.meterStop));
	        logger.info("reason -> " + payload.reason);
	        logger.info("timestamp -> " + payload.timestamp);
	        logger.info("converted timestamp -> " + timeStamp);
	        logger.info("transactionId -> " + String.valueOf(payload.transactionId));	        
	        logger.info("====================================================");
	        
	        int connectId = aprotechSession.getConnectId(payload.transactionId);
	        if (connectId == 0)
	        {
	        	throw new Exception(String.format("StopTransaction getConnectId() fail transactionId = %d", payload.transactionId));
	        }
	        
	        //logger.info("connectId -> " + String.valueOf(connectId));	
	        
	        aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.STOPTRANSACTION);
	        
	        float transactionPower = payload.meterStop - aprotechSession.getLastPower(connectId);
	        
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("cardTag", payload.idTag);	
			sqlParam.put("power", payload.meterStop);	
			sqlParam.put("transactionPower", transactionPower);
			sqlParam.put("timeStamp", timeStamp);
			
			
			List<Map<String,Object>> listChargePriceData = evcmsService.stopTransaction(sqlParam);
			
	        String status = "Accepted";   // "Accepted", "Blocked", "Expired", "ConcurrentTx"
	        		
	        response = String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}}]"
						,uniqueId, status); 
	        
	        AccessDAO dao = new AccessDAO(evcmsService);
	        dao.dropServerTransactionInfo(machineId, connectId);	        
	        
        }catch(Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            logger.error("StopTransaction.process() exception -> " + exceptionAsString);
            
	        response = String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}}]"
						,uniqueId, "Blocked"); 
        }		
		
		return response;
	}
			
    public class Payload 
    {
    	public String idTag;
    	public float meterStop;    	
    	public String reason;
    	public String timestamp;
    	public TransactionData[] transactionData;
    	public int transactionId;
    }
    
    public class TransactionData 
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
}
