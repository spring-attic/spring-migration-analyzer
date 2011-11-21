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

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class used to create instances
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 * 
 */
public final class InstanceCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceCreator.class);

    private InstanceCreator() {
    }

    /**
     * Creates an instance of each of the given <code>classes</code>. Each of the given classes must have a public
     * no-arguments constructor. For any class that does not meet this requirement, no instance will be present in the
     * returned <code>Set</code> of instances
     * 
     * @param <T> The type of the instances
     * @param classes The classes from which instances are to be created
     * @return The created instances
     */
    public static <T> Set<T> createInstances(Set<Class<? extends T>> classes) {
        Set<T> instances = new HashSet<T>(classes.size());

        for (Class<? extends T> clazz : classes) {
            try {
                LOGGER.debug("Create new instance of '{}'", clazz);

                final Constructor<? extends T> defaultConstructor = clazz.getDeclaredConstructor();
                AccessController.doPrivileged(new PrivilegedAction<T>() {

                    @Override
                    public T run() {
                        defaultConstructor.setAccessible(true);
                        return null;
                    }
                });

                instances.add(defaultConstructor.newInstance());
            } catch (RuntimeException e) {
                LOGGER.error("Unable to instantiate '{}'. Classes must have a no-argument constructor", clazz, e);
            } catch (Exception e) {
                LOGGER.error("Unable to instantiate '{}'. Classes must have a no-argument constructor", clazz, e);
            }
        }

        return instances;
    }
}
