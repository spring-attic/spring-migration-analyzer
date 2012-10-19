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

package org.springframework.migrationanalyzer.contributions.ejb;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;
import org.springframework.stereotype.Component;

@Component
final class EntityBeanSummaryController extends AbstractEntityBeanController implements SummaryController<EntityBean> {

    private static final String VIEW_NAME_SUMMARY = "entity-bean-summary";

    private static final String VIEW_NAME_GUIDANCE = "entity-bean-guidance";

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<EntityBean>> results) {
        return new ModelAndView(createModel(results), VIEW_NAME_SUMMARY);
    }

    private Map<String, Object> createModel(Set<AnalysisResultEntry<EntityBean>> results) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("value", getValue(results.size()));
        return model;
    }

    private String getValue(int size) {
        if (size == 1) {
            return "1 Entity Bean";
        }
        return String.format("%s Entity Beans", size);
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<EntityBean>> results, MigrationCost migrationCost) {
        if (migrationCost == MigrationCost.HIGH) {
            return Arrays.asList(new ModelAndView(Collections.<String, Object> emptyMap(), VIEW_NAME_GUIDANCE));
        } else {
            return Collections.<ModelAndView> emptyList();
        }
    }
}
