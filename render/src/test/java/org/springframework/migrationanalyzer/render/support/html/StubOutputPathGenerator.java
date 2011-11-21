/**
 * 
 */

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

import java.util.ArrayList;
import java.util.List;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

final class StubOutputPathGenerator implements RootAwareOutputPathGenerator {

    private final String outputPath;

    private final List<Class<?>> resultTypes = new ArrayList<Class<?>>();

    private final List<FileSystemEntry> fileSystemEntries = new ArrayList<FileSystemEntry>();

    private final List<String> fileNames = new ArrayList<String>();

    private int fileSystemEntryContents = 0;

    private int index = 0;

    private int resultTypeContents = 0;

    private int summary = 0;

    public StubOutputPathGenerator(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public String generatePathFor(Class<?> resultType) {
        this.resultTypes.add(resultType);
        return this.outputPath;
    }

    @Override
    public String generatePathFor(FileSystemEntry fileSystemEntry) {
        this.fileSystemEntries.add(fileSystemEntry);
        return this.outputPath;
    }

    @Override
    public String generatePathFor(String fileName) {
        this.fileNames.add(fileName);
        return this.outputPath;
    }

    @Override
    public String generatePathForFileSystemEntryContents() {
        this.fileSystemEntryContents++;
        return this.outputPath;
    }

    @Override
    public String generatePathForIndex() {
        this.index++;
        return this.outputPath;
    }

    @Override
    public String generatePathForResultTypeContents() {
        this.resultTypeContents++;
        return this.outputPath;
    }

    @Override
    public String generatePathForSummary() {
        this.summary++;
        return this.outputPath;
    }

    @Override
    public String generateRelativePathToRootFor(FileSystemEntry fileSystemEntry) {
        return this.outputPath;
    }

    @Override
    public String generateRelativePathToRootFor(Class<?> resultType) {
        return this.outputPath;
    }

    @Override
    public String generateRelativePathToRootFor(String path) {
        return this.outputPath;
    }

    public List<Class<?>> getResultTypes() {
        return this.resultTypes;
    }

    public List<FileSystemEntry> getFileSystemEntries() {
        return this.fileSystemEntries;
    }

    public List<String> getFileNames() {
        return this.fileNames;
    }

    public int getFileSystemEntryContents() {
        return this.fileSystemEntryContents;
    }

    public int getIndex() {
        return this.index;
    }

    public int getResultTypeContents() {
        return this.resultTypeContents;
    }

    public int getSummary() {
        return this.summary;
    }
}