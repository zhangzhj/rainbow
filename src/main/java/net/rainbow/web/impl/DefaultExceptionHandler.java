package net.rainbow.web.impl;

import net.rainbow.web.ExceptionHandler;
import net.rainbow.web.Invocation;

/**
 * @Description: 主要针对项目中没有进行ExceptionHandler配置的进行直接使用其中返回404
 * 
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-24
 * @version V1.0
 */
public class DefaultExceptionHandler implements ExceptionHandler {

	@Override
	public Object hander(Invocation inv, Exception ex) {
		
		return null;
	}

}
