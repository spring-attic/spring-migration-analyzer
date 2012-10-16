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

import java.util.List;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.StubAnalysisResult;
import org.springframework.migrationanalyzer.render.StubFileSystemEntry;

public class HtmlRenderEngineTests {

    private final FileSystemEntry fileSytemEntry = new StubFileSystemEntry();

    private final AnalysisResultEntry<Object> resultEntry = new AnalysisResultEntry<Object>(this.fileSytemEntry, new Object());

    private final StubAnalysisResult result = new StubAnalysisResult(this.resultEntry);

    private final StubHtmlIndexRenderer indexRenderer = new StubHtmlIndexRenderer();

    private final StubHtmlFileSystemEntryRenderer fileSystemEntryRenderer = new StubHtmlFileSystemEntryRenderer();

    private final StubHtmlResultTypeRenderer resultTypeRenderer = new StubHtmlResultTypeRenderer();

    private final StubHtmlSummaryRenderer summaryRenderer = new StubHtmlSummaryRenderer();

    private final RootAwareOutputPathGenerator outputPathGenerator = new StubOutputPathGenerator("target/path");

    private final StubWriterFactory writerFactory = new StubWriterFactory();

    private final HtmlRenderEngine renderEngine = new HtmlRenderEngine(this.indexRenderer, this.fileSystemEntryRenderer, this.summaryRenderer,
        this.resultTypeRenderer, this.outputPathGenerator, this.writerFactory);

    @Test
    public void render() {
        this.renderEngine.render(this.result);

        assertEquals(1, this.indexRenderer.renderCount);
        assertEquals(1, this.fileSystemEntryRenderer.renderCount);
        assertEquals(1, this.resultTypeRenderer.renderCount);
        assertEquals(1, this.summaryRenderer.renderCount);

        List<String> writersCreated = this.writerFactory.writersCreated;
        assertEquals(8, writersCreated.size());
    }

    private static final class StubHtmlIndexRenderer implements HtmlIndexRenderer {

        private int renderCount;

        @Override
        public void renderIndex() {
            this.renderCount++;
        }
    }

    private static final class StubHtmlFileSystemEntryRenderer implements HtmlFileSystemEntryRenderer {

        private int renderCount;

        @Override
        public void renderFileSystemEntries(AnalysisResult result) {
            this.renderCount++;
        }
    }

    private static final class StubHtmlResultTypeRenderer implements HtmlResultTypeRenderer {

        private int renderCount;

        @Override
        public void renderResultTypes(AnalysisResult analysisResult) {
            this.renderCount++;
        }
    }

    private static final class StubHtmlSummaryRenderer implements HtmlSummaryRenderer {

        private int renderCount;

        @Override
        public void renderSummary(AnalysisResult analysisResult) {
            this.renderCount++;
        }
    }

}
