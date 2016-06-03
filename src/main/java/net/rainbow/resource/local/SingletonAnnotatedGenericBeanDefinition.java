package net.rainbow.resource.local;

import net.rainbow.resource.Local;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;

public class SingletonAnnotatedGenericBeanDefinition extends
		AnnotatedGenericBeanDefinition {

	private static final long serialVersionUID = -236892903729410484L;
	
	public SingletonAnnotatedGenericBeanDefinition(Class<?> beanClass) {
		super(beanClass);
	}

	/** 如果 注解内容为自定义的 则将class的 scope设置一下 */
	public SingletonAnnotatedGenericBeanDefinition(Class<?> beanClass, Local local) {
		super(beanClass);
		setScope("singleton");
	}



}
