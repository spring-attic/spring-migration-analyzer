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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;
import org.springframework.migrationanalyzer.util.IoUtils;

public final class FileWriterFactoryTests {

    private final FileWriterFactory writerFactory = new FileWriterFactory("target");

    @Test
    public void createWriter() throws IOException {
        Writer writer = this.writerFactory.createWriter("alpha/bravo.txt", "prefix");

        try {
            File dir = new File(new File("target", "prefix"), "alpha");
            assertTrue(dir.isDirectory());
            writer.append("hello world");

            assertTrue(new File(dir, "bravo.txt").exists());
        } finally {
            IoUtils.closeQuietly(writer);
        }
    }

}
