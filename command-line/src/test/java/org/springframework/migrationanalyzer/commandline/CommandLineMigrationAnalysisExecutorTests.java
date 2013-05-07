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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemFactory;
import org.springframework.migrationanalyzer.render.RenderEngine;

public final class CommandLineMigrationAnalysisExecutorTests {

    private static final String INPUT_PATH_ARCHIVE_JAR = "src/test/resources/archives/archive.jar";

    private static final String OUTPUT_PATH = "output";

    private static final String REPORT_TYPE_HTML = "html";

    private static final String REPORT_TYPE_XML = "xml";

    private final AnalysisEngine analysisEngine = mock(AnalysisEngine.class);

    private final RenderEngine renderEngine = mock(RenderEngine.class);

    private final Set<RenderEngine> renderEngines = new HashSet<RenderEngine>(Arrays.asList(this.renderEngine));

    private final FileSystemFactory fileSystemFactory = mock(FileSystemFactory.class);

    private final FileSystem fileSystem = mock(FileSystem.class);

    private final ArchiveDiscoverer archiveDiscoverer = mock(ArchiveDiscoverer.class);

    private final AnalysisResult analysisResult = mock(AnalysisResult.class);

    @Test
    public void execute() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);

        File archive = new File(INPUT_PATH_ARCHIVE_JAR);

        configureBehaviour(archive, REPORT_TYPE_HTML, archive);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor(this.analysisEngine, this.renderEngines,
            this.fileSystemFactory, this.archiveDiscoverer, new Configuration(INPUT_PATH_ARCHIVE_JAR, OUTPUT_PATH, new String[] { REPORT_TYPE_HTML },
                new String[0]));

        executor.execute();

        verifyBehaviour(outputLocation, archive, REPORT_TYPE_HTML, archive);
    }

    @Test
    public void executeWithMultipleArchives() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);
        File inputLocation = new File("src/test/resources/archives");

        File archive1 = new File(inputLocation, "alpha.ear");
        File archive2 = new File(new File(inputLocation, "bravo"), "charlie.war");

        configureBehaviour(inputLocation, REPORT_TYPE_HTML, archive1, archive2);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor(this.analysisEngine, this.renderEngines,
            this.fileSystemFactory, this.archiveDiscoverer, new Configuration(inputLocation.getPath(), OUTPUT_PATH,
                new String[] { REPORT_TYPE_HTML }, new String[0]));

        executor.execute();

        verifyBehaviour(outputLocation, inputLocation, REPORT_TYPE_HTML, archive1, archive2);
    }

    @Test
    public void executeWithMultipleOutputTypes() throws IOException {
        File outputLocation = new File(OUTPUT_PATH);
        File inputLocation = new File("src/test/resources/archives");

        File archive = new File(inputLocation, "alpha.ear");

        configureBehaviour(inputLocation, new String[] { REPORT_TYPE_HTML, REPORT_TYPE_XML }, archive);

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor(this.analysisEngine, this.renderEngines,
            this.fileSystemFactory, this.archiveDiscoverer, new Configuration(inputLocation.getPath(), OUTPUT_PATH, new String[] { REPORT_TYPE_HTML,
                REPORT_TYPE_XML }, new String[0]));

        executor.execute();

        verifyBehaviour(outputLocation, inputLocation, new String[] { REPORT_TYPE_HTML, REPORT_TYPE_XML }, archive);
    }

    @Test
    public void executeWithNonExistentInputPath() {
        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor(this.analysisEngine, this.renderEngines,
            this.fileSystemFactory, this.archiveDiscoverer, new Configuration("does/not/exist", OUTPUT_PATH, new String[] { REPORT_TYPE_HTML },
                new String[0]));
        executor.execute();

        verifyNoMoreInteractions(this.analysisEngine, this.fileSystemFactory, this.archiveDiscoverer);
    }

    private void configureBehaviour(File inputLocation, String outputType, File... archives) throws IOException {
        this.configureBehaviour(inputLocation, new String[] { outputType }, archives);
    }

    private void configureBehaviour(File inputLocation, String[] outputTypes, File... archives) throws IOException {

        when(this.archiveDiscoverer.discover(inputLocation)).thenReturn(Arrays.asList(archives));
        for (String outputType : outputTypes) {
            when(this.renderEngine.canRender(outputType)).thenReturn(true);
        }

        for (File archive : archives) {
            when(this.fileSystemFactory.createFileSystem(archive)).thenReturn(this.fileSystem);
            when(this.analysisEngine.analyze(this.fileSystem, new String[0], archive.getName())).thenReturn(this.analysisResult);
        }

    }

    private void verifyBehaviour(File outputLocation, File inputLocation, String outputType, File... archives) {
        this.verifyBehaviour(outputLocation, inputLocation, new String[] { outputType }, archives);
    }

    private void verifyBehaviour(File outputLocation, File inputLocation, String[] outputTypes, File... archives) {
        for (File archive : archives) {
            verify(this.analysisEngine).analyze(this.fileSystem, new String[0], archive.getName());
            for (String outputType : outputTypes) {
                verify(this.renderEngine).render(this.analysisResult, getOutputPath(inputLocation, outputLocation, archive, outputType));
            }
        }
    }

    private String getOutputPath(File inputLocation, File outputLocation, File archive, String outputType) {
        String outputPath;
        if (inputLocation.equals(archive)) {
            outputPath = new File(new File(outputLocation, archive.getName() + ".migration-analysis"), outputType).getAbsolutePath();

        } else {
            outputPath = new File(new File(new File(outputLocation, archive.getParentFile().getPath().substring(inputLocation.getPath().length())),
                archive.getName() + ".migration-analysis"), outputType).getAbsolutePath();
        }
        return outputPath;
    }
}
