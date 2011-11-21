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

public class AbztractSessionBeanControllerTests {

    private final StubSessionBeanController controller = new StubSessionBeanController();

    @SuppressWarnings("unchecked")
    @Test
    public void single() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();
        SessionBean statefulSessionBean = new SessionBean();
        statefulSessionBean.setEjbName("stateful");
        statefulSessionBean.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, statefulSessionBean));
        SessionBean statelessSessionBean = new SessionBean();
        statelessSessionBean.setEjbName("stateless");
        statelessSessionBean.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, statelessSessionBean));

        ModelAndView modelAndView = this.controller.handle(results, null);
        assertEquals("test", modelAndView.getViewName());
        String sessionBeansTitle = (String) modelAndView.getModel().get("sessionBeansTitle");
        assertNotNull(sessionBeansTitle);
        assertEquals("2 Session Beans", sessionBeansTitle);
        String statefulSessionBeansTitle = (String) modelAndView.getModel().get("statefulSessionBeansTitle");
        assertNotNull(statefulSessionBeansTitle);
        assertEquals("1 Stateful Session Bean", statefulSessionBeansTitle);
        Map<String, Map<String, String>> statefulSessionBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("statefulSessionBeans");
        assertNotNull(statefulSessionBeans);
        assertEquals(1, statefulSessionBeans.size());
        String statelessSessionBeanTitle = (String) modelAndView.getModel().get("statelessSessionBeansTitle");
        assertNotNull(statelessSessionBeanTitle);
        assertEquals("1 Stateless Session Bean", statelessSessionBeanTitle);
        Map<String, Map<String, String>> statelessSessionBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get(
            "statelessSessionBeans");
        assertNotNull(statelessSessionBeans);
        assertEquals(1, statelessSessionBeans.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multiple() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();
        SessionBean statefulSessionBean1 = new SessionBean();
        statefulSessionBean1.setEjbName("stateful1");
        statefulSessionBean1.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, statefulSessionBean1));
        SessionBean statefulSessionBean2 = new SessionBean();
        statefulSessionBean2.setEjbName("stateful2");
        statefulSessionBean2.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, statefulSessionBean2));
        SessionBean statelessSessionBean1 = new SessionBean();
        statelessSessionBean1.setEjbName("stateless1");
        statelessSessionBean1.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, statelessSessionBean1));
        SessionBean statelessSessionBean2 = new SessionBean();
        statelessSessionBean2.setEjbName("stateless2");
        statelessSessionBean2.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, statelessSessionBean2));

        ModelAndView modelAndView = this.controller.handle(results, null);
        assertEquals("test", modelAndView.getViewName());
        String sessionBeansTitle = (String) modelAndView.getModel().get("sessionBeansTitle");
        assertNotNull(sessionBeansTitle);
        assertEquals("4 Session Beans", sessionBeansTitle);
        String statefulSessionBeansTitle = (String) modelAndView.getModel().get("statefulSessionBeansTitle");
        assertNotNull(statefulSessionBeansTitle);
        assertEquals("2 Stateful Session Beans", statefulSessionBeansTitle);
        Map<String, Map<String, String>> statefulSessionBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("statefulSessionBeans");
        assertNotNull(statefulSessionBeans);
        assertEquals(2, statefulSessionBeans.size());
        String statelessSessionBeanTitle = (String) modelAndView.getModel().get("statelessSessionBeansTitle");
        assertNotNull(statelessSessionBeanTitle);
        assertEquals("2 Stateless Session Beans", statelessSessionBeanTitle);
        Map<String, Map<String, String>> statelessSessionBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get(
            "statelessSessionBeans");
        assertNotNull(statelessSessionBeans);
        assertEquals(2, statelessSessionBeans.size());
    }

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(SessionBean.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void noMetadata() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();
        SessionBean sessionBean = new SessionBean();
        sessionBean.setEjbName("testName");
        sessionBean.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, sessionBean));

        ModelAndView modelAndView = this.controller.handle(results, null);
        Map<String, Map<String, String>> sessionBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("statefulSessionBeans");
        Map<String, String> metadata = sessionBeans.get("testName");
        assertFalse(metadata.containsKey("EJB Class"));
        assertFalse(metadata.containsKey("Business Local"));
        assertFalse(metadata.containsKey("Business Remote"));
        assertFalse(metadata.containsKey("Home"));
        assertFalse(metadata.containsKey("Remote"));
        assertFalse(metadata.containsKey("Local Home"));
        assertFalse(metadata.containsKey("Local"));
        assertFalse(metadata.containsKey("Service Endpoint"));
        assertTrue(metadata.containsKey("Transaction Type"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void withMetadata() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();
        SessionBean sessionBean = new SessionBean();
        sessionBean.setSessionType(SessionType.STATEFUL);
        sessionBean.setEjbName("testName");
        sessionBean.setEjbClass("testClass");
        sessionBean.setBusinessLocal("testBusinessLocal");
        sessionBean.setBusinessRemote("businessRemote");
        sessionBean.setHome("testHome");
        sessionBean.setRemote("testRemote");
        sessionBean.setLocalHome("testLocalHome");
        sessionBean.setLocal("testLocal");
        sessionBean.setServiceEndpoint("testServiceEndpoint");
        results.add(new AnalysisResultEntry<SessionBean>(null, sessionBean));

        ModelAndView modelAndView = this.controller.handle(results, null);
        Map<String, Map<String, String>> sessionBeans = (Map<String, Map<String, String>>) modelAndView.getModel().get("statefulSessionBeans");
        Map<String, String> metadata = sessionBeans.get("testName");

        assertTrue(metadata.containsKey("EJB Class"));
        assertTrue(metadata.containsKey("Business Local"));
        assertTrue(metadata.containsKey("Business Remote"));
        assertTrue(metadata.containsKey("Home"));
        assertTrue(metadata.containsKey("Remote"));
        assertTrue(metadata.containsKey("Local Home"));
        assertTrue(metadata.containsKey("Local"));
        assertTrue(metadata.containsKey("Service Endpoint"));
        assertTrue(metadata.containsKey("Transaction Type"));
    }

    private final static class StubSessionBeanController extends AbstractModelCreatingSessionBeanController {

        public StubSessionBeanController() {
            super("test");
        }

    }
}
