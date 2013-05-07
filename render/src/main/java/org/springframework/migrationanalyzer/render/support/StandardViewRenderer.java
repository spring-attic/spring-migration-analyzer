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

package org.springframework.migrationanalyzer.render.support;

import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.Controller;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.stereotype.Component;

@Component
final class StandardViewRenderer implements ViewRenderer {

    private final ViewResolver viewResolver;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StandardViewRenderer(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    @Override
    public void renderViewWithEmptyModel(String viewName, Writer writer) {
        this.renderViewWithModel(viewName, Collections.<String, Object> emptyMap(), writer);
    }

    @Override
    public void renderViewWithModel(String viewName, Map<String, Object> model, Writer writer) {
        View view = this.viewResolver.getView(viewName);

        if (view != null) {
            this.logger.debug("Rendering view {} from view name {}", view, viewName);
            view.render(model, writer);
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> void render(Class<?> resultType, Set<AnalysisResultEntry<T>> entries, Set<? extends Controller> controllers, Writer writer,
        OutputPathGenerator outputPathGenerator, String reportType) {
        for (Controller<?> controller : controllers) {
            if (controller.canHandle(resultType)) {
                this.logger.debug("Generating model with {}", controller);

                ModelAndView modelAndView = ((Controller<T>) controller).handle(entries);

                if (outputPathGenerator != null) {
                    modelAndView.getModel().put("link", outputPathGenerator.generatePathFor(resultType));
                    modelAndView.getModel().put("outputPathGenerator", outputPathGenerator);
                }

                renderViewWithModel(reportType + "-" + modelAndView.getViewName(), modelAndView.getModel(), writer);
            }
        }
    }
}
