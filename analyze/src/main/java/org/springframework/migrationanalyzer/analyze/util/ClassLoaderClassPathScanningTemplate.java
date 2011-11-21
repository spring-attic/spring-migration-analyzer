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

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ClassLoaderClassPathScanningTemplate<T> implements ClassPathScanningTemplate {

    private static final String CLASS_SUFFIX = ".class";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Set<Class<? extends T>> resources = new HashSet<Class<? extends T>>();

    private final Class<? extends T> candidateInterface;

    private final ClassLoader classLoader;

    ClassLoaderClassPathScanningTemplate(Class<? extends T> candidateInterface, ClassLoader classLoader) {
        this.candidateInterface = candidateInterface;
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(String path) {
        if (isClassFile(path)) {
            Class<?> candidateType = getClass(this.classLoader, path);
            if ((candidateType != null) && isCandidateEligible(candidateType, this.candidateInterface)) {
                this.resources.add((Class<? extends T>) candidateType);
            }
        }
    }

    Set<Class<? extends T>> getResources() {
        return this.resources;
    }

    private boolean isClassFile(String candidate) {
        return candidate.endsWith(CLASS_SUFFIX);
    }

    private Class<?> getClass(ClassLoader classLoader, String path) {
        String className = path.substring(0, path.length() - CLASS_SUFFIX.length()).replace('/', '.').replace('\\', '.');
        try {
            this.logger.debug("Loading candidate '{}'", path);
            return classLoader.loadClass(className);
        } catch (NoClassDefFoundError e) {
            this.logger.debug("Unable to load '{}': {}", className, e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            this.logger.debug("Unable to load '{}': {}", className, e.getMessage());
            return null;
        }
    }

    private boolean isCandidateEligible(Class<?> candidateType, Class<?> requiredInterface) {
        if (!isAbstract(candidateType) && requiredInterface.isAssignableFrom(candidateType) && !isHidden(candidateType)) {
            this.logger.debug("'{}' implements '{}'", candidateType, requiredInterface);
            return true;
        } else {
            this.logger.debug("'{}' does not implement '{}'", candidateType, requiredInterface);
            return false;
        }
    }

    private boolean isAbstract(Class<?> candidateType) {
        return (candidateType.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT;
    }

    private boolean isHidden(Class<?> candidateType) {
        return (candidateType.getAnnotation(IgnoredByClassPathScan.class)) != null;
    }

}
