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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import org.junit.Test;

public class ZipUtilsTests {

    @Test
    public void singleZip() throws IOException {
        File unzipped = unzip(new File("src/test/resources/zip-utils/a.jar"));
        assertFilesExtractedFromJarA(unzipped);
    }

    @Test
    public void nestedZip() throws IOException {
        File unzipped = unzip(new File("src/test/resources/zip-utils/b.jar"));
        assertFilesExtractedFromJarB(unzipped);
    }

    @Test
    public void zipToLocationThatIsAFile() throws IOException {
        File destination = new File("target", "destination");
        delete(destination);
        assertTrue(destination.createNewFile());

        try {
            ZipUtils.unzipTo(new ZipFile("src/test/resources/zip-utils/a.jar"), destination);
            fail();
        } catch (IOException ioe) {

        }
    }

    @Test
    public void zipToDirectoryThatCannotBeCreated() throws IOException {
        File parent = new File("target", "destination");
        delete(parent);
        assertTrue(parent.createNewFile());

        File destination = new File(parent, "destination");

        try {
            ZipUtils.unzipTo(new ZipFile("src/test/resources/zip-utils/a.jar"), destination);
            fail();
        } catch (IOException ioe) {

        }
    }

    @Test
    public void zipToDirectoryThatContainsFilePreventingDirectoryInZipFromBeingCreated() throws IOException {
        File destination = new File("target", "destination");
        delete(destination);
        assertTrue(destination.mkdirs());

        File a = new File(destination, "a");
        assertTrue(a.createNewFile());

        try {
            ZipUtils.unzipTo(new ZipFile("src/test/resources/zip-utils/a.jar"), destination);
            fail();
        } catch (IOException ioe) {
        }
    }

    private void assertFilesExtractedFromJarA(File root) {
        assertTrue(new File(root, "a").exists());
        assertTrue(new File(root, "a/b.txt").exists());
        assertTrue(new File(root, "META-INF").exists());
        assertTrue(new File(root, "META-INF/MANIFEST.MF").exists());
    }

    private void assertFilesExtractedFromJarB(File root) {
        assertTrue(new File(root, "META-INF").exists());
        assertTrue(new File(root, "META-INF/MANIFEST.MF").exists());

        File aJar = new File(root, "a.jar");
        assertTrue(aJar.exists());

        assertFilesExtractedFromJarA(aJar);
    }

    private static File unzip(File file) throws IOException {
        File unzipped = new File("target", file.getName());
        delete(unzipped);

        ZipUtils.unzipTo(new ZipFile(file), unzipped);
        return unzipped;
    }

    private static void delete(File file) {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            for (File entry : entries) {
                delete(entry);
            }
        }
        file.delete();
    }
}
