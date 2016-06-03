package net.rainbow.web.interceptor;

import net.rainbow.web.Invocation;
/**
 * 简单的拦截器处理链表基础定义
 * <p>
 * 主要方式用于doInterceptor进行连续行处理
 * </p>
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public interface InterceptorChain {
	public Object doInterceptor(Invocation inv,InterceptorChain chain) throws Exception;
}
