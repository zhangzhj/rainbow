package net.rainbow.web.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.web.Invocation;

/**
 * 用于参数类型，
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public class ParamResolver {
	private static List<Class<?>> staticParamResolverFactory = null;
	private static List<Class<?>> paramResolverFactory = null;
	static {
		staticParamResolverFactory = new ArrayList<Class<?>>();
		paramResolverFactory = new ArrayList<Class<?>>();
		loadStaticParamResolver();
		loadParamResoulver();
	}

	/**
	 * 在Controller的方法中可以直接下到参数中的变量内容，其中包括
	 * 
	 * HttpServletRequest.class 当前的请求的request HttpServletResponse.class
	 * 当前的请求的response PageContext.class 页面中数据的Model
	 */
	private static void loadStaticParamResolver() {

		staticParamResolverFactory.add(HttpServletRequest.class);
		staticParamResolverFactory.add(HttpServletResponse.class);
		staticParamResolverFactory.add(Invocation.class);

	}

	/**
	 * 在Controller中直接使用的系统的参数内容，主要设计到请求的参数内容的使用
	 * 
	 * Boolean 当前的请求的Boolean Integer 当前的请求的Integer Double 页面中数据的Double
	 */
	private static void loadParamResoulver() {
		paramResolverFactory.add(boolean.class);
		paramResolverFactory.add(Boolean.class);
		paramResolverFactory.add(Integer.class);
		paramResolverFactory.add(int.class);
		paramResolverFactory.add(Double.class);
	}

	/***
	 * 判断是否为静态Resolver
	 * 
	 * @param clazz
	 * @return
	 */

	public static boolean isStaticResolver(Class<?> clazz) {
		if (staticParamResolverFactory.contains(clazz))
			return true;
		return false;
	}

	/***
	 * 判断是否为静态Resolver
	 * 
	 * @param clazz
	 * @return
	 */

	public static boolean isResolver(Class<?> clazz) {
		if (paramResolverFactory.contains(clazz))
			return true;
		return false;
	}

}
