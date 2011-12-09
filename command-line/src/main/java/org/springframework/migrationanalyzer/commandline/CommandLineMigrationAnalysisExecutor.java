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

package org.springframework.migrationanalyzer.commandline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemFactory;
import org.springframework.migrationanalyzer.analyze.fs.support.DirectoryFileSystemFactory;
import org.springframework.migrationanalyzer.analyze.support.AnalysisEngineFactory;
import org.springframework.migrationanalyzer.analyze.support.StandardAnalysisEngineFactory;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.render.support.RenderEngineFactory;
import org.springframework.migrationanalyzer.render.support.StandardRenderEngineFactory;

final class CommandLineMigrationAnalysisExecutor implements MigrationAnalysisExecutor {

    private static final String[] DEFAULT_EXCLUDES = new String[0];

    private static final String DEFAULT_OUTPUT_PATH = ".";

    private static final String DEFAULT_OUTPUT_TYPE = "html";

    private final AnalysisEngineFactory analysisEngineFactory;

    private final RenderEngineFactory renderEngineFactory;

    private final FileSystemFactory fileSystemFactory;

    private final ArchiveDiscoverer archiveDiscoverer;

    private final String inputPath;

    private final String outputPath;

    private final String outputType;

    private final String[] excludes;

    CommandLineMigrationAnalysisExecutor(String inputPath, String outputType, String outputDirectory, String[] excludes) {
        this(inputPath, outputType, outputDirectory, excludes, new StandardAnalysisEngineFactory(), new StandardRenderEngineFactory(),
            new DirectoryFileSystemFactory(), new ZipArchiveDiscoverer());
    }

    CommandLineMigrationAnalysisExecutor(String inputPath, String outputType, String outputDirectory, String[] excludes,
        AnalysisEngineFactory analysisEngineFactory, RenderEngineFactory renderEngineFactory, FileSystemFactory fileSystemFactory,
        ArchiveDiscoverer archiveDiscoverer) {
        this.analysisEngineFactory = analysisEngineFactory;
        this.renderEngineFactory = renderEngineFactory;
        this.fileSystemFactory = fileSystemFactory;
        this.archiveDiscoverer = archiveDiscoverer;
        this.inputPath = inputPath;
        this.outputType = outputType == null ? DEFAULT_OUTPUT_TYPE : outputType;
        this.outputPath = outputDirectory == null ? DEFAULT_OUTPUT_PATH : outputDirectory;
        this.excludes = excludes == null ? DEFAULT_EXCLUDES : excludes;
    }

    @Override
    public void execute() {
        File inputFile = new File(this.inputPath);

        List<File> discoveredArchives = this.archiveDiscoverer.discover(inputFile);

        for (File discoveredArchive : discoveredArchives) {
            analyzeArchive(discoveredArchive, inputFile);
        }
    }

    private void analyzeArchive(File archive, File inputFile) {
        FileSystem fileSystem = createFileSystem(archive);
        AnalysisEngine analysisEngine = this.analysisEngineFactory.createAnalysisEngine(fileSystem, this.excludes);
        RenderEngine renderEngine = this.renderEngineFactory.create(this.outputType, getOutputPath(inputFile, archive));

        AnalysisResult analysis = analysisEngine.analyze();
        renderEngine.render(analysis);
        fileSystem.cleanup();
    }

    private String getOutputPath(File inputFile, File archive) {
        if (inputFile.equals(archive)) {
            return new File(this.outputPath, archive.getName()).getAbsolutePath();
        } else {
            return new File(this.outputPath, inputFile.toURI().relativize(archive.toURI()).getPath()).getAbsolutePath();
        }
    }

    private FileSystem createFileSystem(File archive) {
        FileSystem fileSystem;

        try {
            fileSystem = this.fileSystemFactory.createFileSystem(archive);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Failed to create FileSystem for archive '%s'", archive), e);
        }
        return fileSystem;
    }
}
