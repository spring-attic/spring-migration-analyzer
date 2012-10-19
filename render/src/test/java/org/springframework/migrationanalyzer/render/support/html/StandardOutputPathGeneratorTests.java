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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

public class StandardOutputPathGeneratorTests {

    private final RootAwareOutputPathGenerator pathGenerator = new StandardOutputPathGenerator();

    @Test
    public void generateForResultType() {
        String path = this.pathGenerator.generatePathFor(Object.class);
        assertEquals("result-type/java.lang.Object.html", path);
    }

    @Test
    public void generateForFileSystemEntry() {
        String path = this.pathGenerator.generatePathFor(createFileSystemEntry("a/b/c/test.xml"));
        assertEquals("file-entry/a/b/c/test.xml.html", path);
    }

    @Test
    public void generateForFileSystemEntryContents() {
        String path = this.pathGenerator.generatePathForFileSystemEntryContents();
        assertEquals("file-entry/contents.html", path);
    }

    @Test
    public void generateForResultTypeContents() {
        String path = this.pathGenerator.generatePathForResultTypeContents();
        assertEquals("result-type/contents.html", path);
    }

    @Test
    public void generateForIndex() {
        String path = this.pathGenerator.generatePathForIndex();
        assertEquals("index.html", path);
    }

    @Test
    public void generateForSummary() {
        String path = this.pathGenerator.generatePathForSummary();
        assertEquals("summary.html", path);
    }

    @Test
    public void generateForFileName() {
        String path = this.pathGenerator.generatePathFor("foo/bar.txt");
        assertEquals("foo/bar.txt", path);
    }

    @Test
    public void generateRelativePathToRootForClass() {
        String path = this.pathGenerator.generatePathRelativeToRootFor(getClass());
        assertEquals("../", path);
    }

    @Test
    public void generateRelativePathToRootForFileSystemEntry() {
        String path = this.pathGenerator.generatePathRelativeToRootFor(createFileSystemEntry("a/b/c/d.txt"));
        assertEquals("../../../../", path);
    }

    private FileSystemEntry createFileSystemEntry(String name) {
        FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);
        when(fileSystemEntry.getName()).thenReturn(name);
        return fileSystemEntry;
    }
}
