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

import java.util.LinkedList;
import java.util.List;

/*
 * InternalComponent describes the type of an InternalComponent instance. It also contains
 * communication channels and dependencies between Port Types
 */
public class InternalComponent extends Component {

    private RequiredExecutionPlatform requiredExecutionPlatform;

    /*
     * Dependencies <PortName,Port Reference>
     */
    private List<RequiredPort> requiredPorts = new LinkedList<RequiredPort>();

    public InternalComponent() {
    }

    public InternalComponent(String name) {
        super(name);
    }

    public InternalComponent(String name, RequiredExecutionPlatform requiredExecutionPlatform) {
        super(name);
        this.requiredExecutionPlatform = requiredExecutionPlatform;
    }

    public InternalComponent(String name, List<Property> properties) {
        super(name, properties);
    }

    public InternalComponent(String name, List<Property> properties, RequiredExecutionPlatform requiredExecutionPlatform) {
        super(name, properties);
        this.requiredExecutionPlatform = requiredExecutionPlatform;
    }

    public InternalComponent(String name, List<Property> properties, List<RequiredPort> requiredPorts) {
        super(name, properties);
        this.requiredPorts = requiredPorts;
    }

    @Override
    public String toString() {
        return "Type " + name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InternalComponent) {
            InternalComponent otherComp = (InternalComponent) other;
            return name.equals(otherComp.getName());
        } else {
            return false;
        }
    }

    public InternalComponentInstance instantiates(String name) {
        return new InternalComponentInstance(name, this);
    }

    /*
     * Getters & Setters
     */

    public List<RequiredPort> getRequiredPorts() {
        return this.requiredPorts;
    }

    public void setRequiredPorts(List<RequiredPort> requiredPorts) {
        this.requiredPorts = requiredPorts;
    }

    public RequiredExecutionPlatform getRequiredExecutionPlatform(){
        return requiredExecutionPlatform;
    }

    public void setRequiredExecutionPlatform(RequiredExecutionPlatform requiredExecutionPlatform){
        this.requiredExecutionPlatform=requiredExecutionPlatform;
    }

}
