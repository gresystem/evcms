package kr.co.watchpoint.evcms.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import kr.co.watchpoint.evcms.dao.EVCMSDAO;


@Service("EVCMSServices") @Primary
public class EVCMSServiceImpl implements EVCMSService{
	private Logger logger = LoggerFactory.getLogger("EVCMSServiceImpl");
	
	@Resource(name="EVCMSDAO")
	private EVCMSDAO evcmsDAO;

	@Override
	public void startTransaction(Map<String, Object> map) throws Exception {
		evcmsDAO.startTransaction(map);
	}		
	
	@Override
	public void addOCPPRecvLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addOCPPRecvLog(map);
	}		

	@Override
	public void addPeriodicLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addPeriodicLog(map);
	}		
	
	@Override
	public void addEvent(Map<String, Object> map) throws Exception {
		evcmsDAO.addEvent(map);
	}		

	@Override
	public void addCardTagRegistTransaction(Map<String, Object> map) throws Exception {
		evcmsDAO.addCardTagRegistTransaction(map);
	}		

	@Override
	public void addMachineUnlockConnectorLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineUnlockConnectorLog(map);
	}		

	@Override
	public void addMachineChangeAvailabilityLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineChangeAvailabilityLog(map);
	}		
	
	@Override
	public void addMachineDiagnosticsStatusLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineDiagnosticsStatusLog(map);
	}		

	@Override
	public void addMachineChangeConfigurationLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineChangeConfigurationLog(map);
	}		
	
	@Override
	public void addMachineResetLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineResetLog(map);
	}		

	@Override
	public void addMachineClearCacheLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineClearCacheLog(map);
	}		

	@Override
	public void addMachineClearChargingProfileLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineClearChargingProfileLog(map);
	}		
	
	@Override
	public void addMachineTriggerMessageLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineTriggerMessageLog(map);
	}		

	@Override
	public void addMachineGetConfigurationLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineGetConfigurationLog(map);
	}		

	@Override
	public void addMachineGetLocalListVersionLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineGetLocalListVersionLog(map);
	}		

	@Override
	public void addMachineSetChargingProfileLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineSetChargingProfileLog(map);
	}		
	
	@Override
	public void addMachineSendLocalListLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineSendLocalListLog(map);
	}		

	@Override
	public void addMachineReserveNowLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineReserveNowLog(map);
	}		

	@Override
	public void addMachineRemoteStartTransactionLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineRemoteStartTransactionLog(map);
	}		

	@Override
	public void addMachineRemoteStopTransactionLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineRemoteStopTransactionLog(map);
	}		
	
	@Override
	public void addMachineCancelReservationLog(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineCancelReservationLog(map);
	}		

	@Override
	public void addMachineGetCompositeScheduleData(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineGetCompositeScheduleData(map);
	}		
	
	@Override
	public void addMachineDiagnosticsFile(Map<String, Object> map) throws Exception {
		evcmsDAO.addMachineDiagnosticsFile(map);
	}	
	
	@Override
	public void updateMachineStatus(Map<String, Object> map) throws Exception {
		evcmsDAO.updateMachineStatus(map);
	}		

	@Override
	public void updateTransaction(Map<String, Object> map) throws Exception {
		evcmsDAO.updateTransaction(map);
	}

	@Override
	public void updateCardTagRegistTransaction(Map<String, Object> map) throws Exception {
		evcmsDAO.updateCardTagRegistTransaction(map);
	}	
	
	@Override
	public void updateConnectorStatus(Map<String, Object> map) throws Exception {
		evcmsDAO.updateConnectorStatus(map);
	}
	
	@Override
	public void updateFirmwareStatus(Map<String, Object> map) throws Exception {
		evcmsDAO.updateFirmwareStatus(map);
	}
	
	@Override
	public void updateFirmwareVersion(Map<String, Object> map) throws Exception {
		evcmsDAO.updateFirmwareVersion(map);
	}
			
	@Override
	public void updateFirmwareUpdateStatus(Map<String, Object> map) throws Exception {
		evcmsDAO.updateFirmwareUpdateStatus(map);
	}
	
	@Override
	public void updateChargeMachineSystemConfig(Map<String, Object> map) throws Exception {
		evcmsDAO.updateChargeMachineSystemConfig(map);
	}

	@Override
	public void updateMachineHeartbeatRecvLog(Map<String, Object> map) throws Exception {
		evcmsDAO.updateMachineHeartbeatRecvLog(map);
	}
	
	@Override
	public void updateServerTransactionInfo(Map<String, Object> map) throws Exception {
		evcmsDAO.updateServerTransactionInfo(map);
	}

	@Override
	public void dropServerTransactionInfo(Map<String, Object> map) throws Exception {
		evcmsDAO.dropServerTransactionInfo(map);
	}
	
	@Override
	public void verifyInstallationStatus(Map<String, Object> map) {
		evcmsDAO.verifyInstallationStatus(map);
	}
	
	@Override
	public List<Map<String, Object>> selectChargeMachineFromMachineID(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeMachineFromMachineID(map);
	}
	
	@Override
	public List<Map<String, Object>> getMemberIdxFromCardTag(Map<String, Object> map) throws Exception {
		return evcmsDAO.getMemberIdxFromCardTag(map);
	}
	
	@Override
	public List<Map<String, Object>> getChargeMachineForRestAccess(Map<String, Object> map) throws Exception {
		return evcmsDAO.getChargeMachineForRestAccess(map);
	}		
	
	@Override
	public List<Map<String, Object>> isCardTagExist(Map<String, Object> map) throws Exception {
		return evcmsDAO.isCardTagExist(map);
	}
	
	@Override
	public List<Map<String, Object>> selectMemberCardTagWithMemberID(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectMemberCardTagWithMemberID(map);
	}
	
	@Override
	public List<Map<String, Object>> stopTransaction(Map<String, Object> map) throws Exception {
		return evcmsDAO.stopTransaction(map);
	}	

	@Override
	public List<Map<String, Object>> getTransactionPowerPrice(Map<String, Object> map) throws Exception {
		return evcmsDAO.getTransactionPowerPrice(map);
	}
	
	@Override
	public List<Map<String, Object>> selectChargeMachineInfo(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeMachineInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getMachineChargeRateTemplateData(Map<String, Object> map) throws Exception {
		return evcmsDAO.getMachineChargeRateTemplateData(map);
	}
	
	@Override
	public List<Map<String, Object>> getMemberID(Map<String, Object> map) throws Exception {
		return evcmsDAO.getMemberID(map);
	}
	
	@Override
	public List<Map<String, Object>> getChargeMachineIdx(Map<String, Object> map) throws Exception {
		return evcmsDAO.getChargeMachineIdx(map);
	}

	@Override
	public List<Map<String, Object>> selectMachineId(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectMachineId(map);
	}
	
	@Override
	public List<Map<String, Object>> selectChargeMachineId(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeMachineId(map);
	}
	
	@Override
	public List<Map<String, Object>> selectChargeStationIdx(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeStationIdx(map);
	}
	
	@Override
	public List<Map<String, Object>> selectAddrGugunInfo(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectAddrGugunInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> selectGugunUpdateInfo(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectGugunUpdateInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> selectChargeMachineSystemConfig(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeMachineSystemConfig(map);
	}
	
	@Override
	public List<Map<String, Object>> selectChargeMachineSystemConfigProfile(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeMachineSystemConfigProfile(map);
	}

	@Override
	public List<Map<String, Object>> selectChargeMachineSystemConfigProfileVersion(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectChargeMachineSystemConfigProfileVersion(map);
	}
	
	@Override
	public List<Map<String, Object>> isExistSerialNumber(Map<String, Object> map) throws Exception {
		return evcmsDAO.isExistSerialNumber(map);
	}
	
	@Override
	public List<Map<String, Object>> getGuestInfoByGuestTag(Map<String, Object> map) throws Exception {
		return evcmsDAO.getGuestInfoByGuestTag(map);
	}
	
	@Override
	public List<Map<String, Object>> getSelectedGuestInfo(Map<String, Object> map) throws Exception {
		return evcmsDAO.getSelectedGuestInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getInstallationStatusBySerialNumber(Map<String, Object> map) throws Exception {
		return evcmsDAO.getInstallationStatusBySerialNumber(map);
	}
	
	@Override
	public List<Map<String, Object>> getLastFirmwareInfo(Map<String, Object> map) throws Exception {
		return evcmsDAO.getLastFirmwareInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getLastReservationId(Map<String, Object> map) throws Exception {
		return evcmsDAO.getLastReservationId(map);
	}
	
	@Override
	public List<Map<String, Object>> addMachineGetCompositeScheduleLog(Map<String, Object> map) throws Exception {
		return evcmsDAO.addMachineGetCompositeScheduleLog(map);
	}	
	
	@Override
	public List<Map<String, Object>> selectServerTransactionInfo(Map<String, Object> map) throws Exception {
		return evcmsDAO.selectServerTransactionInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getMemberProfileChargeTypeFromCardTag(Map<String, Object> map) throws Exception {
		return evcmsDAO.getMemberProfileChargeTypeFromCardTag(map);
	}			
	
	
}
