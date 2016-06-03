package net.rainbow.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.utils.ExceptionUtils;
import net.rainbow.web.converter.ConverterFactory;
import net.rainbow.web.impl.ActionContext;
import net.rainbow.web.impl.ParamResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * 对象最终执行地方
 *
 *
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-01
 * @version V1.0
 */
public class Executor {

	protected Log logger = LogFactory.getLog(getClass());

	private int index = 0;

	private ConverterFactory factory = null;

	private Object[] parameterValues = null;

	private Invocation inv = null;

	public Executor(Invocation inv) {
		this.inv = inv;
		factory = new ConverterFactory();
		
		parameterValues = compileParameterValues();
	}

	public Object execute() throws Exception{

		String beanName = inv.getControllerRef().getBeanName();
		Method action = inv.getMethodRef().getMethod();
		
		try {
			if(logger.isDebugEnabled()){
				StringBuilder sb = new StringBuilder();
				sb.append("Excecute Controller ");
				sb.append(beanName);
				sb.append(" Method "+action.getName());
				logger.debug(sb.toString());
			}
			Object object = inv.getApplicationContext().getBean(beanName);
			return action.invoke(object, parameterValues);
		} catch (Exception e) {
			logger.error(new StringBuilder("[Invoke ").append(beanName)
					+ " Exception] ", e);
			throw ExceptionUtils.buildNestedException(e);
		}
	}

	private Object[] compileParameterValues() {
		Object[] _parameterValues = null;
		Class<?>[] types = inv.getMethodRef().getParameterTypes();
		if (types != null && types.length > 0) {
			_parameterValues = new Object[types.length];
			;
			for (int i = 0; i < types.length; i++) {
				_parameterValues[i] = resolverParameter(types[i]);
			}
		}else{
			//没有参数的处理方式
			_parameterValues = new Object[]{};
		}
		return _parameterValues;
	}

	/**
	 * 获取allow的拦截器进行数据解析
	 * 
	 * @return
	 */
	private Object resolverParameter(Class<?> clazz) {
		Object value = null;
		if (ParamResolver.isStaticResolver(clazz)) {
			if (clazz == HttpServletRequest.class) {
				value = inv.getRequest();
			}
			if (clazz == HttpServletResponse.class) {
				value = inv.getResponse();
			}
			if (clazz == Invocation.class) {
				value = new ActionContext();
			}
		} else {
			if(inv.getRegexValues() != null && inv.getRegexValues().length > 0){
				if(index < inv.getRegexValues().length){
					String temp = inv.getRegexValues()[index];
					value = temp;
					if (factory.canConvert(clazz)) {
						try{
							value = factory.convert(clazz, temp);
						}catch (Exception e) {
							StringBuilder sb = new StringBuilder(200);
							sb.append("convert regex mapping value '");
							sb.append(value);
							sb.append("' to  '"+clazz.getName());
							sb.append("' TYPE check "+inv.getControllerRef().getControllerClass().getName());
							sb.append(".").append(inv.getMethodRef().getMethodName());
							sb.append(" parameters Type");
							throw ExceptionUtils.buildNestedException(sb.toString());
						}
					}
					index++;
				}
			}
		}
		return getDefault(value,clazz);
	}
	
	public Object getDefault(Object value,Class<?> clazz){
		if(value == null){
			if(clazz == String.class){
				value = "";
			}else{
				value = 0;
			}
		}
		return value;
	}
}
