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

package org.springframework.migrationanalyzer.render.support;

import org.springframework.migrationanalyzer.render.RenderEngine;

/**
 * A factory for creating a {@link RenderEngine} based on a desired output type
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface RenderEngineSubFactory {

    /**
     * Whether this factory can render the output type offered
     * 
     * @param outputType The type of output
     * @return {@code true} if this factory can handle the output type, otherwise {@code false}
     */
    boolean canRender(String outputType);

    /**
     * Creates a new {@link RenderEngine} based on the output location a user desires
     * 
     * @param outputPath The path that the chosen {@link RenderEngine} should output to
     * @return The chosen {@link RenderEngine}
     */
    RenderEngine create(String outputPath);
}
