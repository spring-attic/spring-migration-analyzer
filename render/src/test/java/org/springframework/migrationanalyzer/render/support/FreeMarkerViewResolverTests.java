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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class FreeMarkerViewResolverTests {

    @Test
    public void getView() {
        Set<String> templatePaths = new HashSet<String>();
        templatePaths.add("test-view-resolver/test-test.ftl");
        FreemarkerViewResolver viewResolver = new FreemarkerViewResolver(templatePaths);
        View view = viewResolver.getView("test-test");
        assertNotNull(view);
        assertTrue(view instanceof FreemarkerView);
    }

    @Test
    public void getViewDoesNotExist() {
        Set<String> templatePaths = new HashSet<String>();
        templatePaths.add("test-view-resolver/test-test.ftl");
        FreemarkerViewResolver viewResolver = new FreemarkerViewResolver(templatePaths);
        assertNull(viewResolver.getView("does-not-exist"));
    }
}
