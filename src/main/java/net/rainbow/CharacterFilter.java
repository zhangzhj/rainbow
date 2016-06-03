package net.rainbow;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class CharacterFilter implements Filter {
	
	private String encoding = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig.getInitParameter("encoding") == null?"utf-8":filterConfig.getInitParameter("encoding");
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		request.setCharacterEncoding(encoding);
		filterChain.doFilter(request, servletResponse);
	}
	
	public void destroy() {
		this.encoding = null;
	}
}
