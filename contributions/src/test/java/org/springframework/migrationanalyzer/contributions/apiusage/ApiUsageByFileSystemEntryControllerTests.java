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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class ApiUsageByFileSystemEntryControllerTests {

    @SuppressWarnings("unchecked")
    @Test
    public void handle() {
        Set<AnalysisResultEntry<ApiUsage>> resultEntries = new HashSet<AnalysisResultEntry<ApiUsage>>();

        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.FIELD, "JMS", "", "desc2", "view", MigrationCost.MEDIUM)));

        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.ANNOTATED_FIELD, "test", "one", "desc1", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.ANNOTATED_METHOD, "test", "one", "desc2", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.ANNOTATED_METHOD_ARGUMENT, "test", "one", "desc3",
            "view", MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.ANNOTATED_TYPE, "test", "one", "desc4", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.EXTENDS_TYPE, "test", "two", "desc5", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.FIELD, "test", "one", "desc6", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.IMPLEMENTS_INTERFACE, "test", null, "desc7", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.LOCAL_VARIABLE, "test", "one", "desc8", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.METHOD_ARGUMENT, "test", "one", "desc9", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.RETURN_ARGUMENT, "test", "one", "desc10", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.SPRING_CONFIGURATION, "test", "one", "desc11", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.THROWS_EXCEPTION, "test", "one", "desc12", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.DEPLOYMENT_DESCRIPTOR, "test", "one", "desc13", "view",
            MigrationCost.MEDIUM)));

        ApiUsageByFileSystemEntryController controller = new ApiUsageByFileSystemEntryController();
        ModelAndView modelAndView = controller.handle(resultEntries);

        Map<String, Object> model = modelAndView.getModel();
        Map<String, Map<String, List<String>>> apiUsage = (Map<String, Map<String, List<String>>>) model.get("apiUsage");
        assertNotNull(apiUsage);

        Map<String, List<String>> jmsUsage = apiUsage.get("JMS");
        assertNotNull(jmsUsage);
        assertEquals(1, jmsUsage.size());
        List<String> fields = jmsUsage.get("Fields");
        assertNotNull(fields);
        assertEquals(1, fields.size());

        Map<String, List<String>> testUsage = apiUsage.get("test");
        assertNotNull(testUsage);
        assertEquals(12, testUsage.size());
        assertEntryPresent(testUsage, 1, "Annotated fields");
        assertEntryPresent(testUsage, 1, "Annotated methods");
        assertEntryPresent(testUsage, 1, "Annotated method arguments");
        assertEntryPresent(testUsage, 1, "Annotated with");
        assertEntryPresent(testUsage, 1, "Extends");
        assertEntryPresent(testUsage, 1, "Fields");
        assertEntryPresent(testUsage, 1, "Implements");
        assertEntryPresent(testUsage, 1, "Local variables");
        assertEntryPresent(testUsage, 1, "Method arguments");
        assertEntryPresent(testUsage, 1, "Return types");
        assertEntryPresent(testUsage, 2, "References");
        assertEntryPresent(testUsage, 1, "Throws declarations");
    }

    private void assertEntryPresent(Map<String, List<String>> testUsage, int expectedSize, String usageType) {
        List<String> usageDescriptions = testUsage.get(usageType);
        assertNotNull(usageDescriptions);
        assertEquals(expectedSize, usageDescriptions.size());
    }

    @Test
    public void canHandle() {
        assertTrue(new ApiUsageByFileSystemEntryController().canHandle(ApiUsage.class));
        assertFalse(new ApiUsageByFileSystemEntryController().canHandle(Object.class));
    }
}
