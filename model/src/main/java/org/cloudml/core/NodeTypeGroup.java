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

import java.util.ArrayList;
import java.util.Collection;

public class NodeTypeGroup extends NamedElementGroup<Node> {

    public NodeTypeGroup(DeploymentModel context) {
        super(context);
    }
     
    private NodeTypeGroup(DeploymentModel context, Collection<Node> nodes) {
       super(context, nodes);
    }

    @Override
    protected final void abortIfCannotBeAdded(Node node) {
         if (!getContext().getProviders().contains(node.getProvider())) {
            String message = String.format("The provider '%s' (used by node '%s') is not part of the model", node.getProvider().getName(), node.getName());
            throw new IllegalStateException(message);
        }
    }

    @Override
    protected final void abortIfCannotBeRemoved(Node node) {
        if (getContext().isUsed(node)) {
            final String message = String.format("Unable to remove node type '%s' as there are still some related instances", node.getName());
            throw new IllegalStateException(message);
        }
    }
    

    public NodeTypeGroup providedBy(Provider provider) {
        final ArrayList<Node> selectedNodes = new ArrayList<Node>();
        for (Node node : getContent()) {
            if (node.isProvidedBy(provider)) {
                selectedNodes.add(node);
            }
        }
        return new NodeTypeGroup(getContext(), selectedNodes);
    }
    
}
