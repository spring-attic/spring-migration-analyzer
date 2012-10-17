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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;

final class ApiUsageByResultTypeController extends AbstractApiUsageController implements ByResultTypeController<ApiUsage> {

    private static final String VIEW_NAME = "api-by-result-type";

    ApiUsageByResultTypeController() {
        super(VIEW_NAME);
    }

    @Override
    protected Map<String, Object> createModel(Set<AnalysisResultEntry<ApiUsage>> results) {
        Map<String, Set<String>> apiUsage = recordUsages(results);

        Map<String, Set<String>> titledApiUsage = new TreeMap<String, Set<String>>();
        for (Entry<String, Set<String>> entry : apiUsage.entrySet()) {
            Set<String> value = entry.getValue();
            titledApiUsage.put(getTitle(entry.getKey(), value.size()), value);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("apiUsage", titledApiUsage);
        return model;
    }

    private String getTitle(String apiName, int size) {
        if (size == 1) {
            return String.format("%s API (1 user)", apiName);
        }
        return String.format("%s API (%s users)", apiName, size);
    }
}
