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
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;

public class MessageDrivenBeanSummaryControllerTests {

    private final SummaryController<MessageDrivenBean> controller = new MessageDrivenBeanSummaryController();

    @Test
    public void singleMDB() {
        Set<AnalysisResultEntry<MessageDrivenBean>> results = new HashSet<AnalysisResultEntry<MessageDrivenBean>>();
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Foo", "Foo", "Bean")));
        ModelAndView modelAndView = this.controller.handle(results);
        assertEquals("message-driven-bean-summary", modelAndView.getViewName());
        assertEquals("1 Message Driven Bean", modelAndView.getModel().get("messageDrivenBeanValue"));
    }

    @Test
    public void multipleMDBs() {
        Set<AnalysisResultEntry<MessageDrivenBean>> results = new HashSet<AnalysisResultEntry<MessageDrivenBean>>();
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Foo", "Foo", "Bean")));
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Bar", "Bar", "Bean")));
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Baz", "Baz", "Bean")));
        ModelAndView modelAndView = this.controller.handle(results);
        assertEquals("message-driven-bean-summary", modelAndView.getViewName());
        assertEquals("3 Message Driven Beans", modelAndView.getModel().get("messageDrivenBeanValue"));
    }

    @Test
    public void guidance() {
        Set<AnalysisResultEntry<MessageDrivenBean>> results = new HashSet<AnalysisResultEntry<MessageDrivenBean>>();
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Foo", "Foo", "Bean")));

        List<ModelAndView> modelsAndViews = this.controller.handle(results, MigrationCost.LOW);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.MEDIUM);
        assertNotNull(modelsAndViews);
        assertEquals(1, modelsAndViews.size());

        modelsAndViews = this.controller.handle(results, MigrationCost.HIGH);
        assertNotNull(modelsAndViews);
        assertEquals(0, modelsAndViews.size());
    }

    private MessageDrivenBean createMessageDrivenBean(String ejbClass, String ejbName, String transactionType) {
        MessageDrivenBean mdb = new MessageDrivenBean();
        mdb.setEjbClass(ejbClass);
        mdb.setEjbName(ejbName);
        mdb.setTransactionType(transactionType);
        return mdb;
    }
}
