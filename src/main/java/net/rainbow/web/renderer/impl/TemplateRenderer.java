package net.rainbow.web.renderer.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import net.rainbow.resource.local.SingletonAnnotatedGenericBeanDefinition;
import net.rainbow.utils.SpringUtils;
import net.rainbow.web.Invocation;
import net.rainbow.web.impl.view.ViewDispatcher;
import net.rainbow.web.impl.view.ViewDispatcherImpl;
import net.rainbow.web.renderer.AbstractRenderer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.web.context.WebApplicationContext;

public class TemplateRenderer extends AbstractRenderer {

	public TemplateRenderer(String text) {
		setText(text);
	}

	private String text = null;

	private String viewDispatcherName = "viewDispatcher";

	@Override
	public void render(Invocation inv) throws Exception {
		if (!StringUtils.isBlank(text)) {

			if (logger.isDebugEnabled()) {
				logger.debug("Trying to render template :" + text);
			}

			ViewDispatcher dispatcher = getViewDispatcher(inv);
			HttpServletResponse response = inv.getResponse();
			String viewName = getViewName(inv);

			if (StringUtils.isBlank(viewName)) {
				String msg = "Not found view path '" + text;
				response.sendError(404, msg);
				if(logger.isWarnEnabled())
					logger.warn(msg);
				return;
			}
			dispatcher.render(inv, viewName);
		}

	}

	protected ViewDispatcher getViewDispatcher(Invocation inv) {
		ViewDispatcher viewDispatcher = (ViewDispatcher) SpringUtils.getBean(
				inv.getApplicationContext(), viewDispatcherName);
		if (viewDispatcher == null) {
			viewDispatcher = registerViewDispatcher(inv.getApplicationContext());
		}
		return viewDispatcher;
	}

	/**
	 * 注册一个 {@link ViewDispatcher}定义到上下文中，以被这个类的所有实例使用
	 * 
	 * @return
	 */
	protected ViewDispatcher registerViewDispatcher(
			WebApplicationContext applicationContext) {
		// 并发下，重复注册虽然不会错误，但没有必要重复注册
		synchronized (applicationContext) {
			if (SpringUtils.getBean(applicationContext, viewDispatcherName) == null) {
				GenericBeanDefinition beanDefinition = new SingletonAnnotatedGenericBeanDefinition(
						ViewDispatcherImpl.class);
				((BeanDefinitionRegistry) applicationContext
						.getAutowireCapableBeanFactory())
						.registerBeanDefinition(viewDispatcherName,
								beanDefinition);
				if (logger.isDebugEnabled()) {
					logger.debug("registered bean definition:"
							+ ViewDispatcher.class.getName());
				}
			}
			return (ViewDispatcher) SpringUtils.getBean(applicationContext,
					viewDispatcherName);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getViewName(Invocation inv) {

		text = getReallyPath();

		String directoryPath = text.substring(0, text.lastIndexOf("/"));
		File file = new File(inv.getServletContext().getRealPath(directoryPath));

		String viewFile = searchViewFile(file, text.substring(text
				.lastIndexOf("/") + 1, text.length()));
		if (!StringUtils.isBlank(viewFile)) {
			viewFile = directoryPath + "/" + viewFile;
		}

		return viewFile;
	}

	/**
	 * 搜索模板中的view是否存在
	 * 
	 * @param fileNameToFind
	 * @param directoryFile
	 * @param ignoreCase
	 * @return
	 */
	private String searchViewFile(File directoryFile,
			final String fileNameToFind) {

		if (directoryFile.isDirectory()) {
			String[] viewFiles = directoryFile.list(new FilenameFilter() {

				public boolean accept(File dir, String fileName) {
					String _notDirectoryViewName = fileNameToFind;
					String _fileName = fileName;
					if (_fileName.startsWith(_notDirectoryViewName)
							&& new File(dir, fileName).isFile()) {
						if (fileName.length() == fileNameToFind.length()
								&& fileNameToFind.lastIndexOf('.') != -1) {
							return true;
						}
						if (fileName.length() > fileNameToFind.length()
								&& fileName.charAt(fileNameToFind.length()) == '.') {
							return true;
						}
					}
					return false;
				}
			});
			if(viewFiles != null){
			    Arrays.sort(viewFiles);
			    return viewFiles.length == 0 ? null : viewFiles[0];
			}
		}
		return null;

	}

	private String getReallyPath() {
		if (!text.startsWith("/")) {
			text = "/" + text;
		}
		return text;
	}
}
