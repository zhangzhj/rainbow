package net.rainbow.web.interceptor;

import net.rainbow.web.Executor;
import net.rainbow.web.Interceptor;
import net.rainbow.web.Invocation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 *
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-01
 * @version V1.0
 */
public class InterceptorChainImpl implements InterceptorChain {

	protected Log logger = LogFactory.getLog(InterceptorChain.class);
	
	private Interceptor[] interceptors;

	private int index = -1;

	private Object instuction = null;

	public InterceptorChainImpl(Invocation inv, Interceptor[] interceptors) {
		this.interceptors = interceptors;
	}

	@Override
	public Object doInterceptor(Invocation inv, InterceptorChain chain) throws Exception{
		if(logger.isDebugEnabled()){
			logger.debug("Interceptors before : {"+interceptors.length+"} interceptor index : "+(index+1)+"");
		}
		if (++index == interceptors.length) {
			// 直接调用执行类
			return new Executor(inv).execute();
			
		} else if (index < interceptors.length) {
			Interceptor interceptor = interceptors[index];
			Object _instuction = interceptor.intercept(inv, this);
			if(logger.isDebugEnabled()){
				logger.debug("[" + interceptor.getClass().toString() + "] result "+ _instuction);
			}
			if (Boolean.TRUE != _instuction && _instuction != null) {
				return _instuction;
			}

			// 拦截器返回null的，要恢复为原instruction
			// 这个功能非常有用!!
			if (_instuction != null) {
				this.instuction = _instuction;
			}
		}
		return instuction;
	}
}
