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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
final class ReflectiveResultTypeDisplayNameResolver implements ResultTypeDisplayNameResolver {

    private static final String DISPLAY_NAME_ACCESSOR_METHOD_NAME = "getDisplayName";

    private final Map<Class<?>, String> displayNameCache = new ConcurrentHashMap<Class<?>, String>();

    @Override
    public String getDisplayName(Class<?> resultType) {
        String displayName = this.displayNameCache.get(resultType);
        if (displayName == null) {
            displayName = doGetDisplayName(resultType);
            this.displayNameCache.put(resultType, displayName);
        }
        return displayName;
    }

    private String doGetDisplayName(Class<?> resultType) {
        Method displayNameMethod = ReflectionUtils.findMethod(resultType, DISPLAY_NAME_ACCESSOR_METHOD_NAME);
        try {
            if ((displayNameMethod != null)) {
                ReflectionUtils.makeAccessible(displayNameMethod);
                return (String) ReflectionUtils.invokeMethod(displayNameMethod, null);
            }
        } catch (RuntimeException re) {
            // Fall back to built display name
        }

        return buildDisplayName(resultType);
    }

    private String buildDisplayName(Class<?> resultType) {
        StringBuilder displayNameBuilder = new StringBuilder();
        String simpleName = resultType.getSimpleName();
        for (char c : simpleName.toCharArray()) {

            if ((displayNameBuilder.length() > 0) && Character.isUpperCase(c)) {
                displayNameBuilder.append(' ');
            }

            displayNameBuilder.append(c);
        }

        return displayNameBuilder.toString();
    }
}
