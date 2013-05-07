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
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;

@SuppressWarnings("rawtypes")
public class StandardHtmlSummaryRendererTests {

    private final SummaryController summaryController = mock(SummaryController.class);

    private final Set<SummaryController> summaryControllers = new HashSet<SummaryController>(Arrays.asList(this.summaryController));

    private final ViewRenderer viewRenderer = mock(ViewRenderer.class);

    private final OutputFactory outputFactory = mock(OutputFactory.class);

    private final StandardHtmlSummaryRenderer renderer = new StandardHtmlSummaryRenderer(this.summaryControllers, this.viewRenderer,
        mock(RootAwareOutputPathGenerator.class), this.outputFactory);

    @SuppressWarnings("unchecked")
    @Test
    public void renderSummary() {
        AnalysisResult analysisResult = mock(AnalysisResult.class);
        FileSystemEntry entry = mock(FileSystemEntry.class);
        when(analysisResult.getFileSystemEntries()).thenReturn(new HashSet<FileSystemEntry>(Arrays.asList(entry)));
        when(analysisResult.getResultTypes()).thenReturn(new HashSet<Class<?>>(Arrays.asList(Object.class)));

        when(this.summaryController.handle(anySet(), any(MigrationCost.class))).thenReturn(
            Arrays.asList(new ModelAndView(Collections.<String, Object> emptyMap(), "stub-guidance")));
        when(this.summaryController.canHandle(Object.class)).thenReturn(true);

        this.renderer.renderSummary(analysisResult, "path/prefix");

        InOrder inOrder = Mockito.inOrder(this.viewRenderer);

        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-summary-header"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-header"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-guidance-category-header"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-entry-header"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-stub-guidance"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-entry-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-category-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-guidance-category-header"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-entry-header"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-stub-guidance"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-entry-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-category-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-guidance-category-header"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-entry-header"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-stub-guidance"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-entry-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-category-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-guidance-footer"), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-summary-footer"), any(Writer.class));
    }
}
