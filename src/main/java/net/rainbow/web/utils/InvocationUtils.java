package net.rainbow.web.utils;

import javax.servlet.http.HttpServletRequest;

import net.rainbow.RainConstants;
import net.rainbow.web.Invocation;

public class InvocationUtils implements RainConstants{
	
	 // 存放当前线程所处理的请求对象 
    private final static ThreadLocal<HttpServletRequest> currentRequests = new ThreadLocal<HttpServletRequest>();
    
	public static void bindInvocation(HttpServletRequest req,Invocation inv){
		 req.setAttribute(REQUEST_THREAD_LOCAL, inv);
	}
	
	public static void unbindInvocation(HttpServletRequest req){
		req.removeAttribute(REQUEST_THREAD_LOCAL);
	} 
	
	public static void bindCurrentRequest(HttpServletRequest req){
		if(req == null){
			unbindCurrentRequest();
		}else{
			currentRequests.set(req);
		}
	}
	
	public static void unbindCurrentRequest(){
		currentRequests.remove();
	}
	
	public static Invocation getCurrentThreadInv(){
		return getInvocation(currentRequests.get());
	}
	
	public static HttpServletRequest getCurrentThreadRequest(){
		return currentRequests.get();
	}
	
	public static Invocation getInvocation(HttpServletRequest req){
		if(req == null){
			return null;
		}
		return (Invocation)req.getAttribute(REQUEST_THREAD_LOCAL);
	}
}
