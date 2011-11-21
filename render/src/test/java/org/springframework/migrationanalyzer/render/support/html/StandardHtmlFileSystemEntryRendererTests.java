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
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByFileSystemEntryController;
import org.springframework.migrationanalyzer.render.StubAnalysisResult;
import org.springframework.migrationanalyzer.render.support.source.SourceAccessor;

@SuppressWarnings("rawtypes")
public class StandardHtmlFileSystemEntryRendererTests {

    private final Set<ByFileSystemEntryController> fileSystemEntryControllers = new HashSet<ByFileSystemEntryController>();

    private final StubViewRenderer viewRenderer = new StubViewRenderer();

    private final RootAwareOutputPathGenerator outputPathGenerator = new StubOutputPathGenerator("target");

    private final StubWriterFactory writerFactory = new StubWriterFactory();

    private final SourceAccessor sourceAccessor = new StubSourceAccessor();

    private final StandardHtmlFileSystemEntryRenderer renderer = new StandardHtmlFileSystemEntryRenderer(this.fileSystemEntryControllers,
        this.viewRenderer, this.outputPathGenerator, this.writerFactory, this.sourceAccessor);

    @Test
    public void renderFileSystemEntries() {
        AnalysisResult analysisResult = new StubAnalysisResult(
            new AnalysisResultEntry<Object>(new StubFileSystemEntry("a/b/c/Foo.txt"), new Object()));
        this.renderer.renderFileSystemEntries(analysisResult);
        assertEquals(Arrays.asList("file-contents", "by-file-header", "by-file-source", "by-file-footer"), this.viewRenderer.getViewsRendered());
    }

    private static final class StubSourceAccessor implements SourceAccessor {

        @Override
        public String getSource(FileSystemEntry fileSystemEntry) {
            return "the-source";
        }
    }
}
