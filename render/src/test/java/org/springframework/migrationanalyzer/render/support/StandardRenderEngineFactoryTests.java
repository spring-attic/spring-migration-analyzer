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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.render.RenderEngine;

public class StandardRenderEngineFactoryTests {

    private final Set<Class<? extends RenderEngineSubFactory>> subFactoryClasses = new HashSet<Class<? extends RenderEngineSubFactory>>();

    @Test
    public void create() {
        this.subFactoryClasses.add(StubRenderEngineSubFactory.class);
        RenderEngine renderEngine = new StandardRenderEngineFactory(this.subFactoryClasses).create("testOutputType", "testOutputPath");
        assertNotNull(renderEngine);
        assertTrue(renderEngine instanceof StubRenderEngine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noSubFactory() {
        this.subFactoryClasses.add(CannotRenderStubRenderEngineSubFactory.class);
        new StandardRenderEngineFactory(this.subFactoryClasses).create("testOutputType", "testOutputPath");
    }
}
