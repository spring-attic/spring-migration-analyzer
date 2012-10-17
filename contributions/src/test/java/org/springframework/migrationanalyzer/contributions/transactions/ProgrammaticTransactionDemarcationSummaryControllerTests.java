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

package org.springframework.migrationanalyzer.contributions.transactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;

public class ProgrammaticTransactionDemarcationSummaryControllerTests {

    private final SummaryController<ProgrammaticTransactionDemarcation> controller = new ProgrammaticTransactionDemarcationSummaryController();

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(ProgrammaticTransactionDemarcation.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void summaryWithSingleResult() {
        HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results = new HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>>();
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "JTA", null,
            "description")));

        ModelAndView modelAndView = this.controller.handle(results);
        assertNotNull(modelAndView);
        assertEquals("programmatic-transaction-demarcation-summary", modelAndView.getViewName());

        Map<String, Object> model = modelAndView.getModel();

        assertNotNull(model);
        assertEquals(1, model.size());
        Map<String, String> summaries = (Map<String, String>) model.get("summaries");
        assertEquals("JTA: 1 type uses programmatic demarcation", summaries.get("JTA"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void summaryWithMultipleResults() {
        HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results = new HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>>();
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "DataSource",
            null, "description")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "DataSource",
            null, "description")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "JTA", null,
            "description")));

        ModelAndView modelAndView = this.controller.handle(results);
        assertNotNull(modelAndView);
        assertEquals("programmatic-transaction-demarcation-summary", modelAndView.getViewName());

        Map<String, Object> model = modelAndView.getModel();

        assertNotNull(model);
        assertEquals(1, model.size());
        Map<String, String> summaries = (Map<String, String>) model.get("summaries");
        assertEquals("DataSource: 2 types use programmatic demarcation", summaries.get("DataSource"));
        assertEquals("JTA: 1 type uses programmatic demarcation", summaries.get("JTA"));
    }

    @Test
    public void guidanceWithDataSourceResults() {
        HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results = new HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>>();
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "DataSource",
            null, "description")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "DataSource",
            null, "description")));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.HIGH);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());
    }

    @Test
    public void guidanceWithJTASourceResults() {
        HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results = new HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>>();
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "JTA", null,
            "description")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user", "JTA", null,
            "description")));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.HIGH);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());
    }
}
