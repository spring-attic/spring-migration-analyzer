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

import static org.springframework.migrationanalyzer.analyze.util.InstanceCreator.createInstances;

import java.net.URLClassLoader;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.util.ClassPathScanner;
import org.springframework.migrationanalyzer.analyze.util.StandardClassPathScanner;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.render.SummaryController;
import org.springframework.migrationanalyzer.render.support.FreemarkerViewResolver;
import org.springframework.migrationanalyzer.render.support.RenderEngineSubFactory;
import org.springframework.migrationanalyzer.render.support.source.RawFileSourceAccessor;
import org.springframework.migrationanalyzer.render.support.source.SourceAccessor;

@SuppressWarnings("rawtypes")
final class HtmlRenderEngineSubFactory implements RenderEngineSubFactory {

    private static final ClassPathScanner CLASS_PATH_SCANNER = new StandardClassPathScanner();

    private static final String OUTPUT_TYPE = "html";

    private final Set<ByResultTypeController> resultTypeControllers;

    private final Set<ByFileSystemEntryController> fileSystemEntryControllers;

    private final Set<SummaryController> summaryControllers;

    HtmlRenderEngineSubFactory() {
        this(
            CLASS_PATH_SCANNER.findImplementations(ByResultTypeController.class, (URLClassLoader) Thread.currentThread().getContextClassLoader()),
            CLASS_PATH_SCANNER.findImplementations(ByFileSystemEntryController.class, (URLClassLoader) Thread.currentThread().getContextClassLoader()),
            CLASS_PATH_SCANNER.findImplementations(SummaryController.class, (URLClassLoader) Thread.currentThread().getContextClassLoader()));
    }

    HtmlRenderEngineSubFactory(Set<Class<? extends ByResultTypeController>> resultTypeControllers,
        Set<Class<? extends ByFileSystemEntryController>> fileSystemEntryControllers, Set<Class<? extends SummaryController>> summaryControllers) {
        this.resultTypeControllers = createInstances(resultTypeControllers);
        this.fileSystemEntryControllers = createInstances(fileSystemEntryControllers);
        this.summaryControllers = createInstances(summaryControllers);
    }

    @Override
    public boolean canRender(String outputType) {
        return OUTPUT_TYPE.equals(outputType);
    }

    @Override
    public RenderEngine create(String outputPath) {
        RootAwareOutputPathGenerator outputPathGenerator = new StandardOutputPathGenerator();
        ViewRenderer viewRenderer = new StandardViewRenderer(new FreemarkerViewResolver(OUTPUT_TYPE));
        WriterFactory writerFactory = new FileWriterFactory(outputPath);

        HtmlIndexRenderer indexRenderer = new StandardHtmlIndexRenderer(viewRenderer, outputPathGenerator, writerFactory);

        SourceAccessor sourceAccessor = new RawFileSourceAccessor();

        HtmlFileSystemEntryRenderer fileSystemEntryRenderer = new StandardHtmlFileSystemEntryRenderer(this.fileSystemEntryControllers, viewRenderer,
            outputPathGenerator, writerFactory, sourceAccessor);

        HtmlSummaryRenderer summaryRenderer = new StandardHtmlSummaryRenderer(this.summaryControllers, viewRenderer, outputPathGenerator,
            writerFactory);

        HtmlResultTypeRenderer resultTypeRenderer = new StandardHtmlResultTypeRenderer(this.resultTypeControllers, viewRenderer, outputPathGenerator,
            writerFactory);

        return new HtmlRenderEngine(indexRenderer, fileSystemEntryRenderer, summaryRenderer, resultTypeRenderer, outputPathGenerator, writerFactory);
    }

}
