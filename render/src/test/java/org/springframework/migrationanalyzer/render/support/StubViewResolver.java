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

package org.springframework.migrationanalyzer.render.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
public final class StubViewResolver implements ViewResolver {

    private final List<String> viewsRequested = new ArrayList<String>();

    private final View view;

    public StubViewResolver(View view) {
        this.view = view;
    }

    public StubViewResolver() {
        this.view = null;
    }

    @Override
    public View getView(String viewName) {
        this.viewsRequested.add(viewName);
        return this.view;
    }

    public List<String> getViewsRequested() {
        return this.viewsRequested;
    }
}