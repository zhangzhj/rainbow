package net.rainbow.web.impl;

import net.rainbow.web.impl.mapping.MappingNode;

/**
 * 相关Mapping result进行处理解析
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public interface MappingResult {

	/**	主要用于对Mapping结果的存贮	*/
	public MappingNode getMappingNode();

	public String[] getMatchValues();

	public String getUrl();

}
