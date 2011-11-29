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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AnalysisEngineFactory analysisEngineFactory;

    private final RenderEngineFactory renderEngineFactory;

    private final FileSystemFactory fileSystemFactory;

    private final String inputPath;

    private final String outputPath;

    private final String[] outputTypes;

    private final String[] excludes;

    private static final String[] DEFAULT_EXCLUDES = new String[0];

    private static final String DEFAULT_OUTPUT_PATH = ".";

    private final String[] allowedScanExtenstions = { "zip", "war", "ear", "rar" };

    private File inputFile;

    private File outputParentDir;

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
        this.inputFile = new File(this.inputPath);

        if (!this.inputFile.exists()) {
            this.logger.error("{} file doesn't exist", this.inputFile);
            return;
        }

        if (this.inputFile.isDirectory()) {
            List<FileNameStub> filesDetected = new ArrayList<FileNameStub>();

            scanFilesRecursive(this.inputFile, filesDetected);

            this.logger.info("========={} Archive files Detected==========", filesDetected.size());
            for (FileNameStub fileStub : filesDetected) {
                this.logger.info("{}", fileStub.getRelativePath());
            }
            this.logger.info("========================================");

            this.outputParentDir = new File(this.outputPath);
            if (!this.outputParentDir.exists()) {
                this.outputParentDir.mkdir();
            }
            for (FileNameStub fileStub : filesDetected) {
                String outputFileName = new File(this.outputParentDir, fileStub.getRelativePath()).getAbsolutePath();
                outputFileName = outputFileName + "-results";
                String printName = (fileStub.getRelativePath().length() == 0 ? fileStub.getAbsolutePath() : fileStub.getRelativePath());
                this.logger.debug("Analyzing Input file: {} to Output file: {}", printName, outputFileName);
                this.logger.info("Analyzing {}", printName);
                executeFile(fileStub.getAbsolutePath(), outputFileName, this.excludes);

            }
        } else {
            executeFile(this.inputPath, this.outputPath, this.excludes);
        }
    }

    private void scanFilesRecursive(File file, List<FileNameStub> filesDetected) {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles(this.filter)) {
                scanFilesRecursive(childFile, filesDetected);
            }
        } else {
            String relative = this.inputFile.toURI().relativize(file.toURI()).getPath();

            filesDetected.add(new FileNameStub(file.getAbsolutePath(), relative));
        }
    }

    private void executeFile(String inputFileName, String outputFileName, String[] excludes) {
        FileSystem fileSystem = createFileSystem(inputFileName);
        AnalysisEngine analysisEngine = this.analysisEngineFactory.createAnalysisEngine(fileSystem, excludes);
        List<RenderEngine> renderEngines = new ArrayList<RenderEngine>();
        for (String outputType : this.outputTypes) {
            RenderEngine renderEngine = this.renderEngineFactory.create(outputType, outputFileName);
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

    private FileSystem createFileSystem(String fileName) {
        FileSystem fileSystem;

        File inputFile = new File(fileName);
        try {
            fileSystem = this.fileSystemFactory.createFileSystem(inputFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Failed to create FileSystem for input '" + inputFile.getAbsolutePath() + "'"), e);
        }
        return fileSystem;
    }

    private final FilenameFilter filter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            for (String extension : CommandLineMigrationAnalysisExecutor.this.allowedScanExtenstions) {
                if (name.endsWith("." + extension)) {
                    return true;
                }
            }

            return new File(dir, name).isDirectory();
        }
    };

    private class FileNameStub {

        private final String absolutePath;

        private final String relativePath;

        public FileNameStub(String absolutePath, String relativePath) {
            this.absolutePath = absolutePath;
            this.relativePath = relativePath;
        }

        public String getAbsolutePath() {
            return this.absolutePath;
        }

        public String getRelativePath() {
            return this.relativePath;
        }

    }
}
