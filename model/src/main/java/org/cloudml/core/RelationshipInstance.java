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
 * Bindings between ArtefactPortInstances
 * @author Nicolas Ferry
 *
 */
public class RelationshipInstance extends CloudMLElementWithProperties {

	private RequiredPortInstance requiredPortInstance;
	private ProvidedPortInstance providedPortInstance;
	
	private Relationship type;
	
	public RelationshipInstance(){}
	
	public RelationshipInstance(String name, Relationship type){
		this.name=name;
		this.type=type;
	}
	
	public RelationshipInstance(RequiredPortInstance requiredPortInstance, ProvidedPortInstance providedPortInstance, Relationship type){
		this.requiredPortInstance = requiredPortInstance;
		this.providedPortInstance = providedPortInstance;
		this.type=type;
	}
	
	public void setRequiredPortInstance(RequiredPortInstance p){
		this.requiredPortInstance =p;
	}
	
	public void setProvidedPortInstance(ProvidedPortInstance p){
		this.providedPortInstance =p;
	}
	
	public RequiredPortInstance getRequiredPortInstance(){
		return this.requiredPortInstance;
	}
	
	public ProvidedPortInstance getProvidedPortInstance(){
		return this.providedPortInstance;
	}
	
	public Relationship getType(){
		return type;
	}
	
	@Override
    public String toString() {
        return requiredPortInstance + "->"+ providedPortInstance;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RelationshipInstance) {
        	RelationshipInstance otherBinding = (RelationshipInstance) other;
            return (requiredPortInstance.equals(otherBinding.getRequiredPortInstance()) && providedPortInstance.equals(otherBinding.getProvidedPortInstance()));
        } else {
            return false;
        }
    }
	
}
