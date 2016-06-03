package net.rainbow.web.impl.mapping;

import java.util.List;

import net.rainbow.web.impl.Mapping;

import org.springframework.web.context.WebApplicationContext;

/**
 * 用于构建Mapping基础信息，主要用于处理Mapping的内容
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public interface MappingBuilder {
	
	public List<Mapping> builder(WebApplicationContext context) throws Exception;
}