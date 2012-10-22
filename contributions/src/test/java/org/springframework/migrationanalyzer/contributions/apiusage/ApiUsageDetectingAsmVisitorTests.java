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
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.springframework.migrationanalyzer.render.MigrationCost;

public class ApiUsageDetectingAsmVisitorTests {

    @Test
    public void extendsType() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserExtendsType.class, ApiUsageType.EXTENDS_TYPE);
    }

    @Test
    public void hasField() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserHasField.class, ApiUsageType.FIELD);
    }

    @Test
    public void returnArgument() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserReturnArgument.class, ApiUsageType.RETURN_ARGUMENT);
    }

    @Test
    public void methodArgument() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserMethodArgument.class, ApiUsageType.METHOD_ARGUMENT, ApiUsageType.LOCAL_VARIABLE);
    }

    @Test
    public void localVariable() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserLocalVariable.class, ApiUsageType.LOCAL_VARIABLE);
    }

    @Test
    public void implementsInterface() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserImplementsInterface.class, ApiUsageType.IMPLEMENTS_INTERFACE);
    }

    @Test
    public void throwsException() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserThrowsException.class, ApiUsageType.THROWS_EXCEPTION);
    }

    @Test
    public void annotatedConstructor() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserAnnotatedConstructor.class, ApiUsageType.ANNOTATED_METHOD);
    }

    @Test
    public void annotatedField() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserAnnotatedField.class, ApiUsageType.ANNOTATED_FIELD);
    }

    @Test
    public void annotatedMethod() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserAnnotatedMethod.class, ApiUsageType.ANNOTATED_METHOD);
    }

    @Test
    public void annotatedMethodParameter() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserAnnotatedMethodParameter.class, ApiUsageType.ANNOTATED_METHOD_ARGUMENT);
    }

    @Test
    public void annotatedType() throws FileNotFoundException, IOException {
        assertApiUsage(TestApiUserAnnotatedType.class, ApiUsageType.ANNOTATED_TYPE);
    }

    private void assertApiUsage(Class<?> clazz, ApiUsageType... expectedUsage) throws FileNotFoundException, IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("/" + clazz.getName().replace(".", "/") + ".class"));
        ApiUsageDetector detector = new StubApiUsageDetector();

        ApiUsageDetectingAsmVisitor visitor = new ApiUsageDetectingAsmVisitor(detector);

        reader.accept(visitor, 0);

        Set<ApiUsage> results = visitor.getResults();

        assertApiUsage(results, clazz.getName(), expectedUsage);
    }

    private void assertApiUsage(Set<ApiUsage> actualUsage, String user, ApiUsageType... expectedUsage) {
        assertEquals(expectedUsage.length, actualUsage.size());

        List<ApiUsageType> expectedUsageList = new ArrayList<ApiUsageType>(Arrays.asList(expectedUsage));

        for (ApiUsage apiUsage : actualUsage) {
            assertTrue("Actual usage did not contain expected usage of type " + apiUsage.getType(), expectedUsageList.remove(apiUsage.getType()));
            assertUser(apiUsage, user);
            assertApiName(apiUsage);
        }
    }

    private void assertUser(ApiUsage apiUsage, String expectedUser) {
        assertEquals(expectedUser, apiUsage.getUser());
    }

    private void assertApiName(ApiUsage apiUsage) {
        assertEquals("Test API", apiUsage.getApiName());
    }

    private static final class StubApiUsageDetector implements ApiUsageDetector {

        @Override
        public ApiUsage detectApiUsage(String className, ApiUsageType usageType, String user, String description) {
            if (className.startsWith("org.springframework.migrationanalyzer.contributions.apiusage.testapi")) {
                return new ApiUsage(usageType, "Test API", user, description, "view", MigrationCost.MEDIUM);
            }
            return null;
        }
    }
}
