package net.rainbow.web.ve;

import java.util.Properties;

import javax.servlet.ServletContext;

import net.rainbow.RainConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class RuntimeInstance implements RainConstants {

	public static Log logger = LogFactory.getLog(RuntimeInstance.class);
	private static VelocityEngine cVe = null;

	static {
		cVe = new VelocityEngine();
		try {
			Properties p = getVelocityDefaultProperties();
			p.put("resource.loader", "srl");
			p.put("srl.resource.loader.class",
					"net.rainbow.web.impl.view.velocity.LocalStringResourceLoader");
			cVe.init(p);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	public static VelocityEngine getStringVelocityEngine() {
		return cVe;
	}
	
	public static void initCommonVelocity(ServletContext context) throws Exception{
		Properties p = getVelocityDefaultProperties();
		String realPath = context.getRealPath("/");
		p.setProperty("file.resource.loader.path", realPath);
		Velocity.init(p);
	}
	//默认配置直接写死，不纠结了
	public static Properties getVelocityDefaultProperties(){
		Properties p = new Properties();
		p.put("output.encoding", "utf-8");
		p.put("input.encoding", "utf-8");
		return p;
	}
}
