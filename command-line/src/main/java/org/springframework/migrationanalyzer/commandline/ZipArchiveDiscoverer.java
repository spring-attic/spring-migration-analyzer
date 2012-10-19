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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.migrationanalyzer.util.ZipUtils;
import org.springframework.stereotype.Component;

@Component
final class ZipArchiveDiscoverer implements ArchiveDiscoverer {

    private final Logger logger = LoggerFactory.getLogger(ZipArchiveDiscoverer.class);

    @Override
    public List<File> discover(File location) {
        List<File> discovered = new ArrayList<File>();
        doDiscover(location, discovered);
        return discovered;
    }

    private void doDiscover(File candidate, List<File> discovered) {
        if (candidate.isDirectory()) {
            for (File inDirectory : candidate.listFiles()) {
                doDiscover(inDirectory, discovered);
            }
        } else {
            FileInputStream in = null;
            try {
                in = new FileInputStream(candidate);
                if (ZipUtils.isZipFile(in)) {
                    discovered.add(candidate);
                }
            } catch (IOException ioe) {
                this.logger.warn("Unable to examine candidate archive '{}', it will not be analyzed", candidate);
            } finally {
                IoUtils.closeQuietly(in);
            }
        }
    }
}
