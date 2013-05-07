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

package org.springframework.migrationanalyzer.contributions.spring;

import java.util.Collections;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.ModelAndView;

abstract class AbstractSpringIntegrationByFileSystemEntryController<T extends AbstractSpringIntegration> implements ByFileSystemEntryController<T> {

    private final Class<T> resultClass;

    private final String viewName;

    protected AbstractSpringIntegrationByFileSystemEntryController(Class<T> resultClass, String viewName) {
        this.resultClass = resultClass;
        this.viewName = viewName;
    }

    @Override
    public boolean canHandle(Class<?> resultType) {
        return this.resultClass.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<T>> results) {
        return new ModelAndView(Collections.<String, Object> emptyMap(), this.viewName);
    }
}
