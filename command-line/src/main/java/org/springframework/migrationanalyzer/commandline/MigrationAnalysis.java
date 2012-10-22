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

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main entry point to the Migration Analysis application via the command line
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 */
public final class MigrationAnalysis extends AbstractMigrationAnalysis {

    /**
     * Main method for invoking the application
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new MigrationAnalysis().run(args);
    }

    @Override
    protected ConfigurableApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[] { "META-INF/spring/application-context.xml" }, false);
    }

    @Override
    protected void exit(int code) {
        System.exit(code);
    }
}
