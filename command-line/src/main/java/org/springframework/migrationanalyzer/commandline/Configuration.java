/*
 * Copyright 2012 the original author or authors.
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

import java.util.Arrays;

final class Configuration {

    private static final String[] DEFAULT_EXCLUDES = new String[0];

    private static final String DEFAULT_OUTPUT_PATH = ".";

    private static final String[] DEFAULT_OUTPUT_TYPES = new String[] { "html" };

    private final String inputPath;

    private final String outputPath;

    private final String[] outputTypes;

    private final String[] excludes;

    Configuration(String inputPath, String outputPath, String[] outputTypes, String[] excludes) {
        this.inputPath = inputPath;
        this.outputPath = outputPath == null ? DEFAULT_OUTPUT_PATH : outputPath;
        this.outputTypes = outputTypes == null ? DEFAULT_OUTPUT_TYPES : outputTypes;
        this.excludes = excludes == null ? DEFAULT_EXCLUDES : excludes;
    }

    /**
     * @return The path of the archive or directory of archives to analyze
     */
    public String getInputPath() {
        return this.inputPath;
    }

    /**
     * @return The path to which output should be written
     */
    public String getOutputPath() {
        return this.outputPath;
    }

    /**
     * @return The types of report to produce
     */
    public String[] getOutputTypes() {
        return Arrays.copyOf(this.outputTypes, this.outputTypes.length);
    }

    /**
     * @return Ant-style path patterns of entries in the archive(s) which should be excluded from analysis
     */
    public String[] getExcludes() {
        return Arrays.copyOf(this.excludes, this.excludes.length);
    }
}
