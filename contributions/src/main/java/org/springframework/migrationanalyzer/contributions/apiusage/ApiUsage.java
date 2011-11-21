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

import org.springframework.migrationanalyzer.render.MigrationCost;

/**
 * An API's usage
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 */
public final class ApiUsage {

    private final ApiUsageType type;

    private final String apiName;

    private final String user;

    private final String usageDescription;

    private final String guidanceView;

    private final MigrationCost migrationCost;

    /**
     * Create a new instance describing the use of an API
     * 
     * @param type The usage type
     * @param apiName The name of the API
     * @param user The user of the API
     * @param usageDescription The description of the API's usage
     * @param guidanceView The content of the guidance view
     * @param migrationCost The cost of migrating from the API
     */
    public ApiUsage(ApiUsageType type, String apiName, String user, String usageDescription, String guidanceView, MigrationCost migrationCost) {
        this.type = type;
        this.apiName = apiName;
        this.user = user;
        this.usageDescription = usageDescription;
        this.guidanceView = guidanceView;
        this.migrationCost = migrationCost;
    }

    /**
     * The usage type of the API
     * 
     * @return The API's usage type
     */
    public ApiUsageType getType() {
        return this.type;
    }

    /**
     * The name of the API
     * 
     * @return The API's name
     */
    public String getApiName() {
        return this.apiName;
    }

    /**
     * The user of the API
     * 
     * @return The API's user
     */
    public String getUser() {
        return this.user;
    }

    /**
     * The description of the API's usage
     * 
     * @return The description of the API's usage
     */
    public String getUsageDescription() {
        return this.usageDescription;
    }

    /**
     * The content of the guidance view
     * 
     * @return The guidance view's content
     */
    public String getGuidanceView() {
        return this.guidanceView;
    }

    /**
     * The cost of migrating from the API
     * 
     * @return The API's migration cost
     */
    public MigrationCost getMigrationCost() {
        return this.migrationCost;
    }

    @Override
    public String toString() {
        return String.format("'%s' uses the '%s' API (%s)", this.user, this.apiName, this.type);
    }
}
