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

package org.springframework.migrationanalyzer.render.support.html;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

/**
 * Generates the path to an output file relative to a root location
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface RootAwareOutputPathGenerator extends OutputPathGenerator {

    /**
     * Generate the relative path for a path
     * 
     * @param path The path to generate the path for
     * @return The relative path
     */
    String generateRelativePathToRootFor(String path);

    /**
     * Generate the relative path for a {@link FileSystemEntry}
     * 
     * @param fileSystemEntry The entry to generate the path for
     * @return The relative path
     */
    String generateRelativePathToRootFor(FileSystemEntry fileSystemEntry);

    /**
     * Generate the relative path for a given type
     * 
     * @param resultType The type to generate the path for
     * @return The relative path
     */
    String generateRelativePathToRootFor(Class<?> resultType);
}
