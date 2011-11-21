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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.StubFileSystem;

@SuppressWarnings("rawtypes")
public class StandardAnalysisEngineTests {

    @Test
    public void nullResult() {
        Set<EntryAnalyzer> analyzers = new HashSet<EntryAnalyzer>();
        StubEntryAnalyzer analyzer1 = new StubEntryAnalyzer();
        StubEntryAnalyzer analyzer2 = new StubEntryAnalyzer();
        analyzers.add(analyzer1);
        analyzers.add(analyzer2);
        StubFileSystem fileSystem = new StubFileSystem();

        StandardAnalysisEngine analysisEngine = new StandardAnalysisEngine(analyzers, new StubMutableAnalysisResultFactory(), fileSystem,
            new String[0]);
        analysisEngine.analyze();
        assertTrue(analyzer1.getCalled());
        assertTrue(analyzer2.getCalled());
    }

    @Test
    public void analyze() {
        Set<EntryAnalyzer> analyzers = new HashSet<EntryAnalyzer>();
        StubEntryAnalyzer analyzer1 = new StubEntryAnalyzer(new Object());
        StubEntryAnalyzer analyzer2 = new StubEntryAnalyzer();
        analyzers.add(analyzer1);
        analyzers.add(analyzer2);
        StubFileSystem fileSystem = new StubFileSystem();
        StandardAnalysisEngine analysisEngine = new StandardAnalysisEngine(analyzers, new StubMutableAnalysisResultFactory(), fileSystem,
            new String[0]);
        analysisEngine.analyze();
        assertTrue(analyzer1.getCalled());
        assertTrue(analyzer2.getCalled());
    }

    @Test
    public void analyzeWithExclusions() {
        Set<EntryAnalyzer> analyzers = new HashSet<EntryAnalyzer>();
        StubEntryAnalyzer analyzer1 = new StubEntryAnalyzer(new Object());
        StubEntryAnalyzer analyzer2 = new StubEntryAnalyzer();
        analyzers.add(analyzer1);
        analyzers.add(analyzer2);
        StubFileSystem fileSystem = new StubFileSystem();
        StandardAnalysisEngine analysisEngine = new StandardAnalysisEngine(analyzers, new StubMutableAnalysisResultFactory(), fileSystem,
            new String[] { "stub-entry" });
        analysisEngine.analyze();
        assertFalse(analyzer1.getCalled());
        assertFalse(analyzer2.getCalled());
    }
}
