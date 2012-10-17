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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.StubFileSystemEntry;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class SpringConfigurationByResultTypeControllerTests {

    private final SpringConfigurationByResultTypeController controller = new SpringConfigurationByResultTypeController();

    @Test
    public void canHandle() {
        assertFalse(this.controller.canHandle(Object.class));
        assertTrue(this.controller.canHandle(SpringConfiguration.class));
    }

    @Test
    public void viewNameIsSet() {
        ModelAndView modelAndView = this.controller.handle(Collections.<AnalysisResultEntry<SpringConfiguration>> emptySet());
        assertEquals("spring-configuration-by-result-type", modelAndView.getViewName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void modelCreation() {
        Set<AnalysisResultEntry<SpringConfiguration>> resultEntries = new HashSet<AnalysisResultEntry<SpringConfiguration>>();

        StubFileSystemEntry location = new StubFileSystemEntry("foo");
        resultEntries.add(new AnalysisResultEntry<SpringConfiguration>(location, new SpringConfiguration(location, "foo")));
        location = new StubFileSystemEntry("bar");
        resultEntries.add(new AnalysisResultEntry<SpringConfiguration>(location, new SpringConfiguration(location, "bar")));
        location = new StubFileSystemEntry("baz");
        resultEntries.add(new AnalysisResultEntry<SpringConfiguration>(location, new SpringConfiguration(location, "baz")));

        ModelAndView modelAndView = this.controller.handle(resultEntries);
        Map<String, Object> model = modelAndView.getModel();
        assertEquals(1, model.size());

        Map<String, String> springConfigurations = (Map<String, String>) model.get("springConfigurations");
        assertNotNull(springConfigurations);

        assertEquals(3, springConfigurations.size());
        assertNotNull(springConfigurations.get("foo"));
        assertNotNull(springConfigurations.get("bar"));
        assertNotNull(springConfigurations.get("baz"));
    }

}
