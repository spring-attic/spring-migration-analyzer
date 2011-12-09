/*
 * Copyright 2011 the original author or authors.
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

package org.springframework.migrationanalyzer.commandline;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

public final class ZipArchiveDiscovererTests {

    private final ArchiveDiscoverer archiveDiscoverer = new ZipArchiveDiscoverer();

    @Test
    public void discoveryOfSingleArchive() {
        assertDiscoveredArchives(this.archiveDiscoverer.discover(new File("src/test/resources/archives/archive.jar")), new File(
            "stc/test/resources/archive.jar"));
    }

    @Test
    public void discoveryOfSingleArchiveBeneathADirectory() {
        File location = new File("src/test/resources/archives/single");
        assertDiscoveredArchives(this.archiveDiscoverer.discover(location), new File("location", "alpha/bravo.jar"));
    }

    @Test
    public void discoveryOfMultipleArchivesBeneathADirectory() {
        File location = new File("src/test/resources/archives/multiple");
        assertDiscoveredArchives(this.archiveDiscoverer.discover(location), new File(location, "alpha/bravo.jar"), new File(location, "charlie.ear"));
    }

    @Test
    public void discoveryOfNoArchivesBeneathADirectory() {
        assertDiscoveredArchives(this.archiveDiscoverer.discover(new File("src/test/resources/archives/none")));
    }

    private void assertDiscoveredArchives(List<File> discovered, File... expected) {
        assertEquals(expected.length, discovered.size());
    }
}
