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
 * An instance of artefact is an elementary unit to be deployed on a single
 * node. It contains properties, communications channels and dependencies.
 *
 */
public class InternalComponentInstance extends ComponentInstance {

		/*
	 * Dependencies <PortName,PortInstance Reference>
	 */
	private List<RequiredPortInstance> requiredPortInstances = new LinkedList<RequiredPortInstance>();

	public InternalComponentInstance() {
	}

	public InternalComponentInstance(String name, InternalComponent type) {
		super(name,type);
		this.status=State.uninstalled;
	}

	public InternalComponentInstance(String name, InternalComponent type, VMInstance destination) {
		super(name,type,destination);
	}

	public InternalComponentInstance(String name, List<Property> properties, InternalComponent type) {
		super(name, properties, type);
	}

	public InternalComponentInstance(String name, List<Property> properties, InternalComponent type, VMInstance destination) {
		super(name, properties, type, destination);
	}

	public InternalComponentInstance(String name, List<Property> properties, List<RequiredPortInstance> requiredPortInstances, List<ProvidedPortInstance> providedPortInstances) {
		super(name, properties, providedPortInstances);
		this.requiredPortInstances = requiredPortInstances;
	}

	@Override
	public String toString() {
		return "Instance " + name + " : " + getType().getName();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof InternalComponentInstance) {
			InternalComponentInstance otherCompInst = (InternalComponentInstance) other;
			Boolean match= name.equals(otherCompInst.getName()) && type.equals(otherCompInst.getType());
			if(destination != null)
				return name.equals(otherCompInst.getName()) && type.equals(otherCompInst.getType()) && destination.equals(otherCompInst.getDestination());
			else return match && (otherCompInst.getDestination() == null);
		} else {
			return false;
		}
	}

	/*
	 * Getters
	 */

	public List<RequiredPortInstance> getRequiredPortInstances() {
		return this.requiredPortInstances;
	}

	public void setRequiredPortInstances(List<RequiredPortInstance> requiredPortInstances) {
		this.requiredPortInstances = requiredPortInstances;
	}

    
}
