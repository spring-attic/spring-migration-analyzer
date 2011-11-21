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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstraktMigrationAnalysisTests {

    private final StubMigrationAnalysis migrationAnalysis = new StubMigrationAnalysis();

    @Test
    public void execute() {
        this.migrationAnalysis.run(new String[] { "-i", "test input path", "-t", "test output type" });
        assertTrue(this.migrationAnalysis.getStubExecutor().getCalled());
    }

    @Test
    public void executeParseFailure() {
        this.migrationAnalysis.run(new String[0]);
        assertEquals(-1, this.migrationAnalysis.getCode());
    }

    private static class StubMigrationAnalysis extends AbstractMigrationAnalysis {

        private final StubMigrationAnalysisExecutor executor = new StubMigrationAnalysisExecutor();

        private volatile int code;

        @Override
        protected MigrationAnalysisExecutor getExecutor(String inputPath, String outputType, String outpathPath, String[] excludes) {
            return this.executor;
        }

        @Override
        protected void exit(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public StubMigrationAnalysisExecutor getStubExecutor() {
            return this.executor;
        }

    }

    private static class StubMigrationAnalysisExecutor implements MigrationAnalysisExecutor {

        private volatile boolean called = false;

        @Override
        public void execute() {
            this.called = true;
        }

        public boolean getCalled() {
            return this.called;
        }
    }
}
