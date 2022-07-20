package kr.co.watchpoint.evcms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import kr.co.watchpoint.evcms.dao.AccessDAO;
import kr.co.watchpoint.evcms.protocol.OCPP16.MeterValue;
import kr.co.watchpoint.evcms.protocol.OCPP16.conf.GetConfigurationConf;
import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.socket.handler.SocketHandler;
import kr.co.watchpoint.evcms.struct.AprotechWebSocketSession;
import kr.co.watchpoint.evcms.struct.LocalAuthorizationList;
import kr.co.watchpoint.evcms.utils.UniqueID;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RestController 
{
	private Logger logger = LoggerFactory.getLogger("RestController");
	
	@Resource(name="EVCMSService")
	private EVCMSService evcmsService;
	
	@Value("${evm.domain}")
	private String evmDomain;

	@Value("${spring.jmx.default-domain}")
	private String emcmsDomain;
	
	@Value("${ocpp16.message.firmwareupdate.retrievedate}")
	private int firmwareUpdateRetrievedate;

	@Value("${ftp.firmware.download.url}")
	private String ftpFirmwareDownloadUrl;	
	
	@Value("${ftp.addr}")
	private String ftpAddr;	
	
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	
	@RequestMapping(value="/evm/start-card_regist_mode", method=RequestMethod.POST)
	@ResponseBody
	public Object StartCardRegistMode(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
	        // 웹파라미터 체크 시작
			if (params.get("memberId") == null)
			{
				logger.warn(String.format("StartCardRegistMode() memberId = null"));
				rootNode.put("result", "invalid member id parameter");
				return rootNode;
			}
			
			if (params.get("machineNum") == null)
			{
				logger.warn(String.format("StartCardRegistMode() machineNum = null"));
				rootNode.put("result", "invalid machine num parameter");
				return rootNode;
			}
			
			if (params.get("cardNum") == null)
			{
				logger.warn(String.format("StartCardRegistMode() cardNum = null"));
				rootNode.put("result", "invalid card num parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String memberId = params.get("memberId").toString();
			String machineId = params.get("machineNum").toString();
			String cardNum = params.get("cardNum").toString();
			
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("StartCardRegistMode() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("StartCardRegistMode() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			/******************************************************************
			{"vendorId":"UNIEV", "messageId":"uvStartCardRegMode", "data":{"memberID":"kkko"}} 			
			 ******************************************************************/
			
			String msg = String.format("[2,\"%s\",\"DataTransfer\",{\"vendorId\":\"UNIEV\",\"messageId\":\"uvStartCardRegMode\", \"data\":{\"memberId\":\"%s\"}}]", uniqueId, memberId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "uvStartCardRegMode", memberId);			

			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("memberId", memberId);
			sqlParam.put("machineId", machineId);
			sqlParam.put("cardNum", cardNum);
			
			
			evcmsService.addCardTagRegistTransaction(sqlParam);
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception StartCardRegistMode() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/stop-card_regist_mode", method=RequestMethod.POST)
	@ResponseBody
	public Object StopCardRegistMode(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
	        // 웹파라미터 체크 시작
			if (params.get("memberId") == null)
			{
				logger.warn(String.format("StartCardRegistMode() memberId = null"));
				rootNode.put("result", "invalid member id parameter");
				return rootNode;
			}
			
			if (params.get("machineNum") == null)
			{
				logger.warn(String.format("StartCardRegistMode() machineNum = null"));
				rootNode.put("result", "invalid machine num parameter");
				return rootNode;
			}
			// 웹파라미터 체크 종료
			
			String memberId = params.get("memberId").toString();
			String machineId = params.get("machineNum").toString();
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("StopCardRegistMode() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("StopCardRegistMode() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			/******************************************************************
			{    "vendorId":"UNIEV",    "messageId":"uvStopCardRegMode"  } 			
			 ******************************************************************/
			
			String msg = String.format("[2,\"%s\",\"DataTransfer\",{\"vendorId\":\"UNIEV\",\"messageId\":\"uvStopCardRegMode\"}]", uniqueId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "uvStopCardRegMode", "");			
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception StopCardRegistMode() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}	
	
	@RequestMapping(value="/evm/remote-start-transaction", method=RequestMethod.POST)
	@ResponseBody
	public Object RemoteStartTransaction(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
	        // 웹파라미터 체크 시작
			if (params.get("token") == null)
			{
				logger.warn(String.format("RemoteStartTransaction() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("idTag") == null)
			{
				logger.warn(String.format("RemoteStartTransaction() idTag = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("connectId") == null)
			{
				logger.warn(String.format("RemoteStartTransaction() connectId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String idTag = params.get("idTag").toString();
			int connectId = Integer.parseInt(params.get("connectId").toString());

			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			if(idTag.length() != 9) {
				sqlParam.put("cardTag", idTag);	        
				
				List<Map<String,Object>> listCardTagExistData = evcmsService.isCardTagExist(sqlParam);
				if (listCardTagExistData == null || listCardTagExistData.size() < 1)
				{
					logger.warn(String.format("RemoteStartTransaction() idTag = null"));
					rootNode.put("result", "invalid idTag");
					return rootNode;
				}
			}
			
			
			sqlParam.clear();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("RemoteStartTransaction() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("RemoteStartTransaction() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("RemoteStartTransaction() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
					
			String msg = String.format("[2,\"%s\",\"RemoteStartTransaction\",{\"connectorId\":%d, \"idTag\":\"%s\"}]", uniqueId, connectId, idTag); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "RemoteStartTransaction", idTag);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineRemoteStartTransactionLog(machineId, "sended", connectId, idTag);	 
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception RemoteStartTransaction() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/remote-stop-transaction", method=RequestMethod.POST)
	@ResponseBody
	public Object RemoteStopTransaction(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
	        // 웹파라미터 체크 시작
			if (params.get("token") == null)
			{
				logger.warn(String.format("RemoteStopTransaction() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("connectId") == null)
			{
				logger.warn(String.format("RemoteStopTransaction() connectId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			int connectId = Integer.parseInt(params.get("connectId").toString());
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("RemoteStopTransaction() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("RemoteStopTransaction() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("RemoteStopTransaction() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			int transactionId = aprotectSession.getTransactionId(connectId);
			if (transactionId == -1)
			{
				logger.error(String.format("REST RemoteStopTransaction() transaction id not found (%s,%d)", machineId, transactionId));
				
				rootNode.put("result", "트랜젝션 아이디를 찾을 수 없습니다.");
				return rootNode;				
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"RemoteStopTransaction\",{\"transactionId\":%d}]", uniqueId, transactionId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "RemoteStopTransaction", String.valueOf(connectId));
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineRemoteStopTransactionLog(machineId, "sended", connectId);	 
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception RemoteStopTransaction() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/reset-charge-machine", method=RequestMethod.POST)
	@ResponseBody
	public Object ResetChargeMachine(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
	        // 웹파라미터 체크 시작
			if (params.get("token") == null)
			{
				logger.warn(String.format("ResetChargeMachine() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("resetType") == null)
			{
				logger.warn(String.format("ResetChargeMachine() resetType = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String resetType = params.get("resetType").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("ResetChargeMachine() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("ResetChargeMachine() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("ResetChargeMachine() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"Reset\",{\"type\":\"%s\"}]", uniqueId, resetType); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "Reset", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineResetLog(machineId, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception ResetChargeMachine() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/firmware-update", method=RequestMethod.POST)
	@ResponseBody
	public Object FirmwareUpdate(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			// 데이타 검색 시작
			if (params.get("token") == null)
			{
				logger.warn(String.format("FirmwareUpdate() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("FirmwareUpdate() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			
			List<Map<String,Object>> listFirmwareInfoData = evcmsService.getLastFirmwareInfo(sqlParam);
			if (listFirmwareInfoData.size() < 1)
			{
				throw new Exception("firmware not found");
			}
			
			String firmwareFile = listFirmwareInfoData.get(0).get("org_file_name").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("FirmwareUpdate() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("FirmwareUpdate() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.add(Calendar.DATE, firmwareUpdateRetrievedate);
			String retrieveDate = dateFormat.format(cal.getTime()) + "T" + timeFormat.format(cal.getTime()) + "Z";

			String ftpUrl = ftpFirmwareDownloadUrl + "/" + firmwareFile;
			String msg = String.format("[2,\"%s\",\"UpdateFirmware\",{\"location\":\"%s\",\"retrieveDate\":\"%s\"}]", uniqueId, ftpUrl, retrieveDate); 
				
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "UpdateFirmware", "");	
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception FirmwareUpdate() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/firmware-update-all", method=RequestMethod.POST)
	@ResponseBody
	public Object FirmwareUpdateAll(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			
			List<Map<String,Object>> listFirmwareInfoData = evcmsService.getLastFirmwareInfo(sqlParam);
			if (listFirmwareInfoData.size() < 1)
			{
				throw new Exception("firmware not found");
			}
			
			String firmwareFile = listFirmwareInfoData.get(0).get("org_file_name").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("FirmwareUpdateAll() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.add(Calendar.DATE, firmwareUpdateRetrievedate);
			String retrieveDate = dateFormat.format(cal.getTime()) + "T" + timeFormat.format(cal.getTime()) + "Z";

			String ftpUrl = ftpFirmwareDownloadUrl + "/" + firmwareFile;
			
	        Iterator<String> keys = SocketHandler.sessionMap.keySet().iterator();
	        while( keys.hasNext() ){
	            String key = keys.next();
	            AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(key);
	            
				String uniqueId = UniqueID.getNextID();
				String msg = String.format("[2,\"%s\",\"UpdateFirmware\",{\"location\":\"%s\",\"retrieveDate\":\"%s\"}]", uniqueId, ftpUrl, retrieveDate); 
				
				logger.info("send -> " + msg);
				aprotectSession.send(msg, uniqueId, "UpdateFirmware", "");	
	        }
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception FirmwareUpdateAll() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/get-diagnostics", method=RequestMethod.POST)
	@ResponseBody
	public Object GetDiagnostics(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("GetDiagnostics() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("GetDiagnostics() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("GetDiagnostics() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("GetDiagnostics() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			
			//String getDiagnosticsUrl = "http://" + emcmsDomain + "/diagnostics-status-upload?m=" + URLEncoder.encode(machineId,"UTF-8");
			String getDiagnosticsFtpUrl = "ftp://" + machineId + ":" + machineId + "@" + ftpAddr;
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"GetDiagnostics\",{\"location\":\"%s\"}]", uniqueId, getDiagnosticsFtpUrl); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "GetDiagnostics", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineDiagnosticsStatusLog(machineId, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception GetDiagnostics() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/unlock-connector", method=RequestMethod.POST)
	@ResponseBody
	public Object UnlockConnector(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("UnlockConnector() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("connectorId") == null)
			{
				logger.warn(String.format("UnlockConnector() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String connectorId = params.get("connectorId").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("UnlockConnector() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("UnlockConnector() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("UnlockConnector() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
				
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"UnlockConnector\",{\"connectorId\":\"%s\"}]", uniqueId, connectorId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "UnlockConnector", connectorId);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineUnlockConnectorLog(machineId, connectorId, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception UnlockConnector() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}

	@RequestMapping(value="/evm/trigger-message", method=RequestMethod.POST)
	@ResponseBody
	public Object TriggerMessage(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("TriggerMessage() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("requestedMessage") == null)
			{
				logger.warn(String.format("TriggerMessage() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String requestedMessage = params.get("requestedMessage").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("TriggerMessage() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("TriggerMessage() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("TriggerMessage() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
				
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"TriggerMessage\",{\"requestedMessage\":\"%s\"}]", uniqueId, requestedMessage); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "TriggerMessage", requestedMessage);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineTriggerMessageLog(machineId, requestedMessage, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception TriggerMessage() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/change-availability", method=RequestMethod.POST)
	@ResponseBody
	public Object ChangeAvailability(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("ChangeAvailability() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("connectorId") == null)
			{
				logger.warn(String.format("ChangeAvailability() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("type") == null)
			{
				logger.warn(String.format("ChangeAvailability() type = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String connectorId = params.get("connectorId").toString();
			String type = params.get("type").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("ChangeAvailability() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("ChangeAvailability() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("ChangeAvailability() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
				
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"ChangeAvailability\",{\"connectorId\":\"%s\",\"type\":\"%s\"}]", uniqueId, connectorId, type); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "ChangeAvailability", connectorId);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineChangeAvailabilityLog(machineId, connectorId, type, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception ChangeAvailability() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/change-configuration", method=RequestMethod.POST)
	@ResponseBody
	public Object ChangeConfiguration(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("ChangeConfiguration() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("key") == null)
			{
				logger.warn(String.format("ChangeConfiguration() key = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("value") == null)
			{
				logger.warn(String.format("ChangeConfiguration() value = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String key = params.get("key").toString();
			String value = params.get("value").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("ChangeConfiguration() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("ChangeConfiguration() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("ChangeConfiguration() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
				
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"ChangeConfiguration\",{\"key\":\"%s\",\"value\":\"%s\"}]", uniqueId, key, value); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "ChangeConfiguration", key);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineChangeConfigurationLog(machineId, key, value, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception ChangeConfiguration() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}	
	
	@RequestMapping(value="/evm/clear-cache", method=RequestMethod.POST)
	@ResponseBody
	public Object ClearCache(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("ClearCache() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("ClearCache() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("ClearCache() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("ClearCache() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"ClearCache\",{}]", uniqueId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "ClearCache", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineClearCacheLog(machineId, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception ClearCache() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/clear-charging-profile", method=RequestMethod.POST)
	@ResponseBody
	public Object ClearChargingProfile(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("ClearChargingProfile() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("ClearChargingProfile() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("ClearChargingProfile() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("ClearChargingProfile() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"ClearChargingProfile\",{}]", uniqueId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "ClearChargingProfile", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineClearChargingProfileLog(machineId, "sended");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception ClearChargingProfile() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/get-configuration", method=RequestMethod.POST)
	@ResponseBody
	public Object GetConfiguration(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("GetConfiguration() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("GetConfiguration() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("GetConfiguration() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("GetConfiguration() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"GetConfiguration\",{}]", uniqueId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "GetConfiguration", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineGetConfigurationLog(machineId, "sended", "", "");	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception GetConfiguration() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/get-local-list-version", method=RequestMethod.POST)
	@ResponseBody
	public Object GetLocalListVersion(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("GetLocalListVersion() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("GetLocalListVersion() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("GetLocalListVersion() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("GetLocalListVersion() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"GetLocalListVersion\",{}]", uniqueId); 
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "GetLocalListVersion", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineGetLocalListVersionLog(machineId, "sended", 0);	  
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception GetLocalListVersion() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}	
	
	@RequestMapping(value="/evm/send-local-list", method=RequestMethod.POST)
	@ResponseBody
	public Object SendLocalList(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("SendLocalList() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("listVersion") == null)
			{
				logger.warn(String.format("SendLocalList() listVersion = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("localAuthorizationList") == null)
			{
				logger.warn(String.format("SendLocalList() localAuthorizationList = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("updateType") == null)
			{
				logger.warn(String.format("SendLocalList() updateType = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String listVersion = params.get("listVersion").toString();
			String localAuthorizationList = params.get("localAuthorizationList").toString();
			String updateType = params.get("updateType").toString();
			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("SendLocalList() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("SendLocalList() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("SendLocalList() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"SendLocalList\",{\"listVersion\":%s,\"localAuthorizationList\":%s, \"updateType\":\"%s\"}]"
										, uniqueId, listVersion, localAuthorizationList, updateType); 
			
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "SendLocalList", "");
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineSendLocalListLog(machineId, "sended", 0, localAuthorizationList, updateType);
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception SendLocalList() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/set-charging-profile", method=RequestMethod.POST)
	@ResponseBody
	public Object SetChargingProfile(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("SetChargingProfile() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("connectorId") == null)
			{
				logger.warn(String.format("SetChargingProfile() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("csChargingProfiles") == null)
			{
				logger.warn(String.format("SetChargingProfile() csChargingProfiles = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String connectorId = params.get("connectorId").toString();
			String csChargingProfiles = params.get("csChargingProfiles").toString();

			
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("SetChargingProfile() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("SetChargingProfile() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("SetChargingProfile() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"SetChargingProfile\",{\"connectorId\":%s,\"csChargingProfiles\":%s}]"
										, uniqueId, connectorId, csChargingProfiles); 
			
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "SetChargingProfile", connectorId);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineSetChargingProfileLog(machineId, "sended", Integer.parseInt(connectorId), csChargingProfiles);
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception SetChargingProfile() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}	

	@RequestMapping(value="/evm/reserve-now", method=RequestMethod.POST)
	@ResponseBody
	public Object ReserveNow(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("ReserveNow() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("connectorId") == null)
			{
				logger.warn(String.format("ReserveNow() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("expiryDate") == null)
			{
				logger.warn(String.format("ReserveNow() expiryDate = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			if (params.get("idTag") == null)
			{
				logger.warn(String.format("ReserveNow() idTag = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String connectorId = params.get("connectorId").toString();
			String expiryDate = params.get("expiryDate").toString();
			String idTag = params.get("idTag").toString();

	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        
	        String timeStamp = expiryDate.replace("T", " ").replace("Z", "");
	        LocalDateTime parsedDate = LocalDateTime.parse(timeStamp, dateFormatter);
	        String parsedExpiryDate = parsedDate.format(dateFormatter);
	        
	        
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("ReserveNow() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			
			AccessDAO dao = new AccessDAO(evcmsService);
			int reservationId = dao.getReservationId();
			
			if (reservationId == -1)
			{
				logger.error("ReserveNow() reservationId = -1");
				rootNode.put("result", "invalid reservation id");
				return rootNode;				
			}
			
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("ReserveNow() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("ReserveNow() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"ReserveNow\",{\"connectorId\":%s,\"expiryDate\":\"%s\",\"idTag\":\"%s\",\"reservationId\":%d}]"
										, uniqueId, connectorId, expiryDate, idTag, reservationId); 
			
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "ReserveNow", String.valueOf(reservationId));
			
			dao.addMachineReserveNowLog(machineId, "sended", Integer.parseInt(connectorId), reservationId, parsedExpiryDate, idTag);
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception ReserveNow() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}
	
	@RequestMapping(value="/evm/cancel-reservation", method=RequestMethod.POST)
	@ResponseBody
	public Object CancelReservation(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("CancelReservation() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("reservationId") == null)
			{
				logger.warn(String.format("CancelReservation() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String reservationId = params.get("reservationId").toString();

			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("CancelReservation() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("CancelReservation() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("CancelReservation() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"CancelReservation\",{\"reservationId\":%d}]"
										, uniqueId, reservationId); 
			
			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "CancelReservation", reservationId);
			
			AccessDAO dao = new AccessDAO(evcmsService);
			dao.addMachineCancelReservationLog(machineId, "sended", Integer.parseInt(reservationId));
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception CancelReservation() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}	

	@RequestMapping(value="/evm/get-composite-schedule", method=RequestMethod.POST)
	@ResponseBody
	public Object GetCompositeSchedule(HttpServletRequest request, @RequestBody Map<String, Object> params) 
	{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
		
		try
		{	        
			if (params.get("token") == null)
			{
				logger.warn(String.format("GetCompositeSchedule() token = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("connectorId") == null)
			{
				logger.warn(String.format("GetCompositeSchedule() connectorId = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}

			if (params.get("duration") == null)
			{
				logger.warn(String.format("GetCompositeSchedule() duration = null"));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			// 웹파라미터 체크 종료
			
			String token = params.get("token").toString();
			String connectorId = params.get("connectorId").toString();
			String duration = params.get("duration").toString();

			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("token", token);
			
			List<Map<String,Object>> listMachineData = evcmsService.getChargeMachineForRestAccess(sqlParam);
			if (listMachineData == null || listMachineData.size() < 1)
			{
				logger.warn(String.format("GetCompositeSchedule() access token not found: %s", token));
				rootNode.put("result", "invalid parameter");
				return rootNode;
			}
			
			String machineId = listMachineData.get(0).get("machine_id").toString();
			
			// 데이타 검색 종료
			
			if (SocketHandler.sessionMap == null)
			{
				logger.error("GetCompositeSchedule() SocketHandler.sessionMap == null");
				rootNode.put("result", "internal socket handler null");
				return rootNode;
			}
			
			AprotechWebSocketSession aprotectSession = SocketHandler.sessionMap.get(machineId);
			if (aprotectSession == null)
			{
				logger.error(String.format("GetCompositeSchedule() session not found -> %s", machineId));
				rootNode.put("result", "session not found");
				return rootNode;
			}
			
			String uniqueId = UniqueID.getNextID();
			
			String msg = String.format("[2,\"%s\",\"GetCompositeSchedule\",{\"connectorId\":%s, \"duration\":%s}]"
										, uniqueId, connectorId, duration); 
			
			AccessDAO dao = new AccessDAO(evcmsService);
			String compositeScheduleIdx = dao.addMachineGetCompositeScheduleLog(machineId, "sended", Integer.parseInt(connectorId), Integer.parseInt(duration));

			logger.info("send -> " + msg);
			aprotectSession.send(msg, uniqueId, "GetCompositeSchedule", compositeScheduleIdx);
			
			rootNode.put("result", "success");
			return rootNode;
		
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception GetCompositeSchedule() -> " + exceptionAsString);
			
			rootNode.put("result", e.getMessage());
		}		
		
		return rootNode;
	}	
	
}
