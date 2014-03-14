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

/**
 * Created by Nicolas Ferry on 13.02.14.
 */
public abstract class ComponentInstance<T extends Component> extends CloudMLElementWithProperties{

    protected T type;
    private List<ProvidedPortInstance> providedPortInstances = new LinkedList<ProvidedPortInstance>();


    private List<ProvidedExecutionPlatformInstance> providedExecutionPlatformInstances= new LinkedList<ProvidedExecutionPlatformInstance>();


    public ComponentInstance(){}

    public ComponentInstance(String name, T type) {
        super(name);
        this.type=type;
    }

    public ComponentInstance(String name, List<Property> properties, T type) {
        super(name,properties);
        this.type=type;
    }

    public ComponentInstance(String name, List<Property> properties, List<ProvidedPortInstance> providedPortInstances) {
        super(name,properties);
        this.providedPortInstances=providedPortInstances;
    }


    public List<ProvidedPortInstance> getProvidedPortInstances() {
        return this.providedPortInstances;
    }

    public T getType() {
        return this.type;
    }

    public void setProvidedPortInstances(List<ProvidedPortInstance> providedPortInstances) {
        this.providedPortInstances = providedPortInstances;
    }

    public void setType(T type) {
        this.type = type;
    }

    public List<ProvidedExecutionPlatformInstance> getProvidedExecutionPlatformInstances() {
        return providedExecutionPlatformInstances;
    }

    public void setProvidedExecutionPlatformInstances(List<ProvidedExecutionPlatformInstance> providedExecutionPlatformInstances) {
        this.providedExecutionPlatformInstances = providedExecutionPlatformInstances;
    }


    @Override
    public String toString() {
        return "Instance " + name + " : " + getType().getName();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null)
            return false;

        if (other instanceof ComponentInstance) {
            ComponentInstance otherCompInst = (ComponentInstance) other;
            Boolean match= name.equals(otherCompInst.getName()) && type.equals(otherCompInst.getType());
            return match;
        } else {
            return false;
        }
    }
}
