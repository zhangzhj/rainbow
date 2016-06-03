package net.rainbow.web.context;

import java.io.IOException;

import javax.servlet.ServletContext;

import net.rainbow.resource.local.LocalProviderResover;
import net.rainbow.web.utils.RainConfigUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 
 * @Description: 主要实现容器启动context内容的加载
 *               <p>
 *               涉及得到的基础路径为/WEB-INF/conf/applicationContext*.xml
 *               <p>
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-23
 * @version V1.0
 */
public class RainAppContext extends XmlWebApplicationContext {

    /** Default config location for the root context */
    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/conf/applicationContext*.xml";

    public RainAppContext(ServletContext servletContext, boolean refresh) {
        this.setServletContext(servletContext);
        if (refresh) refresh();
    }

    /**
     * 返回对应类型的唯一 Bean, 包括可能的祖先 {@link ApplicationContext} 中对应类型的 Bean.
     * 
     * @param beanType - Bean 的类型
     * 
     * @throws BeansException
     */
    public <T> T getBean(Class<T> beanType) throws BeansException {
        return beanType.cast(BeanFactoryUtils.beanOfTypeIncludingAncestors(this, beanType));
    }

    @Override
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException,
            IOException {
        Resource[] configResources = getConfigResourcesThrows();
        if (configResources != null) {
            reader.loadBeanDefinitions(configResources);
        }
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (int i = 0; i < configLocations.length; i++) {
                reader.loadBeanDefinitions(configLocations[i]);
            }
        }
    }

    private Resource[] getConfigResourcesThrows() {
        return null;
    }
    /**
     * 初始化一些自己需要的类吧，spring真垃圾
     * 
     * (非 Javadoc)
      * <p>Title: prepareBeanFactory</p>
      * <p>Description: TODO</p>
      * @param beanFactory
      * @see org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
     */
    @Override
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
        RainConfigUtils.registerRainConfigProcessors(registry);
        RainConfigUtils.registerRainLocal(registry, LocalProviderResover.resover(this));
        RainConfigUtils.registertVelocityConfigurer(registry, this.getServletContext());
        RainConfigUtils.registertJspViewResolver(registry);
        RainConfigUtils.registertVelocityViewResolver(registry, this.getServletContext());
        super.prepareBeanFactory(beanFactory);
    }

}
