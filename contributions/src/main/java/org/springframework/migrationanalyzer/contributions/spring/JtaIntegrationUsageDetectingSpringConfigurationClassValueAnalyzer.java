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

package org.springframework.migrationanalyzer.contributions.spring;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

final class JtaIntegrationUsageDetectingSpringConfigurationClassValueAnalyzer implements SpringConfigurationClassValueAnalyzer<SpringJtaIntegration> {

    private static final Set<String> JTA_TRANSACTION_MANAGER_CLASS_NAMES = new HashSet<String>();

    static {
        JTA_TRANSACTION_MANAGER_CLASS_NAMES.add("org.springframework.transaction.jta.JtaTransactionManager");
        JTA_TRANSACTION_MANAGER_CLASS_NAMES.add("org.springframework.transaction.jta.OC4JJtaTransactionManager");
        JTA_TRANSACTION_MANAGER_CLASS_NAMES.add("org.springframework.transaction.jta.WebLogicJtaTransactionManager");
        JTA_TRANSACTION_MANAGER_CLASS_NAMES.add("org.springframework.transaction.jta.WebSphereUowTransactionManager");
    }

    @Override
    public SpringJtaIntegration analyze(String className, FileSystemEntry fileSystemEntry) {
        if (JTA_TRANSACTION_MANAGER_CLASS_NAMES.contains(className)) {
            return new SpringJtaIntegration(new File(fileSystemEntry.getName()).getName(), fileSystemEntry);
        } else {
            return null;
        }
    }
}
