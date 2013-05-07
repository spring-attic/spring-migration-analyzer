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

package org.springframework.migrationanalyzer.render.support.html;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.support.ResultTypeDisplayNameResolver;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
final class StandardHtmlResultTypeRenderer implements HtmlResultTypeRenderer {

    private static final String REPORT_TYPE = "html";

    private static final String VIEW_NAME_BY_RESULT_HEADER = "html-by-result-header";

    private static final String VIEW_NAME_BY_RESULT_FOOTER = "html-by-result-footer";

    private static final String VIEW_NAME_RESULT_CONTENTS = "html-result-contents";

    private final RootAwareOutputPathGenerator outputPathGenerator;

    private final Set<ByResultTypeController> resultTypeControllers;

    private final OutputFactory outputFactory;

    private final ViewRenderer viewRenderer;

    private final ResultTypeDisplayNameResolver resultTypeDisplayNameResolver;

    @Autowired
    StandardHtmlResultTypeRenderer(Set<ByResultTypeController> resultTypeControllers, ViewRenderer viewRenderer,
        RootAwareOutputPathGenerator outputPathGenerator, OutputFactory outputFactory, ResultTypeDisplayNameResolver resultTypeDisplayNameResolver) {
        this.resultTypeControllers = resultTypeControllers;
        this.viewRenderer = viewRenderer;
        this.outputPathGenerator = outputPathGenerator;
        this.outputFactory = outputFactory;
        this.resultTypeDisplayNameResolver = resultTypeDisplayNameResolver;
    }

    @Override
    public void renderResultTypes(AnalysisResult analysisResult, String outputPathPrefix) {
        Set<Class<?>> resultTypes = analysisResult.getResultTypes();
        renderResultTypeContents(resultTypes, outputPathPrefix);
        for (Class<?> resultType : resultTypes) {
            Writer writer = null;
            try {
                writer = this.outputFactory.createWriter(this.outputPathGenerator.generatePathFor(resultType), outputPathPrefix);
                renderByResultTypeHeader(resultType, writer);
                this.viewRenderer.render(resultType, analysisResult.getResultEntries(resultType), this.resultTypeControllers, writer,
                    new LocationAwareOutputPathGenerator(this.outputPathGenerator, resultType), REPORT_TYPE);
                renderByResultTypeFooter(writer);
            } finally {
                IoUtils.closeQuietly(writer);
            }
        }
    }

    private void renderResultTypeContents(Set<Class<?>> resultTypes, String archiveName) {
        Writer writer = null;
        try {
            String contentsPath = this.outputPathGenerator.generatePathForResultTypeContents();
            OutputPathGenerator locationAwarePathGenerator = new LocationAwareOutputPathGenerator(this.outputPathGenerator, contentsPath);
            writer = this.outputFactory.createWriter(contentsPath, archiveName);
            Map<String, Object> model = createContentsModel(resultTypes, locationAwarePathGenerator);
            this.viewRenderer.renderViewWithModel(VIEW_NAME_RESULT_CONTENTS, model, writer);
        } finally {
            IoUtils.closeQuietly(writer);
        }
    }

    private Map<String, Object> createContentsModel(Set<Class<?>> resultTypes, OutputPathGenerator locationAwarePathGenerator) {
        Map<String, String> resultUrls = new TreeMap<String, String>();
        for (Class<?> resultType : resultTypes) {
            resultUrls.put(getDisplayName(resultType), locationAwarePathGenerator.generatePathFor(resultType));
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("resultUrls", resultUrls);
        return model;
    }

    private void renderByResultTypeHeader(Class<?> resultType, Writer writer) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("resultType", getDisplayName(resultType));
        model.put("pathToRoot", this.outputPathGenerator.generatePathRelativeToRootFor(resultType));

        this.viewRenderer.renderViewWithModel(VIEW_NAME_BY_RESULT_HEADER, model, writer);
    }

    private void renderByResultTypeFooter(Writer writer) {
        this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_BY_RESULT_FOOTER, writer);
    }

    private String getDisplayName(Class<?> resultType) {
        return this.resultTypeDisplayNameResolver.getDisplayName(resultType);
    }
}
