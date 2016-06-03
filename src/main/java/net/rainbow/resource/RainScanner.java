package net.rainbow.resource;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.rainbow.web.ref.ResourceRef;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

public class RainScanner {

	private static SoftReference<RainScanner> softReference;

	public synchronized static RainScanner getInstance() {
		if (softReference == null || softReference.get() == null) {
			RainScanner yxScanner = new RainScanner();
			softReference = new SoftReference<RainScanner>(yxScanner);

		}
		return softReference.get();
	}

	protected Log logger = LogFactory.getLog(getClass());

	protected Date createTime = new Date();

	protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private List<ResourceRef> classesFolderResources;

	private List<ResourceRef> jarResources;

	private RainScanner() {}

	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 扫描 lib的相关目录
	 * 
	 * @param resourceLoader
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<ResourceRef> getClassesFolderResources() throws IOException {
		if (classesFolderResources == null) {
			if (logger.isInfoEnabled()) {
				logger.info("[classesFolder] start to found available classes folders ...");
			}
			List<ResourceRef> classesFolderResources = new ArrayList<ResourceRef>();
			
			Resource[] metaInfResources = resourcePatternResolver.getResources("classpath*:/");
			for (Resource metaInfResource : metaInfResources) {
				URL urlObject = metaInfResource.getURL();
				if (!"file".equals(urlObject.getProtocol())) {
					if (logger.isDebugEnabled()) {
						logger
								.debug("[classesFolder] Ignored classes folder because "
										+ "not a file protocol url: "
										+ urlObject);
					}
					continue;
				}
				String path = urlObject.getPath();
				Assert.isTrue(path.endsWith("/"));
				if (!path.endsWith("/classes/") && !path.endsWith("/bin/")) {
					continue;
				}
				File file;
				try {
					file = new File(urlObject.toURI());
				} catch (URISyntaxException e) {
					throw new IOException(e);
				}
				if (file.isFile()) {
					if (logger.isDebugEnabled()) {
						logger.debug("[classesFolder] Ignored because not a directory: "
										+ urlObject);
					}
					continue;
				}
				Resource resource = new FileSystemResource(file);
				ResourceRef resourceRef = ResourceRef.toResourceRef(resource);
				if (!classesFolderResources.contains(resourceRef)) {
					classesFolderResources.add(resourceRef);

				}
			}
			Collections.sort(classesFolderResources);
			this.classesFolderResources = new ArrayList<ResourceRef>(
					classesFolderResources);
			if (logger.isInfoEnabled()) {
				logger.info("[classesFolder] found "+ classesFolderResources.size() + " classes folders: "+ classesFolderResources);
			}
		} 
		return classesFolderResources;
	}

	/**
	 * 灏嗚琚壂鎻忕殑jar璧勬簮
	 * 
	 * @param resourceLoader
	 * @return
	 * @throws IOException
	 */
	public List<ResourceRef> getJarResources() throws IOException {
		if (jarResources == null) {
			if (logger.isInfoEnabled()) {
				logger.info("[jarFile] start to found available jar files for rainbow to scanning...");
			}
			List<ResourceRef> jarResources = new ArrayList<ResourceRef>();
			Resource[] metaInfResources = resourcePatternResolver.getResources("classpath*:/META-INF");
			for (Resource metaInfResource : metaInfResources) {
				URL urlObject = metaInfResource.getURL();
				if (ResourceUtils.isJarURL(urlObject)) {
					try {
						String path = URLDecoder.decode(urlObject.getPath(),
								"UTF-8"); // fix 20%
						if (path.startsWith("file:")) {
							path = path.substring("file:".length(), path
									.lastIndexOf('!'));
						} else {
							path = path.substring(0, path.lastIndexOf('!'));
						}
						Resource resource = new FileSystemResource(path);
						if (!jarResources.contains(resource)) {
							ResourceRef ref = ResourceRef
									.toResourceRef(resource);
							if (ref.getModifiers()) {
								jarResources.add(ref);
							}
						}

					} catch (Exception e) {
						logger.error(urlObject, e);
					}
				}
			}
			this.jarResources = jarResources;
			if (logger.isInfoEnabled()) {
				logger.info("[jarFile] found " + jarResources.size()
						+ " jar files: " + jarResources);
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[jarFile] found cached " + jarResources.size()
						+ " jar files: " + jarResources);
			}
		}
		return jarResources;
	}

}
