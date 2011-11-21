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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.StubOutputPathGenerator;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class ApiUsageSummaryControllerTests {

    private final ApiUsageSummaryController controller = new ApiUsageSummaryController();

    @SuppressWarnings("unchecked")
    @Test
    public void single() {
        Set<AnalysisResultEntry<ApiUsage>> results = new HashSet<AnalysisResultEntry<ApiUsage>>();
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test", "desc", "view", MigrationCost.MEDIUM)));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());
        assertEquals("api-summary", modelAndView.getViewName());
        Set<String> entries = (Set<String>) modelAndView.getModel().get("entries");
        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals("test: 1 type uses API", entries.iterator().next());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multiple() {
        Set<AnalysisResultEntry<ApiUsage>> results = new HashSet<AnalysisResultEntry<ApiUsage>>();
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test", "desc", "view", MigrationCost.MEDIUM)));
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test2", "desc", "view", MigrationCost.MEDIUM)));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());
        assertEquals("api-summary", modelAndView.getViewName());
        Set<String> entries = (Set<String>) modelAndView.getModel().get("entries");
        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals("test: 2 types use API", entries.iterator().next());
    }

    @Test
    public void canHandle() {
        assertTrue(new ApiUsageByResultTypeController().canHandle(ApiUsage.class));
        assertFalse(new ApiUsageByResultTypeController().canHandle(Object.class));
    }

    @Test
    public void guidanceWithNoMatchingCosts() {
        Set<AnalysisResultEntry<ApiUsage>> results = new HashSet<AnalysisResultEntry<ApiUsage>>();
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test", "desc", "view", MigrationCost.MEDIUM)));
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test2", "desc", "view", MigrationCost.MEDIUM)));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());
    }

    @Test
    public void guidanceWithMatchingCostsAndDuplicateViews() {
        Set<AnalysisResultEntry<ApiUsage>> results = new HashSet<AnalysisResultEntry<ApiUsage>>();
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test", "desc", "view", MigrationCost.MEDIUM)));
        results.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(null, "test", "test2", "desc", "view", MigrationCost.MEDIUM)));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());
    }
}
