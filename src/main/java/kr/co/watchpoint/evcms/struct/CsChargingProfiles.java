package kr.co.watchpoint.evcms.struct;

public class CsChargingProfiles 
{
	public int chargingProfileId = 0;
	public int stackLevel = 0;	
	public String chargingProfilePurpose = "";
	public String chargingProfileKind = "";
	public CHARGING_SCHEDULE chargingSchedule;

	public class CHARGING_SCHEDULE
	{
		public String chargingRateUnit = "";
		public CHARGING_SCHEDULE_PERIOD[] chargingSchedulePeriod;
	}
	
	public class CHARGING_SCHEDULE_PERIOD
	{
		public int startPeriod = 0;
		public int limit = 0;
	}
}
