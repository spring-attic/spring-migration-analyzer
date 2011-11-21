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
import org.springframework.migrationanalyzer.contributions.StubOutputPathGenerator;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

public class ProgrammaticTransactionDemarcationByResultTypeControllerTests {

    private final ByResultTypeController<ProgrammaticTransactionDemarcation> controller = new ProgrammaticTransactionDemarcationByResultTypeController();

    private final OutputPathGenerator outputPathGenerator = new StubOutputPathGenerator();

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
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user2", "JTA", BEGIN,
            "description1")));
        results.add(new AnalysisResultEntry<ProgrammaticTransactionDemarcation>(null, new ProgrammaticTransactionDemarcation("user1", "DataSource",
            COMMIT, "description1")));

        ModelAndView modelAndView = this.controller.handle(results, this.outputPathGenerator);

        assertNotNull(modelAndView);
        assertEquals("programmatic-transaction-demarcation-by-result-type", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        assertNotNull(model);
        assertEquals(2, model.size());
        assertNotNull(model.get("link"));

        Map<String, Map<String, Map<String, List<String>>>> programmaticDemarcation = (Map<String, Map<String, Map<String, List<String>>>>) model.get("programmaticDemarcation");
        assertNotNull(programmaticDemarcation);

        Map<String, Map<String, List<String>>> jtaDemarcation = programmaticDemarcation.get("JTA");
        assertNotNull(jtaDemarcation);

        Map<String, List<String>> usersOfBegin = jtaDemarcation.get("Begin");
        assertNotNull(usersOfBegin);
        assertEquals(2, usersOfBegin.size());

        List<String> beginUser1 = usersOfBegin.get("user1");
        assertNotNull(beginUser1);
        assertEquals(2, beginUser1.size());

        List<String> beginUser2 = usersOfBegin.get("user2");
        assertNotNull(beginUser2);
        assertEquals(1, beginUser2.size());

        Map<String, List<String>> usersOfRollback = jtaDemarcation.get("Rollback");
        assertNotNull(usersOfRollback);
        assertEquals(1, usersOfRollback.size());

        List<String> rollbackUser1 = usersOfRollback.get("user1");
        assertNotNull(rollbackUser1);
        assertEquals(1, rollbackUser1.size());

        List<String> rollbackUser2 = usersOfRollback.get("user1");
        assertNotNull(rollbackUser2);
        assertEquals(1, rollbackUser2.size());
    }
}
