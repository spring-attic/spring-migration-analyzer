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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;
import org.springframework.migrationanalyzer.render.support.source.SourceAccessor;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
final class StandardXmlFileSystemEntryRenderer implements XmlFileSystemEntryRenderer {

    private static final String REPORT_TYPE = "xml";

    private static final String VIEW_NAME_BY_FILE_ENTRY_HEADER = "xml-by-file-entry-header";

    private static final String VIEW_NAME_BY_FILE_ENTRY_FOOTER = "xml-by-file-entry-footer";

    private static final String VIEW_NAME_BY_FILE_FOOTER = "xml-by-file-footer";

    private static final String VIEW_NAME_BY_FILE_HEADER = "xml-by-file-header";

    private static final String VIEW_NAME_BY_FILE_SOURCE = "xml-by-file-source";

    private final Set<ByFileSystemEntryController> fileSystemEntryControllers;

    private final SourceAccessor sourceAccessor;

    private final ViewRenderer viewRenderer;

    @Autowired
    StandardXmlFileSystemEntryRenderer(Set<ByFileSystemEntryController> fileSystemEntryControllers, SourceAccessor sourceAccessor,
        ViewRenderer viewRenderer) {
        this.fileSystemEntryControllers = fileSystemEntryControllers;
        this.sourceAccessor = sourceAccessor;
        this.viewRenderer = viewRenderer;
    }

    @Override
    public void renderFileSystemEntries(AnalysisResult result, Writer writer) {

        this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_BY_FILE_HEADER, writer);

        Set<FileSystemEntry> fileSystemEntries = result.getFileSystemEntries();

        for (FileSystemEntry fileSystemEntry : fileSystemEntries) {
            AnalysisResult entryResult = result.getResultForEntry(fileSystemEntry);
            renderByFileEntryHeader(fileSystemEntry, writer);
            for (Class<?> resultType : entryResult.getResultTypes()) {
                this.viewRenderer.render(resultType, entryResult.getResultEntries(resultType), this.fileSystemEntryControllers, writer, null,
                    REPORT_TYPE);
            }
            renderByFileSource(fileSystemEntry, writer);
            renderByFileEntryFooter(writer);
        }

        this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_BY_FILE_FOOTER, writer);
    }

    private void renderByFileEntryHeader(FileSystemEntry fileSystemEntry, Writer writer) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fileName", fileSystemEntry.getName());
        this.viewRenderer.renderViewWithModel(VIEW_NAME_BY_FILE_ENTRY_HEADER, model, writer);
    }

    private void renderByFileEntryFooter(Writer writer) {
        this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_BY_FILE_ENTRY_FOOTER, writer);
    }

    private void renderByFileSource(FileSystemEntry fileSystemEntry, Writer writer) {
        Map<String, Object> sourceModel = createSourceModel(fileSystemEntry);

        if (sourceModel != null) {
            this.viewRenderer.renderViewWithModel(VIEW_NAME_BY_FILE_SOURCE, sourceModel, writer);
        }
    }

    private Map<String, Object> createSourceModel(FileSystemEntry fileSystemEntry) {
        String sourceCode = this.sourceAccessor.getSource(fileSystemEntry);

        if (sourceCode != null) {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("source", XmlUtils.escape(sourceCode));
            return model;
        }

        return null;
    }
}
