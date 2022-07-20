package kr.co.watchpoint.evcms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonDateTimeValueAdjust 
{
	private Logger logger = LoggerFactory.getLogger("GsonDateTimeValueAdjust");
	
	public String replaceTimeColon(String fildName, String data)
	{
		String result = "";
		
    	int sidx = data.indexOf(fildName);
    	
    	if (sidx == -1)
    	{
    		result = data;
    	}
    	else
    	{
    		//"timestamp":"2021-03-19T01:39:29.887Z"
    			
    		sidx = sidx + fildName.length() + 1;
    		String prefix = data.substring(0, sidx);
    		String surffix = data.substring(sidx);
    		
    		int eidx = surffix.indexOf("Z");
    		if (eidx != -1)
    		{
    			String dateTime = surffix.substring(0, eidx + 1).replace(':', '+');
    			surffix = surffix.substring(eidx + 1);
    			result = prefix + dateTime + surffix;
    		}
    		else
    		{
    			logger.error("Z not found");
    		}
    	}
    	
		return result;
	}

	public String replaceDTFieldTimeColon(String data)
	{
		String result = "";
		
		int sidx = 0;
		int eidx = 0;
		
    	sidx = data.indexOf("dt");
    	
    	if (sidx != -1)
    	{
    		result = data.substring(0, sidx-1);
    	}
    	
    	boolean first = true;
    	
    	while(sidx != -1)
    	{
    		sidx = data.indexOf(":", sidx);
    		eidx = data.indexOf(",", sidx);
    		if (eidx != -1)
    		{
    			String timeData = data.substring(sidx+1, eidx);
    			timeData = timeData.replace(":", "+");
    			if (first == false)
    			{
    				result += ",{";
    			}
    			result += "\"dt\":" + timeData;
    			
    			sidx = eidx; 
        		eidx = data.indexOf("}", sidx);
        		if (eidx != -1)
        		{    			
        			result += data.substring(sidx, eidx+1);
        		}
        		
        		first = false;
    		}
    		
    		sidx = data.indexOf("dt", eidx);
    	}
		
    	result += data.substring(eidx + 1);
   	
		return result;
	}
	
	public String restoreTimeColon(String data)
	{
		return data.replace('+', ':');
	}	
}
