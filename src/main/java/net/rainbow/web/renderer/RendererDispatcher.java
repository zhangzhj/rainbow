package net.rainbow.web.renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 主要用于执行处理结果的渲染 ，其中以处理分发为主要功能
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public interface RendererDispatcher {
	Log logger = LogFactory.getLog(RendererDispatcher.class);

	public void execute() throws Exception;
}
