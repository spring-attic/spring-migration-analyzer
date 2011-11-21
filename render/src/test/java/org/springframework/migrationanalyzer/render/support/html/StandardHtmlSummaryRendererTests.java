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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.StubAnalysisResult;
import org.springframework.migrationanalyzer.render.SummaryController;

@SuppressWarnings("rawtypes")
public class StandardHtmlSummaryRendererTests {

    private final Set<SummaryController> summaryControllers = new HashSet<SummaryController>();

    private final StubViewRenderer viewRenderer = new StubViewRenderer();

    private final RootAwareOutputPathGenerator outputPathGenerator = new StubOutputPathGenerator("target");

    private final StubWriterFactory writerFactory = new StubWriterFactory();

    private final StandardHtmlSummaryRenderer renderer = new StandardHtmlSummaryRenderer(this.summaryControllers, this.viewRenderer,
        this.outputPathGenerator, this.writerFactory);

    @Test
    public void renderSummary() {
        AnalysisResult analysisResult = new StubAnalysisResult(
            new AnalysisResultEntry<Object>(new StubFileSystemEntry("a/b/c/Foo.txt"), new Object()));

        this.summaryControllers.add(new StubSummaryController(
            Arrays.asList(new ModelAndView(Collections.<String, Object> emptyMap(), "stub-guidance"))));

        this.renderer.renderSummary(analysisResult);

        List<String> expectedViews = Arrays.asList("summary-header", "guidance-header", "guidance-category-header", "guidance-entry-header",
            "stub-guidance", "guidance-entry-footer", "guidance-category-footer", "guidance-category-header", "guidance-entry-header",
            "stub-guidance", "guidance-entry-footer", "guidance-category-footer", "guidance-category-header", "guidance-entry-header",
            "stub-guidance", "guidance-entry-footer", "guidance-category-footer", "guidance-footer, summary-footer");

        List<String> viewsRendered = this.viewRenderer.getViewsRendered();
        assertEquals(19, viewsRendered.size());

        for (int i = 0; i > viewsRendered.size(); i++) {
            assertEquals(expectedViews.get(i), viewsRendered.get(i));
        }
    }

    private static final class StubSummaryController implements SummaryController<Object> {

        private final List<ModelAndView> modelsAndViews;

        private StubSummaryController(List<ModelAndView> modelsAndViews) {
            this.modelsAndViews = modelsAndViews;
        }

        @Override
        public List<ModelAndView> handle(Set<AnalysisResultEntry<Object>> results, MigrationCost migrationCost) {
            return this.modelsAndViews;
        }

        @Override
        public boolean canHandle(Class<?> resultType) {
            return Object.class.equals(resultType);
        }

        @Override
        public ModelAndView handle(Set<AnalysisResultEntry<Object>> results, OutputPathGenerator outputPathGenerator) {
            return null;
        }

    }
}
