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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;
import org.springframework.stereotype.Component;

@Component
final class ApiUsageSummaryController extends AbstractApiUsageController implements SummaryController<ApiUsage> {

    private static final String VIEW_NAME = "api-summary";

    ApiUsageSummaryController() {
        super(VIEW_NAME);
    }

    @Override
    protected Map<String, Object> createModel(Set<AnalysisResultEntry<ApiUsage>> results) {
        Map<String, Set<String>> apiUsage = recordUsages(results);

        Set<String> entries = new TreeSet<String>();
        for (Entry<String, Set<String>> entry : apiUsage.entrySet()) {
            entries.add(getEntry(entry.getKey(), entry.getValue().size()));
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("entries", entries);
        return model;
    }

    private String getEntry(String apiName, int size) {
        if (size == 1) {
            return String.format("%s: 1 type uses API", apiName);
        }
        return String.format("%s: %s types use API", apiName, size);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<ApiUsage>> results, MigrationCost migrationCost) {
        Set<String> guidanceViews = new HashSet<String>();

        for (AnalysisResultEntry<ApiUsage> analysisResultEntry : results) {
            ApiUsage result = analysisResultEntry.getResult();
            if (result.getMigrationCost() == migrationCost) {
                guidanceViews.add(result.getGuidanceView());
            }
        }

        List<ModelAndView> modelsAndViews = new ArrayList<ModelAndView>();

        for (String guidanceView : guidanceViews) {
            modelsAndViews.add(new ModelAndView(Collections.<String, Object> emptyMap(), guidanceView));
        }

        return modelsAndViews;
    }
}
