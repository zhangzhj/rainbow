package net.rainbow.web.renderer;

import java.io.IOException;

import javax.servlet.ServletException;

import net.rainbow.web.Invocation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface Renderer {
	
	Log logger = LogFactory.getLog(Renderer.class);
	
	/**
     * 主要针对Renderer,对已有的存在的Invocation进行数据或者内容的渲染
     * 
     * 
     * @param inv
     * @throws IOException
     * @throws ServletException
     * @throws Exception
     */
    public void render(Invocation inv) throws Exception;
}
