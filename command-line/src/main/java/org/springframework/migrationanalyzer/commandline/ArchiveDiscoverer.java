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
import java.util.List;

interface ArchiveDiscoverer {

    /**
     * Discovers the archives that are to be analyzed by examining the given <code>location</code>. If no archives are
     * found, an empty list must be returned, <strong>not</strong> null.
     * 
     * @param location The location to examine
     * 
     * @return the discovered archives that are to be analyzed
     */
    List<File> discover(File location);
}
