package net.rainbow.web;

/**
 * 全文异常处理架构 方法，其中一个框架中只能存在一个hander如果多个其中会出现异常
 * 
 * 如果系统中没有配hander则可以直接按照框架中defaultHanderException来进行划分
 *
 * @author (sean)zhijun.zhang@gmail.com
 */
public interface ExceptionHandler {
	
	public Object hander(Invocation inv, Exception ex);
}
