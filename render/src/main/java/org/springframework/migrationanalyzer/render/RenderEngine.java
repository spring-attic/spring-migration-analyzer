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

package org.springframework.migrationanalyzer.render;

import org.springframework.migrationanalyzer.analyze.AnalysisResult;

/**
 * An abstraction for rendering the results of an analysis. Implementations are free to render the results into whatever
 * format they choose.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface RenderEngine {

    /**
     * Whether this engine can render the output type offered
     * 
     * @param outputType The type of output
     * @return {@code true} if this engine can handle the output type, otherwise {@code false}
     */
    boolean canRender(String outputType);

    /**
     * Render the analysis result
     * 
     * @param analysisResult The analysis result to render
     * @param outputPath The path to which the output should be rendered
     */
    void render(AnalysisResult analysisResult, String outputPath);

}
