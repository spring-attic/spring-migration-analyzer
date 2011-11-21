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

package org.springframework.migrationanalyzer.render.support;

import static org.springframework.migrationanalyzer.analyze.util.InstanceCreator.createInstances;

import java.net.URLClassLoader;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.util.ClassPathScanner;
import org.springframework.migrationanalyzer.analyze.util.StandardClassPathScanner;
import org.springframework.migrationanalyzer.render.RenderEngine;

/**
 * The standard implementation of {@link RenderEngineFactory}. This implementation delegates creation of
 * {@link RenderEngine}s to implementations of {@link RenderEngineSubFactory}.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 * 
 * @see RenderEngineSubFactory
 */
public final class StandardRenderEngineFactory implements RenderEngineFactory {

    private static final ClassPathScanner CLASS_PATH_SCANNER = new StandardClassPathScanner();

    private final Set<RenderEngineSubFactory> subFactories;

    /**
     * Creates a new instance specifying default values for the set of {@link RenderEngineSubFactory}s
     */
    public StandardRenderEngineFactory() {
        this(CLASS_PATH_SCANNER.findImplementations(RenderEngineSubFactory.class, (URLClassLoader) Thread.currentThread().getContextClassLoader()));
    }

    StandardRenderEngineFactory(Set<Class<? extends RenderEngineSubFactory>> subFactories) {
        this.subFactories = createInstances(subFactories);
    }

    @Override
    public RenderEngine create(String outputType, String outputPath) {
        for (RenderEngineSubFactory subFactory : this.subFactories) {
            if (subFactory.canRender(outputType)) {
                return subFactory.create(outputPath);
            }
        }
        throw new IllegalArgumentException(String.format("No RenderEngine for output type '%s' is available", outputType));
    }

}
