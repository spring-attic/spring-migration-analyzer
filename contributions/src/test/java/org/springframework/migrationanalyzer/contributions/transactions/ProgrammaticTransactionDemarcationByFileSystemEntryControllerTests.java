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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.migrationanalyzer.contributions.transactions.ProgrammaticDataSourceTransactionDemarcationType.COMMIT;
import static org.springframework.migrationanalyzer.contributions.transactions.ProgrammaticJtaTransactionDemarcationType.BEGIN;
import static org.springframework.migrationanalyzer.contributions.transactions.ProgrammaticJtaTransactionDemarcationType.ROLLBACK;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.ModelAndView;

public class ProgrammaticTransactionDemarcationByFileSystemEntryControllerTests {

    private final ByFileSystemEntryController<ProgrammaticTransactionDemarcation> controller = new ProgrammaticTransactionDemarcationByFileSystemEntryController();

    @Test
    public void canHandle() {
        assertTrue(this.controller.canHandle(ProgrammaticTransactionDemarcation.class));
        assertFalse(this.controller.canHandle(Object.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handle() {
        HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>> results = new HashSet<AnalysisResultEntry<ProgrammaticTransactionDemarcation>>();
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user1", "JTA", BEGIN,
            "description1")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user1", "JTA", BEGIN,
            "description2")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user1", "JTA",
            ROLLBACK, "description1")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user1", "JTA", BEGIN,
            "description3")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user1", "DataSource",
            COMMIT, "description1")));

        ModelAndView modelAndView = this.controller.handle(results);

        assertNotNull(modelAndView);
        assertEquals("programmatic-transaction-demarcation-by-file", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());

        Map<String, Map<String, List<String>>> programmaticDemarcation = (Map<String, Map<String, List<String>>>) model.get("programmaticDemarcation");
        assertNotNull(programmaticDemarcation);

        Map<String, List<String>> jtaDemarcation = programmaticDemarcation.get("JTA");
        assertNotNull(jtaDemarcation);

        List<String> usersOfBegin = jtaDemarcation.get("Begin");
        assertNotNull(usersOfBegin);
        assertEquals(3, usersOfBegin.size());

        List<String> usersOfRollback = jtaDemarcation.get("Rollback");
        assertNotNull(usersOfRollback);
        assertEquals(1, usersOfRollback.size());

        Map<String, List<String>> dataSourceDemarcation = programmaticDemarcation.get("DataSource");
        assertNotNull(dataSourceDemarcation);

        List<String> usersOfCommit = dataSourceDemarcation.get("Commit");
        assertNotNull(usersOfCommit);
        assertEquals(1, usersOfCommit.size());
    }
}
