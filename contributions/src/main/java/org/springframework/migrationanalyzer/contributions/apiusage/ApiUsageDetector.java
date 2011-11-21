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

/**
 * A detector for usages of specified APIs
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be thread-safe
 */
public interface ApiUsageDetector {

    /**
     * Detects the usage of a specified API
     * 
     * @param className The name of the class to detect
     * @param usageType The type of usage to detect
     * @param user The user of this API
     * @param usageDescription The description of the API being detected
     * @return An analysis of the API's usage
     */
    ApiUsage detectApiUsage(String className, ApiUsageType usageType, String user, String usageDescription);
}
