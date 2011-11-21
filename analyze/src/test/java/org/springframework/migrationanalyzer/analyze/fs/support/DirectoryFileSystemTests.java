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

package org.springframework.migrationanalyzer.analyze.fs.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

public class DirectoryFileSystemTests {

    private final File root = new File("src/test/resources/directory-file-system");

    private final DirectoryFileSystem fs = new DirectoryFileSystem(this.root);

    @Test
    public void iterator() {
        Iterator<FileSystemEntry> iterator = this.fs.iterator();

        List<String> expected = Arrays.asList("a", "a/d.txt", "b", "b/c", "b/c/e.txt");

        List<String> actual = new ArrayList<String>();

        while (iterator.hasNext()) {
            FileSystemEntry entry = iterator.next();
            assertEquals(entry.getName().endsWith(".txt"), !entry.isDirectory());
            actual.add(entry.getName());
        }

        assertEquals(expected.size(), actual.size());

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorRemoveIsUnsupported() {
        Iterator<FileSystemEntry> iterator = this.fs.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void toStringProvidesAbsolutePathOfRoot() {
        assertEquals(this.root.getAbsolutePath(), this.fs.toString());
    }

    @Test
    public void cleanup() throws IOException {
        File root = new File("target/directory-file-system");
        root.mkdirs();
        File content = new File(root, "content");
        content.createNewFile();

        new DirectoryFileSystem(root).cleanup();

        assertFalse(root.exists());
    }
}
