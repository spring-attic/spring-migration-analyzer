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
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class AbztractEntityBeanControllerTests {

    private final StubEntityBeanController controller = new StubEntityBeanController();

    @SuppressWarnings("unchecked")
    @Test
    public void single() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        EntityBean entityBean = new EntityBean();
        entityBean.setEjbName("testName");
        results.add(new AnalysisResultEntry<EntityBean>(null, entityBean));

        ModelAndView modelAndView = this.controller.handle(results);
        assertEquals("test", modelAndView.getViewName());
        String title = (String) modelAndView.getModel().get("title");
        assertNotNull(title);
        assertEquals("1 Entity Bean", title);
        Map<String, Map<String, String>> entityBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("entityBeans");
        assertNotNull(entityBeans);
        assertEquals(1, entityBeans.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multiple() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        EntityBean entityBean1 = new EntityBean();
        entityBean1.setEjbName("testName1");
        results.add(new AnalysisResultEntry<EntityBean>(null, entityBean1));
        EntityBean entityBean2 = new EntityBean();
        entityBean2.setEjbName("testName2");
        results.add(new AnalysisResultEntry<EntityBean>(null, entityBean2));

        ModelAndView modelAndView = this.controller.handle(results);
        assertEquals("test", modelAndView.getViewName());
        String title = (String) modelAndView.getModel().get("title");
        assertNotNull(title);
        assertEquals("2 Entity Beans", title);
        Map<String, Map<String, String>> entityBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("entityBeans");
        assertNotNull(entityBeans);
        assertEquals(2, entityBeans.size());
    }

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(EntityBean.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void noMetadata() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        EntityBean entityBean = new EntityBean();
        entityBean.setEjbName("testName");
        results.add(new AnalysisResultEntry<EntityBean>(null, entityBean));

        ModelAndView modelAndView = this.controller.handle(results);
        Map<String, Map<String, String>> entityBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("entityBeans");
        Map<String, String> metadata = entityBeans.get("testName");
        assertFalse(metadata.containsKey("EJB Class"));
        assertFalse(metadata.containsKey("Home"));
        assertFalse(metadata.containsKey("Remote"));
        assertFalse(metadata.containsKey("Local Home"));
        assertFalse(metadata.containsKey("Local"));
        assertFalse(metadata.containsKey("Persistence Type"));
        assertFalse(metadata.containsKey("Primary Key Class"));
        assertTrue(metadata.containsKey("Reentrant"));
        assertFalse(metadata.containsKey("CMP Version"));
        assertFalse(metadata.containsKey("CMP Fields"));
        assertFalse(metadata.containsKey("Abstract Schema Name"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void withMetadata() {
        Set<AnalysisResultEntry<EntityBean>> results = new HashSet<AnalysisResultEntry<EntityBean>>();
        EntityBean entityBean = new EntityBean();
        entityBean.setEjbName("testName");
        entityBean.setEjbClass("testClass");
        entityBean.setHome("testHome");
        entityBean.setRemote("testRemote");
        entityBean.setLocalHome("testLocalHome");
        entityBean.setLocal("testLocal");
        entityBean.setPersistenceType("testPersistenceType");
        entityBean.setPrimaryKeyClass("testPrimaryKeyClass");
        entityBean.setCmpVersion("testVersion");
        entityBean.addCmpField("testCmpField");
        entityBean.setAbstractSchemaName("testAbstractSchemaName");
        results.add(new AnalysisResultEntry<EntityBean>(null, entityBean));

        ModelAndView modelAndView = this.controller.handle(results);
        Map<String, Map<String, String>> entityBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("entityBeans");
        Map<String, String> metadata = entityBeans.get("testName");
        assertTrue(metadata.containsKey("EJB Class"));
        assertTrue(metadata.containsKey("Home"));
        assertTrue(metadata.containsKey("Remote"));
        assertTrue(metadata.containsKey("Local Home"));
        assertTrue(metadata.containsKey("Local"));
        assertTrue(metadata.containsKey("Persistence Type"));
        assertTrue(metadata.containsKey("Primary Key Class"));
        assertTrue(metadata.containsKey("Reentrant"));
        assertTrue(metadata.containsKey("CMP Version"));
        assertTrue(metadata.containsKey("CMP Fields"));
        assertTrue(metadata.containsKey("Abstract Schema Name"));
    }

    private final static class StubEntityBeanController extends AbstractModelCreatingEntityBeanController {

        public StubEntityBeanController() {
            super("test");
        }

    }
}
