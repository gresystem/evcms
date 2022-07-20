package kr.co.watchpoint.evcms.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.watchpoint.evcms.service.EVCMSService;
import kr.co.watchpoint.evcms.utils.FileUtils;

@Controller
public class MainController 
{
	private Logger logger = LoggerFactory.getLogger("MainController");
	
	@Resource(name="EVCMSService")
	private EVCMSService evcmsService;
	
	@RequestMapping("/ocpp16")
	public ModelAndView chat() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ocpp16");
		
		return mv;
	}
}
