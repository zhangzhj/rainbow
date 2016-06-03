package net.rainbow.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

public class JSONUtils {


	/** 配置config的 json object工具 */
	public static String getJsonString(Object javaObj) {
		try{
			JsonConfig jsonConfig = configJson();
			JSON json = null;
			if(net.sf.json.util.JSONUtils.isArray(javaObj)){
				json = JSONArray.fromObject(javaObj,jsonConfig);
			}else if(javaObj == null){
				return "";
			}else{
				json = JSONObject.fromObject(javaObj, jsonConfig);
			}
			if(json != null)
				return json.toString();
			return "";
		}catch(Exception e){
			ExceptionUtils.buildNestedException("could't to convert Object to jsonString", e);
		}
		return "";

	}

	/** 得到基本的config配置信息 */
	private static JsonConfig configJson() {
		if (j == null) {
			j = new JsonConfig();
			j.setExcludes(new String[] { "" });
			j.setIgnoreDefaultExcludes(false);
			j.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			j.registerJsonValueProcessor(Date.class, new DateProcessor());
			j.registerJsonValueProcessor(java.sql.Date.class,
					new DateProcessor());
			j.registerJsonValueProcessor(java.sql.Timestamp.class,
					new TimestampProcessor());
		}
		return j;

	}

	/** simple date format for jsonObject */
	private static class DateProcessor implements JsonValueProcessor {
		public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
		private DateFormat dateFormat;

		public DateProcessor() {
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		}

		public Object processArrayValue(Object arg0, JsonConfig arg1) {
			return process(arg0);
		}

		public Object processObjectValue(String arg0, Object arg1,
				JsonConfig arg2) {
			return process(arg1);
		}

		private Object process(Object value) {
			if (value instanceof Date && value != null) {
				return dateFormat.format((Date) value);
			}
			return null;
		}
	}

	/** simple timestamp format */
	private static class TimestampProcessor implements JsonValueProcessor {
		public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
		private DateFormat dateFormat;

		public TimestampProcessor() {
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		}

		public Object processArrayValue(Object obj, JsonConfig config) {
			return process(obj);
		}

		public Object processObjectValue(String str, Object obj,
				JsonConfig config) {
			return process(obj);
		}

		private Object process(Object value) {
			if (value instanceof java.sql.Timestamp && value != null) {
				return dateFormat.format((java.sql.Timestamp) value);
			}
			return null;
		}
	}

	private static JsonConfig j;
}