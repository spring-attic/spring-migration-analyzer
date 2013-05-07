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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Writer;

import org.junit.Test;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;

public class StandardHtmlIndexRendererTests {

    private final ViewRenderer viewRenderer = mock(ViewRenderer.class);

    private final OutputFactory outputFactory = mock(OutputFactory.class);

    private final StandardHtmlIndexRenderer renderer = new StandardHtmlIndexRenderer(this.viewRenderer, mock(RootAwareOutputPathGenerator.class),
        this.outputFactory);

    @Test
    public void renderIndex() {
        this.renderer.renderIndex("path/prefix");
        verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-index"), any(Writer.class));
    }
}
