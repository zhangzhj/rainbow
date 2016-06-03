package net.rainbow;

import org.springframework.web.context.WebApplicationContext;

public interface RainConstants {

	public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

	/** 表示一次请求的调用的的request中的invocation的信息，直接绑定当前request */
	public static final String REQUEST_THREAD_LOCAL = "$$net.rainbow.invoction";

	public static final String RRQUEST_PAGE_CONTEXT = "$$net.rainbow.pagecontext.root";

	public static final String MAPPING_CONTEXT = "$$net.rainbow.mapping.root";

	public static final String[] SUFFIXS_INFOS = new String[] {
			"ExceptionHandler", "Controller", "Interceptor", "Tools" };

	public static final String SUFFIXS_CONTROLLER = "Controller";

	public static final String SUFFIXS_INTERCEPTOR = "Interceptor";

	public static final String PACKAGE_CONTROLLER = "controllers";

	public static final String PACKAGE_CONTROLLER_DOT = "controllers.";

	public static final String PACKAGE_TOOLS = "Tools";

	public static final String CLASS_DOT = ".";

	public static final String PATH_DOT = "/";

	public static final String STATIC_MAPPING_DOT = "-";

	public static final String SUFFIXS_CLASS = ".class";

	public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
	
	public static final String XML_CONTENT_TYPE = "text/xml;charset=UTF-8";

	public static final String JSON_CONTEXT_TYPE = "application/json;charset=utf-8";

	public static final String DEFALULT_CHARSET = "utf-8";

	public static final String DEFAULT_RESOURCE_NAME = "file.resource.loader.path";

}
