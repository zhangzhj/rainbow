package net.rainbow.web.impl.mapping;

import net.rainbow.web.impl.Mapping;
import net.rainbow.web.impl.MappingResult;
import net.rainbow.web.impl.MappingResultImpl;
import net.rainbow.web.impl.RequestPath;
/**
 * 
 * 用于表示静态的页面处理节点或者相关的自动生成的节点处理方式,主要是针对自动生成的Mapping地址判断
 *
 * @author (sean)zhijun.zhang@gmail.com
 */
public class StaticsMapping extends AbstractMapping implements Mapping{

	private String url;
	private MappingNode node;
	
	public StaticsMapping(String url, MappingNode node) {
		this.url = url;
		this.node = node;
	}

	@Override
	public MappingResult _match(RequestPath path) {
		MappingResult result = null;
		if(this.url.equals(path.getActionPath())){
			result =  new MappingResultImpl(node,url,null);
		}
		return result;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MappingNode getNode() {
		return node;
	}

	public void setNode(MappingNode node) {
		this.node = node;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this)
            return true;
        if (obj instanceof StaticsMapping) {
            return ((StaticsMapping)obj).url.equals(this.url);
        }
        return false;
	}
}
