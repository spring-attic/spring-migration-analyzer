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
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.Callback;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.ExceptionCallback;

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
    public void doWithReaderThrowsExceptionWithDirectoryEntry() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        try {
            entry.doWithReader(null);
            fail("doWithReader should fail for a directory entry");
        } catch (RuntimeException re) {
            assertEquals("Cannot create Reader for directory entry 'file-file-system-entry'", re.getMessage());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void doWithInputStreamAndExceptionCallbackThrowsExceptionWithDirectoryEntry() throws Exception {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        try {
            entry.doWithInputStream((ExceptionCallback) null);
            fail("doWithInputStream should fail for a directory entry");
        } catch (RuntimeException re) {
            assertEquals("Cannot create InputStream for directory entry 'file-file-system-entry'", re.getMessage());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void doWithInputStreamAndCallbackThrowsExceptionWithDirectoryEntry() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        try {
            entry.doWithInputStream((FileSystemEntry.Callback) null);
            fail("doWithInputStream should fail for a directory entry");
        } catch (RuntimeException re) {
            assertEquals("Cannot create InputStream for directory entry 'file-file-system-entry'", re.getMessage());
        }
    }

    @Test
    public void doWithReader() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        String result = entry.doWithReader(new FileSystemEntry.Callback<Reader, String>() {

            @Override
            public String perform(Reader reader) {
                BufferedReader bufferedReader = new BufferedReader(reader);
                try {
                    String result = bufferedReader.readLine();
                    assertNull(bufferedReader.readLine());
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        assertEquals("Some content", result);
    }

    @Test
    public void doWithInputStreamWithCallback() {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        String result = entry.doWithInputStream(new Callback<InputStream, String>() {

            @Override
            public String perform(InputStream inputStream) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String result = bufferedReader.readLine();
                    assertNull(bufferedReader.readLine());
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        assertEquals("Some content", result);
    }

    @Test
    public void doWithInputStreamWithExceptionCallback() throws IOException {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        String result = entry.doWithInputStream(new ExceptionCallback<InputStream, String, IOException>() {

            @Override
            public String perform(InputStream inputStream) throws IOException {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String result = bufferedReader.readLine();
                    assertNull(bufferedReader.readLine());
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        assertEquals("Some content", result);
    }

    @Test(expected = Exception.class)
    public void exceptionThrownByExceptionCallbackIsPropagatedToCaller() throws Exception {
        File root = new File("src/test/resources");
        File file = new File(root, "file-file-system-entry/file.txt");

        FileFileSystemEntry entry = new FileFileSystemEntry(root, file);

        entry.doWithInputStream(new ExceptionCallback<InputStream, Void, Exception>() {

            @Override
            public Void perform(InputStream t) throws Exception {
                throw new Exception();
            }
        });
    }
}
