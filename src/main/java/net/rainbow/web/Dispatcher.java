package net.rainbow.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.utils.ExceptionUtils;
import net.rainbow.web.impl.InvocationBean;
import net.rainbow.web.impl.Mapping;
import net.rainbow.web.impl.MappingResult;
import net.rainbow.web.impl.RequestPath;
import net.rainbow.web.interceptor.InterceptorChain;
import net.rainbow.web.interceptor.InterceptorChainImpl;
import net.rainbow.web.renderer.RendererDispatcher;
import net.rainbow.web.renderer.RendererDispatcherImpl;
import net.rainbow.web.utils.InvocationUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 控制分发类
 * 
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-01
 * @version V1.0
 */
public class Dispatcher {

	protected Log logger = LogFactory.getLog(getClass());

	private List<Interceptor> interceptors = null;

	private Interceptor[] allowInterceptors = null;

	private List<Mapping> urlMapping = null;

	private HttpServletRequest request = null;

	private HttpServletResponse response = null;

	private RequestPath path = null;

	private MappingResult result = null;

	private boolean isExecute = false;

	private ExceptionHandler handler = null;

	public Dispatcher(ExceptionHandler exceptionHandler,
			List<Interceptor> interceptors, List<Mapping> mapping,
			HttpServletRequest request, HttpServletResponse response,
			RequestPath path) {
		this.handler = exceptionHandler;
		this.interceptors = interceptors;
		this.urlMapping = mapping;
		this.request = request;
		this.response = response;
		this.path = path;

	}

	/**
	 * 主要执行类，对数据进行简单的处理阶段
	 * 
	 * @throws ServletException
	 */
	public boolean execute() throws ServletException {
		if (isExecute) {
			if (logger.isDebugEnabled()) {
				logger.debug("Dispatcher is running... ");
			}
			return false;
		}

		isExecute = true;

		return _execute();

	}

	private boolean _execute() throws ServletException {
		boolean isMatch = false;
		// 先找到是否存在处理节点，如果不存在直接返回
		result = findMappingNode();
		// 存在请求节点数据进行处理
		if (result != null) {
			isMatch = true;
			logger.info("Url Dispatcher MappingNode ");

			allowInterceptors = findInterceptors();
			Invocation inv = new InvocationBean(request, response, result);

			InvocationUtils.bindCurrentRequest(request);
			InvocationUtils.bindInvocation(request, inv);

			logger.info("Init InterceptorChain to filter Controller before");
			InterceptorChain chain = new InterceptorChainImpl(inv,
					allowInterceptors);

			Object result = null;
			try {
				result = chain.doInterceptor(inv, chain);

			} catch (Exception ex) {
				logger.error(ex);
				boolean isThrow = true;
				if (handler != null) {
					result = handler.hander(inv, ex);
					if (result != null && !Boolean.TRUE.equals(result)) {
						isThrow = false;
					}
				}
				if (isThrow) {
					throw new ServletException(ExceptionUtils.getRootCause(ex));
				}
			} finally {
				try {
					if (result != null && !Boolean.TRUE.equals(result)) {
						if (logger.isDebugEnabled()) {
							logger.debug("rander result [" + result + "]");
						}
						RendererDispatcher render = new RendererDispatcherImpl(
								inv, result);
						render.execute();
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn("Result is [" + result
									+ "] cannot to render ");
						}
					}
				} catch (Exception e) {
					logger.error("dispatcher error", e);
					throw new ServletException(ExceptionUtils.getRootCause(e));
				}

				InvocationUtils.unbindInvocation(request);
				InvocationUtils.unbindCurrentRequest();
			}

		} else {
			logger.info("No MatchNode for ActionUrl [" + path.getActionPath()
					+ "] ");
		}
		return isMatch;
	}

	/**
	 * 获取allow的拦截器进行数据解析
	 * 
	 * @return
	 */
	private Interceptor[] findInterceptors() {
		List<Interceptor> _allowInterceptors = new ArrayList<Interceptor>();

		Method method = result.getMappingNode().getMref().getMethod();
		Class<?> clazz = result.getMappingNode().getRef().getControllerClass();
		for (Interceptor iterceptor : interceptors) {
			// 如果是存在方法的拦截器的直接增加
			if (iterceptor.isAllow(clazz, method)) {
				_allowInterceptors.add(iterceptor);
			}
		}

		if (_allowInterceptors != null) {
			Collections.sort(_allowInterceptors);
			allowInterceptors = new Interceptor[_allowInterceptors.size()];
			_allowInterceptors.toArray(allowInterceptors);
		}
		return allowInterceptors;
	}

	/** 获取Mapping的处理节点 */
	private MappingResult findMappingNode() {
		MappingResult _result = null;
		Mapping mapping = null;
		for (Iterator<Mapping> it = urlMapping.iterator(); it.hasNext();) {
			mapping = it.next();
			_result = mapping.match(path);
			// 如果遇到第一个处理节点默认不在进行后续匹配，所以URI的设计必须要一致，如果冲突只能运行一个
			if (_result != null)
				break;
		}
		return _result;
	}

}
