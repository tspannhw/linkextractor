package com.dataflowdeveloper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author tspann
 *
 */
@RestController
public class DataController {

	@Autowired
	private SoupService soupService;

	public static HttpServletRequest getCurrentRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
		Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
		return servletRequest;
	}

	Logger logger = LoggerFactory.getLogger(DataController.class);

	@RequestMapping(value = "/now")
	@ResponseBody
	public Map<String, String> getTime() {
		Map<String, String> map = new HashMap<>();
		map.put("time", "" + System.currentTimeMillis());
		return map;
	}
	
	@RequestMapping(method = RequestMethod.GET, params = {"url","type"}, value= "/extract/{url}" )
	public List<PrintableLink> extract(@RequestParam(value="url") String url, @RequestParam(value="type") String type) {
		List<PrintableLink> value = soupService.extract(url,type);
		final String userIpAddress = getCurrentRequest().getRemoteAddr();
		final String userAgent = getCurrentRequest().getHeader("user-agent");
		final String userDisplay = String.format("Query:%s %s,IP:%s Browser:%s", url, type, userIpAddress, userAgent);
		logger.error(userDisplay);
		System.out.println("end");
		return value;
	}
}