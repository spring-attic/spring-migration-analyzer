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

final class ProgrammaticTransactionDemarcation {

    private final String user;

    private final String transactionType;

    private final ProgrammaticTransactionDemarcationType type;

    private final String usageDescription;

    ProgrammaticTransactionDemarcation(String user, String transactionType, ProgrammaticTransactionDemarcationType demarcationType,
        String usageDescription) {
        this.user = user;
        this.transactionType = transactionType;
        this.type = demarcationType;
        this.usageDescription = usageDescription;
    }

    String getUser() {
        return this.user;
    }

    String getTransactionType() {
        return this.transactionType;
    }

    ProgrammaticTransactionDemarcationType getType() {
        return this.type;
    }

    String getUsageDescription() {
        return this.usageDescription;
    }
}
