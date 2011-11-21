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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

public class FileFileSystemEntryTests {

    @Test
    public void getName() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);
        assertEquals("file-file-system-entry/file.txt", entry.getName());
    }

    @Test
    public void isDirectory() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        assertTrue(entry.isDirectory());

        root = new File("src/test/resources");
        file = new File(root, "file-file-system-entry/file.txt");

        entry = new FileFileSystemEntry(root, file);

        assertFalse(entry.isDirectory());
    }

    @Test
    public void getReaderFromDirectoryEntry() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        try {
            entry.getReader();
            fail("getReader should fail for a directory entry");
        } catch (RuntimeException re) {
            assertEquals("Cannot create Reader for directory entry 'file-file-system-entry'", re.getMessage());
        }
    }

    @Test
    public void getInputStreamFromDirectoryEntry() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        try {
            entry.getInputStream();
            fail("getInputStream should fail for a directory entry");
        } catch (RuntimeException re) {
            assertEquals("Cannot create InputStream for directory entry 'file-file-system-entry'", re.getMessage());
        }
    }

    @Test
    public void getReader() throws IOException {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        Reader reader = entry.getReader();
        BufferedReader bufferedReader = new BufferedReader(reader);
        assertEquals("Some content", bufferedReader.readLine());
        assertNull(bufferedReader.readLine());

    }

    @Test
    public void getInputStream() throws IOException {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        InputStream inputStream = entry.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals("Some content", bufferedReader.readLine());
        assertNull(bufferedReader.readLine());

    }
}
