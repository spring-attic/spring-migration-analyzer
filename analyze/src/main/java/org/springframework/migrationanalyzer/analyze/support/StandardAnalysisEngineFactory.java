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

import static org.springframework.migrationanalyzer.analyze.util.InstanceCreator.createInstances;

import java.net.URLClassLoader;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisEngine;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.util.StandardClassPathScanner;

/**
 * Standard implementation of <code>AnalysisEngineFactory</code>.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not guaranteed to be thread-safe
 */
public final class StandardAnalysisEngineFactory implements AnalysisEngineFactory {

    private final MutableAnalysisResultFactory analysisResultFactory;

    @SuppressWarnings("rawtypes")
    private final Set<EntryAnalyzer> analyzers;

    /**
     * Creates a new instance of this class providing defaults for the {@link MutableAnalysisResultFactory},
     * {@link FileSystemFactory}, and analyzer classes.
     * 
     * @see InMemoryAnalysisResultFactory
     * @see DirectoryFileSystemFactory
     * @see EntryAnalyzer
     */
    public StandardAnalysisEngineFactory() {
        this(new InMemoryAnalysisResultFactory(), new StandardClassPathScanner().findImplementations(EntryAnalyzer.class,
            (URLClassLoader) Thread.currentThread().getContextClassLoader()));
    }

    @SuppressWarnings("rawtypes")
    StandardAnalysisEngineFactory(MutableAnalysisResultFactory analysisResultFactory, Set<Class<? extends EntryAnalyzer>> analyzerClasses) {
        this.analysisResultFactory = analysisResultFactory;
        this.analyzers = createInstances(analyzerClasses);
    }

    @Override
    public AnalysisEngine createAnalysisEngine(FileSystem fileSystem, String[] excludes) {
        return new StandardAnalysisEngine(this.analyzers, this.analysisResultFactory, fileSystem, excludes);
    }

}
