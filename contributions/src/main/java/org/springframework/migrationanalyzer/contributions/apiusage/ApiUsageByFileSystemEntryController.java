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

package org.springframework.migrationanalyzer.contributions.apiusage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.stereotype.Component;

@Component
final class ApiUsageByFileSystemEntryController implements ByFileSystemEntryController<ApiUsage> {

    private static final String VIEW_NAME = "api-by-file";

    @Override
    public boolean canHandle(Class<?> resultType) {
        return ApiUsage.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<ApiUsage>> results) {

        Map<String, Map<String, List<String>>> usageByApiNameAndUsageType = new TreeMap<String, Map<String, List<String>>>();

        for (AnalysisResultEntry<ApiUsage> analysisResultEntry : results) {
            ApiUsage apiUsage = analysisResultEntry.getResult();

            Map<String, List<String>> usageByUsageType = usageByApiNameAndUsageType.get(apiUsage.getApiName());

            if (usageByUsageType == null) {
                usageByUsageType = new TreeMap<String, List<String>>();
                usageByApiNameAndUsageType.put(apiUsage.getApiName(), usageByUsageType);
            }

            String usageDescription = getUsageDescription(apiUsage.getType());

            List<String> usageOfType = usageByUsageType.get(usageDescription);

            if (usageOfType == null) {
                usageOfType = new ArrayList<String>();
                usageByUsageType.put(usageDescription, usageOfType);
            }

            usageOfType.add(apiUsage.getUsageDescription());
        }

        sortLists(usageByApiNameAndUsageType);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("apiUsage", usageByApiNameAndUsageType);

        return new ModelAndView(model, VIEW_NAME);
    }

    private void sortLists(Map<String, Map<String, List<String>>> usageByApiNameAndUsageType) {
        for (Entry<String, Map<String, List<String>>> entry : usageByApiNameAndUsageType.entrySet()) {
            for (Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {
                Collections.sort(entry2.getValue());
            }
        }
    }

    // CHECKSTYLE:OFF

    private String getUsageDescription(ApiUsageType apiUsageType) {
        switch (apiUsageType) {
            case EXTENDS_TYPE:
                return "Extends";
            case FIELD:
                return "Fields";
            case LOCAL_VARIABLE:
                return "Local variables";
            case IMPLEMENTS_INTERFACE:
                return "Implements";
            case METHOD_ARGUMENT:
                return "Method arguments";
            case RETURN_ARGUMENT:
                return "Return types";
            case THROWS_EXCEPTION:
                return "Throws declarations";
            case ANNOTATED_FIELD:
                return "Annotated fields";
            case ANNOTATED_METHOD:
                return "Annotated methods";
            case ANNOTATED_METHOD_ARGUMENT:
                return "Annotated method arguments";
            case ANNOTATED_TYPE:
                return "Annotated with";
            case SPRING_CONFIGURATION:
            case DEPLOYMENT_DESCRIPTOR:
                return "References";
        }

        throw new IllegalArgumentException(apiUsageType + " is not a valid usage type");
    }

    // CHECKSTYLE:ON
}
