package kr.co.watchpoint.evcms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueID 
{
	private static int seq = 1;
	
	public static String getNextID()
	{
		Date currentDate = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("MMdd");
		String mmdd = dateformat.format(currentDate);
		
		if (seq > 9999)
		{
			seq = 1;
		}
		
		return mmdd +  String.format("%04d", seq++);
	}
}
