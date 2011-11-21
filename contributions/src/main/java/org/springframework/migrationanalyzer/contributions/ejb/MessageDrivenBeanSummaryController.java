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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.SummaryController;

final class MessageDrivenBeanSummaryController extends AbstractMessageDrivenBeanController implements SummaryController<MessageDrivenBean> {

    private static final String VIEW_NAME_SUMMARY = "message-driven-bean-summary";

    private static final String VIEW_NAME_GUIDANCE = "message-driven-bean-guidance";

    private static final String VIEW_NAME_GUIDANCE_DECLARATIVE_TRANSACTIONS = "declarative-transactions-message-driven-bean-guidance";

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<MessageDrivenBean>> results, OutputPathGenerator outputPathGenerator) {
        return new ModelAndView(createModel(results, outputPathGenerator), VIEW_NAME_SUMMARY);
    }

    private Map<String, Object> createModel(Set<AnalysisResultEntry<MessageDrivenBean>> results, OutputPathGenerator outputPathGenerator) {
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("messageDrivenBeanValue", getMessageDrivenBeanValue(results.size()));
        model.put("link", outputPathGenerator.generatePathFor(MessageDrivenBean.class));
        return model;
    }

    private String getMessageDrivenBeanValue(int size) {
        if (size == 1) {
            return "1 Message Driven Bean";
        }
        return String.format("%s Message Driven Beans", size);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<MessageDrivenBean>> results, MigrationCost migrationCost) {
        if (migrationCost == MigrationCost.MEDIUM) {
            List<ModelAndView> modelsAndViews = new ArrayList<ModelAndView>();
            modelsAndViews.add(new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE));

            for (AnalysisResultEntry<MessageDrivenBean> analysisResultEntry : results) {
                MessageDrivenBean result = analysisResultEntry.getResult();

                Set<TransactionPropagationType> transactionPropagationTypes = result.getTransactionPropagationTypes();
                if ((transactionPropagationTypes != null)
                    && (transactionPropagationTypes.contains(TransactionPropagationType.MANDATORY)
                        || transactionPropagationTypes.contains(TransactionPropagationType.REQUIRES_NEW) || transactionPropagationTypes.contains(TransactionPropagationType.REQUIRED))) {

                    modelsAndViews.add((new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE_DECLARATIVE_TRANSACTIONS)));
                    break;
                }
            }
            return modelsAndViews;
        }

        return Collections.<ModelAndView> emptyList();
    }
}
