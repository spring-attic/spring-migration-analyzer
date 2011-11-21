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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.deploymentdescriptors.DeploymentDescriptor;

public class JBossServiceXmlDetectingEntryAnalyzerTests {

    private final EntryAnalyzer<DeploymentDescriptor> entryAnalyzer = new JBossServiceXmlDetectingEntryAnalyzer();

    @Test
    public void jBossServiceXmlIsDetected() throws AnalysisFailedException {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(new StubFileSystemEntry("jboss-service.xml"));
        assertNotNull(analyze);
        assertEquals(1, analyze.size());
    }

    @Test
    public void otherServiceXmlWithCorrectContentAreDetected() throws AnalysisFailedException {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(new StubFileSystemEntry("another-service.xml"));
        assertNotNull(analyze);
        assertEquals(1, analyze.size());
    }

    @Test
    public void filesWithMatchingNameButIncorrectContentAreNotDetected() throws AnalysisFailedException {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(new StubFileSystemEntry("something-else-service.xml"));
        assertNotNull(analyze);
        assertEquals(0, analyze.size());
    }

    @Test
    public void nonServiceXmlProducesEmptySet() throws AnalysisFailedException {
        Set<DeploymentDescriptor> analyze = this.entryAnalyzer.analyze(new StubFileSystemEntry("/something-else.xml"));
        assertNotNull(analyze);
        assertEquals(0, analyze.size());
    }

    private static final class StubFileSystemEntry implements FileSystemEntry {

        private final String name;

        public StubFileSystemEntry(String name) {
            this.name = name;
        }

        @Override
        public InputStream getInputStream() {
            return getClass().getResourceAsStream(this.name);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Reader getReader() {
            return new InputStreamReader(getInputStream());
        }

        @Override
        public boolean isDirectory() {
            return false;
        }
    }
}
