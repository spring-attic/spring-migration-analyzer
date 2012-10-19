/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.migrationanalyzer.render.support;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.util.ClassPathScanner;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

@Component
final class FreemarkerViewResolver implements ViewResolver {

    private static final String VIEW_PATTERN_FORMAT = ".*\\.ftl";

    private final Configuration configuration;

    private final Map<String, String> templatePathCache = new HashMap<String, String>();

    @Autowired
    FreemarkerViewResolver(ClassPathScanner classPathScanner) {
        this(classPathScanner.findResources(Pattern.compile(String.format(VIEW_PATTERN_FORMAT)), //
            (URLClassLoader) Thread.currentThread().getContextClassLoader()));
    }

    FreemarkerViewResolver(Set<String> templatePaths) {
        this.configuration = new Configuration();
        this.configuration.setClassForTemplateLoading(this.getClass(), "/");
        this.configuration.setObjectWrapper(new DefaultObjectWrapper());

        Pattern viewPattern = Pattern.compile(String.format(VIEW_PATTERN_FORMAT));
        for (String templatePath : templatePaths) {
            Matcher matcher = viewPattern.matcher(templatePath);
            if (matcher.find()) {
                String name = new File(templatePath).getName();
                this.templatePathCache.put(name.substring(0, name.length() - ".ftl".length()), templatePath);
            }
        }
    }

    @Override
    public View getView(String viewName) {
        String path = this.templatePathCache.get(viewName);

        if (path == null) {
            return null;
        }

        try {
            return new FreemarkerView(this.configuration.getTemplate(path));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(
                "Unable to find template for '%s'.  Make sure you have a Freemarker template named %s.ftl on the classpath", viewName, viewName), e);
        }
    }

}
