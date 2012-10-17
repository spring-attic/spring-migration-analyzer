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

package org.springframework.migrationanalyzer.contributions.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;

final class SpringConfigurationByResultTypeController implements ByResultTypeController<SpringConfiguration> {

    @Override
    public boolean canHandle(Class<?> resultType) {
        return SpringConfiguration.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<SpringConfiguration>> results) {
        Map<String, FileSystemEntry> springConfigurations = new HashMap<String, FileSystemEntry>();

        for (AnalysisResultEntry<SpringConfiguration> resultEntry : results) {
            SpringConfiguration springConfiguration = resultEntry.getResult();

            springConfigurations.put(springConfiguration.getName(), springConfiguration.getLocation());
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("springConfigurations", springConfigurations);

        return new ModelAndView(model, "spring-configuration-by-result-type");
    }

}
