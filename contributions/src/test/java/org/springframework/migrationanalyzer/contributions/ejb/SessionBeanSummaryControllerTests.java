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

public class SessionBeanSummaryControllerTests {

    private final SessionBeanSummaryController controller = new SessionBeanSummaryController();

    @Test
    public void singles() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();

        SessionBean stateless = new SessionBean();
        stateless.setEjbName("stateless");
        stateless.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateless));

        SessionBean stateful = new SessionBean();
        stateful.setEjbName("stateful");
        stateful.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateful));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());
        assertEquals("session-bean-summary", modelAndView.getViewName());
        String statelessValue = (String) modelAndView.getModel().get("statelessValue");
        assertNotNull(statelessValue);
        assertEquals("1 Stateless Session Bean", statelessValue);
        String statefulValue = (String) modelAndView.getModel().get("statefulValue");
        assertNotNull(statefulValue);
        assertEquals("1 Stateful Session Bean", statefulValue);
    }

    @Test
    public void multiples() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();

        SessionBean stateless1 = new SessionBean();
        stateless1.setEjbName("stateless1");
        stateless1.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateless1));

        SessionBean stateless2 = new SessionBean();
        stateless2.setEjbName("stateless2");
        stateless2.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateless2));

        SessionBean stateful1 = new SessionBean();
        stateful1.setEjbName("stateful1");
        stateful1.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateful1));

        SessionBean stateful2 = new SessionBean();
        stateful2.setEjbName("stateful2");
        stateful2.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateful2));

        ModelAndView modelAndView = this.controller.handle(results, new StubOutputPathGenerator());
        assertEquals("session-bean-summary", modelAndView.getViewName());
        String statelessValue = (String) modelAndView.getModel().get("statelessValue");
        assertNotNull(statelessValue);
        assertEquals("2 Stateless Session Beans", statelessValue);
        String statefulValue = (String) modelAndView.getModel().get("statefulValue");
        assertNotNull(statefulValue);
        assertEquals("2 Stateful Session Beans", statefulValue);
    }

    @Test
    public void canHandle() {
        assertTrue(new SessionBeanSummaryController().canHandle(SessionBean.class));
        assertFalse(new SessionBeanSummaryController().canHandle(Object.class));
    }

    @Test
    public void guidanceWithStatelessBean() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();

        SessionBean stateless1 = new SessionBean();
        stateless1.setEjbName("stateless1");
        stateless1.setSessionType(SessionType.STATELESS);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateless1));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.HIGH);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());
    }

    @Test
    public void guidanceWithStatefulBean() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();

        SessionBean stateful1 = new SessionBean();
        stateful1.setEjbName("stateful1");
        stateful1.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, stateful1));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(2, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.HIGH);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());
    }

    @Test
    public void guidanceWithStatefulBMTBean() {
        Set<AnalysisResultEntry<SessionBean>> results = new HashSet<AnalysisResultEntry<SessionBean>>();

        SessionBean bmtBean = new SessionBean();
        bmtBean.setEjbName("bmt1");
        bmtBean.setTransactionType("Bean");
        bmtBean.setSessionType(SessionType.STATEFUL);
        results.add(new AnalysisResultEntry<SessionBean>(null, bmtBean));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());
    }
}
