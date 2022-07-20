package kr.co.watchpoint.evcms.service;

import java.util.List;
import java.util.Map;

public interface EVCMSService {

	void startTransaction(Map<String, Object> map) throws Exception;
	
	void addOCPPRecvLog(Map<String, Object> map) throws Exception;
	void addPeriodicLog(Map<String, Object> map) throws Exception;
	void addEvent(Map<String, Object> map) throws Exception;
	void addCardTagRegistTransaction(Map<String, Object> map) throws Exception;
	void addMachineUnlockConnectorLog(Map<String, Object> map) throws Exception;
	void addMachineDiagnosticsStatusLog(Map<String, Object> map) throws Exception;
	void addMachineResetLog(Map<String, Object> map) throws Exception;
	void addMachineTriggerMessageLog(Map<String, Object> map) throws Exception;
	void addMachineDiagnosticsFile(Map<String, Object> map) throws Exception;
	void addMachineChangeAvailabilityLog(Map<String, Object> map) throws Exception;
	void addMachineChangeConfigurationLog(Map<String, Object> map) throws Exception;
	void addMachineClearCacheLog(Map<String, Object> map) throws Exception;
	void addMachineClearChargingProfileLog(Map<String, Object> map) throws Exception;	
	void addMachineGetConfigurationLog(Map<String, Object> map) throws Exception;
	void addMachineGetLocalListVersionLog(Map<String, Object> map) throws Exception;
	void addMachineSendLocalListLog(Map<String, Object> map) throws Exception;
	void addMachineSetChargingProfileLog(Map<String, Object> map) throws Exception;
	void addMachineReserveNowLog(Map<String, Object> map) throws Exception;
	void addMachineCancelReservationLog(Map<String, Object> map) throws Exception;
	void addMachineGetCompositeScheduleData(Map<String, Object> map) throws Exception;
	void addMachineRemoteStartTransactionLog(Map<String, Object> map) throws Exception;
	void addMachineRemoteStopTransactionLog(Map<String, Object> map) throws Exception;
	void updateMachineStatus(Map<String, Object> map) throws Exception;
	void updateTransaction(Map<String, Object> map) throws Exception;
	void updateCardTagRegistTransaction(Map<String, Object> map) throws Exception;
	void updateConnectorStatus(Map<String, Object> map) throws Exception;
	void updateFirmwareStatus(Map<String, Object> map) throws Exception;
	void updateFirmwareVersion(Map<String, Object> map) throws Exception;
	void updateChargeMachineSystemConfig(Map<String, Object> map) throws Exception;
	void updateFirmwareUpdateStatus(Map<String, Object> map) throws Exception;
	void updateMachineHeartbeatRecvLog(Map<String, Object> map) throws Exception;
	void updateServerTransactionInfo(Map<String, Object> map) throws Exception;
	void dropServerTransactionInfo(Map<String, Object> map) throws Exception;
	
	void verifyInstallationStatus(Map<String, Object> map);
	
	List<Map<String, Object>> selectChargeMachineFromMachineID(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getMemberIdxFromCardTag(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getChargeMachineForRestAccess(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> isCardTagExist(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectMemberCardTagWithMemberID(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> stopTransaction(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getTransactionPowerPrice(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectChargeMachineInfo(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getMachineChargeRateTemplateData(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getMemberID(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getChargeMachineIdx(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectMachineId(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectChargeMachineId(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectChargeStationIdx(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectAddrGugunInfo(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectGugunUpdateInfo(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectChargeMachineSystemConfig(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectChargeMachineSystemConfigProfile(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectChargeMachineSystemConfigProfileVersion(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> isExistSerialNumber(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getSelectedGuestInfo(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getGuestInfoByGuestTag(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getInstallationStatusBySerialNumber(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getLastFirmwareInfo(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getLastReservationId(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> addMachineGetCompositeScheduleLog(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> selectServerTransactionInfo(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getMemberProfileChargeTypeFromCardTag(Map<String, Object> map) throws Exception;
}
