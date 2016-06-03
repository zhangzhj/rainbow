package net.rainbow.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.rainbow.web.interceptor.InterceptorChain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 全局拦截
 * 
 * 在系统中直接使用拦截器的方法为 extents @see {#ControllerInterceptorAdapter}
 * 
 * 实现before方法，其中返回ture或者null则继续往下，返回其他的则直接终止，按照拦截器的返回进行页面的渲染
 * 
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-24
 * @version V1.0
 */
public interface Interceptor extends Comparable<Interceptor> {

	Log logger = LogFactory.getLog(Interceptor.class);

	public Object intercept(Invocation inv, InterceptorChain chain);

	/** 得到该拦截器的优先级顺序 其中按照 大到小 的顺序执行 */
	public int getPriority();

	/** 返回一个Annotation 的拦截器标志 */
	Class<? extends Annotation> getInterceptorAnnocation();

	/** 执行业务逻辑的后续操作，不能再进行对象的渲染处理，可以进行简单的对象处理结果的处理 */
	public Object after(Invocation inv, Object instruction);

	public boolean isAllow(Class<?> controllerClazz, Method method);
}
