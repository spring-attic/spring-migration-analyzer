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
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

@SuppressWarnings("rawtypes")
public abstract class AbstractSpringIntegrationByResultTypeControllerTests {

    private final String expectedName;

    private final Class<?> handledResultType;

    private final ByResultTypeController controller;

    AbstractSpringIntegrationByResultTypeControllerTests(String expectedName, Class<?> handledResultType, ByResultTypeController controller) {
        this.expectedName = expectedName;
        this.handledResultType = handledResultType;
        this.controller = controller;
    }

    abstract AnalysisResultEntry getAnalysisResultEntry(String path);

    @SuppressWarnings("unchecked")
    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(this.handledResultType));
        assertFalse(this.controller.canHandle(AbstractSpringIntegration.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    public void handle() {
        Set<AnalysisResultEntry> results = new HashSet<AnalysisResultEntry>();
        results.add(getAnalysisResultEntry("alpha/bravo/test-context.xml"));
        results.add(getAnalysisResultEntry("charlie/delta/test-context.xml"));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());

        assertEquals("spring-integration-by-result-type", modelAndView.getViewName());

        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);

        assertEquals(this.expectedName, model.get("name"));

        Map<String, String> users = (Map<String, String>) model.get("users");
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    private static final class StubOutputPathGenerator implements OutputPathGenerator {

        @Override
        public String generatePathFor(Class<?> resultType) {
            return null;
        }

        @Override
        public String generatePathFor(FileSystemEntry fileSystemEntry) {
            return fileSystemEntry.getName();
        }

        @Override
        public String generatePathFor(String fileName) {
            return null;
        }

        @Override
        public String generatePathForFileSystemEntryContents() {
            return null;
        }

        @Override
        public String generatePathForIndex() {
            return null;
        }

        @Override
        public String generatePathForResultTypeContents() {
            return null;
        }

        @Override
        public String generatePathForSummary() {
            return null;
        }

    }
}
