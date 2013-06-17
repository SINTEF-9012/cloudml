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

public class ClientPortInstance extends ArtefactPortInstance<ClientPort>{

	private boolean isOptional=true;
	
	
	public ClientPortInstance(String name, ClientPort type, ArtefactInstance owner, boolean isRemote, boolean isOptional) {
		super(name, type, owner, isRemote);
		this.isOptional=isOptional;
	}
	
	public ClientPortInstance(String name, ClientPort type, List<Property> properties, ArtefactInstance owner, boolean isOptional) {
        super(name, type, properties, owner);
        this.isOptional=isOptional;
    }
	
	public ClientPortInstance(String name, ClientPort type, List<Property> properties, ArtefactInstance owner, boolean isRemote, boolean isOptional){
		super(name, type, properties, owner, isRemote);
		this.isOptional=isOptional;
	}
	
	public void setIsOptional(boolean isOptional){
		this.isOptional=isOptional;
	}
	
	public boolean getIsOptional(){
		return this.isOptional;
	}

	@Override
    public String toString() {
        return "ClientPortInstance " + name + " owner:" + owner.getName() + " optional:"+ isOptional;
    }
	
	@Override
    public boolean equals(Object other) {
        if (other instanceof ClientPortInstance) {
        	ClientPortInstance otherNode = (ClientPortInstance) other;
            return name.equals(otherNode.getName()) && owner.equals(otherNode.getOwner());
        } else {
            return false;
        }
    }
	
}
