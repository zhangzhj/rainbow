package net.rainbow.web.interceptor;

import java.lang.reflect.Method;

public interface ActionSelector {
	
	public boolean isAllow(Class<?> controllerClazz, Method method);
}
