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

package org.springframework.migrationanalyzer.contributions.deploymentdescriptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;

public class DeploymentDescriptorSummaryControllerTests {

    private final SummaryController<DeploymentDescriptor> controller = new DeploymentDescriptorSummaryController();

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(DeploymentDescriptor.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @Test
    public void viewNameIsSet() {
        ModelAndView modelAndView = this.controller.handle(Collections.<AnalysisResultEntry<DeploymentDescriptor>> emptySet());
        assertEquals("deployment-descriptor-summary", modelAndView.getViewName());
    }

    @Test
    public void noDeploymentDescriptorsProducesEmptyMapInModel() {
        ModelAndView modelAndView = this.controller.handle(Collections.<AnalysisResultEntry<DeploymentDescriptor>> emptySet());

        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());

        Object object = model.get("deploymentDescriptorCounts");
        assertNotNull(object);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void modelGeneration() {
        Set<AnalysisResultEntry<DeploymentDescriptor>> resultEntries = new HashSet<AnalysisResultEntry<DeploymentDescriptor>>();

        resultEntries.add(new AnalysisResultEntry<DeploymentDescriptor>(null, new DeploymentDescriptor("category1",
            createFileSystemEntry("a/b/c/dd.xml"), "dd.xml")));
        resultEntries.add(new AnalysisResultEntry<DeploymentDescriptor>(null, new DeploymentDescriptor("category1",
            createFileSystemEntry("d/e/f/dd.xml"), "dd.xml")));
        resultEntries.add(new AnalysisResultEntry<DeploymentDescriptor>(null, new DeploymentDescriptor("category2",
            createFileSystemEntry("d/e/f/hh.xml"), "hh.xml")));

        ModelAndView modelAndView = this.controller.handle(resultEntries);

        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());

        Map<String, AtomicInteger> deploymentDescriptors = (Map<String, AtomicInteger>) model.get("deploymentDescriptorCounts");
        assertEquals(2, deploymentDescriptors.size());

        Iterator<Entry<String, AtomicInteger>> entries = deploymentDescriptors.entrySet().iterator();
        Entry<String, AtomicInteger> entry = entries.next();
        assertEquals("category1", entry.getKey());
        assertEquals(2, entry.getValue().get());

        entry = entries.next();
        assertEquals("category2", entry.getKey());
        assertEquals(1, entry.getValue().get());
    }

    private FileSystemEntry createFileSystemEntry(String name) {
        FileSystemEntry entry = mock(FileSystemEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }
}
