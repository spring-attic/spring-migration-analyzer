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

package org.springframework.migrationanalyzer.contributions.ejb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;

final class MessageDrivenBean extends Ejb implements TransactionalEjb {

    private static final Set<TransactionPropagationType> DEFAULT_TRANSACTION_PROPAGATION_TYPES = new HashSet<TransactionPropagationType>(
        Arrays.asList(TransactionPropagationType.REQUIRED));

    private static final String DEFAULT_TRANSACTION_TYPE = "Container";

    private volatile String transactionType;

    private final Set<TransactionPropagationType> transactionPropagationTypes = new HashSet<TransactionPropagationType>();

    @Override
    public String getTransactionType() {
        return this.transactionType == null ? DEFAULT_TRANSACTION_TYPE : this.transactionType;
    }

    @Override
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public Set<TransactionPropagationType> getTransactionPropagationTypes() {
        if (DEFAULT_TRANSACTION_TYPE.equals(getTransactionType())) {
            if (this.transactionPropagationTypes.isEmpty()) {
                return DEFAULT_TRANSACTION_PROPAGATION_TYPES;
            } else {
                return this.transactionPropagationTypes;
            }
        } else {
            return null;
        }
    }

    @Override
    public void addTransactionPropagationTypes(Set<TransactionPropagationType> transactionPropagationTypes) {
        this.transactionPropagationTypes.addAll(transactionPropagationTypes);
    }

    /**
     * Returns a name for this type, suitable for display in a report
     * 
     * @return The type's display name
     */
    public static String getDisplayName() {
        return "Message-Driven Beans";
    }
}
