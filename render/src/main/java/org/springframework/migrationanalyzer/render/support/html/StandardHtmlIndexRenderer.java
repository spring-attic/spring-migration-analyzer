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

import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.support.ViewRenderer;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.stereotype.Component;

@Component
final class StandardHtmlIndexRenderer implements HtmlIndexRenderer {

    private static final String VIEW_NAME_INDEX = "html-index";

    private final OutputPathGenerator outputPathGenerator;

    private final ViewRenderer viewRenderer;

    private final OutputFactory outputFactory;

    @Autowired
    StandardHtmlIndexRenderer(ViewRenderer viewRenderer, OutputPathGenerator outputPathGenerator, OutputFactory outputFactory) {
        this.viewRenderer = viewRenderer;
        this.outputPathGenerator = outputPathGenerator;
        this.outputFactory = outputFactory;
    }

    @Override
    public void renderIndex(String outputPathPrefix) {
        Writer writer = null;
        try {
            writer = this.outputFactory.createWriter(this.outputPathGenerator.generatePathForIndex(), outputPathPrefix);
            this.viewRenderer.renderViewWithEmptyModel(VIEW_NAME_INDEX, writer);
        } finally {
            IoUtils.closeQuietly(writer);
        }
    }
}
