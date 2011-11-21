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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemException;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;

public class DelegatingByteCodeEntryAnalyzerTests {

    private final StubResultGatheringClassVisitorFactory factory = new StubResultGatheringClassVisitorFactory();

    private final DelegatingByteCodeEntryAnalyzer analyzer = new DelegatingByteCodeEntryAnalyzer(this.factory);

    @Test
    public void analyze() throws AnalysisFailedException {
        this.analyzer.analyze(new StubFileSystemEntry(false));
        assertTrue(this.factory.getVisitor().getGetResultsCalled());
    }

    @Test
    public void analyzeDirectory() throws AnalysisFailedException {
        this.analyzer.analyze(new StubFileSystemEntry(true));
        assertFalse(this.factory.getVisitor().getGetResultsCalled());
    }

    private static class StubFileSystemEntry implements FileSystemEntry {

        private final boolean isDirectory;

        public StubFileSystemEntry(boolean isDirectory) {
            this.isDirectory = isDirectory;
        }

        @Override
        public InputStream getInputStream() {
            try {
                return new FileInputStream(
                    "target/test-classes/org/springframework/migrationanalyzer/contributions/bytecode/DelegatingByteCodeEntryAnalyzerTests.class");
            } catch (FileNotFoundException e) {
                throw new FileSystemException(e);
            }
        }

        @Override
        public String getName() {
            return "test.class";
        }

        @Override
        public Reader getReader() {
            return null;
        }

        @Override
        public boolean isDirectory() {
            return this.isDirectory;
        }

    }

}
