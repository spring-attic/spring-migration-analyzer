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

package org.springframework.migrationanalyzer.contributions.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;

abstract class AbstractSpringIntegrationByResultTypeController<T extends AbstractSpringIntegration> implements ByResultTypeController<T> {

    private static final String TITLE_MULTIPLE_FORMAT = "%s integration (%s users)";

    private static final String TITLE_DESCRIPTION_SINGLE_FORMAT = "%s integration (1 user)";

    private static final String VIEW_NAME = "spring-integration-by-result-type";

    private final String name;

    private final Class<?> resultClass;

    protected AbstractSpringIntegrationByResultTypeController(String name, Class<?> resultClass) {
        this.name = name;
        this.resultClass = resultClass;
    }

    @Override
    public boolean canHandle(Class<?> resultType) {
        return this.resultClass.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<T>> results) {

        String title = getTitle(this.name, results.size());
        Map<String, FileSystemEntry> userLocationsByName = new HashMap<String, FileSystemEntry>();

        for (AnalysisResultEntry<T> analysisResultEntry : results) {
            T result = analysisResultEntry.getResult();
            userLocationsByName.put(result.getUserLocation().getName(), result.getUserLocation());
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", title);
        model.put("userLocationsByName", userLocationsByName);
        model.put("name", this.name);

        return new ModelAndView(model, VIEW_NAME);
    }

    private String getTitle(String name, int count) {
        if (count > 1) {
            return String.format(TITLE_MULTIPLE_FORMAT, name, count);
        } else {
            return String.format(TITLE_DESCRIPTION_SINGLE_FORMAT, name);
        }
    }
}
