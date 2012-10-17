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

package org.springframework.migrationanalyzer.contributions.transactions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;

final class ProgrammaticTransactionDemarcationSummaryController implements SummaryController<ProgrammaticTransactionDemarcation> {

    private static final String VIEW_NAME = "programmatic-transaction-demarcation-summary";

    private static final String VIEW_NAME_GUIDANCE_JTA = "programmatic-jta-transaction-demarcation-guidance";

    private static final String VIEW_NAME_GUIDANCE_DATASOURCE = "programmatic-datasource-transaction-demarcation-guidance";

    @Override
    public boolean canHandle(Class<?> resultType) {
        return ProgrammaticTransactionDemarcation.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results) {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, AtomicInteger> demarcationCountsByTransactionType = determineCountsByTransactionType(results);
        Map<String, String> summaries = getDemarcationSummariesByTransactionType(demarcationCountsByTransactionType);
        model.put("summaries", summaries);

        return new ModelAndView(model, VIEW_NAME);
    }

    private Map<String, AtomicInteger> determineCountsByTransactionType(Set<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results) {
        Map<String, AtomicInteger> counts = new HashMap<String, AtomicInteger>();

        for (AnalysisResultEntry<ProgrammaticTransactionDemarcation> result : results) {
            String transactionType = result.getResult().getTransactionType();
            AtomicInteger count = counts.get(transactionType);
            if (count == null) {
                count = new AtomicInteger(0);
                counts.put(transactionType, count);
            }
            count.incrementAndGet();
        }

        return counts;
    }

    private Map<String, String> getDemarcationSummariesByTransactionType(Map<String, AtomicInteger> demarcationCounts) {
        Map<String, String> summaries = new TreeMap<String, String>();

        Set<Entry<String, AtomicInteger>> entries = demarcationCounts.entrySet();
        for (Entry<String, AtomicInteger> entry : entries) {
            summaries.put(entry.getKey(), getSummary(entry.getKey(), entry.getValue().get()));
        }

        return summaries;
    }

    private String getSummary(String transactionType, int count) {

        if (count == 1) {
            return String.format("%s: 1 type uses programmatic demarcation", transactionType);
        }
        return String.format("%s: %s types use programmatic demarcation", transactionType, count);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results, MigrationCost migrationCost) {
        if (migrationCost == MigrationCost.LOW) {
            for (AnalysisResultEntry<ProgrammaticTransactionDemarcation> analysisResultEntry : results) {
                String transactionType = analysisResultEntry.getResult().getTransactionType();
                if (ProgrammaticDataSourceTransactionDemarcationDetector.TRANSACTION_TYPE_DATASOURCE.equals(transactionType)) {
                    return Arrays.asList(new ModelAndView[] { new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE_DATASOURCE) });
                }
            }
        } else if (migrationCost == MigrationCost.MEDIUM) {
            for (AnalysisResultEntry<ProgrammaticTransactionDemarcation> analysisResultEntry : results) {
                String transactionType = analysisResultEntry.getResult().getTransactionType();
                if (ProgrammaticJtaTransactionDemarcationDetector.TRANSACTION_TYPE_JTA.equals(transactionType)) {
                    return Arrays.asList(new ModelAndView[] { new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE_JTA) });
                }
            }
        }
        return Collections.<ModelAndView> emptyList();
    }
}
