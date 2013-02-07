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

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;

/**
 * A representation of a deployment descriptor
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thead-safe
 */
public final class DeploymentDescriptor {

    private final String category;

    private final FileSystemEntry location;

    private final String name;

    /**
     * Creates a new instance of the descriptor
     * 
     * @param category The category of the descriptor
     * @param location The location of the descriptor
     * @param name The name of the descriptor
     */
    public DeploymentDescriptor(String category, FileSystemEntry location, String name) {
        this.category = category;
        this.location = location;
        this.name = name;
    }

    /**
     * Returns the category of the deployment descriptor
     * 
     * @return the deployment descriptor's category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Returns the name of the deployment descriptor
     * 
     * @return the deployment descriptor's name
     */
    public String getName() {
        return this.name;
    }

    FileSystemEntry getLocation() {
        return this.location;
    }

    /**
     * Returns a name for this type, suitable for display in a report
     * 
     * @return The type's display name
     */
    public static String getDisplayName() {
        return "Deployment Descriptors";
    }
}
