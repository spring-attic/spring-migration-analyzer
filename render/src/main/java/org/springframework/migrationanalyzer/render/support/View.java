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

import java.io.Writer;
import java.util.Map;

/**
 * An abstraction that takes a model and renders output to a {@link Writer}. The type of output that is rendered is
 * implementation dependent.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface View {

    /**
     * Render content based on the {@code model} to the {@link Writer}
     * 
     * @param model The model to use when rendering
     * @param writer The writer to render output
     */
    void render(Map<String, Object> model, Writer writer);

}
