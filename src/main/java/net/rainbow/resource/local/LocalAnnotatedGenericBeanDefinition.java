package net.rainbow.resource.local;

import java.util.Iterator;

import net.rainbow.resource.Local;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;

public class LocalAnnotatedGenericBeanDefinition extends
		AnnotatedGenericBeanDefinition {

	private static final long serialVersionUID = -236892903729410484L;
	
	public LocalAnnotatedGenericBeanDefinition(Class<?> beanClass) {
		super(beanClass);
		setLazyInit(true);
	}
	

	/** 如果 注解内容为自定义的 则将class的 scope设置一下 */
	public LocalAnnotatedGenericBeanDefinition(Class<?> beanClass, Local local) {
		this(beanClass);
		String suffix = null;
		for (Iterator<String> it = local.keySet().iterator(); it.hasNext();) {
			suffix = it.next();
			if (getBeanClassName().endsWith(suffix)) {
				setScope(local.get(suffix));
			}
		}
	}

}
