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

package org.springframework.migrationanalyzer.render.support.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.StubController;
import org.springframework.migrationanalyzer.render.support.StubView;
import org.springframework.migrationanalyzer.render.support.StubViewResolver;

public class StandardViewRendererTests {

    private final StubView view = new StubView();

    private final StubViewResolver viewResolver = new StubViewResolver(this.view);

    private final StubViewResolver nullViewResolver = new StubViewResolver();

    private final StandardViewRenderer viewRenderer = new StandardViewRenderer(this.viewResolver);

    private final StandardViewRenderer viewlessRenderer = new StandardViewRenderer(this.nullViewResolver);

    @Test
    public void renderWithEmptyModel() {
        this.viewRenderer.renderViewWithEmptyModel("the-view", new StringWriter());
        assertEquals(1, this.viewResolver.getViewsRequested().size());
        assertTrue(this.viewResolver.getViewsRequested().contains("the-view"));
        assertEquals(1, this.view.getRenderCount());
    }

    @Test
    public void renderWithModel() {
        this.viewRenderer.renderViewWithModel("the-view", Collections.<String, Object> emptyMap(), new StringWriter());
        assertEquals(1, this.viewResolver.getViewsRequested().size());
        assertTrue(this.viewResolver.getViewsRequested().contains("the-view"));
        assertEquals(1, this.view.getRenderCount());
    }

    @Test
    public void renderWithModelWhenViewIsNull() {
        this.viewlessRenderer.renderViewWithModel("the-view", Collections.<String, Object> emptyMap(), new StringWriter());
        assertEquals(1, this.nullViewResolver.getViewsRequested().size());
        assertTrue(this.nullViewResolver.getViewsRequested().contains("the-view"));
        assertEquals(0, this.view.getRenderCount());
    }

    @Test
    public void render() {
        OutputPathGenerator outputPathGenerator = new StubOutputPathGenerator("target");

        StringWriter writer = new StringWriter();

        StubController stubController1 = new StubController("view-1");
        StubController stubController2 = new StubController("view-2");

        Set<StubController> controllers = new HashSet<StubController>();
        controllers.add(stubController1);
        controllers.add(stubController2);

        this.viewRenderer.render(Object.class, new HashSet<AnalysisResultEntry<Object>>(), controllers, writer, outputPathGenerator);

        assertEquals(1, stubController1.getResultsHandled().size());

        List<String> viewsRequested = this.viewResolver.getViewsRequested();
        assertEquals(2, viewsRequested.size());
        assertTrue(viewsRequested.contains("view-1"));
        assertTrue(viewsRequested.contains("view-2"));
    }
}
