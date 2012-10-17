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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;
import org.springframework.migrationanalyzer.contributions.util.StringUtils;
import org.springframework.migrationanalyzer.render.ModelAndView;

abstract class AbstractModelCreatingMessageDrivenBeanController extends AbstractMessageDrivenBeanController {

    private final String viewName;

    protected AbstractModelCreatingMessageDrivenBeanController(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<MessageDrivenBean>> results) {
        return new ModelAndView(createModel(results), this.viewName);
    }

    protected final Map<String, Object> createModel(Set<AnalysisResultEntry<MessageDrivenBean>> results) {
        Map<String, Map<String, String>> messageDrivenBeans = new TreeMap<String, Map<String, String>>();

        for (AnalysisResultEntry<MessageDrivenBean> analysisResultEntry : results) {
            MessageDrivenBean messageDrivenBean = analysisResultEntry.getResult();

            Map<String, String> metadata = new TreeMap<String, String>();
            addEjbClass(messageDrivenBean, metadata);
            addTransactionType(messageDrivenBean, metadata);

            if ("Container".equals(messageDrivenBean.getTransactionType())) {
                addTransactionPropagationTypes(messageDrivenBean, metadata);
            }

            messageDrivenBeans.put(messageDrivenBean.getEjbName(), metadata);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("messageDrivenBeansTitle", getTitle(results.size()));
        model.put("messageDrivenBeans", messageDrivenBeans);

        return model;
    }

    private String getTitle(int size) {
        if (size == 1) {
            return "1 Message Driven Bean";
        }
        return String.format("%s Message Driven Beans", size);
    }

    private void addEjbClass(MessageDrivenBean messageDrivenBean, Map<String, String> metadata) {
        String ejbClass = messageDrivenBean.getEjbClass();
        if (ejbClass != null) {
            metadata.put("EJB Class", ejbClass);
        }
    }

    private void addTransactionType(MessageDrivenBean messageDrivenBean, Map<String, String> metadata) {
        metadata.put("Transaction Type", messageDrivenBean.getTransactionType());
    }

    private void addTransactionPropagationTypes(MessageDrivenBean messageDrivenBean, Map<String, String> metadata) {
        Set<TransactionPropagationType> transactionPropagationTypes = messageDrivenBean.getTransactionPropagationTypes();
        if (!transactionPropagationTypes.isEmpty()) {
            metadata.put("Declarative transactions", StringUtils.toSeparatedString(transactionPropagationTypes, ", "));
        }
    }
}
