package net.rainbow.web.ref;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.rainbow.web.utils.RainBeanUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

/**
 * Controller 的实体存贮类，主要用于存贮
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public class ControllerRef {

	private static Log logger = LogFactory.getLog(ControllerRef.class);

	public ControllerRef(Class<?> controllerClass, Object controllerObject,
			String relationPath, String controllerName) {
		this.controllerClass = controllerClass;
		this.controllerObject = controllerObject;
		this.relationPath = relationPath;
		this.controllerName = controllerName;
		setBeanName(RainBeanUtils.getBeanNames(controllerClass));
	}

	public ControllerRef(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	private void initControllers() {
		if (this.controllerClass != null) {
			Method[] methods = controllerClass.getDeclaredMethods();
			for (Method method : methods) {
				if (quickPassMethod(method)) {
					continue;
				}

				if (ignoresCommonMethod(method)) {
					continue;
				}

				MethodRef ref = new MethodRef(method, controllerClass);
				if (actions == null) {
					actions = new ArrayList<MethodRef>();
				}
				actions.add(ref);
			}
		}
	}

	private boolean ignoresCommonMethod(Method method) {
		// 来自 Object 的方法
		if (ClassUtils.hasMethod(Object.class, method.getName(), method
				.getParameterTypes())) {
			return true;
		}

		// 以下可能是一些java bean的方法，这些不需要
		String name = method.getName();
		if (name.startsWith("get") && name.length() > 3
				&& Character.isUpperCase(name.charAt("get".length()))
				&& method.getParameterTypes().length == 0
				&& method.getReturnType() != void.class) {
			return true;
		}
		if (name.startsWith("is")
				&& name.length() > 3
				&& Character.isUpperCase(name.charAt("is".length()))
				&& method.getParameterTypes().length == 0
				&& (method.getReturnType() == boolean.class || method
						.getReturnType() == Boolean.class)) {
			return true;
		}
		if (name.startsWith("set") && name.length() > 3
				&& Character.isUpperCase(name.charAt("set".length()))
				&& method.getParameterTypes().length == 1
				&& method.getReturnType() == void.class) {
			return true;
		}
		return false;
	}

	private boolean quickPassMethod(Method method) {
		// public, not static, not abstract, @Ignored
		if (!Modifier.isPublic(method.getModifiers())
				|| Modifier.isAbstract(method.getModifiers())
				|| Modifier.isStatic(method.getModifiers())) {
			if (logger.isDebugEnabled()) {
				logger.debug("ignores method of controller "
						+ controllerClass.getName() + "." + method.getName());
			}
			return true;
		}
		return false;
	}

	public String getRelationPath() {
		return relationPath;
	}

	public void setRelationPath(String relationPath) {
		this.relationPath = relationPath;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Object getControllerObject() {
		return controllerObject;
	}

	public void setControllerObject(Object controllerObject) {
		this.controllerObject = controllerObject;
	}

	public List<MethodRef> getActions() {
		if (actions == null) {
			initControllers();
		}
		return actions;
	}

	public void setActions(List<MethodRef> actions) {
		this.actions = actions;
	}
	

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	private Class<?> controllerClass = null;

	private Object controllerObject = null;

	List<MethodRef> actions = null;

	private String relationPath = null;

	private String controllerName = null;
	
	private String beanName = null;

}
