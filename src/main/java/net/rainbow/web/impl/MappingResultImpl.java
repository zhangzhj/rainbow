package net.rainbow.web.impl;

import net.rainbow.web.impl.mapping.MappingNode;

/**
 * 相关Mapping result进行处理解析
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public class MappingResultImpl implements MappingResult {

	private MappingNode node = null;
	private String url;
	
	//只有使用正则表达式的时候才会出现
	private String[] matchValues = null;
	
	public MappingResultImpl(MappingNode node, String url, String[] matchValues) {
		this.node = node;
		this.url = url;
		this.matchValues = matchValues;
	}

	@Override
	public MappingNode getMappingNode() {
		return node;
	}

	@Override
	public String[] getMatchValues() {
		return matchValues;
	}

	@Override
	public String getUrl() {
		return url;
	}
}
