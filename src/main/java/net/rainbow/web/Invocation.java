package net.rainbow.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.web.context.PageContext;
import net.rainbow.web.ref.ControllerRef;
import net.rainbow.web.ref.MethodRef;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * 主要调用的控制类，用户对一次纯的invocation的信息的传递
 * 其中主要包括对tools、mappingNode中的相互调用的关系存贮，实现对Method的后存贮式开发，不定Action
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public interface Invocation {

	public HttpServletRequest getRequest();

	public HttpServletResponse getResponse();

	/** 得到当前需要执行的Controller的类的相关信息 */
	public ControllerRef getControllerRef();

	/** 得到当前需要执行的Controller的类的相关信息 */
	public MethodRef getMethodRef();

	public WebApplicationContext getApplicationContext();

	public ServletContext getServletContext();

	public void addContext(String key, Object val);

	public void put(String key, Object val);

	public PageContext getContext();

	public Object getContext(String key);

	public String getParameter(String key);

	public String[] getParameterValues(String key);

	public String[] getRegexValues();

	public MultipartFile getParameterFile(String key);
}
