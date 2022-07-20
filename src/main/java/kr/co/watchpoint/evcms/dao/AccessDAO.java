package kr.co.watchpoint.evcms.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.watchpoint.evcms.service.EVCMSService;

public class AccessDAO 
{
	private Logger logger = LoggerFactory.getLogger("AccessDAO");
	
	private EVCMSService evcmsService;
	
	public AccessDAO(EVCMSService service)
	{
		this.evcmsService = service;
	}
	
	public void machineConnected(String machineId)
	{
		try
		{	
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", 2);   // 1:충전중, 2:충전대기, 3:점검중, 4:장애중
			
			evcmsService.updateMachineStatus(sqlParam);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception machineConnected() -> " + exceptionAsString);
		}		
	}
	
	public void machineDisConnected(String machineId)
	{
		try
		{	
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", 3);   // 1:충전중, 2:충전대기, 3:점검중, 4:장애중
			
			evcmsService.updateMachineStatus(sqlParam);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception machineDisConnected() -> " + exceptionAsString);
		}		
	}	
	
	public void startTransaction(String machineId, String cardTag, float meterStart, String timeStamp)
	{
		try
		{	
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("cardTag", cardTag);
			sqlParam.put("meterStart", meterStart);
			sqlParam.put("timeStamp", timeStamp);
			
			evcmsService.startTransaction(sqlParam);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception startTransaction() -> " + exceptionAsString);
		}		
	}

	public void updateTransaction(String machineId, String cardTag, float power, float transactionPower, String timeStamp)
	{
		try
		{	
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("cardTag", cardTag);
			sqlParam.put("power", power);
			sqlParam.put("transactionPower", transactionPower);
			sqlParam.put("timeStamp", timeStamp);
			
			evcmsService.updateTransaction(sqlParam);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception updateTransaction() -> " + exceptionAsString);
		}		
	}
	
	public void stopTransaction(String machineId, String cardTag, float power, float transactionPower, String timeStamp)
	{
		try
		{	
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("cardTag", cardTag);
			sqlParam.put("power", power);
			sqlParam.put("transactionPower", transactionPower);
			sqlParam.put("timeStamp", timeStamp);
			
			evcmsService.stopTransaction(sqlParam);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception stopTransaction() -> " + exceptionAsString);
		}		
	}
	
	public boolean isMachineExist(String machineId)
    {
    	boolean result = false;
    	
		try
		{	
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			
			if (evcmsService == null)
			{
				logger.error("evcmsService == null");
			}
			
			List<Map<String,Object>> listMachineData = evcmsService.selectChargeMachineFromMachineID(sqlParam);

			if (listMachineData == null || listMachineData.size() < 1)
			{
				result = false;
			}
			else
			{
				result = true;
			}
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception isMachineExist() -> " + exceptionAsString);
		}

		return result;
    }
	
	public int getReservationId()
    {
    	int result = -1;
    	
		try
		{	
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			
			if (evcmsService == null)
			{
				logger.error("evcmsService == null");
			}
			
			List<Map<String,Object>> listReservationData = evcmsService.getLastReservationId(sqlParam);

			if (listReservationData == null || listReservationData.size() < 1)
			{
				result = -1;
			}
			else
			{
				result = Integer.parseInt(listReservationData.get(0).get("reservation_id").toString()) + 1;
			}
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception getReservationId() -> " + exceptionAsString);
		}

		return result;
    }
	
	public Object[] getMemberIdxFromCardTag(String cardTag)
    {
		Object[] result = new Object[2];
    	
		try
		{	
			// 데이타 검색 시작
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("cardTag", cardTag);
			
			if (evcmsService == null)
			{
				logger.error("evcmsService == null");
			}
			
			List<Map<String,Object>> listMemberData = evcmsService.getMemberIdxFromCardTag(sqlParam);

			if (listMemberData == null || listMemberData.size() < 1)
			{
				result[0] = -1L;
			}
			else
			{
				result[0] = (long)listMemberData.get(0).get("member_idx");
			}
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception getMemberIdxFromCardTag() -> " + exceptionAsString);
		}

		return result;
    }
	
	
	public void updateConnectorStatus(String machineId, String connectorId, int status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("connectorStatus", status);
			
			evcmsService.updateConnectorStatus(sqlParam);		
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception updateConnectorStatus() -> " + exceptionAsString);
		}		
	}
	
	public void updateFirmwareUpdateStatus(String machineId, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			
			evcmsService.updateFirmwareUpdateStatus(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception updateFirmwareUpdateStatus() -> " + exceptionAsString);
		}
	}
	
	public void addMachineDiagnosticsStatusLog(String machineId, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineDiagnosticsStatusLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineDiagnosticsStatusLog() -> " + exceptionAsString);
		}
	}
	
	public void addMachineUnlockConnectorLog(String machineId, String connectorId, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineUnlockConnectorLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineUnlockConnectorLog() -> " + exceptionAsString);
		}
	}

	public void addMachineChangeAvailabilityLog(String machineId, String connectorId, String type, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("type", type);
			sqlParam.put("status", status);
			
			evcmsService.addMachineChangeAvailabilityLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineChangeAvailabilityLog() -> " + exceptionAsString);
		}
	}

	public void addMachineChangeConfigurationLog(String machineId, String key, String value, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("key", key);
			sqlParam.put("value", value);
			sqlParam.put("status", status);
			
			evcmsService.addMachineChangeConfigurationLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineChangeConfigurationLog() -> " + exceptionAsString);
		}
	}
	
	public void addMachineResetLog(String machineId, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineResetLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineResetLog() -> " + exceptionAsString);
		}
	}

	public void addMachineClearCacheLog(String machineId, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineClearCacheLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineClearCacheLog() -> " + exceptionAsString);
		}
	}

	public void addMachineClearChargingProfileLog(String machineId, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineClearChargingProfileLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineClearChargingProfileLog() -> " + exceptionAsString);
		}
	}
	
	public void addMachineTriggerMessageLog(String machineId, String requestMessage, String status)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("requestMessage", requestMessage);
			sqlParam.put("status", status);
			
			evcmsService.addMachineTriggerMessageLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineTriggerMessageLog() -> " + exceptionAsString);
		}
	}

	public void addMachineGetConfigurationLog(String machineId, String status, String configurationKey, String unknownKey)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			sqlParam.put("configurationKey", configurationKey);
			sqlParam.put("unknownKey", unknownKey);
			
			evcmsService.addMachineGetConfigurationLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineGetConfigurationLog() -> " + exceptionAsString);
		}
	}

	public void addMachineGetLocalListVersionLog(String machineId, String status, int listVersion)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			sqlParam.put("listVersion", listVersion);
			
			evcmsService.addMachineGetLocalListVersionLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineGetLocalListVersionLog() -> " + exceptionAsString);
		}
	}

	public void addMachineSendLocalListLog(String machineId, String status, int listVersion, String localAuthorizationList, String updateType)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			sqlParam.put("listVersion", listVersion);
			sqlParam.put("localAuthorizationList", localAuthorizationList);
			sqlParam.put("updateType", updateType);
			
			evcmsService.addMachineSendLocalListLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineSendLocalListLog() -> " + exceptionAsString);
		}
	}
	
	public void addMachineSetChargingProfileLog(String machineId, String status, int connectorId, String csChargingProfiles)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("status", status);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("csChargingProfiles", csChargingProfiles);
			
			evcmsService.addMachineSendLocalListLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineSetChargingProfileLog() -> " + exceptionAsString);
		}
	}

	public void addMachineReserveNowLog(String machineId, String status, int connectorId, int reservationId, String expiryDate, String idTag)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("reservationId", reservationId);
			sqlParam.put("expiryDate", expiryDate);
			sqlParam.put("idTag", idTag);
			sqlParam.put("status", status);
			
			evcmsService.addMachineReserveNowLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineReserveNowLog() -> " + exceptionAsString);
		}
	}

	public void addMachineCancelReservationLog(String machineId, String status, int reservationId)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("reservationId", reservationId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineCancelReservationLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineCancelReservationLog() -> " + exceptionAsString);
		}
	}

	public void addMachineRemoteStartTransactionLog(String machineId, String status, int connectorId, String idTag)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("idTag", idTag);
			sqlParam.put("status", status);
			
			evcmsService.addMachineRemoteStartTransactionLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineRemoteStartTransactionLog() -> " + exceptionAsString);
		}
	}

	public void addMachineRemoteStopTransactionLog(String machineId, String status, int connectorId)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("status", status);
			
			evcmsService.addMachineRemoteStopTransactionLog(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineRemoteStopTransactionLog() -> " + exceptionAsString);
		}
	}
	
	public String addMachineGetCompositeScheduleLog(String machineId, String status, int connectorId, int duration)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("duration", duration);
			sqlParam.put("status", status);
			
			List<Map<String, Object>> listData = evcmsService.addMachineGetCompositeScheduleLog(sqlParam);
			if(listData != null && listData.size() > 0) {
				return listData.get(0).get("idx").toString();
			}
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineGetCompositeScheduleLog() -> " + exceptionAsString);
		}
		
		return "";
	}
	
	public void addMachineGetCompositeScheduleData(Long compositeScheduleIdx, String chargingRateUnit, int startPeriod, int limit)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("compositeScheduleIdx", compositeScheduleIdx);
			sqlParam.put("chargingRateUnit", chargingRateUnit);
			sqlParam.put("startPeriod", startPeriod);
			sqlParam.put("limit", limit);
			
			evcmsService.addMachineGetCompositeScheduleData(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineGetCompositeScheduleData() -> " + exceptionAsString);
		}
	}
	
	public void addMachineDiagnosticsFile(String machineId, String fileName)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("fileName", fileName);
			
			evcmsService.addMachineDiagnosticsFile(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addMachineDiagnosticsFile() -> " + exceptionAsString);
		}
	}
	
	public void updateServerTransactionInfo(String machineId, int connectorId, int transactionId, String idTag, float lastPower, String updateTime)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			sqlParam.put("transactionId", transactionId);
			sqlParam.put("idTag", idTag);
			sqlParam.put("lastPower", lastPower);
			sqlParam.put("updateTime", updateTime);
			
			evcmsService.updateServerTransactionInfo(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception updateServerTransactionInfo() -> " + exceptionAsString);
		}
	}

	public void dropServerTransactionInfo(String machineId, int connectorId)
	{
		try
		{				
			Map<String,Object> sqlParam = new HashMap<String,Object>();
			sqlParam.put("machineId", machineId);
			sqlParam.put("connectorId", connectorId);
			
			evcmsService.dropServerTransactionInfo(sqlParam);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception dropServerTransactionInfo() -> " + exceptionAsString);
		}
	}
	
	public String getChagrgeMachineIdx(String machineId) {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("machineId", machineId);
			List<Map<String, Object>> listData = evcmsService.getChargeMachineIdx(map);
			if(listData != null && listData.size() > 0) {
				return listData.get(0).get("idx").toString();
			}
		}catch(Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception getChagrgeMachineIdx() -> " + exceptionAsString);
		}
		return null;
	}
	
	public void addEventLogs(String catt, String type, String subtype, String content, String targetIdx) {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ev_catt", catt);
			map.put("ev_type", type);
			map.put("ev_subtype", subtype);
			map.put("ev_content", content);
			map.put("ev_target", targetIdx);
			evcmsService.addEvent(map);
		}catch(Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception addEventLogs() -> " + exceptionAsString);
		}
	}

	public boolean isOnInstallation(String machineId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("machineId", machineId);
			List<Map<String, Object>> result = evcmsService.isExistSerialNumber(map);
			return (result != null && result.size() > 0);

		}catch(Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception isOnInstallation() -> " + exceptionAsString);
		}
		return false;
	}

	public boolean updateInstallVerification(String machineId) {
		try {
			Map<String, Object> map = new  HashMap<String, Object>();
			map.put("machineId",  machineId);
			evcmsService.verifyInstallationStatus(map);
			return true;
		}catch(Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception updateInstallVerification() -> " + exceptionAsString);
			return false;
		}
	}

	public long getGuestIdxByGuestTag(String idTag) {
		try {
			Map<String, Object> map = new  HashMap<String, Object>();
			map.put("guestTag", idTag);
			List<Map<String,Object>> listGuestData = evcmsService.getGuestInfoByGuestTag(map);
			if(listGuestData == null || listGuestData.size() == 0) {
				return -1L;
			}
			return Long.parseLong(listGuestData.get(0).get("idx").toString());
		}catch(Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception getGuestIdxByGuestTag() -> " + exceptionAsString);
		}
		
		return -1L;
	}

	public String getInstallationStatus(String machineId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("machineId", machineId);
			List<Map<String, Object>> listInstallStatus = evcmsService.getInstallationStatusBySerialNumber(map);
			if(listInstallStatus != null && listInstallStatus.size() > 0) {
				return listInstallStatus.get(0).get("status").toString();
			}
		}catch(Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();
			logger.error("exception getInstallationStatus() -> " + exceptionAsString);
		}
		return "";
	}
}
