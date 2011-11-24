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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisEngineFactory;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.render.support.RenderEngineFactory;

public class CommandLineMigrationAnalysisExecutorTests {

    @Test
    public void execute() {
        StubAnalysisEngineFactory analysisEngineFactory = new StubAnalysisEngineFactory();
        StubRenderEngineFactory renderEngineFactory = new StubRenderEngineFactory();

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("input", new String[] { "type" }, "output",
            new String[0], analysisEngineFactory, renderEngineFactory, new StubFileSystemFactory());
        executor.execute();

        assertEquals(1, analysisEngineFactory.analysisEngines.size());
        assertEquals(1, analysisEngineFactory.analysisEngines.get(0).analysisPerformed);

        assertEquals(1, renderEngineFactory.renderEngines.size());
        assertEquals(1, renderEngineFactory.renderEngines.get(0).rendersPerformed);
    }

    @Test
    public void handlingOfNullExcludes() {
        StubAnalysisEngineFactory analysisEngineFactory = new StubAnalysisEngineFactory();
        StubRenderEngineFactory renderEngineFactory = new StubRenderEngineFactory();

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("input", new String[] { "type" }, "output", null,
            analysisEngineFactory, renderEngineFactory, new StubFileSystemFactory());

        executor.execute();

        String[] excludes = analysisEngineFactory.getExcludes();
        assertNotNull(excludes);
        assertEquals(0, excludes.length);
    }

    @Test
    public void handlingOfNullOutputPath() {
        StubAnalysisEngineFactory analysisEngineFactory = new StubAnalysisEngineFactory();
        StubRenderEngineFactory renderEngineFactory = new StubRenderEngineFactory();

        CommandLineMigrationAnalysisExecutor executor = new CommandLineMigrationAnalysisExecutor("input", new String[] { "type" }, null,
            new String[0], analysisEngineFactory, renderEngineFactory, new StubFileSystemFactory());

        executor.execute();

        assertEquals(".", renderEngineFactory.getOutputPath());
    }

    private static final class StubAnalysisEngineFactory implements AnalysisEngineFactory {

        private final List<StubAnalysisEngine> analysisEngines = new ArrayList<StubAnalysisEngine>();

        private volatile String[] excludes;

        @Override
        public AnalysisEngine createAnalysisEngine(FileSystem fileSystem, String[] excludes) {
            this.excludes = excludes;

            StubAnalysisEngine analysisEngine = new StubAnalysisEngine();
            this.analysisEngines.add(analysisEngine);
            return analysisEngine;
        }

        String[] getExcludes() {
            return this.excludes;
        }
    }

    private static final class StubAnalysisEngine implements AnalysisEngine {

        private int analysisPerformed = 0;

        @Override
        public AnalysisResult analyze() {
            this.analysisPerformed++;
            return new StubAnalysisResult();
        }
    }

    private static final class StubAnalysisResult implements AnalysisResult {

        @Override
        public <T> Set<AnalysisResultEntry<T>> getResultEntries(Class<T> type) {
            return null;
        }

        @Override
        public Set<FileSystemEntry> getFileSystemEntries() {
            return null;
        }

        @Override
        public AnalysisResult getResultForEntry(FileSystemEntry fileSystemEntry) {
            return null;
        }

        @Override
        public Set<Class<?>> getResultTypes() {
            return null;
        }
    }

    private static final class StubRenderEngineFactory implements RenderEngineFactory {

        private final List<StubRenderEngine> renderEngines = new ArrayList<StubRenderEngine>();

        private volatile String outputPath;

        @Override
        public RenderEngine create(String outputType, String outputPath) {
            this.outputPath = outputPath;

            StubRenderEngine renderEngine = new StubRenderEngine();
            this.renderEngines.add(renderEngine);
            return renderEngine;
        }

        String getOutputPath() {
            return this.outputPath;
        }

    }

    private static final class StubRenderEngine implements RenderEngine {

        private int rendersPerformed = 0;

        @Override
        public void render(AnalysisResult analysis) {
            this.rendersPerformed++;
        }
    }
}
