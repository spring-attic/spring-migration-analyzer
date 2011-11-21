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

package org.springframework.migrationanalyzer.contributions.apiusage;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.Controller;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

abstract class AbstractApiUsageController implements Controller<ApiUsage> {

    private final String viewName;

    protected AbstractApiUsageController(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public boolean canHandle(Class<?> resultType) {
        return ApiUsage.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<ApiUsage>> results, OutputPathGenerator outputPathGenerator) {
        return new ModelAndView(createModel(results, outputPathGenerator), this.viewName);
    }

    protected abstract Map<String, Object> createModel(Set<AnalysisResultEntry<ApiUsage>> results, OutputPathGenerator outputPathGenerator);

    protected final Map<String, Set<String>> recordUsages(Set<AnalysisResultEntry<ApiUsage>> resultEntries) {
        Map<String, Set<String>> apiUsages = new TreeMap<String, Set<String>>();

        for (AnalysisResultEntry<ApiUsage> usage : resultEntries) {
            ApiUsage result = usage.getResult();
            String apiName = result.getApiName();
            Set<String> usageOfApi = apiUsages.get(apiName);
            if (usageOfApi == null) {
                usageOfApi = new TreeSet<String>();
                apiUsages.put(apiName, usageOfApi);
            }
            recordApiUsage(usageOfApi, result);
        }
        return apiUsages;
    }

    private void recordApiUsage(Set<String> usageOfApi, ApiUsage apiUsage) {
        String user = apiUsage.getUser();
        if (user != null) {
            usageOfApi.add(user);
        }
    }
}
