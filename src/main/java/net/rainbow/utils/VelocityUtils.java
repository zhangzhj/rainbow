package net.rainbow.utils;

import java.io.StringWriter;
import java.util.Map;

import net.rainbow.web.ve.RuntimeInstance;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeSingleton;

public class VelocityUtils {

	public static String chooseCharacterEncoding() {
		return RuntimeSingleton.getString("output.encoding", "utf-8");
	}

	public static String chooseCharacterEncodingInput() {
		return RuntimeSingleton.getString("input.encoding", "utf-8");
	}

	public static Context createContext() {
		Context context = new VelocityContext();
		return context;
	}

	/** 对字符进行解析的方法 */
	public static String mergeStringTemplate(Map<String, Object> context,
			String content) {
		Template t = null;
		StringWriter sw = new StringWriter(300);
		try {
			VelocityEngine ve = RuntimeInstance.getStringVelocityEngine();
			t = ve.getTemplate(content, "utf-8");
			t.merge(new VelocityContext(context), sw);
			return sw.toString();
		} catch (Exception e) {
			throw ExceptionUtils.buildNestedException(e);
		}
	}

	/** 需要对模板中的内容进行解析----其中模板路径为相对于 根目录的路径的任何文件需要全程 */
	/**
	 * 这种破坏编码方式的可以不再进行编写了看看是否可以用其他的方式代替
	 */
	public static String mergePathTemplate(Map<String, Object> context,
			String content) {
		Template t = null;
		StringWriter sw = new StringWriter(600);
		try {
			t = RuntimeSingleton.getTemplate(content, "utf-8");
			t.merge(new VelocityContext(context), sw);
			return sw.toString();
		} catch (Exception e) {
			throw ExceptionUtils.buildNestedException(e);
		}
	}
}
