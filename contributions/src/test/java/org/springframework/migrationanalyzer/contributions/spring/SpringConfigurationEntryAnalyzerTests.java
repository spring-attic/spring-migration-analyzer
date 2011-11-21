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

package org.springframework.migrationanalyzer.contributions.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemException;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;

public class SpringConfigurationEntryAnalyzerTests {

    private final StubSpringConfigurationClassValueAnalyzer classValueAnalyzer = new StubSpringConfigurationClassValueAnalyzer();

    @SuppressWarnings({ "rawtypes" })
    private final SpringConfigurationEntryAnalyzer analyzer = new SpringConfigurationEntryAnalyzer(
        new HashSet<SpringConfigurationClassValueAnalyzer>(Arrays.asList(this.classValueAnalyzer)));

    @Test
    public void springConfigurationFile() throws AnalysisFailedException {
        Set<Object> results = this.analyzer.analyze(new StubFileSystemEntry("spring-jndi.xml"));
        assertNotNull(results);
        assertEquals(4, results.size());

        assertTrue(this.classValueAnalyzer.classNamesAnalyzed.contains("org.springframework.jndi.JndiObjectFactoryBean"));
        assertTrue(this.classValueAnalyzer.classNamesAnalyzed.contains("javax.sql.DataSource"));
    }

    @Test
    public void nonSpringConfigurationXmlFile() throws AnalysisFailedException {
        Set<Object> results = this.analyzer.analyze(new StubFileSystemEntry("web.xml"));
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    private static final class StubFileSystemEntry implements FileSystemEntry {

        private final String name;

        public StubFileSystemEntry(String name) {
            this.name = name;
        }

        @Override
        public InputStream getInputStream() {
            try {
                return new FileInputStream("src/test/resources/org/springframework/migrationanalyzer/contributions/spring/" + this.name);
            } catch (FileNotFoundException e) {
                throw new FileSystemException(e);
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Reader getReader() {
            return null;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }

    }

    private static final class StubSpringConfigurationClassValueAnalyzer implements SpringConfigurationClassValueAnalyzer<String> {

        private final Set<String> classNamesAnalyzed = new HashSet<String>();

        @Override
        public String analyze(String className, FileSystemEntry fse) {
            this.classNamesAnalyzed.add(className);
            return null;
        }
    }
}