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

import org.springframework.stereotype.Component;

@Component
final class ProgrammaticJtaTransactionDemarcationDetector extends ProgrammaticTransactionDemarcationDetector {

    private static final String CLASS_NAME_USER_TRANSACTION = "javax.transaction.UserTransaction";

    private static final String CLASS_NAME_TRANSACTION_MANAGER = "javax.transaction.TransactionManager";

    private static final String METHOD_NAME_BEGIN = "begin";

    private static final String METHOD_NAME_COMMIT = "commit";

    private static final String METHOD_NAME_ROLLBACK = "rollback";

    private static final String METHOD_NAME_SUSPEND = "suspend";

    private static final String METHOD_NAME_RESUME = "resume";

    static final String TRANSACTION_TYPE_JTA = "JTA";

    @Override
    protected ProgrammaticTransactionDemarcation detectProgrammaticTransactionDemarcation(String className, String methodName,
        String containingClassName, String containingMethodDescription) {
        ProgrammaticJtaTransactionDemarcationType demarcationType = null;

        if (CLASS_NAME_USER_TRANSACTION.equals(className)) {
            demarcationType = getUserTransactionDemarcationType(methodName);
        } else if (CLASS_NAME_TRANSACTION_MANAGER.equals(className)) {
            demarcationType = getTransactionManagerDemarcationType(methodName);
        }

        if (demarcationType != null) {
            return new ProgrammaticTransactionDemarcation(containingClassName, TRANSACTION_TYPE_JTA, demarcationType, containingMethodDescription);
        } else {
            return null;
        }
    }

    private ProgrammaticJtaTransactionDemarcationType getTransactionManagerDemarcationType(String methodName) {
        ProgrammaticJtaTransactionDemarcationType demarcationType = null;

        if (METHOD_NAME_BEGIN.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.BEGIN;
        } else if (METHOD_NAME_COMMIT.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.COMMIT;
        } else if (METHOD_NAME_ROLLBACK.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.ROLLBACK;
        } else if (METHOD_NAME_SUSPEND.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.SUSPEND;
        } else if (METHOD_NAME_RESUME.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.RESUME;
        }
        return demarcationType;
    }

    private ProgrammaticJtaTransactionDemarcationType getUserTransactionDemarcationType(String methodName) {
        ProgrammaticJtaTransactionDemarcationType demarcationType = null;

        if (METHOD_NAME_BEGIN.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.BEGIN;
        } else if (METHOD_NAME_COMMIT.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.COMMIT;
        } else if (METHOD_NAME_ROLLBACK.equals(methodName)) {
            demarcationType = ProgrammaticJtaTransactionDemarcationType.ROLLBACK;
        }
        return demarcationType;
    }
}
