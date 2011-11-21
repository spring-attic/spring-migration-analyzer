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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.migrationanalyzer.render.MigrationCost;

final class DetectedApiConfigurationReader {

    private static final String KEY_SUFFIX_INCLUDE = ".include";

    private static final String KEY_SUFFIX_EXCLUDE = ".exclude";

    private static final String KEY_SUFFIX_COST = ".cost";

    private static final String KEY_SUFFIX_GUIDANCE_VIEW = ".guidance";

    Map<String, DetectedApiConfiguration> readConfiguration(String propertiesResourcePath) {

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(propertiesResourcePath));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read properties file from resource path " + propertiesResourcePath, e);
        }

        Map<String, DetectedApiConfiguration> configurationByPackageName = new HashMap<String, DetectedApiConfiguration>();

        Set<String> apiNames = getApiNames(properties.keySet());

        for (String apiName : apiNames) {

            Set<String> excludedPackages = getExcludedPackages(apiName, properties);
            MigrationCost migrationCost = getMigrationCost(apiName, properties);
            String guidanceView = properties.getProperty(apiName + KEY_SUFFIX_GUIDANCE_VIEW);

            DetectedApiConfiguration configuration = new DetectedApiConfiguration(apiName, guidanceView, migrationCost, excludedPackages);

            String packageList = properties.getProperty(apiName + KEY_SUFFIX_INCLUDE);
            if (packageList != null) {
                String[] packages = packageList.split(",");
                for (String pkg : packages) {
                    String trimmed = pkg.trim();
                    configurationByPackageName.put(trimmed, configuration);
                }
            }
        }

        return configurationByPackageName;
    }

    private Set<String> getApiNames(Set<Object> keys) {
        Set<String> apiNames = new HashSet<String>();

        for (Object keyObj : keys) {
            String key = (String) keyObj;
            int index = key.indexOf('.');
            if (index > -1) {
                apiNames.add(key.substring(0, index));
            }
        }

        return apiNames;
    }

    private Set<String> getExcludedPackages(String apiName, Properties properties) {
        String exclusionProperty = properties.getProperty(apiName + KEY_SUFFIX_EXCLUDE);

        Set<String> excludedPackages = new HashSet<String>();

        if (exclusionProperty != null) {
            String[] packages = exclusionProperty.split(",");
            for (String pkg : packages) {
                String trimmed = pkg.trim();
                excludedPackages.add(trimmed);
            }
        }

        return excludedPackages;
    }

    private MigrationCost getMigrationCost(String apiName, Properties properties) {

        MigrationCost migrationCost = null;

        String cost = properties.getProperty(apiName + KEY_SUFFIX_COST);
        if (cost != null) {
            migrationCost = MigrationCost.valueOf(cost);
        }

        return migrationCost;
    }
}
