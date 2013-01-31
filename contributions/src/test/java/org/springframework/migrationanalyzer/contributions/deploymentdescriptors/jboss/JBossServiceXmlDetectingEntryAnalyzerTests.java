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

package org.springframework.migrationanalyzer.contributions.deploymentdescriptors.jboss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Set;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.Callback;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.deploymentdescriptors.DeploymentDescriptor;
import org.springframework.migrationanalyzer.util.IoUtils;

public class JBossServiceXmlDetectingEntryAnalyzerTests {

    private final EntryAnalyzer<DeploymentDescriptor> entryAnalyzer = new JBossServiceXmlDetectingEntryAnalyzer();

    @Test
    public void jBossServiceXmlIsDetected() throws Exception {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(createFileSystemEntry("jboss-service.xml"));
        assertNotNull(analyze);
        assertEquals(1, analyze.size());
    }

    @Test
    public void otherServiceXmlWithCorrectContentAreDetected() throws Exception {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(createFileSystemEntry("another-service.xml"));
        assertNotNull(analyze);
        assertEquals(1, analyze.size());
    }

    @Test
    public void filesWithMatchingNameButIncorrectContentAreNotDetected() throws Exception {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(createFileSystemEntry("something-else-service.xml"));
        assertNotNull(analyze);
        assertEquals(0, analyze.size());
    }

    @Test
    public void nonServiceXmlProducesEmptySet() throws Exception {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(createFileSystemEntry("/something-else.xml"));
        assertNotNull(analyze);
        assertEquals(0, analyze.size());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private FileSystemEntry createFileSystemEntry(final String name) {
        FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);
        when(fileSystemEntry.getName()).thenReturn(name);

        when(fileSystemEntry.doWithInputStream(any(Callback.class))).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                InputStream in = getClass().getResourceAsStream(name);
                try {
                    return ((Callback) invocation.getArguments()[0]).perform(in);
                } finally {
                    IoUtils.closeQuietly(in);
                }
            }
        });

        return fileSystemEntry;
    }
}
