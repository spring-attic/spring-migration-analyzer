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

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

final class LocationAwareOutputPathGenerator implements OutputPathGenerator {

    private final RootAwareOutputPathGenerator delegate;

    private final String relativeLocation;

    LocationAwareOutputPathGenerator(RootAwareOutputPathGenerator delegate, Class<?> relativeTo) {
        this.delegate = delegate;
        this.relativeLocation = this.delegate.generateRelativePathToRootFor(relativeTo);
    }

    LocationAwareOutputPathGenerator(RootAwareOutputPathGenerator delegate, FileSystemEntry relativeTo) {
        this.delegate = delegate;
        this.relativeLocation = this.delegate.generateRelativePathToRootFor(relativeTo);
    }

    LocationAwareOutputPathGenerator(RootAwareOutputPathGenerator delegate, String relativeTo) {
        this.delegate = delegate;
        this.relativeLocation = this.delegate.generateRelativePathToRootFor(relativeTo);
    }

    @Override
    public String generatePathFor(Class<?> resultType) {
        return makePathRelative(this.delegate.generatePathFor(resultType));
    }

    @Override
    public String generatePathFor(FileSystemEntry fileSystemEntry) {
        return makePathRelative(this.delegate.generatePathFor(fileSystemEntry));
    }

    @Override
    public String generatePathFor(String fileName) {
        return makePathRelative(this.delegate.generatePathFor(fileName));
    }

    @Override
    public String generatePathForFileSystemEntryContents() {
        return makePathRelative(this.delegate.generatePathForFileSystemEntryContents());
    }

    @Override
    public String generatePathForIndex() {
        return makePathRelative(this.delegate.generatePathForIndex());
    }

    @Override
    public String generatePathForResultTypeContents() {
        return makePathRelative(this.delegate.generatePathForResultTypeContents());
    }

    @Override
    public String generatePathForSummary() {
        return makePathRelative(this.delegate.generatePathForSummary());
    }

    private String makePathRelative(String path) {
        return this.relativeLocation + path;
    }
}
