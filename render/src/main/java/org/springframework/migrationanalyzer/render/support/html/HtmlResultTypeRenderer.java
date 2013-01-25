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

package org.springframework.migrationanalyzer.render.support.html;

import org.springframework.migrationanalyzer.analyze.AnalysisResult;

/**
 * A renderer that renders HTML output for a given {@link AnalysisResult}
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
interface HtmlResultTypeRenderer {

    /**
     * Render an analysis result
     * 
     * @param analysisResult The result to render
     * @param outputPathPrefix The prefix to be applied to the path of any files output by the rendering
     */
    void renderResultTypes(AnalysisResult analysisResult, String outputPathPrefix);
}
