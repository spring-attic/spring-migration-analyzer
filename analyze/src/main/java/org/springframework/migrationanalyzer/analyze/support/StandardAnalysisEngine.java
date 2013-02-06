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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
final class StandardAnalysisEngine implements AnalysisEngine {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("rawtypes")
    private final Set<EntryAnalyzer> analyzers;

    private final MutableAnalysisResultFactory analysisResultFactory;

    @SuppressWarnings("rawtypes")
    @Autowired
    StandardAnalysisEngine(Set<EntryAnalyzer> analyzers, MutableAnalysisResultFactory analysisResultFactory) {
        this.analyzers = analyzers;
        this.analysisResultFactory = analysisResultFactory;
    }

    @Override
    public AnalysisResult analyze(FileSystem fileSystem, String[] excludes, String archiveName) {
        this.logger.info("Starting analysis");

        MutableAnalysisResult analysisResult = this.analysisResultFactory.create(archiveName);

        for (FileSystemEntry fileSystemEntry : fileSystem) {
            if (!isExcluded(fileSystemEntry, excludes)) {
                for (EntryAnalyzer<?> analyzer : this.analyzers) {
                    this.logger.debug("Analyzing '{}' with '{}'", fileSystemEntry, analyzer);
                    try {
                        recordAnalysis(analyzer.analyze(fileSystemEntry), analysisResult, fileSystemEntry);
                    } catch (Exception e) {
                        this.logger.error("Problem encountered analyzing '{}' with analyzer '{}': {}", fileSystemEntry.getName(), analyzer,
                            e.getMessage());
                    }
                }
            }
        }

        this.logger.info("Analysis complete");

        return analysisResult;
    }

    private boolean isExcluded(FileSystemEntry fileSystemEntry, String[] excludes) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String exclude : excludes) {
            if (pathMatcher.match(exclude, fileSystemEntry.getName())) {
                this.logger.debug("'{}' is excluded as it matches pattern '{}'", fileSystemEntry, exclude);
                return true;
            }
        }

        return false;
    }

    private void recordAnalysis(Set<?> analysisSet, MutableAnalysisResult analysisResult, FileSystemEntry fileSystemEntry) {
        if (analysisResult != null) {
            for (Object analysis : analysisSet) {
                analysisResult.add(new AnalysisResultEntry<Object>(fileSystemEntry, analysis));
            }
        }
    }
}
