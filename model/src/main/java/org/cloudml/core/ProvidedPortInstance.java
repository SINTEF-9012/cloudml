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


public class ProvidedPortInstance extends PortInstance<ProvidedPort> {
	
	
	public ProvidedPortInstance(String name, ProvidedPort type, InternalComponentInstance owner) {
		super(name, type, owner);
	}
	
	public ProvidedPortInstance(String name, ProvidedPort type, List<Property> properties, InternalComponentInstance owner) {
        super(name, type, properties, owner);
    }
	
	public ProvidedPortInstance(String name, ProvidedPort type, List<Property> properties, InternalComponentInstance owner, boolean isRemote){
		super(name, type, properties, owner, isRemote);
	}
	
	@Override
    public String toString() {
        return "ProvidedPortInstance " + name + " component:" + owner.getName();
    }
	
	@Override
    public boolean equals(Object other) {
        if (other instanceof ProvidedPortInstance) {
        	ProvidedPortInstance otherNode = (ProvidedPortInstance) other;
            return name.equals(otherNode.getName()) && owner.equals(otherNode.getOwner());
        } else {
            return false;
        }
    }
	
}
