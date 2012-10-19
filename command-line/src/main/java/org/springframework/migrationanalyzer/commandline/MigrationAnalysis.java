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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Main method for invoking the application
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new MigrationAnalysis().run(args);
    }

    @Override
    protected MigrationAnalysisExecutor getExecutor(final Configuration configuration) {
        try {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[] { "META-INF/spring/application-context.xml" }, false);
            applicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {

                @Override
                public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
                    beanFactory.registerSingleton("configuration", configuration);

                }
            });
            applicationContext.refresh();
            return applicationContext.getBean(MigrationAnalysisExecutor.class);
        } catch (RuntimeException re) {
            this.logger.error("Failed to initialize the application context", re);
            throw re;
        }
    }

    @Override
    protected void exit(int code) {
        System.exit(code);
    }
}
