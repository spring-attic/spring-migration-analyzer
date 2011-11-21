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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerViewTests {

    private final StringWriter writer = new StringWriter();

    private final FreemarkerView view;

    public FreemarkerViewTests() throws IOException {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(this.getClass(), "/freemarker-view");
        configuration.setObjectWrapper(new DefaultObjectWrapper());

        this.view = new FreemarkerView(configuration.getTemplate("test.ftl"));
    }

    @Test
    public void render() {
        this.view.render(Collections.<String, Object> emptyMap(), this.writer);
        assertEquals("Hello World!", this.writer.toString());
    }

    @Test
    public void templateExceptionHandling() throws IOException {
        FreemarkerView view = new FreemarkerView(new StubTemplate(new TemplateException("", null)));
        StubWriter writer = new StubWriter();

        try {
            view.render(Collections.<String, Object> emptyMap(), writer);
            fail();
        } catch (RuntimeException re) {
            assertTrue(writer.flushed);
        }
    }

    @Test
    public void ioExceptionHandling() throws IOException {
        FreemarkerView view = new FreemarkerView(new StubTemplate(new IOException("", null)));
        StubWriter writer = new StubWriter();

        try {
            view.render(Collections.<String, Object> emptyMap(), writer);
            fail();
        } catch (RuntimeException re) {
            assertTrue(writer.flushed);
        }
    }

    private static final class StubWriter extends Writer {

        private boolean flushed;

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
            this.flushed = true;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
        }
    }

    private static final class StubTemplate extends Template {

        private TemplateException templateException;

        private IOException ioException;

        @SuppressWarnings("deprecation")
        public StubTemplate(TemplateException templateException) throws IOException {
            super("stub", new StringReader(""));
            this.templateException = templateException;
        }

        @SuppressWarnings("deprecation")
        public StubTemplate(IOException ioException) throws IOException {
            super("stub", new StringReader(""));
            this.ioException = ioException;
        }

        @Override
        public void process(Object rootMap, Writer out) throws TemplateException, IOException {
            if (this.ioException != null) {
                throw this.ioException;
            }
            if (this.templateException != null) {
                throw this.templateException;
            }
        }
    }
}
