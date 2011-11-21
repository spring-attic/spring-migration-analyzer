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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for working with Zip files.
 */
public final class ZipUtils {

    private static final byte[] ZIP_HEADER = new byte[] { 0x50, 0x4b, 0x03, 0x04 };

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    private ZipUtils() {
    }

    /**
     * Unzips the supplied <code>zip</code> to the supplied <code>destination</code> directory. If the directory does
     * not exist it is created. If <code>destination</code> already exists it <strong>must</strong> be a directory,
     * otherwise an <code>IOException</code> will be thrown.
     * 
     * @param zip the <code>ZipFile</code> to unzip
     * @param destination the directory to which it should be unzipped
     * 
     * @throws IOException if a failure occurs during the unzip.
     */
    public static void unzipTo(ZipFile zip, File destination) throws IOException {
        LOGGER.debug("Unzipping '{}' to '{}'", zip.getName(), destination);

        if (destination.exists()) {
            if (!destination.isDirectory()) {
                throw new IOException("Cannot unzip to " + destination + " as it already exists and it is not a directory");
            }
        } else {
            if (!destination.mkdirs()) {
                throw new IOException("Cannot unzip to " + destination + " as the directory does not exist and it could not be created");
            }
        }

        try {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File destEntry = new File(destination, entry.getName());
                if (entry.isDirectory()) {
                    IoUtils.createDirectoryIfNecessary(destEntry);
                } else {
                    processFileEntry(zip, entry, destEntry);
                }
            }
        } finally {
            IoUtils.closeQuietly(zip);
        }
    }

    private static void processFileEntry(ZipFile zip, ZipEntry entry, File destEntry) throws IOException {
        if (isZipFile(entry, zip)) {
            LOGGER.debug("Found nested ZIP file '{}'", entry.getName());

            File temp = File.createTempFile("unzip", null);
            copy(zip, entry, temp);

            unzipTo(new ZipFile(temp), destEntry);
            if (!temp.delete()) {
                throw new IOException("Failed to delete temporary file " + temp);
            }
        } else {
            File parent = destEntry.getParentFile();
            IoUtils.createDirectoryIfNecessary(parent);
            copy(zip, entry, destEntry);
        }
    }

    private static void copy(ZipFile zip, ZipEntry entry, File destinationFile) throws IOException {
        InputStream source = null;
        OutputStream destination = null;

        try {
            source = zip.getInputStream(entry);
            destination = new FileOutputStream(destinationFile);

            IoUtils.copy(source, destination);
        } finally {
            IoUtils.closeQuietly(source, destination);
        }
    }

    /**
     * Determine whether a {@link ZipEntry} represents a nested {@link ZipFile}
     * 
     * @param zipEntry The entry to analyze
     * @param zipFile The file that the entry is a member of
     * @return {@code true} if the entry is a nested ZIP file, otherwise {@code false}
     * @throws IOException
     */
    public static boolean isZipFile(ZipEntry zipEntry, ZipFile zipFile) throws IOException {
        InputStream input = null;

        try {
            input = zipFile.getInputStream(zipEntry);
            return isZipFile(input);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * Determine whether the content of an {@link InputStream} is a {@link ZipFile}
     * 
     * @param input The input stream to read
     * @return {@code true} if the stream represents a ZIP file, otherwise {@code false}
     * @throws IOException
     */
    public static boolean isZipFile(InputStream input) throws IOException {
        byte[] headerBytes = new byte[ZIP_HEADER.length];
        if (input.read(headerBytes) != headerBytes.length) {
            return false;
        }
        return Arrays.equals(headerBytes, ZIP_HEADER);
    }
}
