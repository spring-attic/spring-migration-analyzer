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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

final class DirectoryFileSystem implements FileSystem {

    private final File root;

    private final Logger log = LoggerFactory.getLogger(DirectoryFileSystem.class);

    DirectoryFileSystem(File root) {
        this.root = root;
    }

    @Override
    public Iterator<FileSystemEntry> iterator() {
        return new FileHierarchyIterator(this.root);
    }

    @Override
    public String toString() {
        return this.root.getAbsolutePath();
    }

    private static final class FileHierarchyIterator implements Iterator<FileSystemEntry> {

        private final Iterator<FileSystemEntry> fileSystemIterator;

        private FileHierarchyIterator(File root) {
            List<FileSystemEntry> entries = new ArrayList<FileSystemEntry>();
            enumerateEntries(root, root, entries);
            this.fileSystemIterator = entries.iterator();
        }

        @Override
        public boolean hasNext() {
            return this.fileSystemIterator.hasNext();
        }

        @Override
        public FileSystemEntry next() {
            return this.fileSystemIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void enumerateEntries(File root, File orignalRootPath, List<FileSystemEntry> entries) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    enumerateEntries(file, orignalRootPath, entries);
                }
                entries.add(new FileFileSystemEntry(orignalRootPath, file));
            }
        }
    }

    @Override
    public void cleanup() {
        try {
            delete(this.root);
        } catch (IOException ioe) {
            // Do nothing
        }
        this.log.info("Cleanup complete");
    }

    private void delete(File toDelete) throws IOException {
        if (toDelete.isDirectory()) {
            for (File child : toDelete.listFiles()) {
                delete(child);
            }
        }
        if (!toDelete.delete()) {
            this.log.warn("Failed to delete file '{}'", toDelete);
            throw new IOException();
        }
    }
}
