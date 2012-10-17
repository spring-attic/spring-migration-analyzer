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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.contributions.StubFileSystemEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;

public class SpringIntegrationSummaryControllerTests {

    private final SummaryController<AbstractSpringIntegration> controller = new SpringIntegrationSummaryController();

    @Test
    public void canHandle() {
        assertFalse(this.controller.canHandle(AbstractSpringIntegration.class));
        assertFalse(this.controller.canHandle(Object.class));

        assertTrue(this.controller.canHandle(SpringEjbIntegration.class));
    }

    @Test
    public void handleProducesOneModelAndViewForEachTypeOfIntegrationGuidance() {
        Set<AnalysisResultEntry<AbstractSpringIntegration>> results = new HashSet<AnalysisResultEntry<AbstractSpringIntegration>>();

        StubFileSystemEntry fileSystemEntry = new StubFileSystemEntry("location1");
        results.add(new AnalysisResultEntry<AbstractSpringIntegration>(fileSystemEntry, new SpringAlphaIntegration("user1", fileSystemEntry)));

        fileSystemEntry = new StubFileSystemEntry("location2");
        results.add(new AnalysisResultEntry<AbstractSpringIntegration>(fileSystemEntry, new SpringAlphaIntegration("user2", fileSystemEntry)));

        fileSystemEntry = new StubFileSystemEntry("location3");
        results.add(new AnalysisResultEntry<AbstractSpringIntegration>(fileSystemEntry, new SpringBravoIntegration("user3", fileSystemEntry)));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertEquals(2, modelsAndViews.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleForSummary() {
        Set<AnalysisResultEntry<AbstractSpringIntegration>> results = new HashSet<AnalysisResultEntry<AbstractSpringIntegration>>();

        StubFileSystemEntry fileSystemEntry = new StubFileSystemEntry("location1");
        results.add(new AnalysisResultEntry<AbstractSpringIntegration>(fileSystemEntry, new SpringAlphaIntegration("user1", fileSystemEntry)));

        fileSystemEntry = new StubFileSystemEntry("location2");
        results.add(new AnalysisResultEntry<AbstractSpringIntegration>(fileSystemEntry, new SpringAlphaIntegration("user2", fileSystemEntry)));

        fileSystemEntry = new StubFileSystemEntry("location3");
        results.add(new AnalysisResultEntry<AbstractSpringIntegration>(fileSystemEntry, new SpringBravoIntegration("user3", fileSystemEntry)));

        ModelAndView modelAndView = this.controller.handle(results);
        assertNotNull(modelAndView);

        assertEquals("spring-integration-summary", modelAndView.getViewName());

        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);

        Map<String, String> usages = (Map<String, String>) model.get("usage");
        assertNotNull(usages);
        assertEquals(2, usages.size());

        assertEquals("Alpha integration: 2 uses", usages.get("Alpha"));
        assertEquals("Bravo integration: 1 use", usages.get("Bravo"));

        Map<String, Class<?>> resultClassesByName = (Map<String, Class<?>>) model.get("resultClassesByName");
        assertNotNull(resultClassesByName);
        assertEquals(2, resultClassesByName.size());

    }

    private static final class SpringAlphaIntegration extends AbstractSpringIntegration {

        SpringAlphaIntegration(String userName, FileSystemEntry userLocation) {
            super("Alpha", "alpha-guidance", MigrationCost.LOW, userName, userLocation);
        }

    }

    private static final class SpringBravoIntegration extends AbstractSpringIntegration {

        SpringBravoIntegration(String userName, FileSystemEntry userLocation) {
            super("Bravo", "bravo-guidance", MigrationCost.LOW, userName, userLocation);
        }

    }
}
