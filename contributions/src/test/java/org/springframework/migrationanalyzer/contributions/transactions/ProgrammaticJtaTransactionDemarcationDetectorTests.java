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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

public class ProgrammaticJtaTransactionDemarcationDetectorTests {

    private final ProgrammaticJtaTransactionDemarcationDetector detector = new ProgrammaticJtaTransactionDemarcationDetector();

    @Test
    public void detectUserTransactionBegin() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.BEGIN, UserTransactionBegin.class, "public void fieldBegin()",
            "public void methodArgumentBegin(javax.transaction.UserTransaction)");
    }

    @Test
    public void detectUserTransactionCommit() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.COMMIT, UserTransactionCommit.class, "public void fieldCommit()",
            "public void methodArgumentCommit(javax.transaction.UserTransaction)");
    }

    @Test
    public void detectUserTransactionRollback() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.ROLLBACK, UserTransactionRollback.class, "public void fieldRollback()",
            "public void methodArgumentRollback(javax.transaction.UserTransaction)");
    }

    @Test
    public void detectTransactionManagerBegin() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.BEGIN, TransactionManagerBegin.class, "public void field()",
            "public void methodArgument(javax.transaction.TransactionManager)");
    }

    @Test
    public void detectTransactionManagerCommit() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.COMMIT, TransactionManagerCommit.class, "public void field()",
            "public void methodArgument(javax.transaction.TransactionManager)");
    }

    @Test
    public void detectTransactionManagerRollback() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.ROLLBACK, TransactionManagerRollback.class, "public void field()",
            "public void methodArgument(javax.transaction.TransactionManager)");
    }

    @Test
    public void detectTransactionManagerSuspend() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.SUSPEND, TransactionManagerSuspend.class, "public void field()",
            "public void methodArgument(javax.transaction.TransactionManager)");
    }

    @Test
    public void detectTransactionManagerResume() throws FileNotFoundException, IOException {
        assertDemarcationDetection(ProgrammaticJtaTransactionDemarcationType.RESUME, TransactionManagerResume.class, "public void field()",
            "public void methodArgument(javax.transaction.TransactionManager)");
    }

    private void assertDemarcationDetection(ProgrammaticTransactionDemarcationType demarcationType, Class<?> clazz, String... usageDescriptions)
        throws IOException, FileNotFoundException {
        List<String> usageDescriptionsList = Arrays.asList(usageDescriptions);

        String user = clazz.getName();
        String fileName = "target/test-classes/" + user.replace(".", "/") + ".class";

        ClassReader reader = new ClassReader(new FileInputStream(fileName));
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
