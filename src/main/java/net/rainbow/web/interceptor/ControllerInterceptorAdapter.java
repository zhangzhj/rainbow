package net.rainbow.web.interceptor;

import net.rainbow.utils.ExceptionUtils;
import net.rainbow.web.Invocation;

import org.springframework.core.Ordered;

public abstract class ControllerInterceptorAdapter extends ControllerInterceptor
		implements Ordered, ActionSelector {

	/** 对拦截器注解进行操作	*/
	@Override
	public Object intercept(Invocation inv, InterceptorChain chain){
		// before
        Object instruction = this.before(inv);

        // break the invocation?
        if (instruction != null && !Boolean.TRUE.equals(instruction)) {
            // if false, don't render anything
            if (Boolean.FALSE.equals(instruction)) {
                instruction = null;
            }
            return instruction; // break  and return
        }
        
        try {
			instruction = chain.doInterceptor(inv, chain);
		} catch (Exception e) {
			throw ExceptionUtils.buildNestedException(e);
		}
        
		// after
        Object _instruction = this.after(inv,instruction);
        if (_instruction != null && !Boolean.TRUE.equals(_instruction)) {
           instruction = _instruction;
        }
        // return rander result
        return instruction;
	}
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
