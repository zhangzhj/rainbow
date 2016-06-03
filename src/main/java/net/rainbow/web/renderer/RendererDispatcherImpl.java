package net.rainbow.web.renderer;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rainbow.RainConstants;
import net.rainbow.utils.JSONUtils;
import net.rainbow.web.Invocation;
import net.rainbow.web.renderer.impl.StreamRender;
import net.rainbow.web.renderer.impl.TemplateRenderer;
import net.rainbow.web.renderer.impl.TextRenderer;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @Description: 主要用于执行处理结果的渲染 ，其中以处理分发为主要功能
 * 
 *               其中可以选择执行后期渲染的类
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-24
 * @version V1.0
 */

public class RendererDispatcherImpl implements RendererDispatcher {

	private boolean isEnabled = logger.isDebugEnabled();

	public void execute() throws Exception {
		Renderer render = getRenderer(this.getResult());
		render.render(inv);
	}

	private Renderer getRenderer(Object result) {

		Renderer render = null;
		if (result instanceof CharSequence) {
			String _result = (String) result;
			if (_result.startsWith("@:")) {
				render = new TextRenderer(_result.substring(2));
			} else if (_result.startsWith("r:")) {
				render = new RedirectRenderer(_result.substring(2));
			} else if (_result.startsWith("f:")) {
				render = new ForwordRenderer(_result.substring(2));
			} else {
				render = new TemplateRenderer(_result);
			}

		} else if (result instanceof Renderer) {
			render = ((Renderer) result);
		} else if (result instanceof InputStream) {
			render = new StreamRender((InputStream) result);
		} else if (result instanceof byte[]) {
			render = new ByteRender((byte[]) result);
		} else {
			render = new JSONRender(result);
		}
		if (isEnabled) {
			logger.debug("render class " + render.getClass().getName()
					+ " to rend template " + result);
		}
		return render;
	}

	private Invocation inv;

	public Invocation getInv() {
		return inv;
	}

	public void setInv(Invocation inv) {
		this.inv = inv;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	private Object result;

	public RendererDispatcherImpl(Invocation inv, Object result) {
		this.inv = inv;
		this.result = result;
	}

	/**
	 * 流操作
	 * 
	 * @author (sean)zhijun.zhang@gmail.com
	 */

	class ByteRender implements Renderer {
		private byte[] bytes = null;

		public ByteRender(byte[] bytes) {
			this.bytes = bytes;
		}

		@Override
		public void render(Invocation inv) throws Exception {
			HttpServletResponse response = inv.getResponse();
			OutputStream os = response.getOutputStream();
			try {
				if (response.getContentType() == null) {
					response.setContentType("application/octet-stream");
				}
				os.write(bytes);

			} finally {
				if (os != null) {
					os.close();
				}
			}
		}
	}

	/**
	 * json 操作
	 * 
	 * @author (sean)zhijun.zhang@gmail.com
	 */

	class JSONRender extends AbstractRenderer implements RainConstants {
		private Object json;

		public JSONRender(Object object) {
			this.json = object;
		}

		@Override
		public void render(Invocation inv) throws Exception {
			HttpServletResponse response = inv.getResponse();
			response.setCharacterEncoding(DEFALULT_CHARSET);
			sendResponse(response, JSONUtils.getJsonString(json));

		}
	}

	/**
	 * 对URL的简单扩展
	 * 
	 * @author (sean)zhijun.zhang@gmail.com
	 */

	class RedirectRenderer extends AbstractRenderer implements RainConstants {
		private String url;

		public RedirectRenderer(String url) {
			this.url = url;
		}

		@Override
		public void render(Invocation inv) throws Exception {
			HttpServletResponse response = inv.getResponse();
			if (StringUtils.isBlank(this.url)) {
				if (logger.isWarnEnabled()) {
					logger.warn("Redirect url [" + this.url + "] is not null ");
				}
			} else {
				response.sendRedirect(this.url);
			}
		}
	}

	/**
	 * 对URL的简单扩展
	 * 
	 * @author (sean)zhijun.zhang@gmail.com
	 */

	class ForwordRenderer extends AbstractRenderer implements RainConstants {
		private String url;

		public ForwordRenderer(String url) {
			this.url = url;
		}

		@Override
		public void render(Invocation inv) throws Exception {
			HttpServletRequest request = inv.getRequest();
			if (StringUtils.isBlank(this.url)) {
				if (logger.isWarnEnabled()) {
					logger.warn("Redirect url [" + this.url + "] is not null ");
				}
			} else {
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(this.url);
				dispatcher.forward(inv.getRequest(), inv.getResponse());
			}
		}
	}
}
