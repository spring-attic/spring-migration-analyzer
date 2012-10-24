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

package org.springframework.migrationanalyzer.render.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class ReflectiveResultTypeDisplayNameResolverTests {

    private final ReflectiveResultTypeDisplayNameResolver resolver = new ReflectiveResultTypeDisplayNameResolver();

    @Test
    public void displayNameIsDerivedFromClassNameIfThereIsNoGetDisplayNameMethod() {
        assertEquals("Reflective Result Type Display Name Resolver Tests", this.resolver.getDisplayName(getClass()));
    }

    @Test
    public void displayNameIsDerivedFromClassNameIfGetDisplayNameMethodIsNotStatic() {
        assertEquals("Not Static", this.resolver.getDisplayName(NotStatic.class));
    }

    @Test
    public void displayNameIsDerivedFromClassNameIfGetDisplayNameMethodHasWrongReturnType() {
        assertEquals("Wrong Return Type", this.resolver.getDisplayName(WrongReturnType.class));
    }

    @Test
    public void getDisplayNameMethodIsUsedIfItsCorrectlyFormed() {
        assertEquals("The display name", this.resolver.getDisplayName(GetDisplayName.class));
    }

    static final class NotStatic {

        public String getDisplayName() {
            return "The display name";
        }
    }

    static final class WrongReturnType {

        public static char[] getDisplayName() {
            return "The display name".toCharArray();
        }
    }

    static final class GetDisplayName {

        public static String getDisplayName() {
            return "The display name";
        }
    }
}
