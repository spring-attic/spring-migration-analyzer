/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.migrationanalyzer.render.support.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.render.support.RenderingException;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.stereotype.Component;

@Component
final class XmlRenderEngine implements RenderEngine {

    private static final String OUTPUT_TYPE = "xml";

    private static final String VIEW_NAME_FOOTER = "xml-footer";

    private static final String VIEW_NAME_HEADER = "xml-header";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final XmlFileSystemEntryRenderer fileSystemEntryRenderer;

    private final ViewRenderer viewRenderer;

    @Autowired
    XmlRenderEngine(XmlFileSystemEntryRenderer fileSystemEntryRenderer, ViewRenderer viewRenderer) {
        this.fileSystemEntryRenderer = fileSystemEntryRenderer;
        this.viewRenderer = viewRenderer;
    }

    @Override
    public boolean canRender(String outputType) {
        return OUTPUT_TYPE.equals(outputType);
    }

    @Override
    public void render(AnalysisResult analysisResult, String outputPath) {
        this.logger.info("Starting XML rendering. Writing output to '{}'", outputPath);

        File output = new File(outputPath, "migration-analysis.xml");
        Writer writer = null;
        try {
            IoUtils.createDirectoryIfNecessary(output.getParentFile());
            writer = new FileWriter(output);
            this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_HEADER, writer);
            this.fileSystemEntryRenderer.renderFileSystemEntries(analysisResult, writer);
            this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_FOOTER, writer);
        } catch (IOException ioe) {
            throw new RenderingException("Failed to create writer for '" + output.getAbsolutePath() + "'", ioe);
        } finally {
            IoUtils.closeQuietly(writer);
        }

        this.logger.info("Finished XML rendering");
    }

}
