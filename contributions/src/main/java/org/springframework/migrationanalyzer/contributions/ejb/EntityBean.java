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

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an EJB EntityBean
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 */
public final class EntityBean extends NonMessageDrivenEjb {

    private String persistenceType;

    private String primaryKeyClass;

    private boolean reentrant;

    private String cmpVersion;

    private final Set<String> cmpFields = new TreeSet<String>();

    private String abstractSchemaName;

    /**
     * Gets the persistence type
     * 
     * @return The persistence type
     */
    public String getPersistenceType() {
        return this.persistenceType;
    }

    /**
     * Sets the persistence type
     * 
     * @param persistenceType The persistence type
     */
    public void setPersistenceType(String persistenceType) {
        this.persistenceType = persistenceType;
    }

    /**
     * Gets the primary key class
     * 
     * @return The primary key class
     */
    public String getPrimaryKeyClass() {
        return this.primaryKeyClass;
    }

    /**
     * Sets the primary key class
     * 
     * @param primaryKeyClass The primary key class
     */
    public void setPrimaryKeyClass(String primaryKeyClass) {
        this.primaryKeyClass = primaryKeyClass;
    }

    /**
     * Gets the reentrant flag
     * 
     * @return The reentrant flag
     */
    public boolean isReentrant() {
        return this.reentrant;
    }

    /**
     * Sets the reetrant flag
     * 
     * @param reentrant The reentrant flag
     */
    public void setReentrant(boolean reentrant) {
        this.reentrant = reentrant;
    }

    /**
     * Gets the cmp version
     * 
     * @return The cmp version
     */
    public String getCmpVersion() {
        return this.cmpVersion;
    }

    /**
     * Sets the cmp version
     * 
     * @param cmpVersion The cmp version
     */
    public void setCmpVersion(String cmpVersion) {
        this.cmpVersion = cmpVersion;
    }

    /**
     * Gets all the cmp fields
     * 
     * @return The cmp fields
     */
    public Set<String> getCmpFields() {
        return this.cmpFields;
    }

    /**
     * Adds a cmp field
     * 
     * @param cmpField The cmp field
     */
    public void addCmpField(String cmpField) {
        this.cmpFields.add(cmpField);
    }

    /**
     * Gets the abstract schema name
     * 
     * @return The abstract schema name
     */
    public String getAbstractSchemaName() {
        return this.abstractSchemaName;
    }

    /**
     * Sets the abstract schema name
     * 
     * @param abstractSchemaName The abstract schema name
     */
    public void setAbstractSchemaName(String abstractSchemaName) {
        this.abstractSchemaName = abstractSchemaName;
    }
}
