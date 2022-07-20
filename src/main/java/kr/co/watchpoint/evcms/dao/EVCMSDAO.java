package kr.co.watchpoint.evcms.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("EVCMSDAO")
public class EVCMSDAO extends AbstractDAO 
{
	@SuppressWarnings("unchecked")
	public Object startTransaction(Map<String,Object> map) throws Exception{
		return insert("evcms.startTransaction", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addOCPPRecvLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addOCPPRecvLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addPeriodicLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addPeriodicLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addEvent(Map<String,Object> map) throws Exception{
		return insert("evcms.addEvent", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addCardTagRegistTransaction(Map<String,Object> map) throws Exception{
		return insert("evcms.addCardTagRegistTransaction", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineUnlockConnectorLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineUnlockConnectorLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineChangeAvailabilityLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineChangeAvailabilityLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addMachineDiagnosticsStatusLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineDiagnosticsStatusLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineChangeConfigurationLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineChangeConfigurationLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addMachineResetLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineResetLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineClearCacheLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineClearCacheLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineClearChargingProfileLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineClearChargingProfileLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addMachineTriggerMessageLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineTriggerMessageLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineGetConfigurationLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineGetConfigurationLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object addMachineDiagnosticsFile(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineDiagnosticsFile", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineGetLocalListVersionLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineGetLocalListVersionLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineSendLocalListLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineSendLocalListLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineReserveNowLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineReserveNowLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineCancelReservationLog(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineCancelReservationLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineGetCompositeScheduleData(Map<String,Object> map) throws Exception{
		return insert("evcms.addMachineGetCompositeScheduleData", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateMachineStatus(Map<String,Object> map) throws Exception{
		return update("evcms.updateMachineStatus", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineSetChargingProfileLog(Map<String,Object> map) throws Exception{
		return update("evcms.addMachineSetChargingProfileLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineRemoteStopTransactionLog(Map<String,Object> map) throws Exception{
		return update("evcms.addMachineRemoteStopTransactionLog", map);
	}

	@SuppressWarnings("unchecked")
	public Object addMachineRemoteStartTransactionLog(Map<String,Object> map) throws Exception{
		return update("evcms.addMachineRemoteStartTransactionLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateTransaction(Map<String,Object> map) throws Exception{
		return update("evcms.updateTransaction", map);
	}

	@SuppressWarnings("unchecked")
	public Object updateCardTagRegistTransaction(Map<String,Object> map) throws Exception{
		return update("evcms.updateCardTagRegistTransaction", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateConnectorStatus(Map<String,Object> map) throws Exception{
		return update("evcms.updateConnectorStatus", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateFirmwareStatus(Map<String,Object> map) throws Exception{
		return update("evcms.updateFirmwareStatus", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateFirmwareVersion(Map<String,Object> map) throws Exception{
		return update("evcms.updateFirmwareVersion", map);
	}
	
	public Object verifyInstallationStatus(Map<String, Object> map) {
		return update("evcms.verifyInstallationStatus", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateChargeMachineSystemConfig(Map<String, Object> map) throws Exception {
		return update("evcms.updateChargeMachineSystemConfig", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateFirmwareUpdateStatus(Map<String,Object> map) throws Exception{
		return update("evcms.updateFirmwareUpdateStatus", map);
	}

	@SuppressWarnings("unchecked")
	public Object updateMachineHeartbeatRecvLog(Map<String,Object> map) throws Exception{
		return update("evcms.updateMachineHeartbeatRecvLog", map);
	}
	
	@SuppressWarnings("unchecked")
	public Object updateServerTransactionInfo(Map<String,Object> map) throws Exception{
		return update("evcms.updateServerTransactionInfo", map);
	}

	@SuppressWarnings("unchecked")
	public Object dropServerTransactionInfo(Map<String,Object> map) throws Exception{
		return update("evcms.dropServerTransactionInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectChargeMachineFromMachineID(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.selectChargeMachineFromMachineID", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMemberIdxFromCardTag(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.getMemberIdxFromCardTag", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getChargeMachineForRestAccess(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.getChargeMachineForRestAccess", map);
	}		
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> isCardTagExist(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.isCardTagExist", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> stopTransaction(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.stopTransaction", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectMemberCardTagWithMemberID(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.selectMemberCardTagWithMemberID", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getTransactionPowerPrice(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.getTransactionPowerPrice", map);
	}	

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectChargeMachineInfo(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.selectChargeMachineInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMachineChargeRateTemplateData(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.getMachineChargeRateTemplateData", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMemberID(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.getMemberID", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getChargeMachineIdx(Map<String,Object> map) throws Exception{
		return (List<Map<String,Object>>)selectList("evcms.getChargeMachineIdx", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectMachineId(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectMachineId", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectChargeMachineId(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectChargeMachineId", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectChargeStationIdx(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectChargeStationIdx", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectAddrGugunInfo(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectAddrGugunInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectGugunUpdateInfo(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectGugunUpdateInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectChargeMachineSystemConfig(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectChargeMachineSystemConfig", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectChargeMachineSystemConfigProfile(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectChargeMachineSystemConfigProfile", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectChargeMachineSystemConfigProfileVersion(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectChargeMachineSystemConfigProfileVersion", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> isExistSerialNumber(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.isExistSerialNumber", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGuestInfoByGuestTag(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.getGuestInfoByGuestTag", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSelectedGuestInfo(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.getSelectedGuestInfo", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInstallationStatusBySerialNumber(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.getInstallationStatusBySerialNumber", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLastFirmwareInfo(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.getLastFirmwareInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLastReservationId(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.getLastReservationId", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> addMachineGetCompositeScheduleLog(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.addMachineGetCompositeScheduleLog", map);
	}		
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectServerTransactionInfo(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.selectServerTransactionInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberProfileChargeTypeFromCardTag(Map<String, Object> map) {
		return (List<Map<String,Object>>)selectList("evcms.getMemberProfileChargeTypeFromCardTag", map);
	}	
	
	
}
