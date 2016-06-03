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
package net.rainbow.web.impl.view.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

/**
 * 主要对字符串进行的模板进行操作处理
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public class StringVelocityConfigurer extends VelocityConfigurer {

    @Override
    protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
        super.postProcessVelocityEngine(velocityEngine);
        velocityEngine.addProperty("resource.loader", "srl");
        velocityEngine.addProperty("srl.resource.loader.class","net.qdevelop.scanner.resource.QResourceLoader");
        velocityEngine.addProperty("input.encoding", "utf-8");
        velocityEngine.addProperty("output.encoding", "utf-8");
        if (logger.isInfoEnabled()) {
            logger.info("StringResourceLoader added to configured VelocityEngine");
        }
    }
}
