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
public class BindingInstance extends WithProperties{

	private ArtefactPortInstance client;
	private ArtefactPortInstance server;
	
	private Binding type;
	
	public BindingInstance(){}
	
	public BindingInstance(String name, Binding type){
		this.name=name;
		this.type=type;
	}
	
	public BindingInstance(ArtefactPortInstance client, ArtefactPortInstance server, Binding type){
		this.client=client;
		this.server=server;
		this.type=type;
	}
	
	public void setClient(ArtefactPortInstance p){
		this.client=p;
	}
	
	public void setServer(ArtefactPortInstance p){
		this.server=p;
	}
	
	public ArtefactPortInstance getClient(){
		return this.client;
	}
	
	public ArtefactPortInstance getServer(){
		return this.server;
	}
	
	public Binding getType(){
		return type;
	}
	
	@Override
    public String toString() {
        return client+ "->"+server;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BindingInstance) {
        	BindingInstance otherBinding = (BindingInstance) other;
            return (client.getName().equals(otherBinding.getClient().getName()) && server.getName().equals(otherBinding.getServer().getName()));
        } else {
            return false;
        }
    }
	
}
