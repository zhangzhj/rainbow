package net.rainbow.web.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.rainbow.web.Interceptor;
import net.rainbow.web.Invocation;
import net.rainbow.web.annotation.Ignored;

/**
 * 用于全局的Controller的控制，对全部的Controller中的action对象都可以进行拦截控制
 * 
 * 優先級高於Action的攔截器
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public abstract class ControllerInterceptor implements Interceptor {

	/**
	 * after controller处理后进行简单的请求处理，直接判断,大多数可以不需要的，如果需要可以重写次 方法
	 */
	@Override
	public Object after(Invocation inv, Object instruction) {
		return null;
	}

	@Override
	public Class<? extends Annotation> getInterceptorAnnocation() {
		return null;
	}

	@Override
	public int compareTo(Interceptor o) {
		if (this.getPriority() > o.getPriority()) {
			return -1;
		}
		if (this.getPriority() < o.getPriority()) {
			return 1;
		}
		return 0;
	}

	/** 在**之前执行的拦截器 其中return null或者为true表示继续执行，其他操作为不在继续执行下一步的操作 */
	public abstract Object before(Invocation inv);

	/**
	 * 有些预留操作，目前只是针对action的拦截操作，以后可以扩展controller的群体操作
	 */
	@Override
	public boolean isAllow(Class<?> controllerClazz, Method method) {
		boolean isAllow = true;
		Annotation pass = method.getAnnotation(Ignored.class);
		Class<? extends Annotation> clazz = this.getInterceptorAnnocation();

		// if @ignored or this not a global then hold back this interceptor
		if (clazz == null && pass != null) {
			logger.info("Method " + method.getName()
					+ " regedit Ignored Annotation ");
			isAllow = false;
		}
		// if this provider Annocation but method not import then hold this
		if (clazz != null && method.getAnnotation(clazz) == null) {
			isAllow = false;
		}

		return isAllow;
	}
}
