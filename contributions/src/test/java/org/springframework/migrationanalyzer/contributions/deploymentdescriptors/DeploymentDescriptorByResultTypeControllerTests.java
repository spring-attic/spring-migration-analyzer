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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class DeploymentDescriptorByResultTypeControllerTests {

    private final ByResultTypeController<DeploymentDescriptor> controller = new DeploymentDescriptorByResultTypeController();

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(DeploymentDescriptor.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @Test
    public void viewNameIsSet() {
        ModelAndView modelAndView = this.controller.handle(Collections.<AnalysisResultEntry<DeploymentDescriptor>> emptySet());
        assertEquals("deployment-descriptor-by-result-type", modelAndView.getViewName());
    }

    @Test
    public void noDeploymentDescriptorsProducesEmptyMapInModel() {
        ModelAndView modelAndView = this.controller.handle(Collections.<AnalysisResultEntry<DeploymentDescriptor>> emptySet());

        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());

        Object object = model.get("deploymentDescriptors");
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
        resultEntries.add(new AnalysisResultEntry<DeploymentDescriptor>(null, new DeploymentDescriptor("category2",
            createFileSystemEntry("d/e/f/gg.xml"), "gg.xml")));

        ModelAndView modelAndView = this.controller.handle(resultEntries);

        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());

        Map<String, Map<String, List<FileSystemEntry>>> deploymentDescriptors = (Map<String, Map<String, List<FileSystemEntry>>>) model.get("deploymentDescriptors");
        assertEquals(2, deploymentDescriptors.size());

        Set<String> categories = deploymentDescriptors.keySet();
        Iterator<String> categoriesIterator = categories.iterator();
        assertEquals("category1", categoriesIterator.next());
        assertEquals("category2", categoriesIterator.next());

        Map<String, List<FileSystemEntry>> category1Entries = deploymentDescriptors.get("category1");
        assertEquals(1, category1Entries.size());

        List<FileSystemEntry> locations = category1Entries.get("dd.xml");
        assertNotNull(locations);

        Map<String, List<FileSystemEntry>> category2Entries = deploymentDescriptors.get("category2");
        assertEquals(2, category2Entries.size());

        Iterator<String> namesIterator = category2Entries.keySet().iterator();
        assertEquals("gg.xml", namesIterator.next());
        assertEquals("hh.xml", namesIterator.next());
    }

    private FileSystemEntry createFileSystemEntry(String name) {
        FileSystemEntry entry = mock(FileSystemEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }
}
