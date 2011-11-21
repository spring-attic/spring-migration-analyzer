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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;
import org.springframework.migrationanalyzer.render.SummaryController;

final class SpringConfigurationSummaryController implements SummaryController<SpringConfiguration> {

    @Override
    public boolean canHandle(Class<?> resultType) {
        return SpringConfiguration.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<SpringConfiguration>> results, OutputPathGenerator outputPathGenerator) {

        int count = results.size();

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("springConfigurationCount", count);
        model.put("link", outputPathGenerator.generatePathFor(SpringConfiguration.class));

        return new ModelAndView(model, "spring-configuration-summary");
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<SpringConfiguration>> results, MigrationCost migrationCost) {
        return Collections.<ModelAndView> emptyList();
    }
}
