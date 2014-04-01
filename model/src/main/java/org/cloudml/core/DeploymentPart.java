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
/*
 */

package org.cloudml.core;

import java.util.List;


public abstract class DeploymentPart extends WithProperties {

    private DeploymentModel deployment;

    public DeploymentPart() {
    }

    public DeploymentPart(String name) {
        super(name);
    }

    public DeploymentPart(String name, List<Property> properties) {
        super(name, properties);
    }
    
    public DeploymentModel getDeployment() {
        if (!isAttachedToADeployment()) {
            final String message = String.format("The element '%s' (of type '%s') is not attached to any deployment model", getName(), this.getClass().getName());
            throw new IllegalStateException(message);
        }
        return deployment;
    }
        
    public boolean isAttachedToADeployment() {
        return deployment != null;
    }
     
    public void attachTo(DeploymentModel deployment) {
       this.deployment = deployment; 
    }
    
    public void detach() {
        this.deployment = null;
    }
    
}
