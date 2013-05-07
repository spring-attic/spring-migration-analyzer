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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemFactory;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.stereotype.Component;

@Component
final class CommandLineMigrationAnalysisExecutor implements MigrationAnalysisExecutor {

    private final Logger logger = LoggerFactory.getLogger(CommandLineMigrationAnalysisExecutor.class);

    private final AnalysisEngine analysisEngine;

    private final Set<RenderEngine> renderEngines;

    private final FileSystemFactory fileSystemFactory;

    private final ArchiveDiscoverer archiveDiscoverer;

    private final Configuration configuration;

    @Autowired
    CommandLineMigrationAnalysisExecutor(AnalysisEngine analysisEngine, Set<RenderEngine> renderEngines, FileSystemFactory fileSystemFactory,
        ArchiveDiscoverer archiveDiscoverer, Configuration configuration) {
        this.analysisEngine = analysisEngine;
        this.renderEngines = renderEngines;
        this.fileSystemFactory = fileSystemFactory;
        this.archiveDiscoverer = archiveDiscoverer;
        this.configuration = configuration;
    }

    @Override
    public void execute() {
        File inputFile = new File(this.configuration.getInputPath());

        if (!inputFile.exists()) {
            this.logger.error("The input path '{}' does not exist.", inputFile);
        } else {
            List<File> discoveredArchives = this.archiveDiscoverer.discover(inputFile);

            for (int i = 0; i < discoveredArchives.size(); i++) {
                this.logger.info("Processing archive {} of {}", i + 1, discoveredArchives.size());
                analyzeArchive(discoveredArchives.get(i), inputFile);
            }
        }
    }

    private void analyzeArchive(File archive, File inputFile) {
        FileSystem fileSystem = createFileSystem(archive);
        AnalysisResult analysis = this.analysisEngine.analyze(fileSystem, this.configuration.getExcludes(), archive.getName());

        for (String outputType : this.configuration.getOutputTypes()) {
            RenderEngine renderEngine = getRenderEngine(outputType);
            renderEngine.render(analysis, getOutputPath(inputFile, archive, outputType));
        }
        fileSystem.cleanup();
    }

    private RenderEngine getRenderEngine(String outputType) {
        for (RenderEngine renderEngine : this.renderEngines) {
            if (renderEngine.canRender(outputType)) {
                return renderEngine;
            }
        }

        throw new IllegalArgumentException(String.format("No rendering engine for report type '%s' is available", outputType));
    }

    private String getOutputPath(File inputFile, File archive, String outputType) {
        File root;

        if (inputFile.equals(archive)) {
            root = new File(this.configuration.getOutputPath());
        } else {
            root = new File(this.configuration.getOutputPath(), archive.getParentFile().getPath().substring(inputFile.getPath().length()));
        }

        String directoryName = archive.getName() + ".migration-analysis";
        return new File(new File(root, directoryName), outputType).getAbsolutePath();
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
