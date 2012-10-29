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

package org.springframework.migrationanalyzer.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipFile;

/**
 * Provides utility methods related to IO
 * 
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 * 
 */
public final class IoUtils {

    private static final int COPY_BUFFER_SIZE = 8192;

    private IoUtils() {

    }

    /**
     * Quietly closes the given <code>Closeable</code>s. <code>null</code> <code>Closeable</code> instances are skipped.
     * Any <code>IOException</code>s thrown from <code>close()</code> are ignored.
     * 
     * @param closeables to be closed
     */
    public static void closeQuietly(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ioe) {
                    // Closing quietly
                }
            }
        }
    }

    /**
     * Quietly closes the given <code>ZipFile</code>s. A <code>null</code> <code>ZipFile</code> is ignored. Any
     * <code>IOException</code>s thrown from <code>close()</code> is ignored.
     * 
     * @param zipFile to be closed
     */
    public static void closeQuietly(ZipFile zipFile) {
        closeQuietly(new CloseableZipFile(zipFile));
    }

    /**
     * Copies the <code>source</code> to the <code>destination</code>.
     * 
     * <p />
     * 
     * It is the caller's responsibility to close the given <code>Reader</code> and <code>Writer</code>.
     * 
     * @param source the source
     * @param destination destination
     * 
     * @throws IOException if an IO error occurs during the copy
     */
    public static void copy(Reader source, Writer destination) throws IOException {

        char[] buffer = new char[COPY_BUFFER_SIZE];

        int read;

        while ((read = source.read(buffer)) > 0) {
            destination.write(buffer, 0, read);
        }
    }

    /**
     * Copies the <code>source</code> to the <code>destination</code>.
     * 
     * <p />
     * 
     * It is the caller's responsibility to close the given <code>InputStream</code> and <code>OutputStream</code>.
     * 
     * @param source the source
     * @param destination destination
     * 
     * @throws IOException if an IO error occurs during the copy
     */
    public static void copy(InputStream source, OutputStream destination) throws IOException {

        byte[] buffer = new byte[COPY_BUFFER_SIZE];

        int read;

        while ((read = source.read(buffer)) > 0) {
            destination.write(buffer, 0, read);
        }
    }

    /**
     * Creates, if necessary, the given <code>directory</code>, throwing an <code>IOException</code> if the directory
     * does not already exist and it cannot be created.
     * 
     * @param directory The directory to create
     * 
     * @throws IOException If the directory did not exist and it could not be created
     */
    public static void createDirectoryIfNecessary(File directory) throws IOException {
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new IOException(String.format("Failed to created the directory '%s'", directory));
        }
    }

    private static final class CloseableZipFile implements Closeable {

        private final ZipFile zipFile;

        private CloseableZipFile(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        @Override
        public void close() throws IOException {
            this.zipFile.close();
        }
    }
}
