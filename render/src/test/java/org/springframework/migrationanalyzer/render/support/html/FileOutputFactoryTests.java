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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.junit.Test;
import org.springframework.migrationanalyzer.util.IoUtils;

public final class FileOutputFactoryTests {

    private final FileOutputFactory outputFactory = new FileOutputFactory();

    @Test
    public void createWriter() throws IOException {
        File dir = new File(new File("build", "prefix"), "alpha");
        File expectedOutput = new File(dir, "bravo.txt");

        if (expectedOutput.exists()) {
            assertTrue(expectedOutput.delete() && dir.delete());
        }

        assertFalse(expectedOutput.exists());

        Writer writer = this.outputFactory.createWriter("alpha/bravo.txt", "build/prefix");

        try {
            assertTrue(dir.isDirectory());
            writer.append("hello world");

            assertTrue(expectedOutput.exists());
        } finally {
            IoUtils.closeQuietly(writer);
        }
    }

    @Test
    public void createOutputStream() throws IOException {
        File dir = new File(new File("build", "prefix"), "charlie");
        File expectedOutput = new File(dir, "delta.txt");

        if (expectedOutput.exists()) {
            assertTrue(expectedOutput.delete() && dir.delete());
        }

        assertFalse(expectedOutput.exists());

        OutputStream output = this.outputFactory.createOutputStream("charlie/delta.txt", "build/prefix");

        try {
            assertTrue(dir.isDirectory());
            output.write(new byte[] { 1, 2, 3, 4 });
            assertTrue(expectedOutput.exists());
        } finally {
            IoUtils.closeQuietly(output);
        }
    }

}
