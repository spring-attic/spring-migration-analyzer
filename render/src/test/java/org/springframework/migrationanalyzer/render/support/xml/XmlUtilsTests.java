/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.migrationanalyzer.render.support.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class XmlUtilsTests {

    @Test
    public void doubleQuoteIsEscaped() {
        assertEquals("&quot;", XmlUtils.escape("\""));
    }

    @Test
    public void singleQuoteIsEscaped() {
        assertEquals("&apos;", XmlUtils.escape("'"));
    }

    @Test
    public void lessThanIsEscaped() {
        assertEquals("&lt;", XmlUtils.escape("<"));
    }

    @Test
    public void greaterThanIsEscaped() {
        assertEquals("&gt;", XmlUtils.escape(">"));
    }

    @Test
    public void ampersandIsEscaped() {
        assertEquals("&amp;", XmlUtils.escape("&"));
    }

    @Test
    public void multipleValuesInTheSameStringAreEscaped() {
        assertEquals("a &quot; b &apos; c &lt; d &gt; e &amp; f", XmlUtils.escape("a \" b ' c < d > e & f"));
    }

}
