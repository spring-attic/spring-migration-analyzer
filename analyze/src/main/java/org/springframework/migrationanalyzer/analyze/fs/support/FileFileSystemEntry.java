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

final class FileFileSystemEntry implements FileSystemEntry {

    private final File root;

    private final File file;

    FileFileSystemEntry(File root, File file) {
        this.root = root;
        this.file = file;
    }

    @Override
    public InputStream getInputStream() {
        if (isDirectory()) {
            throw new FileSystemException(String.format("Cannot create InputStream for directory entry '%s'", getName()));
        }

        try {
            return new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            throw new FileSystemException(e);
        }
    }

    @Override
    public String getName() {
        String name = this.file.getAbsolutePath().substring(this.root.getAbsolutePath().length() + 1);
        name = normalizeWindowsPaths(name);
        return name;
    }

    @Override
    public Reader getReader() {
        if (isDirectory()) {
            throw new FileSystemException(String.format("Cannot create Reader for directory entry '%s'", getName()));
        }

        try {
            return new FileReader(this.file);
        } catch (FileNotFoundException e) {
            throw new FileSystemException(e);
        }
    }

    @Override
    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    private String normalizeWindowsPaths(String name) {
        return name.replace('\\', '/');
    }

    @Override
    public String toString() {
        return getName();
    }
}
