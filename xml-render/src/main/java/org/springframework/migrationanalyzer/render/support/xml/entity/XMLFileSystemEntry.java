/*
 * Copyright 2011 the original author or authors.
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

package org.springframework.migrationanalyzer.render.support.xml.entity;

import java.util.Set;

public class XMLFileSystemEntry {

    private String name;

    private Set<Object> results;

    public Set<Object> getResults() {
        return this.results;
    }

    public void setResults(Set<Object> results) {
        this.results = results;
    }

    public XMLFileSystemEntry(String name, Set<Object> results) {
        this.name = name;
        this.results = results;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}