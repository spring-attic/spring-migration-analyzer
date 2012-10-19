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

import org.springframework.migrationanalyzer.analyze.fs.FileSystem;

/**
 * An <code>AnalysisEngine</code> is used to analyze an application and produce an <code>AnalysisResult</code>.
 * 
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations need not be thread-safe.
 * 
 */
public interface AnalysisEngine {

    /**
     * Requests the engine to perform an analysis of the application contained in the given <code>FileSystem</code>,
     * returning an <code>AnalysisResult</code>. Entries in the application matched by the given Ant path pattern-style
     * <code>excludes</code> will be excluded from the analysis
     * 
     * @param fileSystem The file system containing the application to analyze
     * @param excludes Entries to exclude from the analysis in Ant path pattern format
     * @param archiveName The name of the archive that is being analyzed
     * @return the result of the analysis
     */
    AnalysisResult analyze(FileSystem fileSystem, String[] excludes, String archiveName);

}
