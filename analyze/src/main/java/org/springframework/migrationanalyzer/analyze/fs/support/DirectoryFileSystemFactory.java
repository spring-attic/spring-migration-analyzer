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
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.fs.FileSystem;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemException;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemFactory;
import org.springframework.migrationanalyzer.util.ZipUtils;
import org.springframework.stereotype.Component;

@Component
final class DirectoryFileSystemFactory implements FileSystemFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public FileSystem createFileSystem(File archive) throws IOException {
        this.logger.info("Expanding archive '{}'", archive);

        File unzipped = File.createTempFile(archive.getName(), null);

        if (!unzipped.delete()) {
            throw new FileSystemException("Failed to delete temporary file " + unzipped);
        }

        if (!unzipped.mkdirs()) {
            throw new FileSystemException("Failed to create directory " + unzipped);
        }

        ZipUtils.unzipTo(new ZipFile(archive), unzipped);

        this.logger.info("Archive expanded");
        return new DirectoryFileSystem(unzipped);
    }
}
