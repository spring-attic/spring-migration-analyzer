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

package org.springframework.migrationanalyzer.contributions.util;

import java.util.Collection;

/**
 * Utilities for dealing with Strings
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Convert a collection of items to a string separated by a specified string
     * 
     * @param items The items to convert to the string
     * @param separator The separating string
     * @return The string
     */
    public static String toSeparatedString(Collection<? extends Object> items, String separator) {
        return toSeparatedString(items.toArray(new Object[items.size()]), separator);
    }

    /**
     * Convert a collection of items to a string separated by a specified string
     * 
     * @param items The items to convert to the string
     * @param separator The separating string
     * @return The string
     */
    public static String toSeparatedString(Object[] items, String separator) {

        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            resultBuilder.append(items[i]);
            if (i < (items.length - 1)) {
                resultBuilder.append(separator);
            }
        }

        return resultBuilder.toString();
    }
}
