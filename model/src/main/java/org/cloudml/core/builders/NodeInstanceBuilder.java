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
package org.cloudml.core.builders;

import org.cloudml.core.DeploymentModel;
import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.NodeInstance.*;

import org.cloudml.core.NodeInstance;

public class NodeInstanceBuilder {

    private String instanceName;
    private String typeName;

    public NodeInstanceBuilder() {
        this.instanceName = DEFAULT_NAME;
        this.typeName = DEFAULT_NAME;
    }

    public NodeInstanceBuilder named(String instanceName) {
        this.instanceName = instanceName; 
        return this;
    }

    public NodeInstanceBuilder ofType(String nodeTypeName) {
        this.typeName = nodeTypeName;
        return this;
    }

    public NodeInstance build() {
        final NodeInstance nodeInstance = new NodeInstance();
        nodeInstance.setName(instanceName);
        nodeInstance.setType(aNode().named(typeName).build());
        return nodeInstance;
    }

    public void integrateIn(DeploymentModel model) {
        final NodeInstance nodeInstance = new NodeInstance();
        nodeInstance.setName(instanceName);
        nodeInstance.setType(model.getNodeTypes().named(typeName));
        model.getNodeInstances().add(nodeInstance);
    }
}
