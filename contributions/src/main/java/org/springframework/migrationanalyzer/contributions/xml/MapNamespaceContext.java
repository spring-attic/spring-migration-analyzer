/**
 * 
 */

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

import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

final class MapNamespaceContext implements NamespaceContext {

    private final Map<String, String> namespaceMapping;

    MapNamespaceContext(Map<String, String> namespaceMapping) {
        this.namespaceMapping = namespaceMapping;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        if ((this.namespaceMapping != null) && this.namespaceMapping.containsKey(prefix)) {
            return this.namespaceMapping.get(prefix);
        }
        return XMLConstants.NULL_NS_URI;
    }

    @Override
    public String getPrefix(String namespace) {
        if ((this.namespaceMapping != null) && this.namespaceMapping.containsValue(namespace)) {
            for (Map.Entry<String, String> entry : this.namespaceMapping.entrySet()) {
                if (namespace.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Iterator getPrefixes(String namespace) {
        throw new UnsupportedOperationException();
    }
}
