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

package org.springframework.migrationanalyzer.contributions.apiusage;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
final class StandardApiUsageDetector implements ApiUsageDetector {

    private static final String DETECTED_APIS_PROPERTIES_FILE_NAME = "org/springframework/migrationanalyzer/contributions/apiusage/detected-apis.properties";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, DetectedApiConfiguration> detectedApisByPackageName;

    StandardApiUsageDetector() {
        this(DETECTED_APIS_PROPERTIES_FILE_NAME);
    }

    StandardApiUsageDetector(String detectedApis) {
        this.detectedApisByPackageName = new DetectedApiConfigurationReader().readConfiguration(detectedApis);
    }

    private ApiUsage recordApiUsage(ApiUsageType usageType, DetectedApiConfiguration configuration, String user, String usageDescription) {
        this.logger.debug("'{}' uses the '{}' API ({})", user, configuration.getApiName(), usageType);
        return new ApiUsage(usageType, configuration.getApiName(), user, usageDescription, configuration.getGuidanceView(),
            configuration.getMigrationCost());
    }

    private DetectedApiConfiguration getConfigurationForClass(String className) {
        Set<Entry<String, DetectedApiConfiguration>> entrySet = this.detectedApisByPackageName.entrySet();
        for (Entry<String, DetectedApiConfiguration> entry : entrySet) {
            if (className.startsWith(entry.getKey())) {
                DetectedApiConfiguration configuration = entry.getValue();
                if (!configuration.isExcluded(className)) {
                    return configuration;
                }
            }
        }
        return null;
    }

    @Override
    public ApiUsage detectApiUsage(String className, ApiUsageType usageType, String user, String usageDescription) {
        DetectedApiConfiguration configuration = getConfigurationForClass(className);
        if (configuration != null) {
            return recordApiUsage(usageType, configuration, user, usageDescription);
        }
        return null;
    }
}
