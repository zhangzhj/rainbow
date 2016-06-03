package net.rainbow.web.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.web.Invocation;
import net.rainbow.web.context.PageContext;
import net.rainbow.web.ref.ControllerRef;
import net.rainbow.web.ref.MethodRef;
import net.rainbow.web.utils.InvocationUtils;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

public class ActionContext implements Invocation {

	@Override
	public void addContext(String key, Object val) {
		getCurInv().addContext(key, val);
	}

	public Invocation getCurInv() {
		return InvocationUtils.getCurrentThreadInv();
	}

	@Override
	public WebApplicationContext getApplicationContext() {
		return getCurInv().getApplicationContext();
	}

	@Override
	public PageContext getContext() {
		return InvocationUtils.getCurrentThreadInv().getContext();
	}
	
	@Override
	public Object getContext(String key) {
		return InvocationUtils.getCurrentThreadInv().getContext(key);
	}

	@Override
	public ControllerRef getControllerRef() {
		return getCurInv().getControllerRef();
	}

	@Override
	public MethodRef getMethodRef() {
		return getCurInv().getMethodRef();
	}

	@Override
	public String[] getRegexValues() {
		return getCurInv().getRegexValues();
	}

	@Override
	public HttpServletRequest getRequest() {
		return InvocationUtils.getCurrentThreadRequest();
	}

	@Override
	public HttpServletResponse getResponse() {
		return getCurInv().getResponse();
	}

	@Override
	public ServletContext getServletContext() {
		return getCurInv().getServletContext();
	}
	
	@Override
	public String getParameter(String key) {
		return getCurInv().getParameter(key);
	}
	
	@Override
	public MultipartFile getParameterFile(String key) {
		return getCurInv().getParameterFile(key);
	}
	
	@Override
	public String[] getParameterValues(String key) {
		return getCurInv().getParameterValues(key);
	}
	
	@Override
	public void put(String key, Object val) {
		getCurInv().addContext(key, val);
	}
}
