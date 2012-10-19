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

import java.io.File;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.stereotype.Component;

@Component
final class StandardOutputPathGenerator implements RootAwareOutputPathGenerator {

    private static final String RESULT_TYPE_FORMAT = "result-type/%s.html";

    private static final String FILE_ENTRY_FORMAT = "file-entry/%s.html";

    private static final String INDEX_FORMAT = "index.html";

    private static final String SUMMARY_FORMAT = "summary.html";

    private static final String GENERIC_FORMAT = "%s";

    @Override
    public String generatePathForIndex() {
        return generatePath(INDEX_FORMAT);
    }

    @Override
    public String generatePathForSummary() {
        return generatePath(SUMMARY_FORMAT);
    }

    @Override
    public String generatePathFor(Class<?> resultType) {
        return generatePath(RESULT_TYPE_FORMAT, resultType.getName());
    }

    @Override
    public String generatePathFor(FileSystemEntry fileSystemEntry) {
        return generatePath(FILE_ENTRY_FORMAT, fileSystemEntry.getName());
    }

    @Override
    public String generatePathFor(String fileName) {
        return generatePath(GENERIC_FORMAT, fileName);
    }

    @Override
    public String generatePathForResultTypeContents() {
        return generatePath(RESULT_TYPE_FORMAT, "contents");
    }

    @Override
    public String generatePathForFileSystemEntryContents() {
        return generatePath(FILE_ENTRY_FORMAT, "contents");
    }

    @Override
    public String generatePathRelativeToRootFor(Class<?> resultType) {
        return generatePathRelativeToRootFor(generatePathFor(resultType));
    }

    @Override
    public String generatePathRelativeToRootFor(FileSystemEntry fileSystemEntry) {
        return generatePathRelativeToRootFor(generatePathFor(fileSystemEntry));
    }

    @Override
    public String generatePathRelativeToRootFor(String path) {
        File file = new File(path);
        StringBuilder pathBuilder = new StringBuilder();

        while (file.getParent() != null) {
            pathBuilder.append("../");
            file = file.getParentFile();
        }

        return pathBuilder.toString();
    }

    private String generatePath(String format) {
        return String.format(format);
    }

    private String generatePath(String format, String parameter) {
        return String.format(format, parameter);
    }
}
