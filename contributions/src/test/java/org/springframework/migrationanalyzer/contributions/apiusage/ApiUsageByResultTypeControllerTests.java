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
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class ApiUsageByResultTypeControllerTests {

    @SuppressWarnings("unchecked")
    @Test
    public void handle() {
        Set<AnalysisResultEntry<ApiUsage>> resultEntries = new HashSet<AnalysisResultEntry<ApiUsage>>();

        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.FIELD, "test", "one", "desc", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.FIELD, "JMS", "", "desc", "view", MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null, new ApiUsage(ApiUsageType.FIELD, "test", "two", "desc", "view",
            MigrationCost.MEDIUM)));
        resultEntries.add(new AnalysisResultEntry<ApiUsage>(null,
            new ApiUsage(ApiUsageType.FIELD, "test", null, "desc", "view", MigrationCost.MEDIUM)));

        ApiUsageByResultTypeController controller = new ApiUsageByResultTypeController();
        ModelAndView modelAndView = controller.handle(resultEntries);

        Map<String, Object> model = modelAndView.getModel();
        Map<String, Set<String>> apiUsage = (Map<String, Set<String>>) model.get("apiUsage");
        assertNotNull(apiUsage);

        Set<String> jmsUsage = apiUsage.get("JMS API (1 user)");
        assertNotNull(jmsUsage);
        assertEquals(1, jmsUsage.size());

        Set<String> testUsage = apiUsage.get("test API (2 users)");
        assertNotNull(testUsage);
        assertEquals(2, testUsage.size());
    }

    @Test
    public void canHandle() {
        assertTrue(new ApiUsageByResultTypeController().canHandle(ApiUsage.class));
        assertFalse(new ApiUsageByResultTypeController().canHandle(Object.class));
    }
}
