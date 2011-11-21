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

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.contributions.StubFileSystemEntry;

public class SpringJndiIntegrationByResultTypeControllerTests extends AbstractSpringIntegrationByResultTypeControllerTests {

    public SpringJndiIntegrationByResultTypeControllerTests() {
        super("JNDI", SpringJndiIntegration.class, new SpringJndiIntegrationByResultTypeController());
    }

    @SuppressWarnings("rawtypes")
    @Override
    AnalysisResultEntry getAnalysisResultEntry(String path) {
        FileSystemEntry fileSystemEntry = new StubFileSystemEntry(path);
        SpringJndiIntegration result = new SpringJndiIntegration(new File(path).getName(), fileSystemEntry);
        return new AnalysisResultEntry<SpringJndiIntegration>(fileSystemEntry, result);
    }
}
