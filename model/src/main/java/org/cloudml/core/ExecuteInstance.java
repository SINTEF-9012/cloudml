/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.core;

/**
 * Created by nicolasf on 03.03.14.
 */
public class ExecuteInstance extends CloudMLElementWithProperties {

    private ProvidedExecutionPlatformInstance providedExecutionPlatformInstance;
    private RequiredExecutionPlatformInstance requiredExecutionPlatformInstance;

    public ExecuteInstance(String name, ProvidedExecutionPlatformInstance providedExecutionPlatformInstance, RequiredExecutionPlatformInstance requiredExecutionPlatformInstance){
        super(name);

        abortIfNull(providedExecutionPlatformInstance, "An Execute instance requires a Provided Execution Platform instance");
        abortIfNull(requiredExecutionPlatformInstance, "An Execute instance requires a Required Execution Platform instance");
        unlessExpectationsAreMet(providedExecutionPlatformInstance, requiredExecutionPlatformInstance);
        this.providedExecutionPlatformInstance=providedExecutionPlatformInstance;
        this.requiredExecutionPlatformInstance=requiredExecutionPlatformInstance;
    }

    private void abortIfNull(Object o, String msg){
        if(o == null)
            throw new IllegalArgumentException(msg);
    }

    private void unlessExpectationsAreMet(ProvidedExecutionPlatformInstance provided, RequiredExecutionPlatformInstance required) {
        for (String expectedKey: required.getType().getDemands().keySet()) {
            Object actual = provided.getType().getOffers().get(expectedKey);
            if (actual == null) {
                throw new IllegalArgumentException("Missing expectations: " + expectedKey);
            }
            Object expected = required.getType().getDemands().get(expectedKey);
            if (!expected.equals(actual)) {
                throw new IllegalArgumentException("Unmet expectation '" + expectedKey + "' (expected + '" +  expected + "' but found: '" + actual + "')");
            }
        }
    }

    public RequiredExecutionPlatformInstance getRequiredExecutionPlatformInstance() {
        return requiredExecutionPlatformInstance;
    }

    public void setRequiredExecutionPlatformInstance(RequiredExecutionPlatformInstance requiredExecutionPlatformInstance) {
        abortIfNull(requiredExecutionPlatformInstance, "An Execute instance requires a Required Execution Platform instance");
        this.requiredExecutionPlatformInstance = requiredExecutionPlatformInstance;
    }

    public ProvidedExecutionPlatformInstance getProvidedExecutionPlatformInstance() {
        return providedExecutionPlatformInstance;
    }

    public void setProvidedExecutionPlatformInstance(ProvidedExecutionPlatformInstance providedExecutionPlatformInstance) {
        abortIfNull(providedExecutionPlatformInstance, "An Execute instance requires a Provided Execution Platform instance");
        this.providedExecutionPlatformInstance = providedExecutionPlatformInstance;
    }

}
