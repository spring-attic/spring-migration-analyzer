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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

public class InMemoryAnalysisResultTests {

    private final InMemoryAnalysisResult result = new InMemoryAnalysisResult();

    @Test
    public void getEntryTypeDoesNotExist() {
        assertTrue(this.result.getResultEntries(Object.class).isEmpty());
    }

    @Test
    public void add() {
        this.result.add(new AnalysisResultEntry<Object>(null, new Object()));
        assertFalse(this.result.getResultEntries(Object.class).isEmpty());
    }

    @Test
    public void getResultTypesWhenEmpty() {
        assertTrue(this.result.getResultTypes().isEmpty());
    }

    @Test
    public void getFileSystemEntriesWhenEmpty() {
        assertTrue(this.result.getFileSystemEntries().isEmpty());
    }

    @Test
    public void getResultTypes() {
        this.result.add(new AnalysisResultEntry<Object>(null, new Object()));
        Set<Class<?>> resultTypes = this.result.getResultTypes();
        assertEquals(1, resultTypes.size());
        assertEquals(Object.class, resultTypes.iterator().next());
    }

    @Test
    public void getFileSystemEntries() {
        this.result.add(new AnalysisResultEntry<Object>(null, new Object()));
        Set<FileSystemEntry> fileSystemEntries = this.result.getFileSystemEntries();
        assertEquals(1, fileSystemEntries.size());
        assertNull(fileSystemEntries.iterator().next());
    }

    @Test
    public void getResultForAbsentFileSystemEntry() {
        AnalysisResult analysisResult = this.result.getResultForEntry(null);
        assertNotNull(analysisResult);
        assertTrue(analysisResult.getResultTypes().isEmpty());
    }

    @Test
    public void getResultForFileSystemEntry() {
        this.result.add(new AnalysisResultEntry<Object>(null, new Object()));
        AnalysisResult analysisResult = this.result.getResultForEntry(null);
        assertNotNull(analysisResult);
        Set<Class<?>> resultTypes = analysisResult.getResultTypes();
        assertEquals(1, resultTypes.size());
        assertEquals(Object.class, resultTypes.iterator().next());
    }
}
