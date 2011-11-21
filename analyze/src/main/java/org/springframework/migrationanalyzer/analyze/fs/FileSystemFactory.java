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

package org.springframework.migrationanalyzer.analyze.fs;

import java.io.File;
import java.io.IOException;

/**
 * A <code>FileSystemFactory</code> is used to create <code>FileSystem</code> instances.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations need not be thread-safe.
 * 
 */
public interface FileSystemFactory {

    /**
     * Creates a new <code>FileSystem</code> that will provide access to the file system rooted at the supplied
     * location.
     * 
     * @param root The root of the<code>FileSystem</code>
     * 
     * @return The new <code>FileSystem</code>
     * 
     * @throws IOException when a failure occurs during file system creation
     */
    FileSystem createFileSystem(File archive) throws IOException;

}