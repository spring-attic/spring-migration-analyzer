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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.StubFileSystemEntry;

public class LocationAwareOutputPathGeneratorTests {

    private final OutputPathGenerator generator = new LocationAwareOutputPathGenerator(new StandardOutputPathGenerator(), "d/e/Foo");

    @Test
    public void generatePathForResultType() {
        String path = this.generator.generatePathFor(Object.class);
        assertEquals("../../result-type/java.lang.Object.html", path);
    }

    @Test
    public void generatePathForFileSystemEntry() {
        String path = this.generator.generatePathFor(new StubFileSystemEntry());
        assertEquals("../../file-entry/org/springframework/Foo.class.html", path);
    }

    @Test
    public void generatePathForFileName() {
        String path = this.generator.generatePathFor("a/b/c/Bar.txt");
        assertEquals("../../a/b/c/Bar.txt", path);
    }

    @Test
    public void generatePathForFileSystemEntryContents() {
        String path = this.generator.generatePathForFileSystemEntryContents();
        assertEquals("../../file-entry/contents.html", path);
    }

    @Test
    public void generatePathForResultTypeContents() {
        String path = this.generator.generatePathForResultTypeContents();
        assertEquals("../../result-type/contents.html", path);
    }

    @Test
    public void generatePathForIndex() {
        String path = this.generator.generatePathForIndex();
        assertEquals("../../index.html", path);
    }

    @Test
    public void generatePathForSummary() {
        String path = this.generator.generatePathForSummary();
        assertEquals("../../summary.html", path);
    }
}
