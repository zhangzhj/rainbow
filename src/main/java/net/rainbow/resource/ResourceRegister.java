package net.rainbow.resource;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.rainbow.RainConstants;
import net.rainbow.resource.local.LocalAnnotatedGenericBeanDefinition;
import net.rainbow.resource.vfs.FileObject;
import net.rainbow.resource.vfs.FileSystemManager;
import net.rainbow.web.impl.module.ResourceProvider;
import net.rainbow.web.ref.ResourceRef;
import net.rainbow.web.utils.RainBeanUtils;
import net.rainbow.web.utils.RainConfigUtils;
import net.rainbow.web.utils.RainResourceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;

public class ResourceRegister implements RainConstants {

	protected Log logger = LogFactory.getLog(RainScanner.class);

	private BeanDefinitionRegistry register = null;

	private Local local = null;

	public ResourceRegister(BeanDefinitionRegistry register, Local local) {
		this.register = register;
		this.local = local;
	}

	public void regedit() {
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
			logger.error(e);
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
			className = getClazzName(className);
			String _suffix = "";
			for (Iterator<String> it = local.keySet().iterator(); it.hasNext();) {
				_suffix = it.next();
				if (className.endsWith(_suffix) && !className.equals(_suffix)) {
					Class<?> clazz = ClassUtils.forName(className,
							ResourceProvider.class.getClassLoader());
					addClazzAndRegedist(clazz);
					break;
				}
			}
		}
	}

	private void addClazzAndRegedist(Class<?> clazz) {
		// ioc condition
		if (RainBeanUtils.regCondition(clazz)) {
			String beanName = RainBeanUtils.getBeanNames(clazz);
			beanName = RainBeanUtils.getBeanDefinitionsName(beanName, clazz);

			RainConfigUtils.registerRainConfig(register, beanName,
					new LocalAnnotatedGenericBeanDefinition(clazz, local));
		}
	}

	private String getClazzName(String clazzName) {
		clazzName = StringUtils.removeEnd(clazzName, ".class");
		clazzName = StringUtils.replaceChars(clazzName, '/', '.');
		return clazzName;
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
