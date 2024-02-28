
package com.ufms.web.base;

import java.io.Serializable;
import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ufms.base.utils.AppUtil;

public class WebInitListener implements ServletContextListener ,Serializable {
	
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		AppUtil.applicationContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()); 
		TimeZone zone = TimeZone.getTimeZone("GMT+8");  
	    TimeZone.setDefault(zone);  
	}
}
