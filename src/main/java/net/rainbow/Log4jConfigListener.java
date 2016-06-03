package net.rainbow;

import java.io.FileNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.util.Log4jWebConfigurer;

public class Log4jConfigListener implements ServletContextListener {
	public Log4jConfigListener() {
	}

	public void contextInitialized(ServletContextEvent event) {
		String location = event.getServletContext().getInitParameter(
				"log4jConfigLocation");
		if (StringUtils.isBlank(location)) {
			location = "/";
		}
		location = event.getServletContext().getRealPath(location);
		try {
			Log4jConfigurer.initLogging(location);
		} catch (FileNotFoundException e) {
			event.getServletContext().log("Builder Log4jConfigurer Exception",
					e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		Log4jWebConfigurer.shutdownLogging(event.getServletContext());
	}
}
