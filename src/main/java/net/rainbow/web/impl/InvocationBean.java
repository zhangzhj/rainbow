package net.rainbow.web.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.RainConstants;
import net.rainbow.web.Invocation;
import net.rainbow.web.context.PageContext;
import net.rainbow.web.ref.ControllerRef;
import net.rainbow.web.ref.MethodRef;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 
 * 主要用于请求见数据承载，获取Invocation的对象进行数据的处理，其中包涵的数据
 * <p>
 * 1、PageContext 2、WebApplicationContext 3、HttpServletRequest
 * 4、HttpServletResponse 5、ControllerClass 6、Method 7、ServletContext
 * 
 * </p>
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public class InvocationBean implements Invocation, RainConstants {

	private PageContext pageContext = null;

	private HttpServletRequest request = null;

	private HttpServletResponse response = null;

	private MappingResult mappingResult = null;

	public InvocationBean(HttpServletRequest request,
			HttpServletResponse response, MappingResult mappingResult) {
		setRequest(request);
		setResponse(response);
		this.mappingResult = mappingResult;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public WebApplicationContext getApplicationContext() {
		return (WebApplicationContext) getServletContext().getAttribute(
				ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public ServletContext getServletContext() {
		return getRequest().getSession().getServletContext();
	}

	@Override
	public PageContext getContext() {
		Object object = getRequest().getAttribute(RRQUEST_PAGE_CONTEXT);
		if (object == null) {
			pageContext = new PageContext();
			getRequest().setAttribute(RRQUEST_PAGE_CONTEXT, pageContext);
		} else {
			pageContext = (PageContext) object;
		}

		return pageContext;
	}

	@Override
	public ControllerRef getControllerRef() {
		if (mappingResult.getMappingNode() != null)
			return mappingResult.getMappingNode().getRef();
		return null;
	}

	@Override
	public MethodRef getMethodRef() {
		if (mappingResult.getMappingNode() != null)
			return mappingResult.getMappingNode().getMref();
		return null;
	}

	@Override
	public void addContext(String key, Object val) {
		getContext().put(key, val);
	}

	public Object getContext(String key) {
		return getContext().get(key);
	}

	@Override
	public String[] getRegexValues() {
		return this.mappingResult.getMatchValues();
	}

	@Override
	public String getParameter(String key) {

		return request.getParameter(key);
	}

	@Override
	public MultipartFile getParameterFile(String key) {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest _request = (MultipartHttpServletRequest) request;
			return _request.getFile(key);
		}
		return null;
	}

	@Override
	public String[] getParameterValues(String key) {
		return request.getParameterValues(key);
	}

	@Override
	public void put(String key, Object val) {
		getContext().put(key, val);
	}
}
