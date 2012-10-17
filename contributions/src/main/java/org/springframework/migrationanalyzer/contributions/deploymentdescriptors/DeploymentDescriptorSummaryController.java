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

package org.springframework.migrationanalyzer.contributions.deploymentdescriptors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.render.MigrationCost;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.SummaryController;

final class DeploymentDescriptorSummaryController implements SummaryController<DeploymentDescriptor> {

    @Override
    public boolean canHandle(Class<?> resultType) {
        return DeploymentDescriptor.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<DeploymentDescriptor>> results) {

        Map<String, AtomicInteger> deploymentDescriptorCounts = new TreeMap<String, AtomicInteger>();

        for (AnalysisResultEntry<DeploymentDescriptor> result : results) {
            DeploymentDescriptor deploymentDescriptor = result.getResult();
            AtomicInteger countForCategory = deploymentDescriptorCounts.get(deploymentDescriptor.getCategory());

            if (countForCategory == null) {
                countForCategory = new AtomicInteger(0);
                deploymentDescriptorCounts.put(deploymentDescriptor.getCategory(), countForCategory);
            }

            countForCategory.incrementAndGet();
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deploymentDescriptorCounts", deploymentDescriptorCounts);

        return new ModelAndView(model, "deployment-descriptor-summary");
    }

    @Override
    public List<ModelAndView> handle(Set<AnalysisResultEntry<DeploymentDescriptor>> results, MigrationCost migrationCost) {
        return null;
    }

}
