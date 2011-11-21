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

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsage;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsageDetector;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsageType;
import org.springframework.migrationanalyzer.contributions.apiusage.StandardApiUsageDetector;

final class ApiUsageDetectingClassValueAnalyzer implements SpringConfigurationClassValueAnalyzer<ApiUsage> {

    private final ApiUsageDetector detector = new StandardApiUsageDetector();

    @Override
    public ApiUsage analyze(String className, FileSystemEntry fileSystemEntry) {
        return this.detector.detectApiUsage(className, ApiUsageType.SPRING_CONFIGURATION, fileSystemEntry.getName(), className);
    }
}
