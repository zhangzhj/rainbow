package net.rainbow.web.renderer.impl;



import javax.servlet.http.HttpServletResponse;

import net.rainbow.RainConstants;
import net.rainbow.web.Invocation;
import net.rainbow.web.renderer.AbstractRenderer;

import org.apache.commons.lang.StringUtils;

public class TextRenderer extends AbstractRenderer implements RainConstants {

	public TextRenderer(String text) {
		setText(text);
	}

	public TextRenderer(String text, String contentType) {
		setText(text);
		this.contentType = contentType;
	}

	private String text = null;
	private String contentType = "text/html; charset=utf-8";

	@Override
	public void render(Invocation inv) throws Exception {
		if (StringUtils.isBlank(text)) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("trying to render text:" + text);
		}

		HttpServletResponse response = inv.getResponse();
		String oldEncoding = response.getCharacterEncoding();
		if (StringUtils.isBlank(oldEncoding) || oldEncoding.startsWith("ISO-")) {
			String encoding = inv.getRequest().getCharacterEncoding();
			if(StringUtils.isBlank(encoding))
				encoding = DEFALULT_CHARSET;
			response.setCharacterEncoding(DEFALULT_CHARSET);
			if (logger.isDebugEnabled()) {
				logger.debug("set response.characterEncoding by default:"+ response.getCharacterEncoding());
			}
		}

		if (response.getContentType() == null) {
			response.setContentType(contentType);
			if (logger.isDebugEnabled()) {
				logger.debug("set response content-type by default: "
						+ response.getContentType());
			}
		}
		sendResponse(response, text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
