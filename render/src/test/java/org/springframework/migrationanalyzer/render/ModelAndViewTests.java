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

package org.springframework.migrationanalyzer.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ModelAndViewTests {

    @Test
    public void test() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("a", "alpha");
        model.put("b", "alpha");

        String viewName = "viewName";

        ModelAndView modelAndView = new ModelAndView(model, viewName);
        assertEquals(model, modelAndView.getModel());
        assertSame(viewName, modelAndView.getViewName());
    }

    @Test
    public void modelCanBeUpdated() {
        ModelAndView modelAndView = new ModelAndView(Collections.<String, Object> emptyMap(), "view-name");
        modelAndView.getModel().put("a", "alpha");
        assertEquals("alpha", modelAndView.getModel().get("a"));
    }
}
