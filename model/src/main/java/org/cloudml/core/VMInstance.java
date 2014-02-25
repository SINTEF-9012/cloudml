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

public class VMInstance extends ExternalComponentInstance<VM> {

    private String publicAddress="";
    private String id="";

    public VMInstance() {
    }

    public VMInstance(String name, VM type) {
        super(name, type);
    }

    public VMInstance(String name, VM type, List<Property> properties) {
        super(name, type, properties);
    }
    

    public void setPublicAddress(String publicAddress){
    	this.publicAddress=publicAddress;
    }
    
    public String getPublicAddress(){
    	return this.publicAddress;
    }
    
    public void setId(String id){
    	this.id=id;
    }
    
    public String getId(){
    	return this.id;
    }
    
    //we should introduce genericity
    @Override
    public String toString() {
        return "VMInstance: "+name+" Type:"+type.getName()+"{\n" +
        		"minRam:" + ((VM)type).getMinRam()+"\n"+
        		"minCore" + ((VM)type).getMinCores()+"\n"+
        		"minDisk" + ((VM)type).getMinStorage()+"\n"+
        		"OS" + ((VM)type).getOs()+"\n"+
        		"location" + ((VM)type).getLocation()+"\n"+
        		"publicAdress" + getPublicAddress()+"\n"+
        		"groupName" + ((VM)type).getGroupName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof VMInstance) {
            VMInstance otherNode = (VMInstance) other;
            return name.equals(otherNode.getName()) && type.equals(otherNode.getType());
        } else {
            return false;
        }
    }
}
