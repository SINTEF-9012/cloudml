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

import org.cloudml.core.util.OwnedBy;
import org.cloudml.core.validation.Report;
import static org.cloudml.core.util.OwnedBy.CONTAINED;

public abstract class ExecutionPlatform extends WithResources implements DeploymentElement, OwnedBy<Component> {
    
    private final OptionalOwner<Component> owner;
    
    public ExecutionPlatform(String name) {
        super(name);
        this.owner = new OptionalOwner<Component>();
    }
    
    public abstract ExecutionPlatformInstance<? extends ExecutionPlatform> instantiate();
    
    @Override
    public void validate(Report report) {
        if (owner.isUndefined()) {
            final String error = String.format("no owner for execution platform '%s'", getQualifiedName());
            report.addError(error);
        }
    }
    
    @Override
    public Deployment getDeployment() {
        return getOwner().get().getDeployment();
    }    
    
    public OptionalOwner<Component> getOwner() {
        return owner;
    }
    
    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", owner.getName(), CONTAINED, getName());
    }
}
