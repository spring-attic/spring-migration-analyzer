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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.Controller;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.support.StandardViewRenderer;
import org.springframework.migrationanalyzer.render.support.View;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;
import org.springframework.migrationanalyzer.render.support.ViewResolver;

public class StandardViewRendererTests {

    private final View view = mock(View.class);

    private final ViewResolver viewResolver = mock(ViewResolver.class);

    private final ViewRenderer viewRenderer = new StandardViewRenderer(this.viewResolver);

    @SuppressWarnings("unchecked")
    @Test
    public void renderWithEmptyModel() {
        when(this.viewResolver.getView("the-view")).thenReturn(this.view);
        this.viewRenderer.renderViewWithEmptyModel("the-view", new StringWriter());
        verify(this.view).render(any(Map.class), any(Writer.class));
    }

    @Test
    public void renderWithModel() {
        when(this.viewResolver.getView("the-view")).thenReturn(this.view);
        Map<String, Object> model = Collections.<String, Object> emptyMap();
        this.viewRenderer.renderViewWithModel("the-view", model, new StringWriter());
        verify(this.view).render(same(model), any(Writer.class));
    }

    @Test
    public void renderWithModelWhenViewIsNull() {
        this.viewRenderer.renderViewWithModel("the-view", Collections.<String, Object> emptyMap(), new StringWriter());
        verify(this.viewResolver).getView("the-view");
        verifyNoMoreInteractions(this.view);
    }

    @Test
    public void render() {
        Set<Controller<?>> controllers = new HashSet<Controller<?>>();
        controllers.add(createController("view-1"));
        controllers.add(createController("view-2"));

        this.viewRenderer.render(Object.class, new HashSet<AnalysisResultEntry<Object>>(), controllers, new StringWriter(),
            mock(OutputPathGenerator.class), "test");

        verify(this.viewResolver).getView("test-view-1");
        verify(this.viewResolver).getView("test-view-2");
    }

    @SuppressWarnings("unchecked")
    private Controller<?> createController(String viewName) {
        Controller<?> controller = mock(Controller.class);
        when(controller.handle(any(Set.class))).thenReturn(new ModelAndView(new HashMap<String, Object>(), viewName));
        when(controller.canHandle(Object.class)).thenReturn(true);
        return controller;
    }
}
