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

import java.io.Writer;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.Controller;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

interface ViewRenderer {

    /**
     * Render the view with an empty model
     * 
     * @param viewName The name of the view to render
     * @param writer The writer to render to
     */
    void renderViewWithEmptyModel(String viewName, Writer writer);

    /**
     * Render the view with a {@link Map}-based model
     * 
     * @param viewName The name of the view to render
     * @param model The mode to render with
     * @param writer The writer to render to
     */
    void renderViewWithModel(String viewName, Map<String, Object> model, Writer writer);

    /**
     * Render the view
     * 
     * @param <T> The type of the results
     * 
     * @param resultType The type of the result being rendered
     * @param entries The results to render
     * @param controllers The controllers to render with in the MVC pattern
     * @param writer The writer to render to
     * @param outputPathGenerator The generator for linked output paths
     */
    @SuppressWarnings("rawtypes")
    <T> void render(Class<?> resultType, Set<AnalysisResultEntry<T>> entries, Set<? extends Controller> controllers, Writer writer,
        OutputPathGenerator outputPathGenerator);
}
