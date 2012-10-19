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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;

public class DeploymentDescriptorDetectingEntryAnalyzerTests {

    private final DeploymentDescriptorDetectingEntryAnalyzer analyzer = new DeploymentDescriptorDetectingEntryAnalyzer();

    @Test
    public void noMatchReturnsAnEmptySet() throws AnalysisFailedException {
        Set<DeploymentDescriptor> analysis = this.analyzer.analyze(createFileSystemEntry("a/b/c/d/e/no-match.xml"));
        assertNotNull(analysis);
        assertEquals(0, analysis.size());
    }

    @Test
    public void javaEEDescriptorDetection() throws AnalysisFailedException {
        assertDescriptorDetected("application.xml", "Java EE");
        assertDescriptorDetected("application-client.xml", "Java EE");
        assertDescriptorDetected("ejb-jar.xml", "Java EE");
        assertDescriptorDetected("ra.xml", "Java EE");
        assertDescriptorDetected("web.xml", "Java EE");
        assertDescriptorDetected("webservices.xml", "Java EE");
    }

    @Test
    public void jbossDescriptorDetection() throws AnalysisFailedException {
        assertDescriptorDetected("jaws.xml", "JBoss");
        assertDescriptorDetected("jboss.xml", "JBoss");
        assertDescriptorDetected("jbosscmp-jdbc.xml", "JBoss");
        assertDescriptorDetected("jboss-service.xml", "JBoss");
        assertDescriptorDetected("jboss-web.xml", "JBoss");
    }

    @Test
    public void webLogicDescriptorDetection() throws AnalysisFailedException {
        assertDescriptorDetected("persistence-configuration.xml", "WebLogic");
        assertDescriptorDetected("weblogic.xml", "WebLogic");
        assertDescriptorDetected("weblogic-application.xml", "WebLogic");
        assertDescriptorDetected("weblogic-cmp-rdbms-jar.xml", "WebLogic");
        assertDescriptorDetected("weblogic-ejb-jar.xml", "WebLogic");
        assertDescriptorDetected("weblogic-ra.xml", "WebLogic");
        assertDescriptorDetected("weblogic-webservices.xml", "WebLogic");
        assertDescriptorDetected("weblogic-wsee-clientHandlerChain.xml", "WebLogic");
        assertDescriptorDetected("weblogic-wsee-standaloneclient.xml", "WebLogic");
        assertDescriptorDetected("webservice-policy-ref.xml", "WebLogic");
    }

    @Test
    public void webSphereDescriptorDetection() throws AnalysisFailedException {
        assertDescriptorDetected("client-resource.xmi", "WebSphere");
        assertDescriptorDetected("ibm-application-bnd.xmi", "WebSphere");
        assertDescriptorDetected("ibm-application-bnd.xml", "WebSphere");
        assertDescriptorDetected("ibm-application-client-bnd.xmi", "WebSphere");
        assertDescriptorDetected("ibm-application-client-bnd.xml", "WebSphere");
        assertDescriptorDetected("ibm-application-client-ext.xmi", "WebSphere");
        assertDescriptorDetected("ibm-application-client-ext.xml", "WebSphere");
        assertDescriptorDetected("ibm-application-ext.xmi", "WebSphere");
        assertDescriptorDetected("ibm-application-ext.xml", "WebSphere");
        assertDescriptorDetected("ibm-ejb-access-bean.xml", "WebSphere");
        assertDescriptorDetected("ibm-ejb-jar-bnd.xmi", "WebSphere");
        assertDescriptorDetected("ibm-ejb-jar-bnd.xml", "WebSphere");
        assertDescriptorDetected("ibm-ejb-jar-ext.xmi", "WebSphere");
        assertDescriptorDetected("ibm-ejb-jar-ext.xml", "WebSphere");
        assertDescriptorDetected("ibm-ejb-jar-ext-pme.xmi", "WebSphere");
        assertDescriptorDetected("ibm-ejb-jar-ext-pme.xml", "WebSphere");
        assertDescriptorDetected("ibm-webservices-bnd.xmi", "WebSphere");
        assertDescriptorDetected("ibm-webservices-ext.xmi", "WebSphere");
        assertDescriptorDetected("ibm-web-bnd.xmi", "WebSphere");
        assertDescriptorDetected("ibm-web-bnd.xml", "WebSphere");
        assertDescriptorDetected("ibm-web-ext.xmi", "WebSphere");
        assertDescriptorDetected("ibm-web-ext.xml", "WebSphere");
        assertDescriptorDetected("ibm-web-ext-pme.xmi", "WebSphere");
        assertDescriptorDetected("ibm-web-ext-pme.xml", "WebSphere");
        assertDescriptorDetected("j2c_plugin.xml", "WebSphere");
    }

    private void assertDescriptorDetected(String name, String expectedCategory) throws AnalysisFailedException {
        FileSystemEntry fileSystemEntry = createFileSystemEntry("foo/" + name);
        Set<DeploymentDescriptor> analysis = this.analyzer.analyze(fileSystemEntry);
        assertNotNull(analysis);
        assertEquals(1, analysis.size());

        assertDeploymentDescriptor(expectedCategory, name, fileSystemEntry.getName(), analysis.iterator().next());
    }

    private void assertDeploymentDescriptor(String expectedCategory, String expectedName, String expectedLocation,
        DeploymentDescriptor actualDescriptor) {
        assertEquals(expectedCategory, actualDescriptor.getCategory());
        assertEquals(expectedName, actualDescriptor.getName());
        assertEquals(expectedLocation, actualDescriptor.getLocation().getName());
    }

    private FileSystemEntry createFileSystemEntry(String name) {
        FileSystemEntry entry = mock(FileSystemEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }
}
