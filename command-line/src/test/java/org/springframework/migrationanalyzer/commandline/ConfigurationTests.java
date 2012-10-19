/*
 * Copyright 2012 the original author or authors.
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

package org.springframework.migrationanalyzer.commandline;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class ConfigurationTests {

    @Test
    public void fullySpecifiedConfiguration() {
        assertConfiguration(new Configuration("input/path", "output/path", "output-type", new String[] { "excluded", "items" }), "input/path",
            "output/path", "output-type", new String[] { "excluded", "items" });
    }

    @Test
    public void outputPathDefaultsToCurrentWorkingDirectory() {
        assertConfiguration(new Configuration("input/path", null, "output-type", new String[] { "excluded", "items" }), "input/path", ".",
            "output-type", new String[] { "excluded", "items" });
    }

    @Test
    public void outputTypeDefaultsToHtml() {
        assertConfiguration(new Configuration("input/path", "output/path", null, new String[] { "excluded", "items" }), "input/path", "output/path",
            "html", new String[] { "excluded", "items" });
    }

    @Test
    public void excludesDefaultsToAnEmptyArray() {
        assertConfiguration(new Configuration("input/path", "output/path", "output-type", null), "input/path", "output/path", "output-type",
            new String[0]);
    }

    private void assertConfiguration(Configuration configuration, String expectedInputPath, String expectedOutputPath, String expectedOutputType,
        String[] expectedExcludes) {
        assertEquals(expectedInputPath, configuration.getInputPath());
        assertEquals(expectedOutputPath, configuration.getOutputPath());
        assertEquals(expectedOutputType, configuration.getOutputType());
        assertArrayEquals(expectedExcludes, configuration.getExcludes());

    }

}
