/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.migrationanalyzer.render.support.xml;

import java.io.Writer;

import org.springframework.migrationanalyzer.analyze.AnalysisResult;

interface XmlFileSystemEntryRenderer {

    /**
     * Render the results for each file system entry found in the analysis result
     * 
     * @param result The result of the analysis to be rendered
     * @param writer Used to write the rendered output
     */
    void renderFileSystemEntries(AnalysisResult result, Writer writer);

}
