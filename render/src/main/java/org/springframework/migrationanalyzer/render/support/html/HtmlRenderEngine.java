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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.stereotype.Component;

@Component
final class HtmlRenderEngine implements RenderEngine {

    private static final String OUTPUT_TYPE = "html";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HtmlIndexRenderer indexRenderer;

    private final HtmlFileSystemEntryRenderer fileSystemEntryRenderer;

    private final HtmlSummaryRenderer summaryRenderer;

    private final HtmlResultTypeRenderer resultTypeRenderer;

    private final OutputPathGenerator outputPathGenerator;

    private final OutputFactory outputFactory;

    @Autowired
    HtmlRenderEngine(HtmlIndexRenderer indexRenderer, HtmlFileSystemEntryRenderer fileSystemEntryRenderer, HtmlSummaryRenderer summaryRenderer,
        HtmlResultTypeRenderer resultTypeRenderer, OutputPathGenerator outputPathGenerator, OutputFactory outputFactory) {
        this.indexRenderer = indexRenderer;
        this.fileSystemEntryRenderer = fileSystemEntryRenderer;
        this.summaryRenderer = summaryRenderer;
        this.resultTypeRenderer = resultTypeRenderer;
        this.outputPathGenerator = outputPathGenerator;
        this.outputFactory = outputFactory;
    }

    @Override
    public boolean canRender(String outputType) {
        return OUTPUT_TYPE.equals(outputType);
    }

    @Override
    public void render(AnalysisResult analysisResult, String outputPath) {
        this.logger.info("Starting HTML rendering");

        copyStaticResources(analysisResult.getArchiveName());

        this.indexRenderer.renderIndex(analysisResult);
        this.summaryRenderer.renderSummary(analysisResult);
        this.resultTypeRenderer.renderResultTypes(analysisResult);
        this.fileSystemEntryRenderer.renderFileSystemEntries(analysisResult);

        this.logger.info("Finished HTML rendering");
    }

    private void copyStaticResources(String archiveName) {
        copyResource("css/style.css", archiveName);
        copyBinaryResource("img/ModHdr_BG.png", archiveName);
        copyBinaryResource("img/hdr-background.png", archiveName);
        copyBinaryResource("img/hdr-glow.png", archiveName);
        copyBinaryResource("img/springsource-logo.png", archiveName);
        copyBinaryResource("img/title-background.png", archiveName);
        copyResource("js/script.js", archiveName);
        copyResource("banner.html", archiveName);
    }

    private void copyBinaryResource(String resource, String archiveName) {
        InputStream input = getClass().getResourceAsStream(resource);
        OutputStream output = this.outputFactory.createOutputStream(this.outputPathGenerator.generatePathFor(resource), archiveName);
        try {
            IoUtils.copy(input, output);
        } catch (IOException ioe) {
            throw new ResourceCopyFailedException(ioe);
        } finally {
            IoUtils.closeQuietly(input, output);
        }
    }

    private void copyResource(String resource, String archiveName) {
        Reader input = new InputStreamReader(getClass().getResourceAsStream(resource));
        Writer output = this.outputFactory.createWriter(this.outputPathGenerator.generatePathFor(resource), archiveName);
        try {
            IoUtils.copy(input, output);
        } catch (IOException ioe) {
            throw new ResourceCopyFailedException(ioe);
        } finally {
            IoUtils.closeQuietly(input, output);
        }
    }

    private static final class ResourceCopyFailedException extends RuntimeException {

        private static final long serialVersionUID = -2118345575691657768L;

        private ResourceCopyFailedException(Throwable cause) {
            super(cause);
        }
    }
}
