package net.rainbow.web.impl;

import net.rainbow.web.impl.mapping.MappingNode;

/**
 * 主要用于对请求Url进行匹配操作的处理接口
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public interface Mapping {

	public MappingResult match(RequestPath path);

	public MappingNode getNode();

	public String getUrl();

}
