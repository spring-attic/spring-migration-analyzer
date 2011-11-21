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

package org.springframework.migrationanalyzer.analyze.support;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.StubFileSystem;
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@SuppressWarnings("rawtypes")
public class StandardAnalysisEngineFactoryTests {

    @Test
    public void create() {
        Set<Class<? extends EntryAnalyzer>> analyzerClasses = new HashSet<Class<? extends EntryAnalyzer>>();
        analyzerClasses.add(StubEntryAnalyzer.class);

        AnalysisEngine analysisEngine = new StandardAnalysisEngineFactory(new StubMutableAnalysisResultFactory(), analyzerClasses).createAnalysisEngine(
            new StubFileSystem(), new String[0]);
        assertTrue(analysisEngine instanceof StandardAnalysisEngine);
    }

    @Test
    public void createBadAnalyzer() {
        Set<Class<? extends EntryAnalyzer>> analyzerClasses = new HashSet<Class<? extends EntryAnalyzer>>();
        analyzerClasses.add(StubEntryAnalyzer2.class);

        AnalysisEngine analysisEngine = new StandardAnalysisEngineFactory(new StubMutableAnalysisResultFactory(), analyzerClasses).createAnalysisEngine(
            new StubFileSystem(), new String[0]);
        assertTrue(analysisEngine instanceof StandardAnalysisEngine);
    }

    @IgnoredByClassPathScan
    private static class StubEntryAnalyzer2 implements EntryAnalyzer<Object> {

        @SuppressWarnings("unused")
        private StubEntryAnalyzer2(String test) {
        }

        @Override
        public Set<Object> analyze(FileSystemEntry fileSystemEntry) {
            return null;
        }

    }
}
