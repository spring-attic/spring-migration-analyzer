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

package org.springframework.migrationanalyzer.commandline;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Options;
import org.junit.Test;

public final class OptionsFactoryTests {

    private final Options options = new OptionsFactory().create();

    @Test
    public void numberOfOptions() {
        assertEquals(3, this.options.getOptions().size());
    }

    @Test
    public void numberOfRequiredOptions() {
        assertEquals(0, this.options.getRequiredOptions().size());
    }
}
