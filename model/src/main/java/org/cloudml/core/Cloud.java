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

import java.util.Collection;
import java.util.LinkedList;
import org.cloudml.core.collections.VMInstanceGroup;
import org.cloudml.core.util.OwnedBy;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class Cloud extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private final OptionalOwner<Deployment> owner;
    
    public Cloud(String name) {
        this(name, new LinkedList<VMInstance>());   
    }

    public Cloud(String name, Collection<VMInstance> vmInstances) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
    }

    @Override
    public void validate(Report report) {
        if (owner.isUndefined()) {
            final String message = String.format("The cloud '%s' has no owner", getName());
            report.addWarning(message);
        }
    }
    
    
    

    @Override
    public Deployment getDeployment() {
        return getOwner().get();
    } 

    @Override
    public void accept(Visitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", getOwner().getName(), CONTAINED, getName());
    }

    @Override
    public OptionalOwner<Deployment> getOwner() {
        return owner;
    }
    
    public VMInstanceGroup getVmInstances() {
        return getDeployment().getComponentInstances().onlyVMs().whichBelongsTo(this);
    }

}
