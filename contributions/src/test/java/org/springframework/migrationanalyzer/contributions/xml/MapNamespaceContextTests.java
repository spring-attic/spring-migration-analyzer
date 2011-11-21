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

package org.springframework.migrationanalyzer.contributions.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;

import org.junit.Test;

public final class MapNamespaceContextTests {

    @Test
    public void getNamespaceURIWithNullMapping() {
        assertEquals(XMLConstants.NULL_NS_URI, new MapNamespaceContext(null).getNamespaceURI("prefix"));
    }

    @Test
    public void getPrefixWithNullMapping() {
        assertNull(new MapNamespaceContext(null).getPrefix("namespace"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getPrefixesThrowsUnsupportedOperationException() {
        new MapNamespaceContext(null).getPrefixes("namespace");
    }

    @Test
    public void getPrefixForUnknownNamespace() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("prefix", "namespace");
        assertNull(new MapNamespaceContext(mapping).getPrefix("another-namespace"));
    }

    @Test
    public void getPrefix() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("prefix", "namespace");
        assertEquals("prefix", new MapNamespaceContext(mapping).getPrefix("namespace"));
    }

    @Test
    public void getNamespaceURIForUnknownPrefix() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("prefix", "namespace");
        assertEquals(XMLConstants.NULL_NS_URI, new MapNamespaceContext(mapping).getNamespaceURI("another-prefix"));
    }

    @Test
    public void getNamespace() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("prefix", "namespace");
        assertEquals("namespace", new MapNamespaceContext(mapping).getNamespaceURI("prefix"));
    }
}
