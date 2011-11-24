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

import java.util.ArrayList;
import java.util.List;

public class XMLApplication {

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<XMLFileSystemEntry> fileEntries = null;

    public XMLApplication() {
        this.fileEntries = new ArrayList<XMLFileSystemEntry>();
    }

    public XMLApplication(String name) {
        this.fileEntries = new ArrayList<XMLFileSystemEntry>();
        this.name = name;
    }

    public List<XMLFileSystemEntry> getFileEntries() {
        return this.fileEntries;
    }

    public void setFileEntries(List<XMLFileSystemEntry> fileEntries) {
        this.fileEntries = fileEntries;
    }

}
