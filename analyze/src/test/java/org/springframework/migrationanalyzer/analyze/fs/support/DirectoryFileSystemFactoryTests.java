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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

public class DirectoryFileSystemFactoryTests {

    @Test
    public void createFileSystem() throws IOException {

        FileSystem fileSystem = new DirectoryFileSystemFactory().createFileSystem(new File("src/test/resources/file-system-factory/a.jar"));
        assertNotNull(fileSystem);

        Iterator<FileSystemEntry> iterator = fileSystem.iterator();

        Set<String> entryNames = new HashSet<String>();

        while (iterator.hasNext()) {
            entryNames.add(iterator.next().getName());
        }

        assertEquals(4, entryNames.size());

        assertTrue(entryNames.contains("a/b.txt"));
        assertTrue(entryNames.contains("a"));
        assertTrue(entryNames.contains("META-INF"));
        assertTrue(entryNames.contains("META-INF/MANIFEST.MF"));
    }
}
