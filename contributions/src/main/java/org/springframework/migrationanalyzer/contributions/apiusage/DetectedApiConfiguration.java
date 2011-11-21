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

import java.util.Set;

import org.springframework.migrationanalyzer.render.MigrationCost;

final class DetectedApiConfiguration {

    private final String apiName;

    private final String guidanceView;

    private final MigrationCost migrationCost;

    private final Set<String> excludedPackages;

    DetectedApiConfiguration(String apiName, String guidanceView, MigrationCost migrationCost, Set<String> excludedPackages) {
        this.apiName = apiName;
        this.guidanceView = guidanceView;
        this.migrationCost = migrationCost;
        this.excludedPackages = excludedPackages;
    }

    String getApiName() {
        return this.apiName;
    }

    String getGuidanceView() {
        return this.guidanceView;
    }

    MigrationCost getMigrationCost() {
        return this.migrationCost;
    }

    boolean isExcluded(String className) {
        if (this.excludedPackages != null) {
            for (String exclusion : this.excludedPackages) {
                if (className.startsWith(exclusion)) {
                    return true;
                }
            }
        }

        return false;
    }
}