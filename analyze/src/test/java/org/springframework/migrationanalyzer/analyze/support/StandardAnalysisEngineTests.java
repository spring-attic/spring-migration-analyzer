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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

@SuppressWarnings("rawtypes")
public class StandardAnalysisEngineTests {

    private final FileSystem fileSystem = mock(FileSystem.class);

    private final FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);

    private final MutableAnalysisResultFactory mutableAnalysisResultFactory = mock(MutableAnalysisResultFactory.class);

    public StandardAnalysisEngineTests() {
        when(this.fileSystemEntry.getName()).thenReturn("stub-entry");
        when(this.fileSystem.iterator()).thenReturn(Arrays.asList(this.fileSystemEntry).iterator());
    }

    @Test
    public void nullResult() throws AnalysisFailedException {
        EntryAnalyzer analyzer1 = mock(EntryAnalyzer.class);
        EntryAnalyzer analyzer2 = mock(EntryAnalyzer.class);

        Set<EntryAnalyzer> analyzers = new HashSet<EntryAnalyzer>();
        analyzers.add(analyzer1);
        analyzers.add(analyzer2);

        StandardAnalysisEngine analysisEngine = new StandardAnalysisEngine(analyzers, this.mutableAnalysisResultFactory);
        analysisEngine.analyze(this.fileSystem, new String[0], "archive.war");

        verify(analyzer1).analyze(this.fileSystemEntry);
        verify(analyzer2).analyze(this.fileSystemEntry);
    }

    @Test
    public void analyze() throws AnalysisFailedException {
        EntryAnalyzer analyzer1 = mock(EntryAnalyzer.class);
        EntryAnalyzer analyzer2 = mock(EntryAnalyzer.class);

        Set<EntryAnalyzer> analyzers = new HashSet<EntryAnalyzer>();
        analyzers.add(analyzer1);
        analyzers.add(analyzer2);

        StandardAnalysisEngine analysisEngine = new StandardAnalysisEngine(analyzers, this.mutableAnalysisResultFactory);
        analysisEngine.analyze(this.fileSystem, new String[0], "archive.war");

        verify(analyzer1).analyze(this.fileSystemEntry);
        verify(analyzer2).analyze(this.fileSystemEntry);
    }

    @Test
    public void analyzeWithExclusions() {
        EntryAnalyzer analyzer1 = mock(EntryAnalyzer.class);
        EntryAnalyzer analyzer2 = mock(EntryAnalyzer.class);

        Set<EntryAnalyzer> analyzers = new HashSet<EntryAnalyzer>();
        analyzers.add(analyzer1);
        analyzers.add(analyzer2);

        StandardAnalysisEngine analysisEngine = new StandardAnalysisEngine(analyzers, this.mutableAnalysisResultFactory);
        analysisEngine.analyze(this.fileSystem, new String[] { "stub-entry" }, "archive.war");

        verifyNoMoreInteractions(analyzer1);
        verifyNoMoreInteractions(analyzer2);
    }
}
