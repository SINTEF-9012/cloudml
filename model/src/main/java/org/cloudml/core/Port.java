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

import java.util.List;

public abstract class Port extends CloudMLElementWithProperties {

	protected boolean isLocal = false;
    protected Component component;
    protected int portNumber = 0;

    public Port() {
    }

    public Port(String name, Component component, boolean isLocal) {
        super(name);
        this.component = component;
        this.isLocal = isLocal;
    }

    public Port(String name, List<Property> properties, Component component, boolean isLocal) {
        super(name, properties);
        this.component = component;
        this.isLocal = isLocal;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int n) {
        this.portNumber = n;
    }
    
    public void setIsLocal(boolean isLocal){
    	this.isLocal =isLocal;
    }
    
    public boolean getIsLocal(){
    	return this.isLocal;
    }

    @Override
    public String toString() {
        return "PortType " + name + " ownerType" + component.getName();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Port) {
        	Port otherArt = (Port) other;
            return name.equals(otherArt.getName()) && component.equals(otherArt.getComponent());
        } else {
            return false;
        }
    }
}
