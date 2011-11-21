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

package org.springframework.migrationanalyzer.render.support.html;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.StubController;
import org.springframework.migrationanalyzer.render.SummaryController;

@SuppressWarnings("rawtypes")
public class HtmlRenderEngineSubFactoryTests {

    private final Set<Class<? extends ByResultTypeController>> byResultControllerClasses = new HashSet<Class<? extends ByResultTypeController>>();

    private final Set<Class<? extends ByFileSystemEntryController>> byFileSystemEntryClasses = new HashSet<Class<? extends ByFileSystemEntryController>>();

    private final Set<Class<? extends SummaryController>> summaryClasses = new HashSet<Class<? extends SummaryController>>();

    @Before
    public void setupContollers() {
        this.byResultControllerClasses.add(StubController.class);
        this.byFileSystemEntryClasses.add(StubController.class);
    }

    @Test
    public void createRenderEngine() {
        HtmlRenderEngineSubFactory factory = createRenderingFactory();
        factory.create("outputPath");
    }

    @Test
    public void canRender() {
        assertTrue(createRenderingFactory().canRender("html"));
        assertFalse(createRenderingFactory().canRender("test"));
    }

    private HtmlRenderEngineSubFactory createRenderingFactory() {
        return new HtmlRenderEngineSubFactory(this.byResultControllerClasses, this.byFileSystemEntryClasses, this.summaryClasses);
    }
}
