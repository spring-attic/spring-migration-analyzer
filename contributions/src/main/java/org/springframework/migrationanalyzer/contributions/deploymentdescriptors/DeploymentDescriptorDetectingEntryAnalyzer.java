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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.stereotype.Component;

@Component
final class DeploymentDescriptorDetectingEntryAnalyzer implements EntryAnalyzer<DeploymentDescriptor> {

    private static final String DETECTED_DEPLOYMENT_DESCRIPTORS_PROPERTIES_FILE_NAME = "org/springframework/migrationanalyzer/contributions/deploymentdescriptors/detected-deployment-descriptors.properties";

    private final Map<String, String> deploymentDescriptorsToDetect;

    private final Logger logger = LoggerFactory.getLogger(DeploymentDescriptorDetectingEntryAnalyzer.class);

    DeploymentDescriptorDetectingEntryAnalyzer() {
        this.deploymentDescriptorsToDetect = buildDetectedDeploymentDescriptors(readDetectedDeploymentDescriptors(DETECTED_DEPLOYMENT_DESCRIPTORS_PROPERTIES_FILE_NAME));
    }

    private Map<String, String> buildDetectedDeploymentDescriptors(Properties detectedDeploymentDescriptorsProperties) {
        Set<Object> keySet = detectedDeploymentDescriptorsProperties.keySet();

        Map<String, String> descriptorsToDetect = new HashMap<String, String>();

        for (Object object : keySet) {
            String descriptorCategory = (String) object;
            String descriptorFileNamesProperty = detectedDeploymentDescriptorsProperties.getProperty(descriptorCategory);
            String[] descriptorFileNames = descriptorFileNamesProperty.split(",");
            for (String descriptorFileName : descriptorFileNames) {
                descriptorsToDetect.put(descriptorFileName.trim(), descriptorCategory);
            }
        }

        return descriptorsToDetect;
    }

    private Properties readDetectedDeploymentDescriptors(String propertiesFileName) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFileName);
            properties.load(inputStream);
        } catch (IOException ioe) {
            this.logger.error("Failed to read " + propertiesFileName, ioe);
        } finally {
            IoUtils.closeQuietly(inputStream);
        }

        return properties;
    }

    @Override
    public Set<DeploymentDescriptor> analyze(FileSystemEntry fileSystemEntry) throws AnalysisFailedException {

        Set<DeploymentDescriptor> deploymentDescriptors = new HashSet<DeploymentDescriptor>();

        String entryName = fileSystemEntry.getName();

        if (entryName.contains("/")) {
            entryName = entryName.substring(entryName.lastIndexOf('/') + 1);
        }

        String descriptorCategory = this.deploymentDescriptorsToDetect.get(entryName);

        if (descriptorCategory != null) {
            deploymentDescriptors.add(new DeploymentDescriptor(descriptorCategory, fileSystemEntry, entryName));
        }

        return deploymentDescriptors;
    }

}
