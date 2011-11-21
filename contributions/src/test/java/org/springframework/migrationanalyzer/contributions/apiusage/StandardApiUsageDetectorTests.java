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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StandardApiUsageDetectorTests {

    private final ApiUsageDetector usageDetector = new StandardApiUsageDetector(
        "org/springframework/migrationanalyzer/contributions/apiusage/test-detected-apis.properties");

    @Test
    public void detectedApiReturnsApiUsage() {
        ApiUsage apiUsage = this.usageDetector.detectApiUsage("org.springframework.migrationanalyzer.contributions.apiusage.testapi.core",
            ApiUsageType.ANNOTATED_FIELD, "user", "description");
        assertApiUsage(apiUsage, "Test API", ApiUsageType.ANNOTATED_FIELD, "description", "user");

        apiUsage = this.usageDetector.detectApiUsage("org.springframework.migrationanalyzer.contributions.apiusage.testapi.ext",
            ApiUsageType.ANNOTATED_FIELD, "user", "description");
        assertApiUsage(apiUsage, "Test API", ApiUsageType.ANNOTATED_FIELD, "description", "user");

        apiUsage = this.usageDetector.detectApiUsage("org.springframework.migrationanalyzer.ut", ApiUsageType.THROWS_EXCEPTION, "user", "description");
        assertApiUsage(apiUsage, "ut-api", ApiUsageType.THROWS_EXCEPTION, "description", "user");
    }

    @Test
    public void unrecognisedApiReturnsNull() {
        ApiUsage apiUsage = this.usageDetector.detectApiUsage("foo.bar", ApiUsageType.ANNOTATED_FIELD, "user", "description");
        assertNull(apiUsage);
    }

    @Test
    public void exclusionFromDetection() {
        ApiUsage apiUsage = this.usageDetector.detectApiUsage("org.springframework.migrationanalyzer.ut.somepackage", ApiUsageType.ANNOTATED_FIELD,
            "user", "description");
        assertNotNull(apiUsage);
        apiUsage = this.usageDetector.detectApiUsage("org.springframework.migrationanalyzer.ut.excluded", ApiUsageType.ANNOTATED_FIELD, "user",
            "description");
        assertNull(apiUsage);
    }

    private void assertApiUsage(ApiUsage apiUsage, String expectedApiName, ApiUsageType expectedType, String expectedDescription, String expectedUser) {
        assertNotNull(apiUsage);
        assertEquals(expectedApiName, apiUsage.getApiName());
        assertEquals(expectedType, apiUsage.getType());
        assertEquals(expectedDescription, apiUsage.getUsageDescription());
        assertEquals(expectedUser, apiUsage.getUser());
    }

}
