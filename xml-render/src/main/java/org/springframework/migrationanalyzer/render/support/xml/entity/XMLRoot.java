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

public class XMLRoot {

    private List<XMLApplication> applications = null;

    public XMLRoot() {
        this.applications = new ArrayList<XMLApplication>();
    }

    public List<XMLApplication> getApplications() {
        return this.applications;
    }

    public void setApplications(List<XMLApplication> applications) {
        this.applications = applications;
    }

}