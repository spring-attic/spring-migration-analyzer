/*
 * Copyright 2012 the original author or authors.
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

/**
 * A {@code ResultTypeDisplayNameResolver} is used to resolve the display name for a result type. A display name is a
 * name that is suitable for inclusion in a report.
 * 
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations <strong>must</strong> be thread-safe
 * 
 * 
 * 
 */
public interface ResultTypeDisplayNameResolver {

    /**
     * Returns the name for the given <code>resultType</code> that is suitable for display in a report.
     * 
     * @param resultType The result type
     * @return The result type's display name
     */
    String getDisplayName(Class<?> resultType);

}
