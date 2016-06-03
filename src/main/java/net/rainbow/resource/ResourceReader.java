/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.rainbow.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.rainbow.web.ref.ResourceRef;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceReader {

    protected static Log logger = LogFactory.getLog(ResourceReader.class);


    public static List<Resource> findContextResources() throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("[applicationContext] start to found applicationContext files ...");
        }
        if (logger.isDebugEnabled()) {
                logger.debug("[applicationContext] use scope: class.* base.yx.*.jar");
                logger.debug("[applicationContext] call 'findFiles'");
        }
        List<ResourceRef> resources = RainScanner.getInstance().getClassesFolderResources();
        if (logger.isDebugEnabled()) {
            logger.debug("[applicationContext] exits from 'findFiles'");
            logger.debug("[applicationContext] it has " + resources.size()
                    + " classes folders or jar files " + "in the applicationContext scope: "
                    + resources);

            logger.debug("[applicationContext] iterates the 'findFiles'"
                    + " classes folders or jar files; size=" + resources.size());
        }

        List<Resource> ctxResources = new LinkedList<Resource>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        int index = 0;
        for (ResourceRef ref : resources) {
            index++;
            Resource[] founds = ref.getInnerResources(resourcePatternResolver,"applicationContext*.xml");
            List<Resource> asList = Arrays.asList(founds);
            ctxResources.addAll(asList);
            if (logger.isDebugEnabled()) {
                logger.debug("[applicationContext] found applicationContext resources ("
                        + index + "/" + resources.size() + ": " + asList);
            }
            
        }
        if (logger.isInfoEnabled()) {
            logger.info("[applicationContext] FOUND " + ctxResources.size()
                    + " applcationContext files: " + ctxResources);
        }
        return ctxResources;
    }
}
