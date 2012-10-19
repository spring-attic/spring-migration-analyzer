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

package org.springframework.migrationanalyzer.analyze.util;

import java.net.URLClassLoader;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A <code>ClassPathScanner</code> can be used to scan the class path to locates {@link Class Classes} that meet certain
 * criteria.
 * 
 * Classes annotated with {@link IgnoredByClassPathScan @HiddenFromClassPathScan} <strong>must</strong> be ignored by
 * <code>ClassPathScanner</code> implementations.
 * 
 * @param <T> The type of class to be found by the scanner.
 */
public interface ClassPathScanner {

    /**
     * Returns a {@code Set} of resource names that match the supplied {@code Pattern} found on the class path of the
     * supplied {@link ClassLoader}
     * 
     * @param pattern The pattern that the resources should match
     * @param classLoader The class loader that determines the classpath to scan
     * @return The {@code Set} of resources, never {@code null}
     */
    Set<String> findResources(Pattern pattern, URLClassLoader classLoader);
}