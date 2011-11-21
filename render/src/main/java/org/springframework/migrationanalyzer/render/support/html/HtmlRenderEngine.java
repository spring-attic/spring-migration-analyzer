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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.util.IoUtils;

final class HtmlRenderEngine implements RenderEngine {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HtmlIndexRenderer indexRenderer;

    private final HtmlFileSystemEntryRenderer fileSystemEntryRenderer;

    private final HtmlSummaryRenderer summaryRenderer;

    private final HtmlResultTypeRenderer resultTypeRenderer;

    private final OutputPathGenerator outputPathGenerator;

    private final WriterFactory writerFactory;

    HtmlRenderEngine(HtmlIndexRenderer indexRenderer, HtmlFileSystemEntryRenderer fileSystemEntryRenderer, HtmlSummaryRenderer summaryRenderer,
        HtmlResultTypeRenderer resultTypeRenderer, OutputPathGenerator outputPathGenerator, WriterFactory writerFactory) {
        this.indexRenderer = indexRenderer;
        this.fileSystemEntryRenderer = fileSystemEntryRenderer;
        this.summaryRenderer = summaryRenderer;
        this.resultTypeRenderer = resultTypeRenderer;
        this.outputPathGenerator = outputPathGenerator;
        this.writerFactory = writerFactory;
    }

    @Override
    public void render(AnalysisResult analysisResult) {
        this.logger.info("Starting HTML rendering");

        copyStaticResources();

        this.indexRenderer.renderIndex();
        this.summaryRenderer.renderSummary(analysisResult);
        this.resultTypeRenderer.renderResultTypes(analysisResult);
        this.fileSystemEntryRenderer.renderFileSystemEntries(analysisResult);

        this.logger.info("Finished HTML rendering");
    }

    private void copyStaticResources() {
        copyResource("css/style.css");
        copyResource("img/Carrot_Close.png");
        copyResource("img/Carrot_Open.png");
        copyResource("img/Carrots.png");
        copyResource("img/ModHdr_BG.png");
        copyResource("img/hdr-background.png");
        copyResource("img/hdr-glow.png");
        copyResource("img/springsource-logo.png");
        copyResource("img/title-background.png");
        copyResource("js/script.js");
        copyResource("banner.html");
    }

    private void copyResource(String resource) {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream(resource));
        Writer writer = this.writerFactory.createWriter(this.outputPathGenerator.generatePathFor(resource));
        try {
            IoUtils.copy(reader, writer);
        } catch (IOException ioe) {
            throw new ResourceCopyFailedException(ioe);
        } finally {
            IoUtils.closeQuietly(reader, writer);
        }
    }

    private static final class ResourceCopyFailedException extends RuntimeException {

        private static final long serialVersionUID = -2118345575691657768L;

        private ResourceCopyFailedException(Throwable cause) {
            super(cause);
        }
    }
}
