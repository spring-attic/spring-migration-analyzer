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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;

public class HtmlRenderEngineTests {

    private final AnalysisResult result = mock(AnalysisResult.class);

    private final HtmlIndexRenderer indexRenderer = mock(HtmlIndexRenderer.class);

    private final HtmlFileSystemEntryRenderer fileSystemEntryRenderer = mock(HtmlFileSystemEntryRenderer.class);

    private final HtmlResultTypeRenderer resultTypeRenderer = mock(HtmlResultTypeRenderer.class);

    private final HtmlSummaryRenderer summaryRenderer = mock(HtmlSummaryRenderer.class);

    private final RootAwareOutputPathGenerator outputPathGenerator = mock(RootAwareOutputPathGenerator.class);

    private final OutputFactory outputFactory = mock(OutputFactory.class);

    private final HtmlRenderEngine renderEngine = new HtmlRenderEngine(this.indexRenderer, this.fileSystemEntryRenderer, this.summaryRenderer,
        this.resultTypeRenderer, this.outputPathGenerator, this.outputFactory);

    @Test
    public void render() {
        when(this.outputFactory.createWriter(anyString(), anyString())).thenReturn(new StringWriter());
        when(this.outputFactory.createOutputStream(anyString(), anyString())).thenReturn(new ByteArrayOutputStream());

        this.renderEngine.render(this.result, "output/path");

        verify(this.indexRenderer).renderIndex("output/path");
        verify(this.fileSystemEntryRenderer).renderFileSystemEntries(this.result, "output/path");
        verify(this.resultTypeRenderer).renderResultTypes(this.result, "output/path");
        verify(this.summaryRenderer).renderSummary(this.result, "output/path");
        verify(this.outputFactory, times(3)).createWriter(anyString(), anyString());
        verify(this.outputFactory, times(5)).createOutputStream(anyString(), anyString());
    }
}
