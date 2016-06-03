package net.rainbow.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Description: 忽略表示用于直接忽略Interceptor的拦截访问 
 * 
 * 其中主要用于拦截器如果全局拦截器需要忽略的则直接可以使用
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-24 
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignored {
	
}
