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

public class AbztractMessageDrivenBeanControllerTests {

    private final AbstractModelCreatingMessageDrivenBeanController controller = new StubMessageDrivenBeanController();

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(MessageDrivenBean.class));
        assertFalse(this.controller.canHandle(SessionBean.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void singleMDB() {
        Set<AnalysisResultEntry<MessageDrivenBean>> results = new HashSet<AnalysisResultEntry<MessageDrivenBean>>();
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.foo", "Foo", "Container")));

        ModelAndView modelAndView = this.controller.handle(results);
        Map<String, Object> model = modelAndView.getModel();

        assertEquals("1 Message Driven Bean", model.get("messageDrivenBeansTitle"));

        Map<String, Map<String, String>> mdbs = (Map<String, Map<String, String>>) model.get("messageDrivenBeans");
        assertNotNull(mdbs);
        assertEquals(1, mdbs.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multipleMDBs() {
        Set<AnalysisResultEntry<MessageDrivenBean>> results = new HashSet<AnalysisResultEntry<MessageDrivenBean>>();
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Foo", "Foo", "Container")));
        results.add(new AnalysisResultEntry<MessageDrivenBean>(null, createMessageDrivenBean("ut.Bar", "Bar", "Container")));

        ModelAndView modelAndView = this.controller.handle(results);
        Map<String, Object> model = modelAndView.getModel();
        assertEquals("2 Message Driven Beans", model.get("messageDrivenBeansTitle"));

        Map<String, Map<String, String>> mdbs = (Map<String, Map<String, String>>) model.get("messageDrivenBeans");
        assertNotNull(mdbs);
        assertEquals(2, mdbs.size());
    }

    private static final class StubMessageDrivenBeanController extends AbstractModelCreatingMessageDrivenBeanController {

        public StubMessageDrivenBeanController() {
            super("stub");
        }
    }

    private MessageDrivenBean createMessageDrivenBean(String ejbClass, String ejbName, String transactionType) {
        MessageDrivenBean mdb = new MessageDrivenBean();
        mdb.setEjbClass(ejbClass);
        mdb.setEjbName(ejbName);
        mdb.setTransactionType(transactionType);
        return mdb;
    }
}
