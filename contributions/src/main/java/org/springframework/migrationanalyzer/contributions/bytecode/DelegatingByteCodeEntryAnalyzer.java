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

package org.springframework.migrationanalyzer.contributions.bytecode;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;

final class DelegatingByteCodeEntryAnalyzer implements EntryAnalyzer<Object> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CLASS_SUFFIX = ".class";

    private final ResultGatheringClassVisitorFactory factory;

    DelegatingByteCodeEntryAnalyzer() {
        this(new DelegatingClassVisitorFactory());
    }

    DelegatingByteCodeEntryAnalyzer(ResultGatheringClassVisitorFactory factory) {
        this.factory = factory;
    }

    @Override
    public Set<Object> analyze(FileSystemEntry fileSystemEntry) throws AnalysisFailedException {
        if (!fileSystemEntry.isDirectory() && fileSystemEntry.getName().endsWith(CLASS_SUFFIX)) {
            this.logger.debug("Doing bytecode analysis of '{}'", fileSystemEntry);

            ResultGatheringClassVisitor<Object> classVisitor = this.factory.create();

            ClassReader reader;
            try {
                reader = new ClassReader(fileSystemEntry.getInputStream());
                reader.accept(classVisitor, 0);
            } catch (IOException e) {
                throw new AnalysisFailedException("Failed to read class file '" + fileSystemEntry.getName() + "'", e);
            }

            return classVisitor.getResults();
        }
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

}
