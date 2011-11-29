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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

abstract class AbstractMigrationAnalysis {

    private static final Options OPTIONS = new OptionsFactory().create();

    final void run(String[] args) {
        CommandLine commandLine = null;
        try {
            commandLine = new PosixParser().parse(OPTIONS, args);
            String inputPath = commandLine.getOptionValue(OptionsFactory.OPTION_KEY_INPUT_PATH);
            String outputType = commandLine.getOptionValue(OptionsFactory.OPTION_KEY_OUTPUT_TYPE);
            String outputPath = commandLine.getOptionValue(OptionsFactory.OPTION_KEY_OUTPUT_PATH);
            String[] excludes = commandLine.getOptionValues(OptionsFactory.OPTION_KEY_EXCLUDE);
            getExecutor(inputPath, outputType, outputPath, excludes).execute();
        } catch (ParseException e) {
            new HelpFormatter().printHelp("migration-analysis.[sh | bat] [OPTION]...", OPTIONS);
            exit(-1);
        }

    }

    protected abstract MigrationAnalysisExecutor getExecutor(String inputPath, String outputType, String outputPath, String[] excludes);

    protected abstract void exit(int code);
}
