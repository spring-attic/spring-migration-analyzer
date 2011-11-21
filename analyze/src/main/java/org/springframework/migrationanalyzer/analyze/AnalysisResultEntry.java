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

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

/**
 * An <code>AnalysisResultEntry</code> is an entry in an <code>AnalysisResult</code>.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 * 
 */
public final class AnalysisResultEntry<T> {

    private final FileSystemEntry fileSystemEntry;

    private final T result;

    /**
     * Creates a new <code>AnalysisResultEntry</code> with the given <code>result</code> that is associated with the
     * given <code>fileSystemEntry</code>.
     * 
     * @param fileSystemEntry The <code>FileSystemEntry</code> for the result
     * @param result The result
     */
    public AnalysisResultEntry(FileSystemEntry fileSystemEntry, T result) {
        this.fileSystemEntry = fileSystemEntry;
        this.result = result;
    }

    /**
     * Returns the result's file system entry
     * 
     * @return The result's file system entry
     */
    public FileSystemEntry getFileSystemEntry() {
        return this.fileSystemEntry;
    }

    /**
     * Returns the result
     * 
     * @return the result
     */
    public T getResult() {
        return this.result;
    }

    /**
     * Returns the type of the result
     * 
     * @return the result's type
     */
    public Class<?> getResultType() {
        return this.result.getClass();
    }
}
