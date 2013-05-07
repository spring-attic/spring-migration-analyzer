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
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;
import org.springframework.migrationanalyzer.render.support.source.SourceAccessor;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("rawtypes")
final class StandardHtmlFileSystemEntryRenderer implements HtmlFileSystemEntryRenderer {

    private static final String REPORT_TYPE = "html";

    private static final String VIEW_NAME_BY_FILE_HEADER = "html-by-file-header";

    private static final String VIEW_NAME_BY_FILE_SOURCE = "html-by-file-source";

    private static final String VIEW_NAME_BY_FILE_FOOTER = "html-by-file-footer";

    private static final String VIEW_NAME_FILE_CONTENTS = "html-file-contents";

    private final RootAwareOutputPathGenerator outputPathGenerator;

    private final SourceAccessor sourceAccessor;

    private final Set<ByFileSystemEntryController> fileSystemEntryControllers;

    private final ViewRenderer viewRenderer;

    private final OutputFactory outputFactory;

    @Autowired
    StandardHtmlFileSystemEntryRenderer(Set<ByFileSystemEntryController> fileSystemEntryControllers, ViewRenderer viewRenderer,
        RootAwareOutputPathGenerator outputPathGenerator, OutputFactory outputFactory, SourceAccessor sourceAccessor) {
        this.fileSystemEntryControllers = fileSystemEntryControllers;
        this.viewRenderer = viewRenderer;
        this.outputPathGenerator = outputPathGenerator;
        this.outputFactory = outputFactory;
        this.sourceAccessor = sourceAccessor;
    }

    @Override
    public void renderFileSystemEntries(AnalysisResult analysisResult, String outputPathPrefix) {
        Set<FileSystemEntry> fileSystemEntries = analysisResult.getFileSystemEntries();

        renderFileSystemEntryContents(fileSystemEntries, outputPathPrefix);

        for (FileSystemEntry fileSystemEntry : fileSystemEntries) {
            Writer writer = null;
            try {
                writer = this.outputFactory.createWriter(this.outputPathGenerator.generatePathFor(fileSystemEntry), outputPathPrefix);
                AnalysisResult entryResult = analysisResult.getResultForEntry(fileSystemEntry);
                renderByFileHeader(fileSystemEntry, writer);
                for (Class<?> resultType : entryResult.getResultTypes()) {
                    this.viewRenderer.render(resultType, entryResult.getResultEntries(resultType), this.fileSystemEntryControllers, writer,
                        new LocationAwareOutputPathGenerator(this.outputPathGenerator, fileSystemEntry), REPORT_TYPE);
                }
                renderByFileSource(fileSystemEntry, writer);
                renderByFileFooter(writer);
            } finally {
                IoUtils.closeQuietly(writer);
            }
        }
    }

    private void renderFileSystemEntryContents(Set<FileSystemEntry> fileSystemEntries, String archiveName) {
        String contentsPath = this.outputPathGenerator.generatePathForFileSystemEntryContents();
        OutputPathGenerator locationAwarePathGenerator = new LocationAwareOutputPathGenerator(this.outputPathGenerator, contentsPath);

        Map<String, Object> model = createContentsModel(fileSystemEntries, locationAwarePathGenerator);

        Writer writer = null;

        try {
            writer = this.outputFactory.createWriter(contentsPath, archiveName);
            this.viewRenderer.renderViewWithModel(VIEW_NAME_FILE_CONTENTS, model, writer);
        } finally {
            IoUtils.closeQuietly(writer);
        }
    }

    private Map<String, Object> createContentsModel(Set<FileSystemEntry> fileSystemEntries, OutputPathGenerator locationAwarePathGenerator) {
        Map<String, Set<String>> entryUrls = new TreeMap<String, Set<String>>();
        for (FileSystemEntry fileSystemEntry : fileSystemEntries) {
            String baseName = getBaseName(fileSystemEntry.getName());
            if (baseName != null) {
                Set<String> urls = entryUrls.get(baseName);
                if (urls == null) {
                    urls = new TreeSet<String>();
                    entryUrls.put(baseName, urls);
                }
                urls.add(locationAwarePathGenerator.generatePathFor(fileSystemEntry));
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("entryUrls", entryUrls);
        return model;
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
            model.put("source", sourceCode);
            return model;
        }

        return null;
    }

    private void renderByFileHeader(FileSystemEntry fileSystemEntry, Writer writer) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fileName", fileSystemEntry.getName());
        model.put("pathToRoot", this.outputPathGenerator.generatePathRelativeToRootFor(fileSystemEntry));
        this.viewRenderer.renderViewWithModel(VIEW_NAME_BY_FILE_HEADER, model, writer);
    }

    private void renderByFileFooter(Writer writer) {
        this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_BY_FILE_FOOTER, writer);
    }

    private String getBaseName(String path) {
        if (path != null) {
            return path.substring(path.lastIndexOf('/') + 1);
        }
        return null;
    }
}
