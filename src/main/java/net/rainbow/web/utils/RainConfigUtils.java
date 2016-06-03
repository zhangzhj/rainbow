package net.rainbow.web.utils;

import java.util.Properties;

import javax.servlet.ServletContext;

import net.rainbow.RainConstants;
import net.rainbow.resource.Local;
import net.rainbow.resource.ResourceRegister;
import net.rainbow.resource.local.LocalAnnotatedGenericBeanDefinition;
import net.rainbow.web.impl.ActionContext;
import net.rainbow.web.impl.DefaultExceptionHandler;
import net.rainbow.web.impl.view.ViewDispatcher;
import net.rainbow.web.impl.view.velocity.ClassVelocityConfigurer;
import net.rainbow.web.ve.RuntimeInstance;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

public class RainConfigUtils implements RainConstants {

    /**
     * 容器启动前初始化项目代码
     * 
     * 
     * @param registry
     */
    public static void registerRainConfigProcessors(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(ACTION_CONTEXT)) registerRainConfig(registry,
                ACTION_CONTEXT, new LocalAnnotatedGenericBeanDefinition(ActionContext.class));
        if (!registry.containsBeanDefinition(DEFAULT_EXCEPTION_HANDLER)) registerRainConfig(
                registry, DEFAULT_EXCEPTION_HANDLER, new LocalAnnotatedGenericBeanDefinition(
                        DefaultExceptionHandler.class));

    }

    /**
     * 容器启动前初始化local 配置
     * 
     * @param registry
     * @param local
     */
    public synchronized static void registerRainLocal(BeanDefinitionRegistry registry, Local local) {
        ResourceRegister register = new ResourceRegister(registry, local);
        register.regedit();
    }

    /**
     * 初始化jsp 编译 代码
     * 
     * @param registry
     */
    public synchronized static void registertJspViewResolver(BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(InternalResourceViewResolver.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("viewClass", JstlView.class));
        beanDefinition.setPropertyValues(propertyValues);
        registerRainConfig(registry, ViewDispatcher.jspViewResolverName, beanDefinition);
    }

    public synchronized static void registertVelocityConfigurer(BeanDefinitionRegistry registry,
            ServletContext context) {
        //注册velocityConfigurer 单例模式
        Properties velocityProperties = RuntimeInstance.getVelocityDefaultProperties();
        velocityProperties.setProperty(DEFAULT_RESOURCE_NAME, context.getRealPath("/"));
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ClassVelocityConfigurer.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues
                .addPropertyValue(new PropertyValue("velocityProperties", velocityProperties));
        propertyValues.addPropertyValue(new PropertyValue("resourceLoaderPath", "/"));
        beanDefinition.setPropertyValues(propertyValues);
        beanDefinition.setScope("singleton");
        registerRainConfig(registry, "velocityConfigurer", beanDefinition);
    }
    /**
     * 注册velocity view的实例类，後續讀取也能快點吧，瞎寫了，spring真sb
     * TODO
     *
     * @param registry
     * @param context
     */
    public synchronized static void registertVelocityViewResolver(BeanDefinitionRegistry registry,
            ServletContext context) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        beanDefinition.setBeanClass(VelocityViewResolver.class);
        propertyValues.addPropertyValue(new PropertyValue("contentType", DEFAULT_CONTENT_TYPE));
        beanDefinition.setPropertyValues(propertyValues);
        registerRainConfig(registry,ViewDispatcher.velocityViewResolverName, beanDefinition);
    }

    //初始化操作
    public static void registerRainConfig(BeanDefinitionRegistry registry, String key,
            BeanDefinition beanDefine) {
        registry.registerBeanDefinition(key, beanDefine);
    }

    private static String ACTION_CONTEXT = "actionContext";

    private static String DEFAULT_EXCEPTION_HANDLER = "defaultExceptionHandler";
}
