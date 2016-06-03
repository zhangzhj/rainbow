package net.rainbow.web.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Request请求中的基础的信息进行封装使用
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public class RequestPath implements Serializable {

	private static final long serialVersionUID = -8315445870913213513L;

	public RequestPath(HttpServletRequest request) {
		ctxPath = request.getContextPath();
		uri = request.getRequestURI();
		actionPath = uri.startsWith(ctxPath) ? uri.substring(ctxPath.length())
				: uri;
		if (!actionPath.startsWith("/"))
			actionPath = "/" + actionPath;
		if (actionPath.endsWith("/"))
			actionPath = actionPath.substring(0, actionPath.length() - 1);

		// 修复如果是使用欢迎页的内容则直接进行判断
		if (!StringUtils.isBlank(request.getServletPath())) {
			actionPath = request.getServletPath();
		}

		// 设置请求类型
		if ("post".equalsIgnoreCase(request.getMethod())) {
			method = RequestMethod.POST;
		} else if ("get".equalsIgnoreCase(request.getMethod())) {
			method = RequestMethod.GET;
		} else {
			method = RequestMethod.ALL;
		}
	}

	private String suffic;
	private String actionPath;
	private String ctxPath;
	private String uri;
	private RequestMethod method;

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

	public String getSuffic() {
		return suffic;
	}

	public void setSuffic(String suffic) {
		this.suffic = suffic;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public String getCtxPath() {
		return ctxPath;
	}

	public void setCtxPath(String ctxPath) {
		this.ctxPath = ctxPath;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
