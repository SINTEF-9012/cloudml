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

import org.cloudml.core.collections.CloudGroup;
import org.cloudml.core.visitors.Visitor;

public class VMInstance extends ExternalComponentInstance<VM> {

    private String id = "";
    private final CloudGroup clouds;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    private String hostname = "";

    public int core=0;

    public String getProviderSpecificType() {
        return providerSpecificType;
    }

    public void setProviderSpecificType(String providerSpecificType) {
        this.providerSpecificType = providerSpecificType;
    }

    private String providerSpecificType="";

    public VMInstance(VM type) {
        this(NamedElement.DEFAULT_NAME, type);
    }

    public VMInstance(String name, VM type) {
        super(name, type);
        this.clouds = new CloudGroup();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitVMInstance(this);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public int getCore(){
        return this.core;
    }

    public void setCore(int core){
        this.core=core;
    }

    public CloudGroup clouds() {
        return this.clouds;
    }

    public boolean belongsTo(Cloud cloud) {
        return clouds.contains(cloud);
    }

    @Override
    public String toString() {
        return "VMInstance: " + getName() + " Type:" + getType().getName() + "{\n"
                + "minRam:" + (getType()).getMinRam() + "\n"
                + "minCore" + (getType()).getMinCores() + "\n"
                + "minDisk" + (getType()).getMinStorage() + "\n"
                + "OS" + (getType()).getOs() + "\n"
                + "location" + (getType()).getLocation() + "\n"
                + "publicAdress" + getPublicAddress() + "\n"
                + "groupName" + (getType()).getGroupName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof VMInstance) {
            VMInstance otherNode = (VMInstance) other;
            return getName().equals(otherNode.getName()) && getType().equals(otherNode.getType());
        }
        else {
            return false;
        }
    }
}
