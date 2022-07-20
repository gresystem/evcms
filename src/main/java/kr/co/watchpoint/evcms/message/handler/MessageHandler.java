package kr.co.watchpoint.evcms.message.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.watchpoint.evcms.config.EVCMSConfig;
import kr.co.watchpoint.evcms.protocol.OCPP16.Authorize;
import kr.co.watchpoint.evcms.protocol.OCPP16.BootNotification;
import kr.co.watchpoint.evcms.protocol.OCPP16.DiagnosticsStatusNotification;
import kr.co.watchpoint.evcms.protocol.OCPP16.FirmwareStatusNotification;
import kr.co.watchpoint.evcms.protocol.OCPP16.Heartbeat;
import kr.co.watchpoint.evcms.protocol.OCPP16.MeterValue;
import kr.co.watchpoint.evcms.protocol.OCPP16.StartTransaction;
import kr.co.watchpoint.evcms.protocol.OCPP16.StatusNotification;
import kr.co.watchpoint.evcms.protocol.OCPP16.StopTransaction;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.CancelReservationConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.ChangeAvailabilityConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.ChangeConfigurationConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.ClearChargingProfileConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.GetConfigurationConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.GetDiagnosticsConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.GetLocalListVersionConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.RemoteStartTransactionConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.RemoteStopTransactionConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.ReserveNowConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.ResetConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.SendLocalListConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.SetChargingProfileConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.TriggerMessageConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.UnlockConnectorConf;
import kr.co.watchpoint.evcms.protocol.OCPP16.custom.UvCardReg;
import kr.co.watchpoint.evcms.protocol.OCPP16.custom.UvCardRegStatus;
import kr.co.watchpoint.evcms.protocol.OCPP16.custom.UvGetIdTagProfile;
import kr.co.watchpoint.evcms.protocol.OCPP16.custom.conf.UvStartCardRegModeConf;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.socket.handler.SocketHandler;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.struct.ReqMessageInfo;

public class MessageHandler 
{
	private static Logger logger = LoggerFactory.getLogger("MessageHandler");
	
	public static String process(AprotechWebSocketSession aprotectSession, EVCMSService evcmsService, String machineId, String rawData)
	{
		String response = "";
	
        try
        {
	        Gson gson = new Gson();
	        Object[] call = gson.fromJson(rawData, Object[].class);
	        if (call == null)
	        {
	        	logger.error("process() invalid Call Data -> " + rawData);
	        	return response;
	        }
	        
	        if (call.length == 3)
	        {
	        	ReqMessageInfo reqMessageInfo = SocketHandler.reqMap.get(call[1]);
	        	if (reqMessageInfo == null)
	        	{
	        		logger.warn(rawData);
	        		return response;
	        	}
	        	
	        	if (reqMessageInfo.messageId.compareToIgnoreCase("uvStartCardRegMode") == 0)
	        	{
	        		UvStartCardRegModeConf uvStartCardRegModeConf = new UvStartCardRegModeConf();
		        	response = uvStartCardRegModeConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("UnlockConnector") == 0)
	        	{
	        		UnlockConnectorConf unlockConnectorConf = new UnlockConnectorConf();
		        	response = unlockConnectorConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	} 
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("Reset") == 0)
	        	{
	        		ResetConf resetConf = new ResetConf();
		        	response = resetConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("TriggerMessage") == 0)
	        	{
	        		TriggerMessageConf triggerMessageConf = new TriggerMessageConf();
		        	response = triggerMessageConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("GetDiagnostics") == 0)
	        	{
	        		GetDiagnosticsConf getDiagnosticsConf = new GetDiagnosticsConf();
		        	response = getDiagnosticsConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	        	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("ChangeAvailability") == 0)
	        	{
	        		ChangeAvailabilityConf changeAvailabilityConf = new ChangeAvailabilityConf();
		        	response = changeAvailabilityConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	        	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("ChangeConfiguration") == 0)
	        	{
	        		ChangeConfigurationConf changeConfigurationConf = new ChangeConfigurationConf();
		        	response = changeConfigurationConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	        
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("ClearChargingProfile") == 0)
	        	{
	        		ClearChargingProfileConf clearChargingProfileConf = new ClearChargingProfileConf();
		        	response = clearChargingProfileConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	     
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("GetConfiguration") == 0)
	        	{
	        		GetConfigurationConf getConfigurationConf = new GetConfigurationConf();
		        	response = getConfigurationConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	   
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("GetLocalListVersion") == 0)
	        	{
	        		GetLocalListVersionConf getLocalListVersionConf = new GetLocalListVersionConf();
		        	response = getLocalListVersionConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	 	        	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("SendLocalList") == 0)
	        	{
	        		SendLocalListConf sendLocalListConf = new SendLocalListConf();
		        	response = sendLocalListConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	 
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("SetChargingProfile") == 0)
	        	{
	        		SetChargingProfileConf setChargingProfileConf = new SetChargingProfileConf();
		        	response = setChargingProfileConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	 	        	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("ReserveNow") == 0)
	        	{
	        		ReserveNowConf reserveNowConf = new ReserveNowConf();
		        	response = reserveNowConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	 	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("CancelReservation") == 0)
	        	{
	        		CancelReservationConf cancelReservationConf = new CancelReservationConf();
		        	response = cancelReservationConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("RemoteStartTransaction") == 0)
	        	{
	        		RemoteStartTransactionConf remoteStartTransactionConf = new RemoteStartTransactionConf();
		        	response = remoteStartTransactionConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}		        	
	        	else if(reqMessageInfo.messageId.compareToIgnoreCase("RemoteStopTransactionConf") == 0)
	        	{
	        		RemoteStopTransactionConf remoteStopTransactionConf = new RemoteStopTransactionConf();
		        	response = remoteStopTransactionConf.process(aprotectSession, evcmsService, machineId, reqMessageInfo.data, rawData);	       
		        	SocketHandler.reqMap.remove(call[1]);
	        	}		        	
	        	
	        	return response;
	        }
	        	
	        if (call.length != 4)
	        {
	        	logger.error("process() invalid Call Data -> " + rawData);
	        	return response;	        	
	        }
	        
	        int messageTypeId = Math.round(Float.parseFloat(call[0].toString()));
	        String uniqueId = call[1].toString();
	        String action = call[2].toString();
	        Object payload = call[3];
	        
//	        logger.info(String.format("MessageTypeId -> %d", messageTypeId));
//	        logger.info("UniqueId -> " + uniqueId);
//	        logger.info("Action -> " + action);

//    		try
//    		{	
//    			Map<String,Object> sqlParam = new HashMap<String,Object>();
//    			sqlParam.put("machineId", machineId);
//    			sqlParam.put("message", action);
//    			sqlParam.put("uniqueId", uniqueId);
//    			sqlParam.put("payload", payload.toString());
//    			
//    			logger.info("machineId -> " + machineId);
//    			logger.info("message -> " + action);
//    			logger.info("uniqueId -> " + uniqueId);
//    			logger.info("payload -> " + payload);
//    			
//    			evcmsService.addOCPPRecvLog(sqlParam);
//    			
//    		} catch (Exception e) {
//    			//logger.error("exception userInfo() -> " + e.getStackTrace());
//    			
//    			StringWriter sw = new StringWriter(); 
//    			e.printStackTrace(new PrintWriter(sw)); 
//    			String exceptionAsString = sw.toString();
//    			logger.error("exception process() -> " + exceptionAsString);
//    		}

    		
	        switch(action)
	        {
	        case "BootNotification":
	        	BootNotification bootNotification = new BootNotification();
	        	response = bootNotification.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "StatusNotification":
	        	StatusNotification statusNotification = new StatusNotification();
	        	response = statusNotification.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "Authorize":
	        	Authorize authorize = new Authorize();
	        	response = authorize.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "StartTransaction":
	        	StartTransaction startTransaction = new StartTransaction();
	        	response = startTransaction.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "MeterValue":
	        	MeterValue meterValue = new MeterValue();
	        	response = meterValue.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "StopTransaction":
	        	StopTransaction stopTransaction = new StopTransaction();
	        	response = stopTransaction.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "Heartbeat":
	        	Heartbeat heartbeat = new Heartbeat();
	        	response = heartbeat.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        case "FirmwareStatusNotification":
	        	FirmwareStatusNotification firmwareStatusNotification = new FirmwareStatusNotification();
	        	response = firmwareStatusNotification.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
        	
	        case "DataTransfer":

	        	String customMessageReq = payload.toString();
	        	
	        	if (customMessageReq.indexOf("uvCardRegStatus") != -1)
	        	{
	        		UvCardRegStatus uvCardRegStatus = new UvCardRegStatus();
		        	response = uvCardRegStatus.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);		        		
	        	}		       
	        	else if (customMessageReq.indexOf("uvCardReg") != -1)
	        	{
	        		UvCardReg uvCardReg = new UvCardReg();
		        	response = uvCardReg.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);		        		
	        	}		       		        	
	        	else if (customMessageReq.indexOf("uvGetIdTagProfile") != -1)
	        	{
	        		UvGetIdTagProfile uvGetIdTagProfile = new UvGetIdTagProfile();
		        	response = uvGetIdTagProfile.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);		        		
	        	}		   
	        	
	        	break;
	        	
	        case "DiagnosticsStatusNotification":
	        	DiagnosticsStatusNotification diagnosticsStatusNotification = new DiagnosticsStatusNotification();
	        	response = diagnosticsStatusNotification.process(aprotectSession, evcmsService, machineId, messageTypeId, uniqueId, rawData);
	        	break;
	        	
	        default:
	        	logger.error("invalid Action -> " + action);
	        }
	        
        }catch(Exception e) {
        	logger.error(String.format("process() json parsing exception : %s", e.getMessage()));
        }
        
		return response;
	}
}
