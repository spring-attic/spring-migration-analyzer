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
import java.util.ArrayList;
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

    private final AnalysisEngineFactory analysisEngineFactory;

    private final RenderEngineFactory renderEngineFactory;

    private final FileSystemFactory fileSystemFactory;

    private final String inputPath;

    private final String outputPath;

    private final String[] outputTypes;

    private final String[] excludes;

    private static final String[] DEFAULT_EXCLUDES = new String[0];

    private static final String DEFAULT_OUTPUT_PATH = ".";

    CommandLineMigrationAnalysisExecutor(String inputPath, String[] outputTypes, String outputPath, String[] excludes) {
        this(inputPath, outputTypes, outputPath, excludes, new StandardAnalysisEngineFactory(), new StandardRenderEngineFactory(),
            new DirectoryFileSystemFactory());
    }

    CommandLineMigrationAnalysisExecutor(String inputPath, String[] outputTypes, String outputPath, String[] excludes,
        AnalysisEngineFactory analysisEngineFactory, RenderEngineFactory renderEngineFactory, FileSystemFactory fileSystemFactory) {
        this.analysisEngineFactory = analysisEngineFactory;
        this.renderEngineFactory = renderEngineFactory;
        this.fileSystemFactory = fileSystemFactory;
        this.inputPath = inputPath;
        this.outputTypes = outputTypes;
        this.outputPath = outputPath == null ? DEFAULT_OUTPUT_PATH : outputPath;
        this.excludes = excludes == null ? DEFAULT_EXCLUDES : excludes;
    }

    @Override
    public void execute() {
        FileSystem fileSystem = createFileSystem();
        AnalysisEngine analysisEngine = this.analysisEngineFactory.createAnalysisEngine(fileSystem, this.excludes);
        List<RenderEngine> renderEngines = new ArrayList<RenderEngine>();
        for (String outputType : this.outputTypes) {
            RenderEngine renderEngine = this.renderEngineFactory.create(outputType, this.outputPath);
            if (renderEngine != null) {
                renderEngines.add(renderEngine);
            }
        }

        AnalysisResult analysis = analysisEngine.analyze();
        for (RenderEngine renderEngine : renderEngines) {
            renderEngine.render(analysis);
        }

        fileSystem.cleanup();
    }

    private FileSystem createFileSystem() {
        FileSystem fileSystem;

        File inputFile = new File(this.inputPath);
        try {
            fileSystem = this.fileSystemFactory.createFileSystem(inputFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Failed to create FileSystem for input '" + inputFile.getAbsolutePath() + "'"), e);
        }
        return fileSystem;
    }
}
