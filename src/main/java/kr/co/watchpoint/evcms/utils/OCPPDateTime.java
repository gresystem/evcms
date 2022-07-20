package kr.co.watchpoint.evcms.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class OCPPDateTime 
{
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	
	public String getCurrentDateTime()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
        return dateFormat.format(cal.getTime()) + "T" + timeFormat.format(cal.getTime()) + "Z";
	}
	
	public String getIncrementDateTime(int day)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return simpleDateFormat.format(cal.getTime());
	}	
	
	public String convertLocalDateTimeStringFromUtc(String utcDateTime) throws Exception
	{
		SimpleDateFormat utcDateTimeFormat = null;
		
		if (utcDateTime.length() == 20)
		{
			utcDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			utcDateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = utcDateTimeFormat.parse(utcDateTime);
			
	        DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        currentTFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	        return currentTFormat.format(date);			
		}
		else if (utcDateTime.length() == 24)
		{
			utcDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			utcDateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = utcDateTimeFormat.parse(utcDateTime);
			
	        DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        currentTFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	        return currentTFormat.format(date);			
		}

		throw new Exception("OCPPDateTime.convertLocalDateTimeStringFromUtc() invalid utc time : " + utcDateTime);
	}		
}
