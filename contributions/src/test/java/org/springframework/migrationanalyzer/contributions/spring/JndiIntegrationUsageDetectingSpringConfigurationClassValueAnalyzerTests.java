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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.migrationanalyzer.contributions.StubFileSystemEntry;

public final class JndiIntegrationUsageDetectingSpringConfigurationClassValueAnalyzerTests {

    private final JndiIntegrationUsageDetectingSpringConfigurationClassValueAnalyzer analyzer = new JndiIntegrationUsageDetectingSpringConfigurationClassValueAnalyzer();

    @Test
    public void analyzeClassWithNameStartingWithOrgSpringFrameworkJndi() {
        SpringJndiIntegration result = this.analyzer.analyze("org.springframework.jndi.Alpha", new StubFileSystemEntry("entry-name"));
        assertNotNull(result);
    }

    @Test
    public void analyzeNonJndiPackageClass() {
        SpringJndiIntegration result = this.analyzer.analyze("org.springframework.beans.Alpha", new StubFileSystemEntry("entry-name"));
        assertNull(result);
    }
}
