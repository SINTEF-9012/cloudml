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

public class NodePortInstance extends WithProperties{
	private NodeInstance owner;
	private NodePort type;
	
        public NodePortInstance() {}
        
	public NodePortInstance(String name, NodePort type, NodeInstance owner){
		super(name);
		this.owner=owner;
		this.type=type;
	}
	
	public NodePortInstance(String name, NodePort type, List<Property> properties, NodeInstance owner){
		super(name,properties);
		this.type=type;
		this.owner=owner;
	}
	
	public NodeInstance getOwner(){
		return this.owner;
	}
	
	public void setType(NodePort type){
		this.type=type;
	}
		
	public NodePort getType(){
		return this.type;
	}
	
	@Override
    public String toString() {
        return "NodePortInstance: "+name+" owner"+owner.getName()+" type:"+type.getName();
    }
        
            @Override
    public boolean equals(Object other) {
        if (other instanceof NodePortInstance) {
            NodePortInstance otherNode = (NodePortInstance) other;
            return name.equals(otherNode.getName()) && type.equals(otherNode.getType());
        } else {
            return false;
        }
    }
	
}
