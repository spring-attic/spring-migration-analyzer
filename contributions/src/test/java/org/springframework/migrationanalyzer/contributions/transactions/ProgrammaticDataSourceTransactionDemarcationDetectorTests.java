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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

public class ProgrammaticDataSourceTransactionDemarcationDetectorTests {

    private final ProgrammaticDataSourceTransactionDemarcationDetector detector = new ProgrammaticDataSourceTransactionDemarcationDetector();

    @Test
    public void commit() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticDataSourceTransactionDemarcationType.COMMIT, ConnectionCommit.class, "public void field()",
            "public void methodArgument(java.sql.Connection)");
    }

    @Test
    public void rollback() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticDataSourceTransactionDemarcationType.ROLLBACK, ConnectionRollback.class, "public void field()",
            "public void methodArgument(java.sql.Connection)");
    }

    @Test
    public void releaseSavepoint() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticDataSourceTransactionDemarcationType.RELEASE_SAVEPOINT, ConnectionReleaseSavepoint.class,
            "public void field()", "public void methodArgument(java.sql.Connection)");
    }

    @Test
    public void setSavepoint() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticDataSourceTransactionDemarcationType.SET_SAVEPOINT, ConnectionSetSavepoint.class,
            "public void field()", "public void methodArgument(java.sql.Connection)");
    }

    private void assertDemarcationDetection(ProgrammaticDataSourceTransactionDemarcationType demarcationType, Class<?> clazz,
        String... usageDescriptions) throws IOException, FileNotFoundException {
        List<String> usageDescriptionsList = Arrays.asList(usageDescriptions);

        String user = clazz.getName();

        ClassReader reader = new ClassReader(getClass().getResourceAsStream("/" + clazz.getName().replace(".", "/") + ".class"));
        reader.accept(this.detector, 0);

        Set<ProgrammaticTransactionDemarcation> results = this.detector.getResults();

        assertNotNull(results);
        assertEquals(usageDescriptions.length, results.size());

        for (ProgrammaticTransactionDemarcation result : results) {
            assertEquals(demarcationType, result.getType());
            assertEquals(user, result.getUser());
            assertTrue(usageDescriptionsList + " does not contain " + result.getUsageDescription(),
                usageDescriptionsList.contains(result.getUsageDescription()));
        }
    }
}
