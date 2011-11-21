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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.springframework.migrationanalyzer.util.IoUtils;

final class FileWriterFactory implements WriterFactory {

    private final String rootPath;

    FileWriterFactory(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public Writer createWriter(String path) {
        File file = new File(this.rootPath, path);
        try {
            IoUtils.createDirectoryIfNecessary(file.getParentFile());
            return new FileWriter(file);
        } catch (IOException e) {
            throw new WriterCreationFailedException("Failed to create writer for '" + path + "'", e);
        }
    }
}
