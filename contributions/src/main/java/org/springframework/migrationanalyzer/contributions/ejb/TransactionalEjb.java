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

import java.util.Set;

import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;

interface TransactionalEjb {

    /**
     * Set the transaction type
     * 
     * @param transactionType The transaction type
     */
    void setTransactionType(String transactionType);

    /**
     * Get the transaction type
     * 
     * @return The transaction type
     */
    String getTransactionType();

    /**
     * Add a collection of transaction propagation types
     * 
     * @param transactionPropagationTypes The transaction propagation types
     */
    void addTransactionPropagationTypes(Set<TransactionPropagationType> transactionPropagationTypes);

    /**
     * Get the collection of transaction propagation types
     * 
     * @return The transaction propagation types
     */
    Set<TransactionPropagationType> getTransactionPropagationTypes();
}
