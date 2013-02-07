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
import static org.mockito.Mockito.when;

import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.support.ResultTypeDisplayNameResolver;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;

@SuppressWarnings("rawtypes")
public class StandardHtmlResultTypeRendererTests {

    private final Set<ByResultTypeController> resultTypeControllers = new HashSet<ByResultTypeController>();

    private final ResultTypeDisplayNameResolver resultTypeDisplayNameResolver = mock(ResultTypeDisplayNameResolver.class);

    private final ViewRenderer viewRenderer = mock(ViewRenderer.class);

    private final StandardHtmlResultTypeRenderer renderer = new StandardHtmlResultTypeRenderer(this.resultTypeControllers, this.viewRenderer,
        mock(RootAwareOutputPathGenerator.class), mock(OutputFactory.class), this.resultTypeDisplayNameResolver);

    @SuppressWarnings("unchecked")
    @Test
    public void render() {
        when(this.resultTypeDisplayNameResolver.getDisplayName(any(Class.class))).thenAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[0].toString();
            }

        });

        AnalysisResult analysisResult = mock(AnalysisResult.class);
        FileSystemEntry entry = mock(FileSystemEntry.class);
        when(analysisResult.getFileSystemEntries()).thenReturn(new HashSet<FileSystemEntry>(Arrays.asList(entry)));
        when(analysisResult.getResultTypes()).thenReturn(new HashSet<Class<?>>(Arrays.asList(Object.class)));

        this.renderer.renderResultTypes(analysisResult, "path/prefix");

        InOrder inOrder = Mockito.inOrder(this.viewRenderer);

        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-result-contents"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithModel(eq("html-by-result-header"), any(Map.class), any(Writer.class));
        inOrder.verify(this.viewRenderer).renderViewWithEmptyModel(eq("html-by-result-footer"), any(Writer.class));
    }
}
