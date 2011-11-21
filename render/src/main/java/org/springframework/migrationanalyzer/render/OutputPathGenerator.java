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

package org.springframework.migrationanalyzer.render;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

/**
 * A utility that can generate the output path for parts of an analysis rendering
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface OutputPathGenerator {

    /**
     * Generates the path for the index page
     * 
     * @return The path
     */
    String generatePathForIndex();

    /**
     * Generates the path to the summary page
     * 
     * @return The path
     */
    String generatePathForSummary();

    /**
     * Generates the path for a result type
     * 
     * @param resultType The result type
     * @return The path
     */
    String generatePathFor(Class<?> resultType);

    /**
     * Generates the path for a {@link FileSystemEntry}
     * 
     * @param fileSystemEntry The file system entry
     * @return The path
     */
    String generatePathFor(FileSystemEntry fileSystemEntry);

    /**
     * Generates the path for a specific file
     * 
     * @param fileName The name of the file
     * @return The path
     */
    String generatePathFor(String fileName);

    /**
     * Generates the path to the contents page of the result types
     * 
     * @return The path
     */
    String generatePathForResultTypeContents();

    /**
     * Generates the path to the contents page of the {@link FileSystemEntry}s
     * 
     * @return The path
     */
    String generatePathForFileSystemEntryContents();
}