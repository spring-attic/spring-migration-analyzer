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

/**
 * Provides an abstraction of the file system. Used to iterate over the entries in an application that is being
 * analyzed.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations need not be thread-safe
 * 
 */
public interface FileSystem extends Iterable<FileSystemEntry> {

    /**
     * Instructs the <code>FileSystem</code> to cleanup, for example by deleting any temporary files it has created.
     * Once cleaned up, the <code>FileSystem</code>'s behavior is undefined.
     */
    void cleanup();

}
