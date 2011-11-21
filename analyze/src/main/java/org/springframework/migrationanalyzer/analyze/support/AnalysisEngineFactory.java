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

import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;

/**
 * An <code>AnalysisEngineFactory</code> is used to create <code>AnalysisEngine</code>s.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations need not be thread-safe.
 * 
 */
public interface AnalysisEngineFactory {

    /**
     * Creates a new <code>AnalysisEngine</code> that can be used to analyze the application contained in the given
     * <code>FileSystem</code>. Entries in the application covered in the given Ant path pattern-style
     * <code>excludes</code> will be excluded from the analysis
     * 
     * @param FileSystem The file system containing the application to analyze
     * @param excludes Entries to exclude from the analysis in Ant path pattern format
     * @return an appropriately-configured <code>AnalysisEngine</code>
     */
    AnalysisEngine createAnalysisEngine(FileSystem fileSystem, String[] excludes);
}
