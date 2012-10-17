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

import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;

/**
 * An interface for types that act as the controller in the MVC pattern. Implementations of this interface are used to
 * render the output from analysis.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface Controller<T> {

    /**
     * Whether this controller can handle the type of result offered
     * 
     * @param resultType The type of result
     * @return {@code true} if this controller can handle the result type, otherwise {@code false}
     */
    boolean canHandle(Class<?> resultType);

    /**
     * Handle a collection of results
     * 
     * @param results The results to use
     * @return The model to render
     */
    ModelAndView handle(Set<AnalysisResultEntry<T>> results);
}
