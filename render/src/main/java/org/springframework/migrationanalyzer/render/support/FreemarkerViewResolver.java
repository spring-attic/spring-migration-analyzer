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

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.migrationanalyzer.analyze.util.StandardClassPathScanner;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * A view resolver that creates {@link FreemarkerView}s for a given output type
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 * 
 * @see FreemarkerView
 */
public final class FreemarkerViewResolver implements ViewResolver {

    private static final String VIEW_PATTERN_FORMAT = ".*%s-(.*)\\.ftl";

    private final String outputType;

    private final Configuration configuration;

    private final Map<String, String> templatePathCache = new HashMap<String, String>();

    /**
     * Creates a new instance of this resolver that resolves views for a specified type of output
     * 
     * @param outputType The type of output to resolve views for
     */
    public FreemarkerViewResolver(String outputType) {
        this(outputType, //
            new StandardClassPathScanner().findResources(Pattern.compile(String.format(VIEW_PATTERN_FORMAT, outputType)), //
                (URLClassLoader) Thread.currentThread().getContextClassLoader()));
    }

    FreemarkerViewResolver(String outputType, Set<String> templatePaths) {
        this.outputType = outputType;
        this.configuration = new Configuration();
        this.configuration.setClassForTemplateLoading(this.getClass(), "/");
        this.configuration.setObjectWrapper(new DefaultObjectWrapper());

        Pattern viewPattern = Pattern.compile(String.format(VIEW_PATTERN_FORMAT, outputType));
        for (String templatePath : templatePaths) {
            Matcher matcher = viewPattern.matcher(templatePath);
            if (matcher.find()) {
                this.templatePathCache.put(matcher.group(1), templatePath);
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
                "Unable to find template for '%s'.  Make sure you have a Freemarker template named %s-%s.ftl on the classpath", viewName,
                this.outputType, viewName), e);
        }
    }

}
