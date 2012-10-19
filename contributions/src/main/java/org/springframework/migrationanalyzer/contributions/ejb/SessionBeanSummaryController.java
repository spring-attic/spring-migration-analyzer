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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;
import org.springframework.stereotype.Component;

@Component
final class SessionBeanSummaryController extends AbstractSessionBeanController implements SummaryController<SessionBean> {

    private static final String VIEW_NAME_SUMMARY = "session-bean-summary";

    private static final String VIEW_NAME_GUIDANCE_STATELESS = "stateless-session-bean-guidance";

    private static final String VIEW_NAME_GUIDANCE_STATEFUL = "stateful-session-bean-guidance";

    private static final String VIEW_NAME_GUIDANCE_DECLARATIVE_TRANSACTIONS = "declarative-transactions-session-bean-guidance";

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<SessionBean>> results) {
        Map<String, Object> model = new HashMap<String, Object>();

        int stateful = 0;
        int stateless = 0;

        for (AnalysisResultEntry<SessionBean> result : results) {
            if (result.getResult().getSessionType() == SessionType.STATEFUL) {
                stateful++;
            } else {
                stateless++;
            }
        }

        model.put("statelessValue", getStatelessValue(stateless));
        model.put("statefulValue", getStatefulValue(stateful));

        return new ModelAndView(model, VIEW_NAME_SUMMARY);
    }

    private String getStatelessValue(int size) {
        if (size == 1) {
            return "1 Stateless Session Bean";
        }
        return String.format("%s Stateless Session Beans", size);
    }

    private String getStatefulValue(int size) {
        if (size == 1) {
            return "1 Stateful Session Bean";
        }
        return String.format("%s Stateful Session Beans", size);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<SessionBean>> results, MigrationCost migrationCost) {
        if (migrationCost == MigrationCost.LOW) {
            return handleLowMigrationCostEntries(results);
        } else if (migrationCost == MigrationCost.MEDIUM) {
            return handleMediumMigrationCostEntries(results);
        }
        return Collections.<ModelAndView> emptyList();
    }

    private List<ModelAndView> handleLowMigrationCostEntries(Set<AnalysisResultEntry<SessionBean>> results) {
        for (AnalysisResultEntry<SessionBean> analysisResultEntry : results) {
            if (SessionType.STATELESS.equals(analysisResultEntry.getResult().getSessionType())) {
                return Arrays.asList(new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE_STATELESS));
            }
        }
        return Collections.<ModelAndView> emptyList();
    }

    private List<ModelAndView> handleMediumMigrationCostEntries(Set<AnalysisResultEntry<SessionBean>> results) {
        List<ModelAndView> modelsAndViews = new ArrayList<ModelAndView>();

        for (AnalysisResultEntry<SessionBean> analysisResultEntry : results) {
            SessionBean result = analysisResultEntry.getResult();

            Set<TransactionPropagationType> transactionPropagationTypes = result.getTransactionPropagationTypes();
            if ((transactionPropagationTypes != null)
                && (transactionPropagationTypes.contains(TransactionPropagationType.MANDATORY)
                    || transactionPropagationTypes.contains(TransactionPropagationType.REQUIRES_NEW) || transactionPropagationTypes.contains(TransactionPropagationType.REQUIRED))) {
                modelsAndViews.add(new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE_DECLARATIVE_TRANSACTIONS));
                break;
            }
        }

        for (AnalysisResultEntry<SessionBean> analysisResultEntry : results) {
            if (SessionType.STATEFUL.equals(analysisResultEntry.getResult().getSessionType())) {
                modelsAndViews.add(new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE_STATEFUL));
                break;
            }
        }

        return modelsAndViews;
    }
}
