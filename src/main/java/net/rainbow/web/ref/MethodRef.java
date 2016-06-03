package net.rainbow.web.ref;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodRef {

	public MethodRef(Method method, Class<?> controllerClazz) {
		this.method = method;
		this.controllerClazz = controllerClazz;
		init();
	}

	/** 去加载Method的Annotation 信息和验证parameterType是否正确 */
	private void init() {
		setMethodName(method.getName());
		setAnnotations(method.getAnnotations());
		setParameterTypes(method.getParameterTypes());
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?> getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(Class<?> controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}

	private String methodName = null;

	private Method method = null;

	private Class<?>[] parameterTypes = null;

	private Annotation[] annotations = null;

	private Class<?> controllerClazz = null;
}
