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

import java.util.ArrayList;
import java.util.Collection;


public class NodeInstanceGroup extends NamedElementGroup<NodeInstance> {

    public NodeInstanceGroup(DeploymentModel context) {
        super(context);
    }

    public NodeInstanceGroup(DeploymentModel context, Collection<NodeInstance> content) {
        super(context, content);
    }    

    @Override
    protected void abortIfCannotBeAdded(NodeInstance instance) {
         if (!getContext().getNodeTypes().contains(instance.getType())) {
            final String message = String.format("The node type '%s', associated with instance '%s' is not part of this model", instance.getType().getName(), instance.getName());
            throw new IllegalArgumentException(message);
        }
    }
    
    
    public NodeInstanceGroup ofType(Node node) {
        final ArrayList<NodeInstance> selectedInstances = new ArrayList<NodeInstance>();
        for (NodeInstance nodeInstance : this) {
            if (nodeInstance.getType().equals(node)) {
                selectedInstances.add(nodeInstance);
            }
        }
        return new NodeInstanceGroup(getContext(), selectedInstances);
    }
    
    
}
