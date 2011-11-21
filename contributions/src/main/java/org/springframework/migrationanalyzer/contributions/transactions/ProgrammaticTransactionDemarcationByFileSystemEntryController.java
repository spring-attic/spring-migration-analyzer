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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

final class ProgrammaticTransactionDemarcationByFileSystemEntryController implements ByFileSystemEntryController<ProgrammaticTransactionDemarcation> {

    private static final String VIEW_NAME = "programmatic-transaction-demarcation-by-file";

    @Override
    public boolean canHandle(Class<?> resultType) {
        return ProgrammaticTransactionDemarcation.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results, OutputPathGenerator outputPathGenerator) {

        Map<String, Map<String, List<String>>> programmaticDemarcationByType = new TreeMap<String, Map<String, List<String>>>();

        for (AnalysisResultEntry<ProgrammaticTransactionDemarcation> demarcationResultEntry : results) {
            ProgrammaticTransactionDemarcation demarcation = demarcationResultEntry.getResult();

            String transactionType = demarcation.getTransactionType();

            Map<String, List<String>> demarcationForTransactionType = programmaticDemarcationByType.get(transactionType);

            if (demarcationForTransactionType == null) {
                demarcationForTransactionType = new TreeMap<String, List<String>>();
                programmaticDemarcationByType.put(transactionType, demarcationForTransactionType);
            }

            String demarcationDescription = demarcation.getType().toDisplayString();

            List<String> demarcationOfType = demarcationForTransactionType.get(demarcationDescription);

            if (demarcationOfType == null) {
                demarcationOfType = new ArrayList<String>();
                demarcationForTransactionType.put(demarcationDescription, demarcationOfType);
            }

            demarcationOfType.add(demarcation.getUsageDescription());
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("programmaticDemarcation", programmaticDemarcationByType);
        model.put("link", outputPathGenerator.generatePathFor(ProgrammaticTransactionDemarcation.class));

        return new ModelAndView(model, VIEW_NAME);
    }
}
