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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemException;
import org.springframework.migrationanalyzer.util.IoUtils;

final class FileFileSystemEntry implements FileSystemEntry {

    private final File root;

    private final File file;

    FileFileSystemEntry(File root, File file) {
        this.root = root;
        this.file = file;
    }

    @Override
    public <T, U extends Exception> T doWithInputStream(ExceptionCallback<InputStream, T, U> callback) throws U {
        InputStream in = null;
        try {
            in = getInputStream();
            return callback.perform(in);
        } finally {
            IoUtils.closeQuietly(in);
        }
    }

    @Override
    public <T> T doWithInputStream(Callback<InputStream, T> callback) {
        InputStream in = null;
        try {
            in = getInputStream();
            return callback.perform(in);
        } finally {
            IoUtils.closeQuietly(in);
        }
    }

    @Override
    public <T> T doWithReader(Callback<Reader, T> callback) {
        if (isDirectory()) {
            throw new FileSystemException(String.format("Cannot create Reader for directory entry '%s'", getName()));
        } else {
            Reader reader = null;
            try {
                reader = new FileReader(this.file);
                return callback.perform(reader);
            } catch (FileNotFoundException e) {
                throw new FileSystemException(e);
            } finally {
                IoUtils.closeQuietly(reader);
            }
        }
    }

    @Override
    public String getName() {
        String name = this.file.getAbsolutePath().substring(this.root.getAbsolutePath().length() + 1);
        name = normalizeWindowsPaths(name);
        return name;
    }

    @Override
    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    @Override
    public String toString() {
        return getName();
    }

    private String normalizeWindowsPaths(String name) {
        return name.replace('\\', '/');
    }

    private InputStream getInputStream() {
        if (isDirectory()) {
            throw new FileSystemException(String.format("Cannot create InputStream for directory entry '%s'", getName()));
        } else {
            try {
                return new FileInputStream(this.file);
            } catch (FileNotFoundException e) {
                throw new FileSystemException(e);
            }
        }
    }
}
