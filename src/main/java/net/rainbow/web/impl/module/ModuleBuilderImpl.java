package net.rainbow.web.impl.module;

import java.util.List;

import net.rainbow.web.ref.ControllerRef;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class ModuleBuilderImpl implements ModuleBuilder {

	/**
	 * 留作后期使用，目前制作Controller的注册使用
	 */
	@Override
	public List<ControllerRef> builder(WebApplicationContext context,
			List<ControllerRef> refs) {
		registerBeanDefinitions(context, refs);
		return null;
	}

	/**
	 * 注册相关Controller到IOC容器中去
	 * 
	 * 其中注册名字为UserController 为userController，将首字母小写为Controller在容器中的名字
	 * 
	 * @param context
	 * @param refs
	 */
	private void registerBeanDefinitions(WebApplicationContext context,
			List<ControllerRef> refs) {
		DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ((XmlWebApplicationContext) context)
				.getBeanFactory();
		for (ControllerRef ref : refs) {
			String beanName = ref.getBeanName();
			Class<?> clazz = ref.getControllerClass();
			if (StringUtils.isEmpty(beanName)
					&& clazz.isAnnotationPresent(Component.class)) {
				beanName = clazz.getAnnotation(Component.class).value();
			}
			if (StringUtils.isEmpty(beanName)
					&& clazz.isAnnotationPresent(Service.class)) {
				beanName = clazz.getAnnotation(Service.class).value();
			}
			bf.registerBeanDefinition(beanName,
					new AnnotatedGenericBeanDefinition(clazz));
		}
	}
}
