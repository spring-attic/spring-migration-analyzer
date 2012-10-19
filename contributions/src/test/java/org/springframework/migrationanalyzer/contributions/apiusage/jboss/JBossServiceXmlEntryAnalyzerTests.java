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

package org.springframework.migrationanalyzer.contributions.apiusage.jboss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsage;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsageDetector;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsageType;
import org.springframework.migrationanalyzer.render.MigrationCost;

public class JBossServiceXmlEntryAnalyzerTests {

    private final EntryAnalyzer<ApiUsage> entryAnalyzer = new JBossServiceXmlEntryAnalyzer(new StubApiUsageDetector());

    @Test
    public void mBeanCodeClassDetection() throws AnalysisFailedException {
        FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);
        when(fileSystemEntry.getName()).thenReturn("jboss-service.xml");
        when(fileSystemEntry.getInputStream()).thenReturn(getClass().getResourceAsStream("jboss-service.xml"));

        Set<ApiUsage> analysis = this.entryAnalyzer.analyze(fileSystemEntry);

        assertNotNull(analysis);
        assertEquals(6, analysis.size());
    }

    private static final class StubApiUsageDetector implements ApiUsageDetector {

        @Override
        public ApiUsage detectApiUsage(String className, ApiUsageType usageType, String user, String usageDescription) {
            return new ApiUsage(usageType, "Test", user, usageDescription, "view", MigrationCost.MEDIUM);
        }
    }
}
