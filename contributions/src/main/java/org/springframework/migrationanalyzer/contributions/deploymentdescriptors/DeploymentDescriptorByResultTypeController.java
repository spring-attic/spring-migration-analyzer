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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.ByResultTypeController;
import org.springframework.migrationanalyzer.render.ModelAndView;

final class DeploymentDescriptorByResultTypeController implements ByResultTypeController<DeploymentDescriptor> {

    @Override
    public boolean canHandle(Class<?> resultType) {
        return DeploymentDescriptor.class.equals(resultType);
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<DeploymentDescriptor>> results) {
        Map<String, Map<String, List<FileSystemEntry>>> deploymentDescriptors = new TreeMap<String, Map<String, List<FileSystemEntry>>>();

        for (AnalysisResultEntry<DeploymentDescriptor> resultEntry : results) {
            DeploymentDescriptor deploymentDescriptor = resultEntry.getResult();

            Map<String, List<FileSystemEntry>> descriptorsForCategory = deploymentDescriptors.get(deploymentDescriptor.getCategory());

            if (descriptorsForCategory == null) {
                descriptorsForCategory = new TreeMap<String, List<FileSystemEntry>>();
                deploymentDescriptors.put(deploymentDescriptor.getCategory(), descriptorsForCategory);
            }

            List<FileSystemEntry> descriptorsWithName = descriptorsForCategory.get(deploymentDescriptor.getName());

            if (descriptorsWithName == null) {
                descriptorsWithName = new ArrayList<FileSystemEntry>();
                descriptorsForCategory.put(deploymentDescriptor.getName(), descriptorsWithName);
            }

            descriptorsWithName.add(deploymentDescriptor.getLocation());
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deploymentDescriptors", deploymentDescriptors);

        return new ModelAndView(model, "deployment-descriptor-by-result-type");
    }
}
