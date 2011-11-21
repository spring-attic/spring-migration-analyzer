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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
public final class StubController implements ByResultTypeController<Object>, ByFileSystemEntryController<Object>, SummaryController<Object> {

    private final List<Set<AnalysisResultEntry<Object>>> resultsHandled = new ArrayList<Set<AnalysisResultEntry<Object>>>();

    private final String viewName;

    public StubController(String viewName) {
        this.viewName = viewName;
    }

    StubController() {
        this(null);
    }

    public List<Set<AnalysisResultEntry<Object>>> getResultsHandled() {
        return this.resultsHandled;
    }

    @Override
    public boolean canHandle(Class<?> resultType) {
        return resultType.equals(Object.class);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<Object>> results, OutputPathGenerator outputPathGenerator) {
        this.resultsHandled.add(results);
        return new ModelAndView(null, this.viewName);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<Object>> results, MigrationCost migrationCost) {
        return Arrays.asList(new ModelAndView(null, this.viewName));
    }
}