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

import java.util.Set;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

/**
 * An <code>Analyzer</code> is used to perform migration analysis of an individual entry.
 */
public interface EntryAnalyzer<T> {

    /**
     * Performs analysis of the given <code>fileSystemEntry</code>.
     * 
     * @param fileSystemEntry The <code>FileSystemEntry</code> to analyze.
     * @throws AnalysisFailedException if a failure occurs during analysis of the entry
     */
    Set<T> analyze(FileSystemEntry fileSystemEntry) throws AnalysisFailedException;
}
