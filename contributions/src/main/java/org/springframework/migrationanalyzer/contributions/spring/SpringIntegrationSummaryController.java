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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;
import org.springframework.stereotype.Component;

@Component
final class SpringIntegrationSummaryController implements SummaryController<AbstractSpringIntegration> {

    private static final String SUMMARY_DESCRIPTION_MULTIPLE_FORMAT = "%s integration: %s uses";

    private static final String SUMMARY_DESCRIPTION_SINGLE_FORMAT = "%s integration: 1 use";

    private static final String VIEW_NAME_SUMMARY = "spring-integration-summary";

    @Override
    public boolean canHandle(Class<?> resultType) {
        return (!AbstractSpringIntegration.class.equals(resultType)) && AbstractSpringIntegration.class.isAssignableFrom(resultType);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<AbstractSpringIntegration>> results, MigrationCost migrationCost) {

        Set<String> guidanceViews = new HashSet<String>();

        List<ModelAndView> modelsAndViews = new ArrayList<ModelAndView>();

        for (AnalysisResultEntry<AbstractSpringIntegration> analysisResultEntry : results) {
            AbstractSpringIntegration springIntegration = analysisResultEntry.getResult();
            if (migrationCost.equals(springIntegration.getMigrationCost()) && !guidanceViews.contains(springIntegration.getGuidanceView())) {
                guidanceViews.add(springIntegration.getGuidanceView());
                modelsAndViews.add(new ModelAndView(Collections.<String, Object> emptyMap(), springIntegration.getGuidanceView()));
            }
        }
        return modelsAndViews;
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<AbstractSpringIntegration>> results) {

        Map<String, AtomicInteger> counts = new HashMap<String, AtomicInteger>();
        Map<String, Class<?>> resultClassesByName = new HashMap<String, Class<?>>();

        for (AnalysisResultEntry<AbstractSpringIntegration> result : results) {
            String name = result.getResult().getName();
            AtomicInteger count = counts.get(name);
            if (count == null) {
                count = new AtomicInteger(0);
                counts.put(name, count);
            }
            count.incrementAndGet();

            if (!resultClassesByName.containsKey(name)) {
                resultClassesByName.put(name, result.getResult().getClass());
            }
        }

        Map<String, String> usageDescriptions = new HashMap<String, String>();

        Set<Entry<String, AtomicInteger>> entries = counts.entrySet();
        for (Entry<String, AtomicInteger> entry : entries) {
            usageDescriptions.put(entry.getKey(), getUsageDescription(entry.getKey(), entry.getValue().get()));
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("usage", usageDescriptions);
        model.put("resultClassesByName", resultClassesByName);

        return new ModelAndView(model, VIEW_NAME_SUMMARY);
    }

    private String getUsageDescription(String key, int value) {
        if (value > 1) {
            return String.format(SUMMARY_DESCRIPTION_MULTIPLE_FORMAT, key, value);
        } else {
            return String.format(SUMMARY_DESCRIPTION_SINGLE_FORMAT, key);
        }
    }
}
