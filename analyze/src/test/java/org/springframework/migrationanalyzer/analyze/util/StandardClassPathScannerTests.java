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

package org.springframework.migrationanalyzer.analyze.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

public class StandardClassPathScannerTests {

    private final StandardClassPathScanner scanner = new StandardClassPathScanner();

    @Test
    public void resourcesInJar() throws MalformedURLException {
        URLClassLoader classLoader = new URLClassLoader(
            new URL[] { new File("src/test/resources/classpath-scanner/classpath-scanner.jar").toURI().toURL() });

        Set<String> matchedResources = this.scanner.findResources(Pattern.compile(".*example\\.properties"), classLoader);
        assertContains(matchedResources, "org/springframework/migrationanalyzer/internal/example.properties");
    }

    @Test
    public void resourcesInDirectory() throws MalformedURLException {
        URLClassLoader classLoader = new URLClassLoader(new URL[] { new File("src/test/resources/classpath-scanner/classes/").toURI().toURL() });

        Set<String> matchedResources = this.scanner.findResources(Pattern.compile(".*example\\.properties"), classLoader);
        assertContains(matchedResources, new File("org/springframework/migrationanalyzer/internal/example.properties").getPath());
    }

    private void assertContains(Set<String> matchedResources, String... candidates) {
        assertEquals(candidates.length, matchedResources.size());
        for (String candidate : candidates) {
            assertTrue("Matches did not contain " + candidate, matchedResources.contains(candidate));
        }
    }
}
