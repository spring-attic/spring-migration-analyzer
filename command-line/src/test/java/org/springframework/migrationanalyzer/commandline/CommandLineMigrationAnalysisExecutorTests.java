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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemFactory;
import org.springframework.migrationanalyzer.analyze.support.AnalysisEngineFactory;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.render.support.RenderEngineFactory;

public final class CommandLineMigrationAnalysisExecutorTests {

    private static final String OUTPUT_PATH = "output";

    private static final String REPORT_TYPE = "html";

    private final AnalysisEngineFactory analysisEngineFactory = mock(AnalysisEngineFactory.class);

    private final AnalysisEngine analysisEngine = mock(AnalysisEngine.class);

    private final RenderEngineFactory renderEngineFactory = mock(RenderEngineFactory.class);

    private final RenderEngine renderEngine = mock(RenderEngine.class);

    private final FileSystemFactory fileSystemFactory = mock(FileSystemFactory.class);

    private final FileSystem fileSystem = mock(FileSystem.class);

    private final ArchiveDiscoverer archiveDiscoverer = mock(ArchiveDiscoverer.class);

    private final AnalysisResult analysisResult = mock(AnalysisResult.class);

    @Test
    public void execute() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);

        File archive = new File("alpha.ear");

        configureBehaviour(outputLocation, archive, archive);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("alpha.ear", REPORT_TYPE, OUTPUT_PATH,
            new String[0], this.analysisEngineFactory, this.renderEngineFactory, this.fileSystemFactory, this.archiveDiscoverer);
        executor.execute();

        verifyBehaviour(outputLocation, archive, archive);
    }

    @Test
    public void executeWithDefaultExcludes() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);

        File archive = new File("alpha.ear");

        configureBehaviour(outputLocation, archive, archive);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("alpha.ear", REPORT_TYPE, OUTPUT_PATH, null,
            this.analysisEngineFactory, this.renderEngineFactory, this.fileSystemFactory, this.archiveDiscoverer);
        executor.execute();

        verifyBehaviour(outputLocation, archive, archive);
    }

    @Test
    public void executeWithDefaultOutputPath() throws IOException {
        File outputLocation = new File(".");

        File archive = new File("alpha.ear");

        configureBehaviour(outputLocation, archive, archive);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("alpha.ear", REPORT_TYPE, null, new String[0],
            this.analysisEngineFactory, this.renderEngineFactory, this.fileSystemFactory, this.archiveDiscoverer);
        executor.execute();

        verifyBehaviour(outputLocation, archive, archive);
    }

    @Test
    public void executeWithDefaultReportType() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);

        File archive = new File("alpha.ear");

        configureBehaviour(outputLocation, archive, archive);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("alpha.ear", null, OUTPUT_PATH, new String[0],
            this.analysisEngineFactory, this.renderEngineFactory, this.fileSystemFactory, this.archiveDiscoverer);
        executor.execute();

        verifyBehaviour(outputLocation, archive, archive);
    }

    @Test
    public void handlingOfMultipleArchives() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);
        File inputLocation = new File("my-apps");

        File archive1 = new File(inputLocation, "alpha.ear");
        File archive2 = new File(new File(inputLocation, "bravo"), "charlie.war");

        configureBehaviour(new File(OUTPUT_PATH), new File("my-apps"), archive1, archive2);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("my-apps", REPORT_TYPE, OUTPUT_PATH, new String[0],
            this.analysisEngineFactory, this.renderEngineFactory, this.fileSystemFactory, this.archiveDiscoverer);
        executor.execute();

        verifyBehaviour(outputLocation, inputLocation, archive1, archive2);
    }

    private void configureBehaviour(File outputLocation, File inputLocation, File... archives) throws IOException {

        when(this.archiveDiscoverer.discover(inputLocation)).thenReturn(Arrays.asList(archives));

        for (File archive : archives) {
            when(this.fileSystemFactory.createFileSystem(archive)).thenReturn(this.fileSystem);
            when(this.renderEngineFactory.create(REPORT_TYPE, getOutputPath(inputLocation, outputLocation, archive))).thenReturn(this.renderEngine);
        }

        when(this.analysisEngineFactory.createAnalysisEngine(this.fileSystem, new String[0])).thenReturn(this.analysisEngine);
        when(this.analysisEngine.analyze()).thenReturn(this.analysisResult);
    }

    private void verifyBehaviour(File outputLocation, File inputLocation, File... archives) {
        verify(this.analysisEngineFactory, times(archives.length)).createAnalysisEngine(this.fileSystem, new String[0]);
        verify(this.analysisEngine, times(archives.length)).analyze();

        for (File archive : archives) {
            verify(this.renderEngineFactory).create(REPORT_TYPE, getOutputPath(inputLocation, outputLocation, archive));
        }

        verify(this.renderEngine, times(archives.length)).render(this.analysisResult);
    }

    private String getOutputPath(File inputLocation, File outputLocation, File archive) {
        if (inputLocation.equals(archive)) {
            return new File(outputLocation, inputLocation.getPath()).getAbsolutePath();
        } else {
            return new File(outputLocation, archive.getPath().substring(inputLocation.getName().length())).getAbsolutePath();
        }
    }
}
