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

package org.springframework.migrationanalyzer.analyze;

import java.util.Set;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

/**
 * An <code>AnalysisResult</code> is produced by an <code>AnalysisEngine</code>.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations need not be thread-safe
 * 
 */
public interface AnalysisResult {

    /**
     * Returns the name of the result, typically derived from the name of the archive that was analyzed to produce this
     * result
     * 
     * @return The name of the result
     */
    String getName();

    /**
     * Returns a <code>Set</code> of results of the given <code>type</code>.
     * 
     * @param <T> The type of the results
     * @param type The <code>Class</code> of the result type
     * @return The results of the requested type
     */
    <T> Set<AnalysisResultEntry<T>> getResultEntries(Class<T> type);

    /**
     * Returns a <code>Set</code> of all of the result types in the result
     * 
     * @return the <code>Set</code> of result types
     */
    Set<Class<?>> getResultTypes();

    /**
     * Returns a <code>Set</code> of all of the <code>FileSystemEntry</code> instances in the result
     * 
     * @return the <code>Set</code> of <code>FileSystemEntry</code> instances
     */
    Set<FileSystemEntry> getFileSystemEntries();

    /**
     * Returns a portion of the overall <code>AnalysisResult</code> that only includes information for the given
     * <code>fileSystemEntry</code>.
     * 
     * @param fileSystemEntry The file system entry for which a result is required
     * @return An <code>AnalysisResult</code> that only contains results for the given file system entry
     */
    AnalysisResult getResultForEntry(FileSystemEntry fileSystemEntry);

}