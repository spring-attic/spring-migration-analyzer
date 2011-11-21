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

abstract class NonMessageDrivenEjb extends Ejb {

    private String home;

    private String remote;

    private String localHome;

    private String local;

    /**
     * Gets the home interface
     * 
     * @return The home interface
     */
    public final String getHome() {
        return this.home;
    }

    /**
     * Sets the home interface
     * 
     * @param home The home interface
     */
    public final void setHome(String home) {
        this.home = home;
    }

    /**
     * Gets the remote interface
     * 
     * @return The remote interface
     */
    public final String getRemote() {
        return this.remote;
    }

    /**
     * Sets the remote interface
     * 
     * @param remote The remote interface
     */
    public final void setRemote(String remote) {
        this.remote = remote;
    }

    /**
     * Gets the local home interface
     * 
     * @return The local home interface
     */
    public final String getLocalHome() {
        return this.localHome;
    }

    /**
     * Sets the local home interface
     * 
     * @param localHome The local home interface
     */
    public final void setLocalHome(String localHome) {
        this.localHome = localHome;
    }

    /**
     * Gets the local interface
     * 
     * @return The local interface
     */
    public final String getLocal() {
        return this.local;
    }

    /**
     * Sets the local interface
     * 
     * @param local The local interface
     */
    public final void setLocal(String local) {
        this.local = local;
    }
}
