package net.rainbow.web.renderer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractRenderer implements Renderer{
	
	protected void sendResponse(HttpServletResponse response, String text) throws IOException {
        if (StringUtils.isNotEmpty(text)) {
            PrintWriter out = response.getWriter();
            if (logger.isDebugEnabled()) {
            	logger.debug("write text to response:" + text);
            }
            out.print(text);
            out.close();
        }
    }
}
