package net.rainbow.web.impl.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.rainbow.web.annotation.Get;
import net.rainbow.web.annotation.Post;
import net.rainbow.web.impl.Mapping;
import net.rainbow.web.impl.MappingResult;
import net.rainbow.web.impl.RequestMethod;
import net.rainbow.web.impl.RequestPath;
/**
 * 对Mapping的一个基础设置
 *
 *
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-9-2 
 * @version V1.0
 */
public abstract class AbstractMapping implements Mapping {

	public MappingResult match(RequestPath path) {
		Method method = getNode().getMref().getMethod();
		Annotation get = method.getAnnotation(Get.class);
		/** 如果 包涵get注解但是请求不是Get方法直接干掉 */
		if (get != null && RequestMethod.GET != path.getMethod()) {
			return null;
		}
		Annotation post = method.getAnnotation(Post.class);
		/** 如果 包涵post注解但是请求不是Post方法直接干掉 */
		if (post != null && RequestMethod.POST != path.getMethod()) {
			return null;
		}
		return _match(path);
	}

	protected abstract MappingResult _match(RequestPath path);
}
