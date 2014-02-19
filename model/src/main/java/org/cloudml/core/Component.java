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
public abstract class Component extends CloudMLElementWithProperties {

    private List<ProvidedPort> providedPorts = new LinkedList<ProvidedPort>();

    public Component() {
    }

    public Component(String name) {
        super(name);
    }

    public Component(String name, List<Property> properties) {
        super(name, properties);
    }

    public Component(String name, List<Property> properties, List<ProvidedPort> providedPorts) {
        super(name, properties);
        this.providedPorts=providedPorts;
    }

    public List<ProvidedPort> getProvidedPorts() {
        return this.providedPorts;
    }

    public void setProvidedPorts(List<ProvidedPort> providedPorts) {
        this.providedPorts = providedPorts;
    }


    @Override
    public String toString() {
        return "Type " + name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Component) {
            Component otherComp = (Component) other;
            return name.equals(otherComp.getName());
        } else {
            return false;
        }
    }
}
