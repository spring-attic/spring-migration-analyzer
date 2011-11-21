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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;

final class FreemarkerView implements View {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Template template;

    FreemarkerView(Template template) {
        this.template = template;
    }

    @Override
    public void render(Map<String, Object> model, Writer writer) {
        this.logger.debug("Rendering FreemarkerView based on '{}'", this.template.getName());

        try {
            this.template.process(model, writer);
        } catch (TemplateException e) {
            throw new RenderingException(String.format("Unable to write view '%s'", this.template.getName()), e);
        } catch (IOException e) {
            throw new RenderingException(String.format("Unable to write view '%s'", this.template.getName()), e);
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    @Override
    public String toString() {
        return this.template.getName();
    }
}
