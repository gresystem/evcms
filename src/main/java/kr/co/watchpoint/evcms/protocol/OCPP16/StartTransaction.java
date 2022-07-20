package kr.co.watchpoint.evcms.protocol.OCPP16;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.constants.EventStatus;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.utils.GsonDateTimeValueAdjust;
import kr.co.watchpoint.evcms.utils.OCPPDateTime;

public class StartTransaction 
{
	private Logger logger = LoggerFactory.getLogger("StartTransaction");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        StartTransaction.Payload payload = gson.fromJson(payloadString, StartTransaction.Payload.class);
	        
	        //String timeStamp = payload.timestamp.replace("T", " ").replace("Z", "");
	        //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	        
	        //LocalDateTime parsedDate = LocalDateTime.parse(timeStamp, dateFormatter);
	        
	        //timeStamp = parsedDate.format(dateFormatter);
	        
	        OCPPDateTime dateTime = new OCPPDateTime();
	        String timeStamp = dateTime.convertLocalDateTimeStringFromUtc(payload.timestamp);
	        
// [2,"1004","StartTransaction",{"connectorId":1,"idTag":"1234","meterStart":0,"timestamp":"2021-03-19T01:39:04.847Z"}]
	        		
	        logger.info("================= StartTransaction =================");
	        logger.info("connectorId -> " + String.valueOf(payload.connectorId));
	        logger.info("idTag -> " + payload.idTag);
	        logger.info("meterStart -> " + String.valueOf(payload.meterStart));
	        logger.info("timestamp -> " + payload.timestamp);
	        logger.info("converted timestamp -> " + timeStamp);
	        logger.info("====================================================");
	        
	        AccessDAO dao = new AccessDAO(evcmsService);
	        String status = "Accepted";   // "Accepted", "Blocked", "Expired", "Invalid", "ConcurrentTx"
	        
	        //설치 중이거나 존재하지 않는 충전기의 경우 충전을 시작하지 않는다.
	        if(!dao.isMachineExist(machineId)) {
	        	status = "Blocked";
	        	return String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}}]"
						,uniqueId, status); 
			}
	        
	        int connectId = payload.connectorId;
	        
	        aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.STARTTRANSACTION);
	        
	        // MeterValue 메시지에 idTag 값이 없는 관계로 세션에 idTag 정보 추가
	        aprotechSession.updateIdTag(connectId, payload.idTag);
	        aprotechSession.updateLastPower(connectId, payload.meterStart);
	        
			dao.startTransaction(machineId, payload.idTag, payload.meterStart, timeStamp);	   
	                
	        aprotechSession.updateTransactionId(++EVCMSConfig.transactionId, connectId);
	        
	        response = String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}, \"transactionId\":%d}]"
	        						,uniqueId, status, EVCMSConfig.transactionId); 
	        
	        dao.updateServerTransactionInfo(machineId, connectId, EVCMSConfig.transactionId, payload.idTag, payload.meterStart, timeStamp);	      
	        		
        }catch(Exception e) {
        	//logger.error(String.format("process() json parsing exception : %s", e.getMessage()));
        	StringWriter sw = new StringWriter(); 
        	e.printStackTrace(new PrintWriter(sw)); 
        	String exceptionAsString = sw.toString(); 
        	logger.error(String.format("StartTransaction.process() exception : %s", exceptionAsString));
        	
	        response = String.format("[3,\"%s\",{\"idTagInfo\":{\"status\":\"%s\"}, \"transactionId\":%d}]"
					,uniqueId, "Blocked", EVCMSConfig.transactionId);         	
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public int connectorId;
    	public String idTag;
    	public float meterStart;
    	public String timestamp;
    }
}
