package net.rainbow.web.utils;

import java.lang.reflect.Modifier;

import net.rainbow.RainConstants;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class RainBeanUtils implements RainConstants {
	/**
	 * 返回一个Bean的className的首字母小写的名字
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getBeanNames(Class<?> clazz) {
		String beanName = null;
		if (clazz != null) {
			beanName = clazz.getName();
			beanName = beanName.substring(beanName.lastIndexOf(".") + 1);
			beanName = beanName.substring(0, 1).toLowerCase()
					+ beanName.substring(1);
		}
		return beanName;
	}

	/**
	 *返回Controller的名字
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getControllerNames(Class<?> clazz) {
		String beanName = null;
		if (clazz != null) {
			beanName = clazz.getName().substring(
					clazz.getName().lastIndexOf(".") + 1);
			beanName = beanName.substring(0, 1).toLowerCase()
					+ beanName.substring(1);
			beanName = beanName.replace(SUFFIXS_CONTROLLER, "");
		}
		return beanName;
	}

	public static String getBeanDefinitionsName(String beanName, Class<?> clazz) {

		if (StringUtils.isEmpty(beanName)
				&& clazz.isAnnotationPresent(Component.class)) {
			beanName = clazz.getAnnotation(Component.class).value();
		}

		if (StringUtils.isEmpty(beanName)
				&& clazz.isAnnotationPresent(Service.class)) {
			beanName = clazz.getAnnotation(Service.class).value();
		}
		return beanName;
	}

	/** 判断class的基础注册条件 主要针对 */
	public static boolean regCondition(Class<?> clazz) {
		if (Modifier.isAbstract(clazz.getModifiers())
				|| Modifier.isInterface(clazz.getModifiers())
				|| Modifier.isFinal(clazz.getModifiers()))
			return false;
		return true;
	}

}
