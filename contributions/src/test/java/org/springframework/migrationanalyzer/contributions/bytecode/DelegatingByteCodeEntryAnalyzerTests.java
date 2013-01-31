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

package org.springframework.migrationanalyzer.contributions.bytecode;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.ExceptionCallback;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.util.IoUtils;

public class DelegatingByteCodeEntryAnalyzerTests {

    private final ResultGatheringClassVisitorFactory factory = mock(ResultGatheringClassVisitorFactory.class);

    private final FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);

    @SuppressWarnings("unchecked")
    private final ResultGatheringClassVisitor<Object> visitor = mock(ResultGatheringClassVisitor.class);

    public DelegatingByteCodeEntryAnalyzerTests() {
        when(this.factory.create()).thenReturn(this.visitor);
    }

    private final DelegatingByteCodeEntryAnalyzer analyzer = new DelegatingByteCodeEntryAnalyzer(this.factory);

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void analyze() throws Exception {
        when(this.fileSystemEntry.getName()).thenReturn("Test.class");
        when(this.fileSystemEntry.doWithInputStream(any(ExceptionCallback.class))).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                InputStream input = getClass().getResourceAsStream(DelegatingByteCodeEntryAnalyzerTests.class.getSimpleName() + ".class");
                try {
                    return ((ExceptionCallback) invocation.getArguments()[0]).perform(input);
                } finally {
                    IoUtils.closeQuietly(input);
                }
            }
        });
        this.analyzer.analyze(this.fileSystemEntry);
        verify(this.visitor).getResults();
    }

    @Test
    public void analyzeDirectory() throws AnalysisFailedException {
        when(this.fileSystemEntry.isDirectory()).thenReturn(true);
        this.analyzer.analyze(this.fileSystemEntry);
        verifyNoMoreInteractions(this.visitor);
    }
}
