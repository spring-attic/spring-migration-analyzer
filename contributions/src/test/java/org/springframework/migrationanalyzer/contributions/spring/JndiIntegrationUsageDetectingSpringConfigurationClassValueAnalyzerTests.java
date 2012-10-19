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

package org.springframework.migrationanalyzer.contributions.spring;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

public final class JndiIntegrationUsageDetectingSpringConfigurationClassValueAnalyzerTests {

    private final JndiIntegrationUsageDetectingSpringConfigurationClassValueAnalyzer analyzer = new JndiIntegrationUsageDetectingSpringConfigurationClassValueAnalyzer();

    @Test
    public void analyzeClassWithNameStartingWithOrgSpringFrameworkJndi() {
        SpringJndiIntegration result = this.analyzer.analyze("org.springframework.jndi.Alpha", createFileSystemEntry("entry-name"));
        assertNotNull(result);
    }

    @Test
    public void analyzeNonJndiPackageClass() {
        SpringJndiIntegration result = this.analyzer.analyze("org.springframework.beans.Alpha", createFileSystemEntry("entry-name"));
        assertNull(result);
    }

    private FileSystemEntry createFileSystemEntry(String name) {
        FileSystemEntry entry = mock(FileSystemEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }
}
