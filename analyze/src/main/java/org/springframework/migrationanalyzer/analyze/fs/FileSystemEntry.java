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
     * Calls the given {@code callback} with the {@code InputStream} for the entry.
     * 
     * @param callback The callback to be called with the {@code InputStream}
     * @return The value returned by the callback
     */
    <T> T doWithInputStream(Callback<InputStream, T> callback);

    /**
     * Calls the given {@code callback} with the {@code InputStream} for the entry.
     * 
     * @param callback The callback to be called with the {@code InputStream}
     * @return The value returned by the callback
     * @throws U The exception thrown by the callback
     */
    <T, U extends Exception> T doWithInputStream(ExceptionCallback<InputStream, T, U> callback) throws U;

    /**
     * Calls the given {@code callback} with the {@code Reader} for the entry.
     * 
     * @param callback The callback to be called with the {@code Reader}
     * @return The value returned by the callback
     */
    <T> T doWithReader(Callback<Reader, T> callback);

    /**
     * A callback for working with a {@code FileSystemEntry}'s {@code InputStream} or {@code Reader}. The callback may
     * throw an exception from {@code perform}.
     * 
     * <p />
     * 
     * <strong>Concurrent Semantics</strong><br />
     * 
     * {@code FileSystemEntry} does not require callbacks to be thread-safe
     * 
     */
    interface ExceptionCallback<T, U, V extends Exception> {

        /**
         * Called by {@code FileSystemEntry}, passing in the {@code InputStream} or {@code Reader} as required.
         * 
         * @param t The entry's {@code InputStream} or {@code Reader}
         * @return The value to be returned to the caller of the callback method
         * @throws V The exception to be thrown to the caller of the callback method
         */
        // CHECKSTYLE:OFF
        U perform(T t) throws V;
        // CHECKSTYLE:ON
    }

    /**
     * A callback for working with a {@code FileSystemEntry}'s {@code InputStream} or {@code Reader}.
     * 
     * <p />
     * 
     * <strong>Concurrent Semantics</strong><br />
     * 
     * {@code FileSystemEntry} does not require callbacks to be thread-safe
     * 
     */
    interface Callback<T, U> {

        /**
         * Called by {@code FileSystemEntry}, passing in the {@code InputStream} or {@code Reader} as required.
         * 
         * @param t The entry's {@code InputStream} or {@code Reader}
         * @return The value to be returned to the caller of the callback method
         */
        U perform(T t);
    }
}