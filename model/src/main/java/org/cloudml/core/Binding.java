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
 * Binding between to Artefact
 * @author Nicolas Ferry
 *
 */
public class Binding extends WithProperties{
	
	private ArtefactPort client;
	private ArtefactPort server;
	
	private Resource clientResource;
	private Resource serverResource;
	
	public Binding(){}
	
	public Binding(ArtefactPort client, ArtefactPort server){
		this.client=client;
		this.server=server;
	}
	
	public BindingInstance instanciates(String name) {
        return new BindingInstance(name, this);
    }

    public BindingInstance instanciates(ArtefactPortInstance client, ArtefactPortInstance server) {
        return new BindingInstance(client, server, this);
    }
	
	public void setClient(ArtefactPort p){
		this.client=p;
	}
	
	public void setServer(ArtefactPort p){
		this.server=p;
	}
	
	public ArtefactPort getClient(){
		return this.client;
	}
	
	public ArtefactPort getServer(){
		return this.server;
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
        return client+ "->"+server;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Binding) {
        	Binding otherBinding = (Binding) other;
            return (client.getName().equals(otherBinding.getClient().getName()) && server.getName().equals(otherBinding.getServer().getName()));
        } else {
            return false;
        }
    }

}
