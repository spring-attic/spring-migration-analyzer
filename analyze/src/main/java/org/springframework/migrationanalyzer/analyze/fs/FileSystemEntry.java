/*
 * Copyright 2009 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

package org.springframework.migrationanalyzer.analyze.fs;

import java.io.InputStream;
import java.io.Reader;

/**
 * A <code>FileSystemEntry</code> represents a single entry in a {@link FileSystem}.
 */
public interface FileSystemEntry {

    /**
     * Returns the name of the entry
     * 
     * @return the entry's name
     */
    String getName();

    /**
     * <code>true</code> if the entry is a directory, otherwise <code>false</code>.
     * 
     * @return
     */
    boolean isDirectory();

    /**
     * Returns an {@link InputStream} providing access to the entry's contents
     * 
     * @return an <code>InputStream</code> for the entry
     * 
     */
    InputStream getInputStream();

    /**
     * Returns a {@link Reader} providing access to the entry's contents
     * 
     * @return a <code>Reader</code> for the entry.
     */
    Reader getReader();
}