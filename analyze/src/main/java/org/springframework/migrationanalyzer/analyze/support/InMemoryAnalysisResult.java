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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

final class InMemoryAnalysisResult implements MutableAnalysisResult {

    private static final String FORMAT_NAME = "migration-analysis-%s";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<Class<?>, Set<AnalysisResultEntry<?>>> resultsByType = new HashMap<Class<?>, Set<AnalysisResultEntry<?>>>();

    private final Map<FileSystemEntry, Set<AnalysisResultEntry<?>>> resultsByFileSystemEntry = new HashMap<FileSystemEntry, Set<AnalysisResultEntry<?>>>();

    private final String name;

    InMemoryAnalysisResult(String archiveName) {
        this.name = String.format(FORMAT_NAME, archiveName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<AnalysisResultEntry<T>> getResultEntries(Class<T> type) {
        Set<AnalysisResultEntry<?>> resultsForType = getResultsForType(type, false);

        Set<AnalysisResultEntry<T>> resultEntries = new HashSet<AnalysisResultEntry<T>>();
        if (resultsForType != null) {
            for (AnalysisResultEntry<?> result : resultsForType) {
                resultEntries.add((AnalysisResultEntry<T>) result);
            }
        }

        return resultEntries;
    }

    @Override
    public void add(AnalysisResultEntry<?> entry) {
        getResultsForType(entry.getResultType(), true).add(entry);
        getResultsForFileSystemEntry(entry.getFileSystemEntry(), true).add(entry);
        this.logger.debug("Result of type '{}' added for '{}'", new Object[] { entry.getResultType(), entry.getFileSystemEntry() });
    }

    private <T> Set<AnalysisResultEntry<?>> getResultsForType(Class<?> type, boolean createIfAbsent) {
        Set<AnalysisResultEntry<?>> resultsForType = this.resultsByType.get(type);
        if ((resultsForType == null) && createIfAbsent) {
            resultsForType = new HashSet<AnalysisResultEntry<?>>();
            this.resultsByType.put(type, resultsForType);
        }
        return resultsForType;
    }

    private Set<AnalysisResultEntry<?>> getResultsForFileSystemEntry(FileSystemEntry entry, boolean createIfAbsent) {
        Set<AnalysisResultEntry<?>> resultsForEntry = this.resultsByFileSystemEntry.get(entry);
        if ((resultsForEntry == null) && createIfAbsent) {
            resultsForEntry = new HashSet<AnalysisResultEntry<?>>();
            this.resultsByFileSystemEntry.put(entry, resultsForEntry);
        }
        return resultsForEntry;
    }

    @Override
    public Set<FileSystemEntry> getFileSystemEntries() {
        return this.resultsByFileSystemEntry.keySet();
    }

    @Override
    public AnalysisResult getResultForEntry(FileSystemEntry fileSystemEntry) {
        Set<AnalysisResultEntry<?>> resultsForFileSystemEntry = getResultsForFileSystemEntry(fileSystemEntry, false);
        InMemoryAnalysisResult result = new InMemoryAnalysisResult(this.name);

        if (resultsForFileSystemEntry != null) {
            for (AnalysisResultEntry<?> analysisResultEntry : resultsForFileSystemEntry) {
                result.add(analysisResultEntry);
            }
        }

        return result;
    }

    @Override
    public Set<Class<?>> getResultTypes() {
        return this.resultsByType.keySet();
    }

    @Override
    public String toString() {
        return String.format("%s result types, %s file system entries", this.resultsByType.size(), this.resultsByFileSystemEntry.size());
    }

    @Override
    public String getName() {
        return this.name;
    }

}
