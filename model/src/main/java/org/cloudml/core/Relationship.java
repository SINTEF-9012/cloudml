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
 * Relationship between two InternalComponents
 * @author Nicolas Ferry
 *
 */
public class Relationship extends CloudMLElementWithProperties {
	
	private RequiredPort requiredPort;
	private ProvidedPort providedPort;

	private Resource clientResource;
	private Resource serverResource;
	
	public Relationship(){}

    public Relationship(String name){
        super(name);
    }
	
	public Relationship(RequiredPort requiredPort, ProvidedPort providedPort){
		this.requiredPort = requiredPort;
		this.providedPort = providedPort;
	}
	
	public RelationshipInstance instantiates(String name) {
        return new RelationshipInstance(name, this);
    }

    public RelationshipInstance instantiates(RequiredPortInstance client, ProvidedPortInstance server) {
        return new RelationshipInstance(client, server, this);
    }
	
	public void setRequiredPort(RequiredPort p){
		this.requiredPort =p;
	}
	
	public void setProvidedPort(ProvidedPort p){
		this.providedPort =p;
	}
	
	public RequiredPort getRequiredPort(){
		return this.requiredPort;
	}
	
	public ProvidedPort getProvidedPort(){
		return this.providedPort;
	}
	
	public void setClientResource(Resource clientResource){
		this.clientResource=clientResource;
	}
	
	public Resource getClientResource(){
		return clientResource;
	}
	
	public void setServerResource(Resource serverResource){
		this.serverResource=serverResource;
	}
	
	public Resource getServerResource(){
		return serverResource;
	}
	
	@Override
    public String toString() {
        return requiredPort + "->"+ providedPort;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Relationship) {
        	Relationship otherRelationship = (Relationship) other;
            return (requiredPort.getName().equals(otherRelationship.getRequiredPort().getName()) && providedPort.getName().equals(otherRelationship.getProvidedPort().getName()));
        } else {
            return false;
        }
    }

}
