package net.rainbow.web.impl.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.rainbow.RainConstants;
import net.rainbow.resource.RainScanner;
import net.rainbow.resource.vfs.FileObject;
import net.rainbow.resource.vfs.FileSystemManager;
import net.rainbow.web.ref.ControllerRef;
import net.rainbow.web.ref.ResourceRef;
import net.rainbow.web.utils.RainBeanUtils;
import net.rainbow.web.utils.RainResourceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

/**
 * Provider 主要提供class的ioc容器的注入和加载对基础的Class进行扫描
 * 
 * 其中主要按照className的后缀扫描来进行判断的
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @version 1.0
 */
public class ResourceProvider implements RainConstants {

	protected Log logger = LogFactory.getLog(RainScanner.class);

	private List<ControllerRef> ControllerRefclazz = new ArrayList<ControllerRef>();

	private List<String> toolsNames = new ArrayList<String>();

	private volatile static ResourceProvider resourceProvider = null;

	public static ResourceProvider getInstance() {
		if (resourceProvider == null) {
			synchronized (ResourceProvider.class) {
				resourceProvider = new ResourceProvider();
			}
		}
		return resourceProvider;
	}

	private ResourceProvider() {
		init();
	}

	/*************************************************************************/

	public List<ControllerRef> provideControllerAndRegedit() throws IOException {
		return ControllerRefclazz;
	}

	public List<String> provideTools() throws IOException {
		return toolsNames;
	}

	// 暂时不执行jar内部的文件 refs.addAll(QuickScanner.getInstance().getJarResources());
	private void init() {

		try {
			RainScanner scanner = RainScanner.getInstance();
			List<ResourceRef> refs = scanner.getClassesFolderResources();
			List<ResourceRef> _refs = scanner.getJarResources();
			refs.addAll(_refs);

			FileSystemManager fileManager = new FileSystemManager();

			for (ResourceRef ref : refs) {
				String urlString = RainResourceUtils.converterFile(ref);
				if (logger.isInfoEnabled()) {
					logger.info("[resource] scanning resource " + urlString);
				}

				FileObject rootObject = fileManager.resolveFile(urlString);
				addClazz(rootObject, rootObject);

			}
		} catch (Exception e) {
			logger.equals(e);
		}
	}

	private void addClazz(FileObject root, FileObject file) throws Exception {

		for (FileObject _file : file.getChildren()) {
			if (_file != null && _file.getType().hasChildren()) {
				addClazz(root, _file);
			} else {
				addFileClass(root, _file);
			}
		}
	}

	/** 处理需要注入的容器的Bean */
	private void addFileClass(FileObject root, FileObject file)
			throws IOException, ClassNotFoundException, LinkageError {

		String className = root.getName().getRelativeName(file.getName());

		if (isClass(className)) {
			className = StringUtils.removeEnd(className, ".class");
			className = StringUtils.replaceChars(className, '/', '.');
			try {
				Class<?> clazz = ClassUtils.forName(className,
						ResourceProvider.class.getClassLoader());

				if (className.endsWith(SUFFIXS_CONTROLLER)
						&& !className.equals(SUFFIXS_CONTROLLER)
						&& RainBeanUtils.regCondition(clazz)) {
					addControllerClazz(RainBeanUtils.getBeanNames(clazz), clazz);
				}
				if (className.endsWith(PACKAGE_TOOLS)
						&& !className.equals(PACKAGE_TOOLS)
						&& RainBeanUtils.regCondition(clazz)) {
					toolsNames.add(RainBeanUtils.getBeanNames(clazz));
				}
			} catch (Exception e) {
				logger.equals(e);
			}
		}

	}

	/**
	 * 添加Controller的实体对象，设置其中遇到的基础的设置方法
	 * 
	 * @param beanName
	 * @param clazz
	 */
	private void addControllerClazz(String beanName, Class<?> clazz) {
		ControllerRef ref = new ControllerRef(clazz);
		String pkName = clazz.getPackage().getName();
		if (isControllerPk(pkName)) {
			int _index = pkName.indexOf(PACKAGE_CONTROLLER);
			String relativePath = pkName.substring(_index);
			relativePath = StringUtils.remove(relativePath,
					PACKAGE_CONTROLLER_DOT);
			relativePath = StringUtils.replace(relativePath, CLASS_DOT,
					PATH_DOT);
			ref.setRelationPath(relativePath);
		}
		ref.setControllerName(RainBeanUtils.getControllerNames(clazz));
		ref.setBeanName(beanName);
		ControllerRefclazz.add(ref);

	}

	/** 最简单的判断包路径用来设置controller的路径 */
	private boolean isControllerPk(String pkName) {

		if (!StringUtils.isBlank(pkName)
				&& !pkName.endsWith(PACKAGE_CONTROLLER)
				&& pkName.indexOf(PACKAGE_CONTROLLER) > -1)

			return true;

		return false;
	}

	private boolean isClass(String fileUrl) {
		boolean flag = false;
		if (fileUrl != null && !"".equals(fileUrl)
				&& fileUrl.endsWith(".class")) {

			flag = true;
		}
		return flag;
	}
}
