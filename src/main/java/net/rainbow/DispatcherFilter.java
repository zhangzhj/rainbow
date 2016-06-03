package net.rainbow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.utils.ExceptionUtils;
import net.rainbow.utils.SpringUtils;
import net.rainbow.web.Dispatcher;
import net.rainbow.web.ExceptionHandler;
import net.rainbow.web.Interceptor;
import net.rainbow.web.context.RainAppContext;
import net.rainbow.web.impl.Mapping;
import net.rainbow.web.impl.RequestPath;
import net.rainbow.web.impl.mapping.MappingBuilder;
import net.rainbow.web.impl.mapping.MappingBuilderImpl;
import net.rainbow.web.interceptor.ControllerInterceptorAdapter;
import net.rainbow.web.ve.RuntimeInstance;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartResolver;

/**
 * 容器主要入口
 * <p>
 * web.xml中配置dispatcherFilter
 * </p>
 * <code>
 * <filter>
 * 		<filter-name>dispatcherFilter</filter-name>
 * 		<filter-class>net.qdevelop.DispatcherFilter</filter-class>
 * 	</filter>
 * 	<filter-mapping>
 * 		<filter-name>dispatcherFilter</filter-name>
 * 		<url-pattern>/*</url-pattern>
 * 		<dispatcher>REQUEST</dispatcher>
 * 		<dispatcher>FORWARD</dispatcher>
 * 		<dispatcher>INCLUDE</dispatcher>
 * 	</filter-mapping>
 * </code>
 * 
 * </p>
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-01
 * @version V1.0
 */
public class DispatcherFilter extends GenericFilterBean implements RainConstants {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (!initFinally) {
            response.sendError(503, "project is now starting please waiting~");
        }
        // 如果是Multi部分直接进行转换
        request = checkMultipart(request);
        // 对Request进行简单的封装
        RequestPath path = new RequestPath(request);
        if (logger.isDebugEnabled()) {
            logger.debug("request action Path " + path.getActionPath());
        }
        boolean matched = false;

        // 对直接""的不进行处理，后期做好准备 -目前暂时不会出现，除非手动进行 空串的匹配模式
        if (quickPass(path, req, res, filterChain)) {
            doPass(path, req, res, filterChain);
            return;
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Dispatcher url ActionUrl [" + path.getActionPath() + "]");
                logger.debug("=========================> start" + path.getUri());
            }
            // 控制类处理
            Dispatcher dispatcher = new Dispatcher(handler, interceptors, urlMappings, request,
                    response, path);

            matched = dispatcher.execute();
            if (logger.isDebugEnabled()) {
                logger.debug("=========================> end" + matched);
            }

        } catch (Throwable exception) {
            // 直接打印异常跟进行显示
            StringBuilder sb = new StringBuilder();
            sb.append("Request Exception Path : ");
            sb.append(path.getActionPath());
            throw new ServletException(sb.toString(), ExceptionUtils.getRootCause(exception));
        }

        // 容器中不存在处理节点直接转交有filter后续进行处理
        if (!matched) {
            doPass(path, req, res, filterChain);
        }
    }

    private void doPass(RequestPath path, ServletRequest req, ServletResponse res,
            FilterChain filterChain) throws IOException, ServletException {
        if (logger.isInfoEnabled()) {
            logger.info("[Not Matched] Url " + path.getActionPath()
                    + " has't dispose Node in container ");
        }
        // 往下进行处理
        filterChain.doFilter(req, res);
    }

    private boolean quickPass(RequestPath path, ServletRequest req, ServletResponse res,
            FilterChain filterChain) throws IOException, ServletException {
        boolean isMatch = false;
        for (String p : quickpass) {
            if (p.equalsIgnoreCase(path.getActionPath())) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    protected HttpServletRequest checkMultipart(HttpServletRequest request)
            throws MultipartException {

        if (multipartResolver != null && multipartResolver.isMultipart(request)) {
            if (logger.isInfoEnabled()) {
                logger.debug("Request is a MultipartHttpServletRequest");
            }
            if (request instanceof HttpServletRequest) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Resolve HttpServletRequest to a MultipartHttpServletRequest");
                }
                return multipartResolver.resolveMultipart(request);
            }
        }else{
            request = new XssRequestWapper(request);
        }
        return request;
    }

    /**
     * QuickDevelop 初始化的主要入口
     * 
     * 主要的初始化方法，用于初始化WebApplicationContext的位置
     * 
     */
    @Override
    protected void initFilterBean() throws ServletException {
        try {
            applicationContext = initWebApplicationContext();
            prepareUrlMappings();
            prepareInteceptors();
            prepareExceptionHandler();
            prepareMultiResolver();
            initFinally = true;
            if (logger.isInfoEnabled()) {
                logger.info("[rainbow] component start successful...");
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ServletException(e);
        }
    }

    private void prepareMultiResolver() {
        if (multipartResolver == null) {
            DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ((XmlWebApplicationContext) applicationContext)
                    .getBeanFactory();
            multipartResolver = (MultipartResolver) SpringUtils.getBean(bf, multipartResolverName);

        }
    }

    /** 初始化规定的拦截器相关组件 */
    private void prepareInteceptors() {
        if (interceptors == null) {
            interceptors = new ArrayList<Interceptor>();
            DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ((XmlWebApplicationContext) applicationContext)
                    .getBeanFactory();
            String[] beanNames = SpringUtils.getBeanNames(bf, ControllerInterceptorAdapter.class);
            for (String beanName : beanNames) {
                Interceptor i = (Interceptor) SpringUtils.getBean(bf, beanName);
                interceptors.add(i);
            }
            Collections.sort(interceptors);
            if (logger.isInfoEnabled()) {
                StringBuilder builder = new StringBuilder("[init] found ");
                builder.append(interceptors.size());
                builder.append(" interceptor class ..");
                logger.info(builder.toString());
            }

        }
    }

    /** * 初始化异常处理组件 如果存在异常处理则使用，如果不存在系统中配置的异常处理则直接使用 系统默认的 */
    private void prepareExceptionHandler() {
        if (handler == null) {
            DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ((XmlWebApplicationContext) applicationContext)
                    .getBeanFactory();
            String[] beanNames = SpringUtils.getBeanNames(bf, ExceptionHandler.class);
            if (beanNames != null) {
                for (String beanName : beanNames) {
                    if (!"defaultExceptionHandler".equalsIgnoreCase(beanName)) handler = (ExceptionHandler) SpringUtils
                            .getBean(bf, beanName);
                }
            }
            if (handler == null) {
                handler = (ExceptionHandler) SpringUtils.getBean(bf, "defaultExceptionHandler");
            }
            if (logger.isInfoEnabled()) {
                logger.info("[init] loading ExceptionHandler " + handler.getClass()
                        + " for this container");
            }
        }
    }

    /**
     * 初始化 applicationContext的写法，其中webContext只初始化一遍
     * 
     * @throws Exception
     */
    protected synchronized WebApplicationContext initWebApplicationContext() throws Exception {
        Object context = getServletContext().getAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (applicationContext == null && context == null) {
            if (logger.isInfoEnabled()) {
                logger.info("[init] webApplicationContext starting ...");
            }
            // 创建自身AppContext 直接用来进行自身设置加载模式
            RainAppContext rootContext = new RainAppContext(getServletContext(), false);

            String contextConfigLocation = this.getInitParameter("contextConfigLocation",
                    RainAppContext.DEFAULT_CONFIG_LOCATION);
            rootContext.setConfigLocation(contextConfigLocation);
            rootContext.refresh();

            if (logger.isInfoEnabled()) {
                logger.info("[init] webApplicationContext start finish ...");
            }

            getServletContext().setAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);

            // 初始化工具方式
            RuntimeInstance.initCommonVelocity(getServletContext());
            applicationContext = rootContext;
        }

        return applicationContext;
    }

    protected void prepareUrlMappings() throws Exception {
        MappingBuilder builder = new MappingBuilderImpl();
        try {
            // 得到相对ControllerRefs用来进行简单事务处理
            urlMappings = builder.builder(applicationContext);
            getServletContext().setAttribute(MAPPING_CONTEXT, urlMappings);
            if (logger.isInfoEnabled()) {
                logger.info("[init] building " + urlMappings.size()
                        + " UrlMapping Objects for container");
            }
        } catch (Exception e) {
            logger.error("Building UrlMapping Error", e);
        }
    }

    /**
     * 得到web.xml中的初始化参数信息，主要用于页面初始化相关方案
     * 
     * @param initParam
     * @param defaultParam
     * @return
     */
    private String getInitParameter(String initParam, String defaultParam) {
        String p = getFilterConfig().getInitParameter(initParam);
        if (StringUtils.isBlank(p)) p = defaultParam;
        return p;
    }

    @Override
    public void destroy() {
        if (applicationContext != null) {
            try {
                if (applicationContext instanceof AbstractApplicationContext) {
                    ((AbstractApplicationContext) applicationContext).close();
                }
            } catch (Throwable e) {
                logger.error("", e);
                getServletContext().log("", e);
            }
        }
        if (urlMappings != null) {
            urlMappings.clear();
        }
        if (interceptors != null) {
            interceptors.clear();
        }
        multipartResolver = null;
        interceptors = null;
        handler = null;
        super.destroy();
    }

    private MultipartResolver multipartResolver;

    private String multipartResolverName = "multipartResolver";

    // 全局处理Mapping方式
    private List<Mapping> urlMappings = null;

    // 主要针对Interceptors进行预加载，在拦截处理器进行处理，其中getAnnocation为null表示为全局变量
    private List<Interceptor> interceptors = null;

    private ExceptionHandler handler = null;

    private String[] quickpass = new String[] { "" };

    private WebApplicationContext applicationContext = null;

    private volatile boolean initFinally = false;
}
