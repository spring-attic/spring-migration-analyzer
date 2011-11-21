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

import java.util.List;

import org.junit.Test;

public class StandardHtmlIndexRendererTests {

    private final StubViewRenderer viewRenderer = new StubViewRenderer();

    private final RootAwareOutputPathGenerator outputPathGenerator = new StubOutputPathGenerator("target");

    private final StubWriterFactory writerFactory = new StubWriterFactory();

    private final StandardHtmlIndexRenderer renderer = new StandardHtmlIndexRenderer(this.viewRenderer, this.outputPathGenerator, this.writerFactory);

    @Test
    public void renderIndex() {

        this.renderer.renderIndex();

        List<String> viewsRendered = this.viewRenderer.getViewsRendered();
        assertEquals(1, viewsRendered.size());
        assertTrue(viewsRendered.contains("index"));
    }
}
