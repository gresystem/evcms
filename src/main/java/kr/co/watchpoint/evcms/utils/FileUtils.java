package kr.co.watchpoint.evcms.utils;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.Iterator; 
import java.util.List; 
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component; 
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.multipart.MultipartHttpServletRequest; 

public class FileUtils 
{ 
	public List<Map<String,Object>> parseInsertFileInfo(MultipartHttpServletRequest multipartHttpServletRequest, String firmwareUploadDirectory) throws Exception
	{
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames(); 
		
		MultipartFile multipartFile = null; 
		String originalFileName = null; 
		String originalFileExtension = null; 
		String storedFileName = null; 
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(); 
		Map<String, Object> listMap = null; 
		
		File file = new File(firmwareUploadDirectory); 
		if(file.exists() == false)
		{ 
			file.mkdirs(); 
		} 
		
		while(iterator.hasNext())
		{ 
			multipartFile = multipartHttpServletRequest.getFile(iterator.next()); 
			if(multipartFile.isEmpty() == false)
			{ 
				originalFileName = multipartFile.getOriginalFilename(); 
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); 
				storedFileName = getRandomString() + originalFileExtension; 
				
				//file = new File(firmwareUploadDirectory + "/" + storedFileName); 
				file = new File(firmwareUploadDirectory + "/" + originalFileName);
				multipartFile.transferTo(file); 
				
				listMap = new HashMap<String,Object>(); 
				listMap.put("ORIGINAL_FILE_NAME", originalFileName); 
				listMap.put("STORED_FILE_NAME", storedFileName); 
				listMap.put("FILE_SIZE", multipartFile.getSize()); 
				list.add(listMap); 
			} 
		} 
		
		return list; 
	} 
	
	public String getRandomString()
	{ 
		return UUID.randomUUID().toString().replaceAll("-", ""); 
	}
	
}
