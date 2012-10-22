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

package org.springframework.migrationanalyzer.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

public class AbstraktMigrationAnalysisTests {

    private final StubMigrationAnalysis migrationAnalysis = new StubMigrationAnalysis();

    @Test
    public void execute() {
        this.migrationAnalysis.run(new String[] { "test input path", "-t", "test output type" });
        assertNotNull(this.migrationAnalysis.applicationContext.getBean(Configuration.class));
        verify(this.migrationAnalysis.executor).execute();
    }

    @Test
    public void executeParseFailure() {
        this.migrationAnalysis.run(new String[0]);
        assertEquals(-1, this.migrationAnalysis.getCode());
    }

    private static class StubMigrationAnalysis extends AbstractMigrationAnalysis {

        private final MigrationAnalysisExecutor executor = mock(MigrationAnalysisExecutor.class);

        private final StaticApplicationContext applicationContext = new StaticApplicationContext();

        private volatile int code;

        public StubMigrationAnalysis() {
            this.applicationContext.getDefaultListableBeanFactory().registerSingleton("executor", this.executor);
        }

        @Override
        protected void exit(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        @Override
        protected ConfigurableApplicationContext getApplicationContext() {
            return this.applicationContext;
        }
    }
}
