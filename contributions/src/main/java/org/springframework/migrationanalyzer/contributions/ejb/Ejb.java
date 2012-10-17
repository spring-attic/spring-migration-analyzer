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

abstract class Ejb implements Comparable<Ejb> {

    private volatile String ejbName;

    private volatile String ejbClass;

    /**
     * Gets the EJB name
     * 
     * @return The EJB name
     */
    public final String getEjbName() {
        return this.ejbName == null ? this.ejbClass.substring(this.ejbClass.lastIndexOf('.') + 1) : this.ejbName;
    }

    /**
     * Sets the EJB name
     * 
     * @param ejbName The EJB name
     */
    public final void setEjbName(String ejbName) {
        this.ejbName = ejbName;
    }

    /**
     * Gets the EJB class
     * 
     * @return The EJB class
     */
    public final String getEjbClass() {
        return this.ejbClass;
    }

    /**
     * Sets the EJB class
     * 
     * @param ejbClass The EJB class
     */
    public final void setEjbClass(String ejbClass) {
        this.ejbClass = ejbClass;
    }

    @Override
    public final int compareTo(Ejb o) {
        return this.getEjbName().compareTo(o.getEjbName());
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((getEjbName() == null) ? 0 : getEjbName().hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Ejb other = (Ejb) obj;
        if (getEjbName() == null) {
            if (other.getEjbName() != null) {
                return false;
            }
        } else if (!getEjbName().equals(other.getEjbName())) {
            return false;
        }
        return true;
    }
}
