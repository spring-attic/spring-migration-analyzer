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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

final class DeploymentDescriptorByResultTypeController implements ByResultTypeController<DeploymentDescriptor> {

    @Override
    public boolean canHandle(Class<?> resultType) {
        return DeploymentDescriptor.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<DeploymentDescriptor>> results, OutputPathGenerator outputPathGenerator) {
        Map<String, Map<String, Map<String, String>>> deploymentDescriptors = new TreeMap<String, Map<String, Map<String, String>>>();

        for (AnalysisResultEntry<DeploymentDescriptor> resultEntry : results) {
            DeploymentDescriptor deploymentDescriptor = resultEntry.getResult();

            Map<String, Map<String, String>> descriptorsForCategory = deploymentDescriptors.get(deploymentDescriptor.getCategory());

            if (descriptorsForCategory == null) {
                descriptorsForCategory = new TreeMap<String, Map<String, String>>();
            }

            deploymentDescriptors.put(deploymentDescriptor.getCategory(), descriptorsForCategory);

            Map<String, String> descriptorsWithName = descriptorsForCategory.get(deploymentDescriptor.getName());

            if (descriptorsWithName == null) {
                descriptorsWithName = new TreeMap<String, String>();
                descriptorsForCategory.put(deploymentDescriptor.getName(), descriptorsWithName);
            }

            FileSystemEntry location = deploymentDescriptor.getLocation();
            descriptorsWithName.put(location.getName(), outputPathGenerator.generatePathFor(location));
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deploymentDescriptors", deploymentDescriptors);

        return new ModelAndView(model, "deployment-descriptor-by-result-type");
    }

}
