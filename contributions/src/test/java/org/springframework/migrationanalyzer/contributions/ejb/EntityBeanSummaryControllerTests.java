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

package org.springframework.migrationanalyzer.contributions.ejb;

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

public class EntityBeanSummaryControllerTests {

    private final EntityBeanSummaryController controller = new EntityBeanSummaryController();

    @Test
    public void single() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        results.add(new AnalysisResultEntry<EntityBean>(null, new EntityBean()));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());
        assertEquals("entity-bean-summary", modelAndView.getViewName());
        String value = (String) modelAndView.getModel().get("value");
        assertNotNull(value);
        assertEquals("1 Entity Bean", value);
    }

    @Test
    public void multiple() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        results.add(new AnalysisResultEntry<EntityBean>(null, new EntityBean()));
        results.add(new AnalysisResultEntry<EntityBean>(null, new EntityBean()));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());
        assertEquals("entity-bean-summary", modelAndView.getViewName());
        String value = (String) modelAndView.getModel().get("value");
        assertNotNull(value);
        assertEquals("2 Entity Beans", value);
    }

    @Test
    public void canHandle() {
        assertTrue(new EntityBeanSummaryController().canHandle(EntityBean.class));
        assertFalse(new EntityBeanSummaryController().canHandle(Object.class));
    }

    @Test
    public void guidanceWithEntityBeanInResults() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        results.add(new AnalysisResultEntry<EntityBean>(null, new EntityBean()));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.HIGH);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());
    }
}
