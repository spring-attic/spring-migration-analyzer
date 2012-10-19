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
final class ProgrammaticDataSourceTransactionDemarcationDetector extends ProgrammaticTransactionDemarcationDetector {

    private static final String CLASS_NAME_CONNECTION = "java.sql.Connection";

    private static final String METHOD_NAME_COMMIT = "commit";

    private static final String METHOD_NAME_ROLLBACK = "rollback";

    private static final String METHOD_NAME_SET_SAVEPOINT = "setSavepoint";

    private static final String METHOD_NAME_RELEASE_SAVEPOINT = "releaseSavepoint";

    static final String TRANSACTION_TYPE_DATASOURCE = "DataSource";

    @Override
    protected ProgrammaticTransactionDemarcation detectProgrammaticTransactionDemarcation(String className, String methodName,
        String containingClassName, String containingMethodDescription) {
        ProgrammaticDataSourceTransactionDemarcationType demarcationType = null;

        if (CLASS_NAME_CONNECTION.equals(className)) {
            if (METHOD_NAME_COMMIT.equals(methodName)) {
                demarcationType = ProgrammaticDataSourceTransactionDemarcationType.COMMIT;
            } else if (METHOD_NAME_ROLLBACK.equals(methodName)) {
                demarcationType = ProgrammaticDataSourceTransactionDemarcationType.ROLLBACK;
            } else if (METHOD_NAME_SET_SAVEPOINT.equals(methodName)) {
                demarcationType = ProgrammaticDataSourceTransactionDemarcationType.SET_SAVEPOINT;
            } else if (METHOD_NAME_RELEASE_SAVEPOINT.equals(methodName)) {
                demarcationType = ProgrammaticDataSourceTransactionDemarcationType.RELEASE_SAVEPOINT;
            }
        }

        if (demarcationType != null) {
            return new ProgrammaticTransactionDemarcation(containingClassName, TRANSACTION_TYPE_DATASOURCE, demarcationType,
                containingMethodDescription);
        }

        return null;
    }

}
