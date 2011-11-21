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

import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

public class OptionsFactoryTests {

    private final OptionsFactory factory = new OptionsFactory();

    @Test
    public void numberOfOptions() {
        Options options = this.factory.create();
        assertEquals(4, options.getOptions().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void numberOfRequiredOptions() {
        Options options = this.factory.create();

        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(2, requiredOptions.size());
    }
}
