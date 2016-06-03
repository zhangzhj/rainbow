package net.rainbow.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @Description: 用于承载基础加载的后缀配置信息
 * 
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-26
 * @version V1.0
 */
public class Local extends HashMap<String, String> {
	private static final long serialVersionUID = -3970368802097690691L;

	public Local(Map<String, String> map) {
		String key = null;
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			key = it.next();
			this.put(key, map.get(key));
		}
	}

	public Local() {
	}
}
