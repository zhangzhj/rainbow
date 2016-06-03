package net.rainbow.resource.vfs;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;
public class FileSystemManager {

    protected Log logger = LogFactory.getLog(FileSystemManager.class);

    private boolean traceEnabled = logger.isTraceEnabled();

    @SuppressWarnings("unchecked")
    private Map<String, FileObject> cached = new LRUMap(10000);

    public FileObject resolveFile(String urlString) throws IOException {
        if (traceEnabled) {
            logger.trace("[fs] resolveFile ... by urlString '" + urlString + "'");
        }
        FileObject object = cached.get(urlString);
        if (object == null && !urlString.endsWith("/")) {
            object = cached.get(urlString + "/");
        }
        if (object != null) {
            if (traceEnabled) {
                logger.trace("[fs][s] found cached file for urlString '" + urlString + "'");
            }
            return object;
        }
        // not found in cache, resolves it!
        return resolveFile(new URL(urlString));
    }

    public synchronized FileObject resolveFile(URL url) throws IOException {
        try {
            if (traceEnabled) {
                logger.trace("[fs] resolveFile ... by url '" + url + "'");
            }
            String urlString = url.toString();
            FileObject object = cached.get(urlString);

            if (object != null) {
                if (traceEnabled) {
                    logger.trace("[fs] found cached file for url '" + urlString + "'");
                }
                return object;
            }
            if (ResourceUtils.isJarURL(url)) {
                if (!urlString.endsWith("/")) {
                    object = resolveFile(urlString + "/");
                }
                if (object == null || !object.exists()) {
                    object = new JarFileObject(this, url);
                    if (traceEnabled) {
                        logger.trace("[fs] create jarFileObject for '" + urlString + "'");
                    }
                }
            } else {
                File file = ResourceUtils.getFile(url);
                if (file.isDirectory()) {
                    if (!urlString.endsWith("/")) {
                        urlString = urlString + "/";
                        url = new URL(urlString);
                    }
                } else if (file.isFile()) {
                    if (urlString.endsWith("/")) {
                        urlString = StringUtils.removeEnd(urlString, "/");
                        url = new URL(urlString);
                    }
                }
                object = new SimpleFileObject(this, url);
                if (traceEnabled) {
                    logger.trace("[fs] create simpleFileObject for '" + urlString + "'");
                }
            }
            if (object.exists()) {
                cached.put(urlString, object);
            }
            return object;
        } catch (IOException e) {
            logger.error(e.getMessage() + ":" + url, e);
            throw e;
        }
    }

    public synchronized void clearCache() {
        cached.clear();
    }

}
