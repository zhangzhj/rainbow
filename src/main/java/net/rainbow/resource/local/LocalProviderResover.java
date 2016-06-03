package net.rainbow.resource.local;

import net.rainbow.RainConstants;
import net.rainbow.resource.Local;
import net.rainbow.utils.SpringUtils;

import org.springframework.beans.factory.BeanFactory;

public class LocalProviderResover implements RainConstants {

	private static String beanName = "localBean";

	private static String defaultScope = "prototype";

	public static Local resover(BeanFactory beanFactory) {
		Local local = null;
		if (beanFactory.containsBean(beanName)) {
			local = (Local) SpringUtils.getBean(beanFactory, beanName);
		} else {
			local = new Local();
		}
		for (String suffix : SUFFIXS_INFOS) {
			local.put(suffix, defaultScope);
		}
		return local;
	}
}
