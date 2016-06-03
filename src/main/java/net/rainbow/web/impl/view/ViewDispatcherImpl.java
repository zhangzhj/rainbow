package net.rainbow.web.impl.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.rainbow.RainConstants;
import net.rainbow.utils.ExceptionUtils;
import net.rainbow.utils.SpringUtils;
import net.rainbow.web.Invocation;
import net.rainbow.web.impl.module.ResourceProvider;
import net.rainbow.web.utils.RainConfigUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

public class ViewDispatcherImpl implements ViewDispatcher, RainConstants {

    private ApplicationContext applicationContext = null;

    //    private DefaultListableBeanFactory beanFactory = null;

    private Log logger = LogFactory.getLog(ViewDispatcher.class);

    @Override
    public void render(Invocation inv, String viewPath) throws IOException {
        this.applicationContext = inv.getApplicationContext();
        ViewResolver viewResolver = getResolver(viewPath, inv);
        try {
            View view = viewResolver.resolveViewName(viewPath, inv.getRequest().getLocale());
            prepareTools(inv);
            setContextType(inv, viewPath);
            view.render(inv.getContext(), inv.getRequest(), inv.getResponse());
        } catch (Exception e) {
            logger.error(e);
            throw ExceptionUtils.buildNestedException(e);
        }
    }

    public ViewResolver getResolver(String viewPath, Invocation inv) throws IOException {
        // jsp template
        if (viewPath.endsWith(".jsp")) {
            return getJspViewResolver();
        } else {
            return getVelocityViewResolver(viewPath, inv);
        }
    }

    private void prepareTools(Invocation inv) throws Exception {
        ResourceProvider provider = ResourceProvider.getInstance();

        List<String> tools = provider.provideTools();

        for (String toolName : tools) {
            Object object = SpringUtils.getBean(getApplicationContext(), toolName);
            inv.addContext(toolName, object);
        }
        inv.addContext("session", inv.getRequest().getSession());
        inv.addContext("request", inv.getRequest());
        inv.addContext("response", inv.getResponse());
    }

    private ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    protected synchronized ViewResolver getJspViewResolver() throws IOException {
        ViewResolver jspViewResolver = (ViewResolver) SpringUtils.getBean(getApplicationContext(),
                jspViewResolverName);
        //如果启动注册失败，则行注册操作
        if (jspViewResolver == null) {
            //注册操作
            RainConfigUtils
                    .registertJspViewResolver((DefaultListableBeanFactory) ((XmlWebApplicationContext) getApplicationContext())
                            .getBeanFactory());

            logger.info("registered bean definition named " + jspViewResolverName + ": "
                    + InternalResourceViewResolver.class.getName());
            jspViewResolver = (ViewResolver) SpringUtils.getBean(getApplicationContext(),
                    jspViewResolverName);
        }
        return jspViewResolver;
    }

    private synchronized VelocityViewResolver getVelocityViewResolver(String viewPath,
            Invocation inv) throws IOException {

        ViewResolver velocityViewResolver = (ViewResolver) SpringUtils.getBean(
                getApplicationContext(), velocityViewResolverName);
        if (velocityViewResolver != null) {
            return (VelocityViewResolver) velocityViewResolver;
        } else {
            RainConfigUtils
                    .registertVelocityViewResolver(
                            (DefaultListableBeanFactory) ((XmlWebApplicationContext) getApplicationContext())
                                    .getBeanFactory(), inv.getServletContext());

            logger.info("registered bean definition named " + velocityViewResolverName + ": "
                    + VelocityViewResolver.class.getName());
            return (VelocityViewResolver) SpringUtils.getBean(getApplicationContext(),
                    velocityViewResolverName);
        }
    }

    private void setContextType(Invocation inv, String fileName) {
        HttpServletResponse response = inv.getResponse();
        String oldEncoding = response.getCharacterEncoding();
        if (StringUtils.isBlank(oldEncoding) || oldEncoding.startsWith("ISO-")) {
            String encoding = inv.getRequest().getCharacterEncoding();
            if (StringUtils.isBlank(encoding)) encoding = DEFALULT_CHARSET;
            response.setCharacterEncoding(DEFALULT_CHARSET);
            if (logger.isDebugEnabled()) {
                logger.debug("set response.characterEncoding by default:"
                        + response.getCharacterEncoding());
            }
        }

        if (response.getContentType() == null) {
            String contentType = DEFAULT_CONTENT_TYPE;
            if (fileName.endsWith(".xml")) {
                contentType = XML_CONTENT_TYPE;
            }
            response.setContentType(contentType);
            if (logger.isDebugEnabled()) {
                logger.debug("set response content-type by default: " + response.getContentType());
            }
        }
    }

    /**
     * 先去获取配置，如果存在用配置信息，如果不存在用基础信息
     * 
     * 暂时废弃不用，不支持这么多扩展了
     * 
     * @return
     * @throws IOException
     */
    //    public Properties getBaseConfig() throws IOException {
    //        RainBaseConfig config = null;
    //        Properties properties = null;
    //        if (SpringUtils.getBean(getApplicationContext(), RainBaseConfig.class) != null) {
    //            config = SpringUtils.getBean(getApplicationContext(), RainBaseConfig.class);
    //            if (config != null) {
    //                properties = config.getVelocityProperties();
    //            }
    //        }
    //        if (properties == null) {
    //            properties = RuntimeInstance.getVelocityDefaultProperties();
    //        }
    //        return properties;
    //    }
}
