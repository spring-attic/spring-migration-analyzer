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

package org.springframework.migrationanalyzer.contributions;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.support.html.RootAwareOutputPathGenerator;

public final class StubOutputPathGenerator implements RootAwareOutputPathGenerator {

    @Override
    public String generatePathFor(Class<?> resultType) {
        return "testPath";
    }

    @Override
    public String generatePathFor(FileSystemEntry fileSystemEntry) {
        return "testPath";
    }

    @Override
    public String generatePathFor(String fileName) {
        return "testPath";
    }

    @Override
    public String generatePathForFileSystemEntryContents() {
        return "testPath";
    }

    @Override
    public String generatePathForIndex() {
        return "testPath";
    }

    @Override
    public String generatePathForResultTypeContents() {
        return "testPath";
    }

    @Override
    public String generatePathForSummary() {
        return "testPath";
    }

    @Override
    public String generateRelativePathToRootFor(FileSystemEntry fileSystemEntry) {
        return "testPath";
    }

    @Override
    public String generateRelativePathToRootFor(Class<?> resultType) {
        return "testPath";
    }

    @Override
    public String generateRelativePathToRootFor(String path) {
        return "testPath";
    }

}
