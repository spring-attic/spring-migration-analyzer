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
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.StubFileSystemEntry;
import org.springframework.migrationanalyzer.contributions.StubOutputPathGenerator;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class SpringConfigurationSummaryControllerTests {

    private final SpringConfigurationSummaryController controller = new SpringConfigurationSummaryController();

    @Test
    public void canHandle() {
        assertFalse(this.controller.canHandle(Object.class));
        assertTrue(this.controller.canHandle(SpringConfiguration.class));
    }

    @Test
    public void viewNameIsSet() {
        ModelAndView modelAndView = this.controller.handle(Collections.<AnalysisResultEntry<SpringConfiguration>> emptySet(),
            new StubOutputPathGenerator());
        assertEquals("spring-configuration-summary", modelAndView.getViewName());
    }

    @Test
    public void modelCreation() {
        Set<AnalysisResultEntry<SpringConfiguration>> resultEntries = new HashSet<AnalysisResultEntry<SpringConfiguration>>();

        resultEntries.add(new AnalysisResultEntry<SpringConfiguration>(new StubFileSystemEntry("foo"), new SpringConfiguration(null, null)));
        resultEntries.add(new AnalysisResultEntry<SpringConfiguration>(new StubFileSystemEntry("bar"), new SpringConfiguration(null, null)));
        resultEntries.add(new AnalysisResultEntry<SpringConfiguration>(new StubFileSystemEntry("baz"), new SpringConfiguration(null, null)));

        ModelAndView modelAndView = this.controller.handle(resultEntries, new StubOutputPathGenerator());
        Map<String, Object> model = modelAndView.getModel();
        assertEquals(2, model.size());
        assertEquals(3, model.get("springConfigurationCount"));
        assertEquals("testPath", model.get("link"));
    }

}
