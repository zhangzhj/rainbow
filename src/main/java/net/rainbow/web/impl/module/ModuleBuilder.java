package net.rainbow.web.impl.module;

import java.util.List;

import net.rainbow.web.ref.ControllerRef;

import org.springframework.web.context.WebApplicationContext;
/**
 * 查找并且注册Controller相关模块用最简单的
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public interface ModuleBuilder {
	
	public List<ControllerRef> builder(WebApplicationContext context,List<ControllerRef> refs);
}
