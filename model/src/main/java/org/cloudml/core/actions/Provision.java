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
package org.cloudml.core.actions;

import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.builders.NodeInstanceBuilder;

public class Provision extends AbstractAction<NodeInstance> {
 
    private final Node nodeType;
    
       
    public Provision(StandardLibrary library, Node nodeType) {
        super(library);
        this.nodeType = nodeType;
    }
    
    @Override
    public NodeInstance applyTo(DeploymentModel deployment) {
        String instanceName = getLibrary().createUniqueNodeInstanceName(deployment, nodeType);
        NodeInstanceBuilder builder = aNodeInstance()
                .named(instanceName)
                .ofType(nodeType.getName());
        builder.integrateIn(deployment);
        return deployment.getNodeInstances().named(instanceName);
    }
   
}
