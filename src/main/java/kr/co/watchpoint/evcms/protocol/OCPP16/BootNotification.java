package kr.co.watchpoint.evcms.protocol.OCPP16;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.constants.EventStatus;
import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.utils.OCPPDateTime;

public class BootNotification 
{
	private Logger logger = LoggerFactory.getLogger("BootNotification");
	
	public String process(AprotechWebSocketSession aprotechSession, EVCMSService evcmsService, String machineId, int messageTypeId, String uniqueId, String rawData)
	{
		String response = "";
		
        try
        {
	        Gson gson = new Gson();
	        Object[] message = gson.fromJson(rawData, Object[].class);
	        
	        String payloadString = gson.toJson(message[3]);
	        BootNotification.Payload payload = gson.fromJson(payloadString, BootNotification.Payload.class);
	        
	        logger.info("================= BootNotification =================");
	        logger.info("chargePointModel -> " + payload.chargePointModel);
	        logger.info("chargePointSerialNumber -> " + payload.chargePointSerialNumber);
	        logger.info("chargePointVendor -> " + payload.chargePointVendor);
	        logger.info("firmwareVersion -> " + payload.firmwareVersion);
	        logger.info("imsi -> " + payload.imsi);
	        logger.info("====================================================");
	        
	        OCPPDateTime dateTime = new OCPPDateTime();
	        
			AccessDAO dao = new AccessDAO(evcmsService);
			
			dao.machineConnected(machineId);
			dao.addEventLogs(EventStatus.EVT_CATT_MACHINE, EventStatus.EVT_TYPE_NORMAL, EventStatus.EVT_SUBTYPE_MACHINE_DEVICE, "충전기 " + machineId +" 부팅 완료", dao.getChagrgeMachineIdx(machineId));
	        String status = "Accepted";   // "Accepted", "Pending", "Rejected"
	        
	        aprotechSession.updateMachineState(AprotechWebSocketSession.MachineState.BOOTCOMPLETE);

	        response = String.format("[3,\"%s\",{\"status\":\"%s\",\"currentTime\":\"%s\",\"interval\":%d}]"
	        						,uniqueId, status, dateTime.getCurrentDateTime(), EVCMSConfig.bootNotificationInterval); 
			
	        		
        }catch(Exception e) {
        	logger.error(String.format("BootNotification.process() exception : %s", e.getMessage()));
        	
        	OCPPDateTime dateTime = new OCPPDateTime();
	        response = String.format("[3,\"%s\",{\"status\":\"%s\",\"currentTime\":\"%s\",\"interval\":%d}]"
					,uniqueId, "Rejected", dateTime.getCurrentDateTime(), EVCMSConfig.bootNotificationInterval);        	
        }		
		
		return response;
	}
	
    public class Payload 
    {
    	public String chargePointModel;
    	public String chargePointSerialNumber;
    	public String chargePointVendor;
    	public String firmwareVersion;
    	public String imsi;
    }	
}
