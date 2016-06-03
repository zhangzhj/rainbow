package net.rainbow.web.ref;

import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
/**
 * 其中Resource主要对资源进行反解析,主要生成Ref，同时过滤需要解析的jar
 * 
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public class ResourceRef implements Comparable<ResourceRef> {

    private static final Log logger = LogFactory.getLog(ResourceRef.class);

    private Resource resource;

    private boolean modifiers;

    public static ResourceRef toResourceRef(Resource folder) throws IOException {
        ResourceRef rr = new ResourceRef(folder);
        boolean modifiers = false;
        if (!"jar".equals(rr.getProtocol())) {
            if (logger.isDebugEnabled()) {
                logger.debug("modifiers by default[" + rr.getResource().getURI() + "]");
            }
        } else {
            JarFile jarFile = new JarFile(rr.getResource().getFile());
            Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
            	String r = manifest.getMainAttributes().getValue("Rainbow");
                if (!StringUtils.isBlank(r)) {
                	modifiers = true;
                }
            }
        }
        rr.setModifiers(modifiers);
        return rr;
    }

    public ResourceRef(Resource resource) {
        setResource(resource);
    }


    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }


    public void setModifiers(boolean modifiers) {
        this.modifiers = modifiers;
    }
    
    
    public boolean getModifiers() {
    	return modifiers;
    }


    public Resource getInnerResource(String subPath) throws IOException {
        Assert.isTrue(!subPath.startsWith("/"));
        String rootPath = resource.getURI().getPath();
        if (getProtocol().equals("jar")) {
            return new UrlResource("jar:file:" + rootPath + "!/" + subPath);
        } else {
            return new FileSystemResource(rootPath + subPath); // 已使用FileSystemResource不用file:打头
        }
    }

    public Resource[] getInnerResources(ResourcePatternResolver resourcePatternResolver,
            String subPath) throws IOException {
        subPath = getInnerResourcePattern(subPath);
        return resourcePatternResolver.getResources(subPath);
    }

    public String getInnerResourcePattern(String subPath) throws IOException {
        Assert.isTrue(!subPath.startsWith("/"), subPath);
        String rootPath = resource.getURI().getPath();
        if (getProtocol().equals("jar")) {
            subPath = "jar:file:" + rootPath + ResourceUtils.JAR_URL_SEPARATOR + subPath;
        } else {
            subPath = "file:" + rootPath + subPath;
        }
        return subPath;
    }

    public String getProtocol() {
        if (resource.getFilename().toLowerCase().endsWith(".jar")
                || resource.getFilename().toLowerCase().endsWith(".zip")
                || resource.getFilename().toLowerCase().endsWith(".tar")
                || resource.getFilename().toLowerCase().endsWith(".gz")) {
            return "jar";
        }
        return "file";
    }

    @Override
    public int compareTo(ResourceRef o) {
        try {
            return this.resource.getURI().compareTo(o.resource.getURI());
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Override
    public int hashCode() {
        return 13 * resource.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (resource == null) return false;
        if (obj instanceof Resource) {
            return resource.equals(obj);
        } else if (obj instanceof ResourceRef) {
            return resource.equals(((ResourceRef) obj).resource);
        }
        return false;
    }

    @Override
    public String toString() {
        try {
            return resource.getURL().getFile() ;
        } catch (IOException e) {
            return resource + Boolean.toString(modifiers);
        }
    }
}
